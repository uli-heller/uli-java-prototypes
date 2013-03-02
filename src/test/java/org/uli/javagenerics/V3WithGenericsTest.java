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
public class V3WithGenericsTest {
    /**
     * Test method for {@link org.uli.javagenerics.V3WithGenerics#get(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testGetString() {
        String s = V3WithGenerics.getInstance().get("String");
        assertEquals("a string", s);
    }

    @Test
    public void testGetBoolean() {
        Boolean b = V3WithGenerics.getInstance().get("Boolean");
        assertEquals(true, b);
    }

    @Test
    public void testGetLong() {
        Long l = V3WithGenerics.getInstance().get("Long");
        assertEquals((Long) 42L, l);
    }

    @Test(expected = ClassCastException.class)
    public void testGetStringException() {
        String s = V3WithGenerics.getInstance().get("Boolean");
        assertEquals("a string", s); // never reached
    }
}
