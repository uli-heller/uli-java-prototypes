package org.uli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Test;

public class EtcHostsTest {
    @Test
    public void testLocalhost() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("127.0.0.1 localhost");
        assertEquals(1, l.size());
        assertEquals("localhost", l.get(0).getHostName());
        assertEquals("127.0.0.1", l.get(0).getHostAddress());
    }

    @Test
    public void testLocaluli() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("127.128.129.130 localuli");
        assertEquals(1, l.size());
        assertEquals("localuli", l.get(0).getHostName());
        assertEquals("127.128.129.130", l.get(0).getHostAddress());
    }

    @Test
    public void testLocalhost6() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("::1 localhost");
        assertEquals(1, l.size());
        assertEquals("localhost", l.get(0).getHostName());
        assertEquals("0:0:0:0:0:0:0:1", l.get(0).getHostAddress());
    }

    @Test
    public void testMulti() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("127.128.129.130 localuli localuli.daemons-point.com a.b.c");
        assertEquals(3, l.size());
        assertEquals("localuli", l.get(0).getHostName());
        assertEquals("127.128.129.130", l.get(0).getHostAddress());
        assertEquals("localuli.daemons-point.com", l.get(1).getHostName());
        assertEquals("127.128.129.130", l.get(1).getHostAddress());
        assertEquals("a.b.c", l.get(2).getHostName());
        assertEquals("127.128.129.130", l.get(2).getHostAddress());
    }

    @Test
    public void testEmpty() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("   ");
        assertEquals(0, l.size());
    }

    @Test
    public void testComment() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("   #comment ");
        assertEquals(0, l.size());
    }

    @Test
    public void testMultiComment() throws UnknownHostException {
        EtcHosts etcHosts = new EtcHosts();
        List<InetAddress> l = etcHosts.parseLine("127.128.129.130 localuli localuli.daemons-point.com a.b.c #Comment");
        assertEquals(3, l.size());
        assertEquals("localuli", l.get(0).getHostName());
        assertEquals("127.128.129.130", l.get(0).getHostAddress());
        assertEquals("localuli.daemons-point.com", l.get(1).getHostName());
        assertEquals("127.128.129.130", l.get(1).getHostAddress());
        assertEquals("a.b.c", l.get(2).getHostName());
        assertEquals("127.128.129.130", l.get(2).getHostAddress());
    }

    @Test
    public void testEtcHosts() throws IOException {
        EtcHosts etcHosts = EtcHosts.from(new ByteArrayInputStream("127.128.129.130 localuli #comment\r\n127.128.129.131 localuli2 localuli2.daemons-point.com a.b.c #Comment".getBytes()));
        InetAddress ia = etcHosts.getByHostName("localuli");
        assertEquals("localuli", ia.getHostName());
        assertEquals("127.128.129.130", ia.getHostAddress());
        ia = etcHosts.getByHostName("localuli.daemons-point.com");
        assertNull(ia);
        ia = etcHosts.getByHostName("localuli2");
        assertEquals("localuli2", ia.getHostName());
        assertEquals("127.128.129.131", ia.getHostAddress());
        ia = etcHosts.getByHostName("localuli2.daemons-point.com");
        assertEquals("localuli2.daemons-point.com", ia.getHostName());
        assertEquals("127.128.129.131", ia.getHostAddress());

        etcHosts.merge(new ByteArrayInputStream("127.128.129.132 localuli2".getBytes()));
        ia = etcHosts.getByHostName("localuli2");
        assertEquals("localuli2", ia.getHostName());
        assertEquals("127.128.129.132", ia.getHostAddress());
    }

    @Test
    public void testEtcHostsReverse() throws IOException {
        EtcHosts etcHosts = EtcHosts.from(new ByteArrayInputStream("127.128.129.130 localuli #comment\r\n127.128.129.131 localuli2 localuli2.daemons-point.com a.b.c #Comment".getBytes()));
        InetAddress ia = etcHosts.getByAddress("127.128.129.130");
        assertEquals("localuli", ia.getHostName());
        assertEquals("127.128.129.130", ia.getHostAddress());

        ia = etcHosts.getByAddress("localuli");
        assertNull(ia);

        // When there are multiple hostnames in one line, return the first one
        ia = etcHosts.getByAddress("127.128.129.131");
        assertEquals("localuli2", ia.getHostName());
        assertEquals("127.128.129.131", ia.getHostAddress());

        // When additional ip addresses are merged, return the associated hostname
        etcHosts.merge(new ByteArrayInputStream("127.128.129.132 localuli2".getBytes()));
        ia = etcHosts.getByAddress("127.128.129.132");
        assertEquals("localuli2", ia.getHostName());
        assertEquals("127.128.129.132", ia.getHostAddress());

        // When an existing ip address is merged, return the associated hostname
        etcHosts.merge(new ByteArrayInputStream("127.128.129.130 a b c".getBytes()));
        ia = etcHosts.getByAddress("127.128.129.130");
        assertEquals("a", ia.getHostName());
        assertEquals("127.128.129.130", ia.getHostAddress());

        // When an existing ip address is merged in multiple lines, return the first associated hostname
        etcHosts.merge(new ByteArrayInputStream("127.128.129.130 d e f\r\n127.128.129.130 g h i".getBytes()));
        ia = etcHosts.getByAddress("127.128.129.130");
        assertEquals("d", ia.getHostName());
        assertEquals("127.128.129.130", ia.getHostAddress());
    }
}
