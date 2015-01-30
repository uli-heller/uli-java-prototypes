/**
 * 
 */
package org.uli.springdatajpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.uli.springdatajpa.entities.LombokPerson;
import org.uli.springdatajpa.entities.TraditionalPerson;

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
            }
            session.close();
        }
    }

    @Test
    public void findAllLombokPersons() {
        val lombokPersons = lombokPersonRepository.findAll();
        assertThat(lombokPersons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
    }
    
    @Test
    public void findByLastName() {
        val person = lombokPersonRepository.findByLastName("lastName-9");
        assertThat(person, CoreMatchers.notNullValue());
        assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
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
        assertThat(person, CoreMatchers.notNullValue());
        assertThat(person.getFirstName(), CoreMatchers.is("firstName-9"));
        }
}
