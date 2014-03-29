/**
 * 
 */
package org.uli.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.uli.perfidix.BeanPropertySetter.Implementation;

/**
 * @author uli
 *
 */
public class BeanPropertySetterTest {
    @Test
    public void standardJdk() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.JAVA);
        bps.setProperty(p,  "personId", 1);
        bps.setProperty(p,  "firstName", "Uli");
        bps.setProperty(p,  "lastName", "Heller");
        assertEquals(Integer.valueOf(1), p.getPersonId());
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }

    @Test
    public void cachedJdk() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.JAVA_METHOD_CACHE);
        bps.setProperty(p,  "personId", 1);
        bps.setProperty(p,  "firstName", "Uli");
        bps.setProperty(p,  "lastName", "Heller");
        assertEquals(Integer.valueOf(1), p.getPersonId());
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }

    @Test
    public void beanUtils() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.BEANUTILS);
        bps.setProperty(p,  "personId", 1);
        bps.setProperty(p,  "firstName", "Uli");
        bps.setProperty(p,  "lastName", "Heller");
        assertEquals(Integer.valueOf(1), p.getPersonId());
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }

    @Test
    public void spring() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.SPRING);
        bps.setProperty(p,  "personId", 1);
        bps.setProperty(p,  "firstName", "Uli");
        bps.setProperty(p,  "lastName", "Heller");
        assertEquals(Integer.valueOf(1), p.getPersonId());
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }
}
