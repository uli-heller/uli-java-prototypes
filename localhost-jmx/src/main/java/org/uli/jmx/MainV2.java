/**
 * Obsolete, do not use
 */
package org.uli.jmx;

import java.io.IOException;


/**
 * @author uli
 *
 */
public class MainV2 {
    static public void main(String[] args) throws IOException {
        int protocolPort=22334;
        int namingPort=22335;
        int i=0;
        if (args.length > i) {
            protocolPort = Integer.parseInt(args[i++]);
            namingPort = protocolPort;
        }
        if (args.length > i) {
            namingPort = Integer.parseInt(args[i++]);
        }
        JmxServletV2.LocalJMXPort ljp = new JmxServletV2.LocalJMXPort(protocolPort, namingPort);
        ljp.start();
        System.in.read();
        ljp.stop();
    }
}
