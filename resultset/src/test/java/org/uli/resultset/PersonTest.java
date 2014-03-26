/**
 * 
 */
package org.uli.resultset;

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

/**
 * @author uli
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class PersonTest {
    @Autowired SessionFactory sessionFactory;

    private static boolean fInitialized = false;
    private static Map<Integer, Person> persons = new HashMap<Integer, Person>();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Session session = sessionFactory.openSession();
        if (! fInitialized) {
            Query delete = session.createQuery("delete from Person p");
            delete.executeUpdate();
            for (int i=0; i<40; i++) {
                Person person = new Person();
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
        Query query = session.createQuery("select p from Person p");
        @SuppressWarnings("unchecked")
        List<Object> resultList = query.list();
        assertFalse(resultList.isEmpty());
        assertEquals(persons.size(), resultList.size());
        session.close();
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
    }
}
