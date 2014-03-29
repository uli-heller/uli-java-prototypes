/**
 * 
 */
package org.uli.perfidix;

import org.perfidix.annotation.Bench;
import org.uli.perfidix.BeanPropertySetter.Implementation;

/**
 * @author uli
 *
 */
public class BeanPropertySetterBenchmark {
    @Bench
    public void standardJava() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.JAVA);
        bps.setProperty(p, "personId", 1);
        bps.setProperty(p, "firstName", "Uli");
        bps.setProperty(p, "lastName", "Heller");
    }

    @Bench
    public void cachedJava() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.JAVA_METHOD_CACHE);
        bps.setProperty(p, "personId", 1);
        bps.setProperty(p, "firstName", "Uli");
        bps.setProperty(p, "lastName", "Heller");
    }

    @Bench
    public void commonsBeanUtils() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.BEANUTILS);
        bps.setProperty(p, "personId", 1);
        bps.setProperty(p, "firstName", "Uli");
        bps.setProperty(p, "lastName", "Heller");
    }

    @Bench
    public void spring() throws Exception {
        Person p = new Person();
        BeanPropertySetter bps = new BeanPropertySetter(Implementation.SPRING);
        bps.setProperty(p, "personId", 1);
        bps.setProperty(p, "firstName", "Uli");
        bps.setProperty(p, "lastName", "Heller");
    }
}
