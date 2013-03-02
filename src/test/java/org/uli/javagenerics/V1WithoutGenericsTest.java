/**
 * 
 */
package org.uli.javagenerics;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author uli
 *
 */
public class V1WithoutGenericsTest {
    /**
     * Test method for {@link org.uli.javagenerics.V1WithoutGenerics#get(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testGetString() {
        String s = (String) V1WithoutGenerics.getInstance().get("String");
        assertEquals("a string", s);
    }

    @Test
    public void testGetBoolean() {
        Boolean b = (Boolean) V1WithoutGenerics.getInstance().get("Boolean");
        assertEquals(true, b);
    }

    @Test
    public void testGetLong() {
        Long l = (Long) V1WithoutGenerics.getInstance().get("Long");
        assertEquals((Long) 42L, (Long) l);
    }

    @Test(expected = ClassCastException.class)
    public void testGetStringException() {
        String s = (String) V1WithoutGenerics.getInstance().get("Boolean");
        assertEquals("a string", s); // never reached
    }
}
