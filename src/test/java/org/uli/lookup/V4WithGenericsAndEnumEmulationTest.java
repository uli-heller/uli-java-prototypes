/**
 * 
 */
package org.uli.lookup;

import static org.junit.Assert.*;

import org.junit.Test;
import org.uli.lookup.V4WithGenericsAndEnumEmulation;


/**
 * @author uli
 *
 */
public class V4WithGenericsAndEnumEmulationTest {
    /**
     * Test method for {@link org.uli.lookup.V4WithGenericsAndEnumEmulation#get(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testGetString() {
        String s = V4WithGenericsAndEnumEmulation.getInstance().get(V4WithGenericsAndEnumEmulation.Element.STRING_ELEMENT);
        assertEquals("a string", s);
    }

    @Test
    public void testGetBoolean() {
        Boolean b = V4WithGenericsAndEnumEmulation.getInstance().get(V4WithGenericsAndEnumEmulation.Element.BOOLEAN_ELEMENT);
        assertEquals(true, b);
    }

    @Test
    public void testGetLong() {
        Long l = V4WithGenericsAndEnumEmulation.getInstance().get(V4WithGenericsAndEnumEmulation.Element.LONG_ELEMENT);
        assertEquals((Long) 42L, l);
    }

    //@Test(expected = ClassCastException.class)
    public void testGetStringException() {
        // The line below leads to a compiler error
        // -> This test doesn't make any sense
        //String s = V4WithGenericsAndEnum.getInstance().get(V4WithGenericsAndEnum.Element.BOOLEAN_ELEMENT);
    }
}
