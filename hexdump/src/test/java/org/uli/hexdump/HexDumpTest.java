package org.uli.hexdump;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class HexDumpTest {

    @Test
    public void test1() {
        String one = "1";
        String hexDump = HexDump.hexDump(one.getBytes());
        assertEquals("00000000: 31                                   '1'" + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test12345678() {
        String oneToEight = "12345678";
        String hexDump = HexDump.hexDump(oneToEight.getBytes());
        assertEquals("00000000: 31323334 35363738                    '12345678'" + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test1234567890123456() {
        String oneToSixteen = "1234567890123456";
        String hexDump = HexDump.hexDump(oneToSixteen.getBytes());
        assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'" + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test123456789012345612345678() {
        String oneToTwentyFour = "123456789012345612345678";
        String hexDump = HexDump.hexDump(oneToTwentyFour.getBytes());
        assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'"
                    + System.getProperty("line.separator")
                    + "00000010: 31323334 35363738                    '12345678'"
                    + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void testS0x3f() {
        String s3f = new String(new byte[] { 0x3f });
        String hexDump = HexDump.hexDump(s3f.getBytes());
        assertEquals("00000000: 3f                                   '?'"
                    + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void testS0x12() {
        String s12 = new String(new byte[] { 0x12 });
        String hexDump = HexDump.hexDump(s12.getBytes());
        assertEquals("00000000: 12                                   '.'"
                    + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void testS0x7f() {
        String s7f = new String(new byte[] { 0x7f });
        String hexDump = HexDump.hexDump(s7f.getBytes());
        assertEquals("00000000: 7f                                   '.'"
                    + System.getProperty("line.separator"), hexDump);
    }
    
    @Test
    public void testDumpToList() {
        String oneToTwentyFour = "123456789012345612345678";
        List<String> hexDump = new HexDump().dumpToList(oneToTwentyFour.getBytes());
        assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'", hexDump.get(0));
        assertEquals("00000010: 31323334 35363738                    '12345678'",         hexDump.get(1));
    }
}
