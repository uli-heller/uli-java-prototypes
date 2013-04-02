package org.uli.hexdump;

import static org.junit.Assert.*;
import org.junit.Test;

public class HexDumpTest {
    @Test
    public void test1() {
	String one="1";
	String hexDump=HexDump.hexDump(one.getBytes());
	assertEquals("00000000: 31                                   '1'"+System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test12345678() {
	String one="12345678";
	String hexDump=HexDump.hexDump(one.getBytes());
	assertEquals("00000000: 31323334 35363738                    '12345678'"+System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test1234567890123456() {
	String one="1234567890123456";
	String hexDump=HexDump.hexDump(one.getBytes());
	assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'"+System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test123456789012345612345678() {
	String one="123456789012345612345678";
	String hexDump=HexDump.hexDump(one.getBytes());
	assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'"
                     +System.getProperty("line.separator")
                     +"00000010: 31323334 35363738                    '12345678'"
                     +System.getProperty("line.separator"), hexDump);
    }
}
