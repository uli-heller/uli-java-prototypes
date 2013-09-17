/**
 * 
 */
package org.uli.jmx;

import java.io.IOException;


/**
 * @author uli
 *
 */
public class MainV3 {
    static public void main(String[] args) throws IOException {
        int port=22334;
        int i=0;
        if (args.length > i) {
            port = Integer.parseInt(args[i++]);
        }
        JmxServletV3.LocalJMXPort ljp = new JmxServletV3.LocalJMXPort(port);
        JmxServletV3.LocalJMXPort.jmxHandle jmxHandle = ljp.start();
        System.in.read();
        jmxHandle.stop();
    }
}
