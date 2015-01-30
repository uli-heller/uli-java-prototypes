/**
 * 
 */
package org.uli.springdatajpa.repositories;

import static org.junit.Assert.assertThat;

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
import org.springframework.transaction.annotation.Transactional;
import org.uli.springdatajpa.entities.LombokAddress;
import org.uli.springdatajpa.entities.LombokPerson;

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Slf4j
public class LombokPersonRepositoryTest {
    @Autowired
    LombokPersonRepository lombokPersonRepository;
    @Autowired
    SessionFactory sessionFactory;
   
    private static AtomicBoolean fInitialized = new AtomicBoolean(false);

    private final static int NUMBER_OF_PERSONS=40;
    
    @Before
    public void setup() {
        if (!fInitialized.getAndSet(true)) {
            log.debug("Initialize persons");
            Session session = sessionFactory.openSession();
            Query delete = session.createQuery("delete from LombokPerson p");
            delete.executeUpdate();
            for (int i=0; i<NUMBER_OF_PERSONS; i++) {
                LombokPerson person = new LombokPerson();
                person.setFirstName("firstName-"+i);
                person.setLastName("lastName-"+i);
                session.saveOrUpdate(person);
                val addresses = createAddresses(session, person, i);
            }
            session.close();
        }
    }

    private List<LombokAddress> createAddresses(Session s, LombokPerson p, int number) {
        List<LombokAddress> result = new LinkedList<LombokAddress>();
        for (int i=0; i<number; i++) {
            LombokAddress a = LombokAddress.builder().street("street - "+p.getLastName()).city("city - "+p.getLastName()).build();
            a.setPersonId(p.getPersonId());
            result.add(a);
            s.saveOrUpdate(a);
        }
        return result;
    }
    @Test
    public void findAllLombokPersons() {
        val lombokPersons = lombokPersonRepository.findAll();
        assertThat(lombokPersons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
    }
    
    @Test @Transactional
    public void findByLastName() {
        val person = lombokPersonRepository.findByLastName("lastName-9");
        assertThat(person, CoreMatchers.notNullValue());
        assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        assertThat(person.getAddresses(), CoreMatchers.notNullValue());
        assertThat(person.getAddresses().size(), CoreMatchers.is(9));
    }
    
    @Test
    public void findByLastNameNotFound() {
        val person = lombokPersonRepository.findByLastName("lastNameNotFound");
        assertThat(person, CoreMatchers.nullValue());
        //assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
    }
    
    @Test(expected=IncorrectResultSizeDataAccessException.class)
    public void findByLastNameMulti() {
        LombokPerson person = LombokPerson.builder().firstName("uli").lastName("lastName-9").build();
        lombokPersonRepository.saveAndFlush(person);
        person = lombokPersonRepository.findByLastName("lastName-9");
        //assertThat(person, CoreMatchers.notNullValue());
        //assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        }
}
