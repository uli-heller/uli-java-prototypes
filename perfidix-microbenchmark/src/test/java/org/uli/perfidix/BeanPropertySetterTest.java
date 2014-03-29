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
    public void basic() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter();
        bps.setProperty(p,  "personId", 1);
        bps.setProperty(p,  "firstName", "Uli");
        bps.setPropertyBeanUtils(p,  "lastName", "Heller");
        assertEquals("Uli", p.getFirstName());
        assertEquals("Heller", p.getLastName());
    }
}
