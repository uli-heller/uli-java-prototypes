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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uli.springdatajpa.entities.TraditionalPerson;

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Slf4j
public class TraditionalPersonRepositoryTest {
    @Autowired
    TraditionalPersonRepository traditionalPersonRepository;
    @Autowired
    SessionFactory sessionFactory;
   
    private static AtomicBoolean fInitialized = new AtomicBoolean(false);

    private final static int NUMBER_OF_PERSONS=40;
    
    @Before
    public void setup() {
        if (!fInitialized.getAndSet(true)) {
            log.debug("Initialize persons");
            Session session = sessionFactory.openSession();
            Query delete = session.createQuery("delete from TraditionalPerson p");
            delete.executeUpdate();
            for (int i=0; i<NUMBER_OF_PERSONS; i++) {
                TraditionalPerson person = new TraditionalPerson();
                person.setFirstName("firstName-"+i);
                person.setLastName("lastName-"+i);
                session.saveOrUpdate(person);
            }
            session.close();
        }
    }

    @Test
    public void findAllTraditionalPersons() {
        val traditionalPersons = traditionalPersonRepository.findAll();
        assertThat(traditionalPersons.size(), CoreMatchers.is(NUMBER_OF_PERSONS));
    }
}
