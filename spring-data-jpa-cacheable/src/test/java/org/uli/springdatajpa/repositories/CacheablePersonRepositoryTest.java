/**
 * 
 */
package org.uli.springdatajpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
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
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.uli.springdatajpa.cache.GuavaCache;
import org.uli.springdatajpa.cache.GuavaCacheManager;
import org.uli.springdatajpa.entities.Address;
import org.uli.springdatajpa.entities.CacheableAddress;
import org.uli.springdatajpa.entities.CacheablePerson;
import org.uli.springdatajpa.entities.Person;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CacheablePersonRepositoryTest.MyTestConfig.class})
@Slf4j
public class CacheablePersonRepositoryTest {
    @Autowired
    CacheablePersonRepository cacheablePersonRepository;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    CacheManager cacheManager;

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
        CacheablePerson.clearNoArgsConstructorCallCounter();
        CacheableAddress.clearNoArgsConstructorCallCounter();
        this.clearAllCaches();
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

    private void clearAllCaches() {
        val cacheNames = this.cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            val cache = this.cacheManager.getCache(cacheName);
            cache.clear();
        }
    }

    @Test @Transactional
    public void findAllPersons() {
        val persons = cacheablePersonRepository.findAll();
        assertThat(persons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
        for (val person : persons) {
            assertNotNull(person.getAddresses());
            assertTrue(person.getAddresses().size() >= 0);
        }
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(780, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
    
    @Test @Transactional
    public void findAllPersonsMultiSingleTransaction() {
        for (int i=0; i<10; ++i) {
            val persons = cacheablePersonRepository.findAll();
            assertThat(persons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
            for (val person : persons) {
                assertNotNull(person.getAddresses());
                assertTrue(person.getAddresses().size() >= 0);
            }
        }
        // Same numbers as without loop
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(780, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
    @Test
    public void findAllPersonsMultiMultiTransaction() {
        for (int i=0; i<10; ++i) {
            findAllPersonsMultiHelper();
        }
        assertEquals(10*NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(10*780, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }

    // @Transactional // annotation doesn't work for some reason...
    private void findAllPersonsMultiHelper() {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status=transactionManager.getTransaction(defaultTransactionDefinition);
        val persons = cacheablePersonRepository.findAll();
        assertThat(persons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
        for (val person : persons) {
            assertNotNull(person.getAddresses());
            assertTrue(person.getAddresses().size() >= 0);
        }
        transactionManager.rollback(status);
    }
    
    @Test @Transactional
    public void findByLastName() {
        val person = cacheablePersonRepository.findByLastName("lastName-9");
        assertThat(person, CoreMatchers.notNullValue());
        assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertThat(person.getAddresses(), CoreMatchers.notNullValue());
        assertThat(person.getAddresses().size(), CoreMatchers.is(9));
        assertEquals(1, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(9, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
    
    @Test //@Transactional
    public void findByLastName2() {
        log.trace("->");
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status=transactionManager.getTransaction(defaultTransactionDefinition);
        val person = cacheablePersonRepository.findByLastName("lastName-9");
        assertThat(person, CoreMatchers.notNullValue());
        assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertThat(person.getAddresses(), CoreMatchers.notNullValue());
        assertThat(person.getAddresses().size(), CoreMatchers.is(9));
        transactionManager.rollback(status);
        assertEquals(1, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(9, CacheableAddress.getNoArgsConstructorCallCounter().get());

        defaultTransactionDefinition = new DefaultTransactionDefinition();
        status=transactionManager.getTransaction(defaultTransactionDefinition);
        val person9 = cacheablePersonRepository.findByLastName("lastName-9");
        assertThat(person9, CoreMatchers.notNullValue());
        assertThat(person9.getFirstName(), CoreMatchers.is("firstName-9"));
        assertThat(person9.getAddresses(), CoreMatchers.notNullValue());
        assertThat(person9.getAddresses().size(), CoreMatchers.is(9));
        transactionManager.rollback(status);
        assertEquals(1, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(9, CacheableAddress.getNoArgsConstructorCallCounter().get());
        log.trace("<-");
    }
    
    @Test
    public void findByLastNameNotFound() {
        val person = cacheablePersonRepository.findByLastName("lastNameNotFound");
        assertThat(person, CoreMatchers.nullValue());
        //assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertEquals(0, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
    
    @Test(expected=IncorrectResultSizeDataAccessException.class)
    public void findByLastNameMulti() {
        CacheablePerson person = CacheablePerson.builder().firstName("uli").lastName("lastName-9").build();
        cacheablePersonRepository.saveAndFlush(person);
        person = cacheablePersonRepository.findByLastName("lastName-9");
        //assertThat(person, CoreMatchers.notNullValue());
        //assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertEquals(NUMBER_OF_PERSONS, CacheablePerson.getNoArgsConstructorCallCounter().get());
        assertEquals(0, CacheableAddress.getNoArgsConstructorCallCounter().get());
    }
    
    @Configuration
    @EnableCaching
    @Import(org.uli.springdatajpa.repositories.TestConfig.class)
    static class MyTestConfig {
        @Bean
        public CacheManager cacheManager() {
            log.trace("->");
          GuavaCacheManager guavaCacheManager = new GuavaCacheManager();
          // you cannot return an "empty" cache manager, a NullPointerException will be thrown
          Cache<Object, ValueWrapper> dummyCache = CacheBuilder.newBuilder().build();
          GuavaCache dummyGuavaCache = new GuavaCache("dummyCache", dummyCache);
          guavaCacheManager.setCaches(Arrays.asList(dummyGuavaCache));
          log.trace("<- cacheManager={}", guavaCacheManager);
          return guavaCacheManager;
        }
    }
}
