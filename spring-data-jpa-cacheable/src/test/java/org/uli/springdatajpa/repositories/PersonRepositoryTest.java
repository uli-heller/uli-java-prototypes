/**
 * 
 */
package org.uli.springdatajpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.hamcrest.CoreMatchers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.uli.springdatajpa.entities.Address;
import org.uli.springdatajpa.entities.Person;

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Slf4j
public class PersonRepositoryTest {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    PlatformTransactionManager transactionManager;

    private static AtomicBoolean fInitialized = new AtomicBoolean(false);

    private final static int NUMBER_OF_PERSONS=40;
    
    @Before
    public void setup() {
        if (!fInitialized.getAndSet(true)) {
            log.debug("Initialize persons");
            Session session = sessionFactory.openSession();
            Query delete = session.createQuery("delete from Person p");
            delete.executeUpdate();
            for (int i=0; i<NUMBER_OF_PERSONS; i++) {
                Person person = new Person();
                person.setFirstName("firstName-"+i);
                person.setLastName("lastName-"+i);
                session.saveOrUpdate(person);
                @SuppressWarnings("unused")
                val addresses = createAddresses(session, person, i);
            }
            session.close();
        }
        Person.clearNoArgsConstructorCallCounter();
        Address.clearNoArgsConstructorCallCounter();
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

    @Test @Transactional
    public void findAllPersons() {
        val persons = personRepository.findAll();
        assertThat(persons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
        for (val person : persons) {
            assertNotNull(person.getAddresses());
            assertTrue(person.getAddresses().size() >= 0);
        }
        assertEquals(NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(780, Address.getNoArgsConstructorCallCounter().get());
    }
    
    @Test @Transactional
    public void findAllPersonsMultiSingleTransaction() {
        for (int i=0; i<10; ++i) {
            val persons = personRepository.findAll();
            assertThat(persons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
            for (val person : persons) {
                assertNotNull(person.getAddresses());
                assertTrue(person.getAddresses().size() >= 0);
            }
        }
        // Same numbers as without loop
        assertEquals(NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(780, Address.getNoArgsConstructorCallCounter().get());
    }
    @Test
    public void findAllPersonsMultiMultiTransaction() {
        for (int i=0; i<10; ++i) {
            findAllPersonsMultiHelper();
        }
        assertEquals(10*NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(10*780, Address.getNoArgsConstructorCallCounter().get());
    }

    // @Transactional // annotation doesn't work for some reason...
    private void findAllPersonsMultiHelper() {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status=transactionManager.getTransaction(defaultTransactionDefinition);
        val persons = personRepository.findAll();
        assertThat(persons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
        for (val person : persons) {
            assertNotNull(person.getAddresses());
            assertTrue(person.getAddresses().size() >= 0);
        }
        transactionManager.rollback(status);
    }
    
    @Test @Transactional
    public void findByLastName() {
        val person = personRepository.findByLastName("lastName-9");
        assertThat(person, CoreMatchers.notNullValue());
        assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertThat(person.getAddresses(), CoreMatchers.notNullValue());
        assertThat(person.getAddresses().size(), CoreMatchers.is(9));
        assertEquals(1, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(9, Address.getNoArgsConstructorCallCounter().get());
    }
    
    @Test
    public void findByLastNameNotFound() {
        val person = personRepository.findByLastName("lastNameNotFound");
        assertThat(person, CoreMatchers.nullValue());
        //assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertEquals(0, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
    }
    
    @Test(expected=IncorrectResultSizeDataAccessException.class)
    public void findByLastNameMulti() {
        Person person = Person.builder().firstName("uli").lastName("lastName-9").build();
        personRepository.saveAndFlush(person);
        person = personRepository.findByLastName("lastName-9");
        //assertThat(person, CoreMatchers.notNullValue());
        //assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertEquals(NUMBER_OF_PERSONS, Person.getNoArgsConstructorCallCounter().get());
        assertEquals(0, Address.getNoArgsConstructorCallCounter().get());
        }
}
