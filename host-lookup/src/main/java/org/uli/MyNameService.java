/**
 * 
 */
package org.uli;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.dns.DNSNameService;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyNameService implements NameService {

    static {
        // System.setProperty("sun.net.spi.nameservice.nameservers",
        // "localhost"); // ETL
    }
    NameService ns;

    public MyNameService() throws Exception {
        ns = new DNSNameService();
    }

    public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
        System.out.println("*** lookup host = " + host);
        byte[] a = new byte[4];
        a[0] = (byte) 127;
        a[1] = 0;
        a[2] = 0;
        a[3] = (byte) 1;
        InetAddress[] ia = new InetAddress[1];
        ia[0] = InetAddress.getByAddress(host, a);
        return ia;
    }

    public String getHostByAddr(byte[] addr) throws UnknownHostException {
        throw new Error("not implemented");
    }
}
