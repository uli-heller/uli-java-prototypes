/**
 * 
 */
package org.uli.springdatajpa.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uli.springdatajpa.entities.Person;

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class PersonTest {
    @Autowired SessionFactory sessionFactory;
    @Autowired EntityManagerFactory entityManagerFactory;
    
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
        Person.clearNoArgsConstructorCallCounter();
        Address.clearNoArgsConstructorCallCounter();
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    @After
    public void tearDown() {
        entityManager.close();
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
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select p from Person p");
        @SuppressWarnings("unchecked")
        List<Object> resultList = query.list();
        assertFalse(resultList.isEmpty());
        assertEquals(persons.size(), resultList.size());
        session.close();
        assertEquals(NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }
    
    @Test
    public void testFind() {
        Session session = sessionFactory.openSession();
        for (Person p : persons.values()) {
            Integer personId = p.getPersonId();
            Person dbPerson = (Person) session.get(Person.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
        }
        session.close();
        assertEquals(NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }

    @Test
    public void testMultiFind() {
        for (int i=0; i<10; ++i) {
            Session session = sessionFactory.openSession();
            for (Person p : persons.values()) {
                Integer personId = p.getPersonId();
                Person dbPerson = (Person) session.get(Person.class, personId);
                assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
                assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
                assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
            }
            session.close();
        }
        assertEquals(10*NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }
    
    @Test
    public void testEntityManagerNotEmpty() {
            javax.persistence.Query query = entityManager.createQuery("select p from Person p");
            @SuppressWarnings("unchecked")
            List<Object> resultList = query.getResultList();
            assertFalse(resultList.isEmpty());
            assertEquals(persons.size(), resultList.size());
            assertEquals(NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
            assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }
    @Test
    public void testEntityManagerFind() {
        for (Person p : persons.values()) {
            Integer personId = p.getPersonId();
            Person dbPerson = (Person) entityManager.find(Person.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
        }
        // The entity manager seems to cache everything :)
        assertEquals(/* 10* */NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }
    @Test
    public void testEntityManagerFindWithAddresses() {
        for (Person p : persons.values()) {
            Integer personId = p.getPersonId();
            Person dbPerson = (Person) entityManager.find(Person.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
            assertTrue(dbPerson.getAddresses().size() >= 0);
        }
        // The entity manager seems to cache everything :)
        assertEquals(/* 10* */NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(780, Address.getNoArgsConstructorCallCounter().get());
    }
    @Test
    public void testEntityManagerMultiFind() {
        for (int i=0; i<10; ++i) {
            for (Person p : persons.values()) {
                Integer personId = p.getPersonId();
                Person dbPerson = (Person) entityManager.find(Person.class, personId);
                assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
                assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
                assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
            }
        }
        // The entity manager seems to cache everything :)
        assertEquals(/* 10* */NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }
    @Test
    public void testEntityManagerMultiFindWithAddresses() {
        for (int i=0; i<10; ++i) {
            for (Person p : persons.values()) {
                Integer personId = p.getPersonId();
                Person dbPerson = (Person) entityManager.find(Person.class, personId);
                assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
                assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
                assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
                assertTrue(dbPerson.getAddresses().size() >= 0);
            }
        }
        // The entity manager seems to cache everything :)
        assertEquals(/* 10* */NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(780, Address.getNoArgsConstructorCallCounter().get());
    }
}
