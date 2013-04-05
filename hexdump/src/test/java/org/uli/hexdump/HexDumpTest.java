package org.uli.hexdump;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

public class HexDumpTest {

    @Test
    public void test1() {
        String one = "1";
        String hexDump = HexDump.hexDump(one.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 31                                   '1'" + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test12345678() {
        String oneToEight = "12345678";
        String hexDump = HexDump.hexDump(oneToEight.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 31323334 35363738                    '12345678'" + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test1234567890123456() {
        String oneToSixteen = "1234567890123456";
        String hexDump = HexDump.hexDump(oneToSixteen.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'" + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void test123456789012345612345678() {
        String oneToTwentyFour = "123456789012345612345678";
        String hexDump = HexDump.hexDump(oneToTwentyFour.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'"
                    + System.getProperty("line.separator")
                    + "00000010: 31323334 35363738                    '12345678'"
                    + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void testS0x3f() {
        String s3f = new String(new byte[] { 0x3f }, Charset.defaultCharset());
        String hexDump = HexDump.hexDump(s3f.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 3f                                   '?'"
                    + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void testS0x12() {
        String s12 = new String(new byte[] { 0x12 }, Charset.defaultCharset());
        String hexDump = HexDump.hexDump(s12.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 12                                   '.'"
                    + System.getProperty("line.separator"), hexDump);
    }

    @Test
    public void testS0x7f() {
        String s7f = new String(new byte[] { 0x7f }, Charset.defaultCharset());
        String hexDump = HexDump.hexDump(s7f.getBytes(Charset.defaultCharset()));
        assertEquals("00000000: 7f                                   '.'"
                    + System.getProperty("line.separator"), hexDump);
    }
    
    @Test
    public void testDumpToList() {
        String oneToTwentyFour = "123456789012345612345678";
        List<String> hexDump = HexDump.builder().build().dumpToList(oneToTwentyFour.getBytes(Charset.defaultCharset()));
        assertEquals(2, hexDump.size());
        assertEquals("00000000: 31323334 35363738 39303132 33343536  '1234567890123456'", hexDump.get(0));
        assertEquals("00000010: 31323334 35363738                    '12345678'",         hexDump.get(1));
    }

    @Test
    public void testHexOnly() {
        String oneToTwentyFour = "123456789012345612345678";
        HexDump hd = HexDump.builder().bytesPerLine(32).dumpHex(true).dumpText(false).build();
        List<String> hexDump = hd.dumpToList(oneToTwentyFour.getBytes(Charset.defaultCharset()));
        assertEquals(1, hexDump.size());
        assertEquals("00000000: 31323334 35363738 39303132 33343536 31323334 35363738", hexDump.get(0));
    }

    @Test
    public void testTextOnly() {
        String oneToTwentyFour = "123456789012345612345678";
        HexDump hd = HexDump.builder().bytesPerLine(32).dumpHex(false).dumpText(true).build();
        List<String> hexDump = hd.dumpToList(oneToTwentyFour.getBytes(Charset.defaultCharset()));
        assertEquals(1, hexDump.size());
        assertEquals("00000000:  '123456789012345612345678'", hexDump.get(0));
    }
}
