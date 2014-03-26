/**
 * 
 */
package org.uli.resultset;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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
public class ResultSetMapperTest {
    @Autowired SessionFactory sessionFactory;
    @Autowired DataSource dataSource;

    private static boolean fInitialized = false;
    private static List<Person> persons = new LinkedList<Person>();

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
                persons.add(person);
            }
            fInitialized = true;
        }
        session.close();
    }

    @Test
    public void testNotEmpty() throws Exception {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select * from person");
        ResultSet rs = ps.executeQuery();
        assertTrue(rs.first());
    }

    @Test
    public void testPersons() throws Exception {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select * from person order by id");
        ResultSet rs = ps.executeQuery();
        ResultSetMapper<Person> rsm = new ResultSetMapper<Person>();
        List<Person> thesePersons = rsm.mapResultSetToObject(rs, Person.class);
        assertTrue(thesePersons.size() > 0);
        assertEquals(persons, thesePersons);
    }
}
