/**
 * 
 */
package org.uli;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import sun.net.spi.nameservice.NameService;

public class MyNameService implements NameService {
    EtcHosts etcHosts;

    public MyNameService() throws Exception {
        InputStream is = MyNameService.class.getResourceAsStream("/etc_hosts");
        etcHosts = EtcHosts.from(is);
    }

    public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
        InetAddress ia = this.etcHosts.getByHostName(host);
        if (ia == null) {
            throw new UnknownHostException(host);
        }
        return new InetAddress[]{ia};
    }

    public String getHostByAddr(byte[] addr) throws UnknownHostException {
        InetAddress ia = this.etcHosts.getByAddress(addr);
        if (ia == null) {
            throw new UnknownHostException(Arrays.toString(addr));
        }
        return ia.getHostName();
    }
}
