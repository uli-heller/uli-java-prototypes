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
public class V2WithGenericsTest {
    /**
     * Test method for {@link org.uli.javagenerics.V2WithGenerics#get(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testGetString() {
        String s = V2WithGenerics.getInstance().getString("String");
        assertEquals("a string", s);
    }

    @Test
    public void testGetBoolean() {
        Boolean b = V2WithGenerics.getInstance().getBoolean("Boolean");
        assertEquals(true, b);
    }

    @Test
    public void testGetLong() {
        Long l = V2WithGenerics.getInstance().getLong("Long");
        assertEquals((Long) 42L, l);
    }

    @Test(expected = ClassCastException.class)
    public void testGetStringException() {
        String s = V2WithGenerics.getInstance().getString("Boolean");
        assertEquals("a string", s); // never reached
    }
}
