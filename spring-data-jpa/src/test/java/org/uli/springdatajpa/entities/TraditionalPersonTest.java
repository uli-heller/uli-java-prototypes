/**
 * 
 */
package org.uli.springdatajpa.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TraditionalPersonTest {
    @Autowired SessionFactory sessionFactory;

    private static boolean fInitialized = false;
    private static Map<Integer, TraditionalPerson> persons = new HashMap<Integer, TraditionalPerson>();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Session session = sessionFactory.openSession();
        if (! fInitialized) {
            Query delete = session.createQuery("delete from TraditionalPerson p");
            delete.executeUpdate();
            for (int i=0; i<40; i++) {
                TraditionalPerson person = new TraditionalPerson();
                person.setFirstName("firstName-"+i);
                person.setLastName("lastName-"+i);
                session.saveOrUpdate(person);
                persons.put(person.getPersonId(), person);
            }
            fInitialized = true;
        }
        session.close();
    }

    @Test
    public void testNotEmpty() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select p from TraditionalPerson p");
        @SuppressWarnings("unchecked")
        List<Object> resultList = query.list();
        assertFalse(resultList.isEmpty());
        assertEquals(persons.size(), resultList.size());
        session.close();
    }
    
    @Test
    public void testFind() {
        Session session = sessionFactory.openSession();
        for (TraditionalPerson p : persons.values()) {
            Integer personId = p.getPersonId();
            TraditionalPerson dbPerson = (TraditionalPerson) session.get(TraditionalPerson.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
        }
        session.close();
    }
}
