/**
 * 
 */
package org.uli.lombokjpa;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author uli
 *
 */
public class AnotherLombokPersonTest {
    private static final String PERSISTENCE_UNIT_NAME = "jpa";
    private static EntityManagerFactory entityManagerFactory;

    private static Map<Integer, AnotherLombokPerson> persons = new HashMap<Integer, AnotherLombokPerson>();

    @BeforeClass
    static public void initEm() {
        Map<String,String> persistenceProperties = new HashMap<String,String>();
        persistenceProperties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        persistenceProperties.put("javax.persistence.jdbc.url","jdbc:h2:h2Db"); 
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceProperties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query delete = entityManager.createQuery("delete from AnotherLombokPerson p");
	entityManager.getTransaction().begin();
        delete.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select p from AnotherLombokPerson p");

        if (query.getResultList().isEmpty()) {
            // Create new persons
            entityManager.getTransaction().begin();
            for (int i=0; i<40; i++) {
                AnotherLombokPerson person = AnotherLombokPerson.builder()
                  .firstName("firstName-"+i)
                  .lastName("lastName-"+i)
		  .build();
                entityManager.persist(person);
                persons.put(person.getPersonId(), person);
            }
            entityManager.getTransaction().commit();
        }
        entityManager.close();
    }

    @Test
    public void testNotEmpty() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select p from AnotherLombokPerson p");
        @SuppressWarnings("unchecked")
        List<Object> resultList = query.getResultList();
        assertFalse(resultList.isEmpty());
        assertEquals(persons.size(), resultList.size());
        entityManager.close();
    }
    
    @Test
    public void testFind() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        for (AnotherLombokPerson p : persons.values()) {
            Integer personId = p.getPersonId();
            AnotherLombokPerson dbPerson = entityManager.find(AnotherLombokPerson.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
        }
        entityManager.close();
    }

    @Test
    public void testGetReference() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        for (AnotherLombokPerson p : persons.values()) {
            Integer personId = p.getPersonId();
            AnotherLombokPerson dbPerson = entityManager.getReference(AnotherLombokPerson.class, personId);
            assertEquals("Person-"+personId+", personId:",  personId, dbPerson.getPersonId());
            assertEquals("Person-"+personId+", firstName:", p.getFirstName(), dbPerson.getFirstName());
            assertEquals("Person-"+personId+", lastName:",  p.getLastName(),  dbPerson.getLastName());
        }
        entityManager.close();
    }
}
