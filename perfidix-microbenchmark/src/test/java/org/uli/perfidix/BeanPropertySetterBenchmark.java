/**
 * 
 */
package org.uli.perfidix;

import org.perfidix.annotation.Bench;

/**
 * @author uli
 *
 */
public class BeanPropertySetterBenchmark {
    @Bench
    public void standardJava() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter();
        bps.setProperty(p, "personId", 1);
        bps.setProperty(p, "firstName", "Uli");
        bps.setProperty(p,  "lastName", "Heller");
    }

    @Bench
    public void commonsBeanUtils() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter();
        bps.setPropertyBeanUtils(p, "personId", 1);
        bps.setPropertyBeanUtils(p, "firstName", "Uli");
        bps.setPropertyBeanUtils(p,  "lastName", "Heller");
    }
}
