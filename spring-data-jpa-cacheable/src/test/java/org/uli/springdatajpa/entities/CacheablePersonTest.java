/**
 * 
 */
package org.uli.springdatajpa.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lombok.val;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.uli.springdatajpa.entities.Person;

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CacheablePersonTest {
    @Autowired SessionFactory sessionFactory;
    @Autowired EntityManagerFactory entityManagerFactory;
    @Autowired PlatformTransactionManager transactionManager;
    EntityManager entityManager;

    private static boolean fInitialized = false;
    private static Map<Integer, Person> persons = new HashMap<Integer, Person>();

    private final static int NUMBER_OF_PERSONS=40;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        if (! fInitialized) {
            Session session = sessionFactory.openSession();
            Query delete = session.createQuery("delete from Person p");
            delete.executeUpdate();
            for (int i=0; i<NUMBER_OF_PERSONS; i++) {
                Person person = new Person();
                person.setFirstName("firstName-"+i);
                person.setLastName("lastName-"+i);
                session.saveOrUpdate(person);
                persons.put(person.getPersonId(), person);
                @SuppressWarnings("unused")
                val addresses = createAddresses(session, person, i);
            }
            fInitialized = true;
            session.close();
        }
        CacheablePerson.clearNoArgsConstructorCallCounter();
        CacheableAddress.clearNoArgsConstructorCallCounter();
        entityManager = entityManagerFactory.createEntityManager();
    }

    private List<Address> createAddresses(Session s, Person p, int number) {
        List<Address> result = new LinkedList<Address>();
        for (int i=0; i<number; i++) {
            Address a = Address.builder().street("street - "+p.getLastName()).city("city - "+p.getLastName()).build();
            a.setPersonId(p.getPersonId());
            result.add(a);
            s.saveOrUpdate(a);
        }
        return result;
    }
    
    @Test
    public void testNotEmpty() {
        javax.persistence.Query query = entityManager.createQuery("select p from CacheablePerson p");
        @SuppressWarnings("unchecked")
        List<Object> resultList = query.getResultList();
        assertFalse(resultList.isEmpty());
        assertEquals(persons.size(), resultList.size());
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
    
    @Test
    public void testFind() {
        for (Person p : persons.values()) {
            Integer personId = p.getPersonId();
            CacheablePerson dbPerson = (CacheablePerson) entityManager.find(CacheablePerson.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
        }
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }

    @Test
    public void testMultiFind() {
        for (int i=0; i<10; ++i) {
            for (Person p : persons.values()) {
                Integer personId = p.getPersonId();
                CacheablePerson dbPerson = (CacheablePerson) entityManager.find(CacheablePerson.class, personId);
                assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
                assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
                assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
            }
        }
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }

    @Test
    public void testMultiFindMultiTransactions() {
        for (int i=0; i<10; ++i) {
            DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
            TransactionStatus status=transactionManager.getTransaction(defaultTransactionDefinition);
            for (Person p : persons.values()) {
                Integer personId = p.getPersonId();
                CacheablePerson dbPerson = (CacheablePerson) entityManager.find(CacheablePerson.class, personId);
                assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
                assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
                assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
            }
            transactionManager.rollback(status);
        }
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
}
