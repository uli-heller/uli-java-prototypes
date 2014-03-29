/**
 * 
 */
package org.uli.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author uli
 *
 */
public class BeanPropertySetterTest {
    @Test
    public void standardJdk() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter();
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
        BeanPropertySetter bps = new BeanPropertySetter();
        bps.setPropertyBeanUtils(p,  "personId", 1);
        bps.setPropertyBeanUtils(p,  "firstName", "Uli");
        bps.setPropertyBeanUtils(p,  "lastName", "Heller");
        assertEquals(Integer.valueOf(1), p.getPersonId());
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }

    @Test
    public void spring() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter();
        bps.setPropertySpring(p,  "personId", 1);
        bps.setPropertySpring(p,  "firstName", "Uli");
        bps.setPropertySpring(p,  "lastName", "Heller");
        assertEquals(Integer.valueOf(1), p.getPersonId());
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }
}
