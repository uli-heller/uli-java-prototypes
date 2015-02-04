/**
 * 
 */
package org.uli.jmx;

import java.io.IOException;


/**
 * @author uli
 *
 */
public class Main {
    static public void main(String[] args) throws IOException {
        int port=22334;
        int i=0;
        if (args.length > i) {
            port = Integer.parseInt(args[i++]);
        }
        JmxServlet.LocalJMXPort ljp = new JmxServlet.LocalJMXPort(port);
        JmxServlet.LocalJMXPort.jmxHandle jmxHandle = ljp.start();
        System.in.read();
        jmxHandle.stop();
    }
}
