// http://vafer.org/blog/20061010091658/
// Obsolete, do not use
package org.uli.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIServerSocketFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.net.ServerSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JmxServletV2 extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static LocalJMXPort localJMXPort = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        int port = 11223;
        if (pathInfo != null) {
            if (pathInfo.startsWith("/start")) {
                String portString = request.getParameter("port");
                if (portString != null) {
                    port = Integer.parseInt(portString);
                }
                start(port);
            } else if (pathInfo.startsWith("/stop")) {
                stop();
            }
        }
    }

    private void start(int port) {
        synchronized (JmxServletV2.class) {
            stop();
            localJMXPort = new LocalJMXPort(port);
            try {
                localJMXPort.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void stop() {
        synchronized (JmxServletV2.class) {
            if (localJMXPort != null) {
                localJMXPort.stop();
                localJMXPort = null;
            }
        }
    }

    static public class LocalJMXPort {

        JMXConnectorServer rmiServer;

        public LocalJMXPort(int protocolPort) {
            this(protocolPort, protocolPort);
        }

        public LocalJMXPort(int protocolPort, int namingPort) {
            this.protocolPort = protocolPort;
            this.namingPort = namingPort;
            this.rmiServer = null;
        }

        public void stop() {
            if (rmiServer != null) {
                try {
                    rmiServer.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rmiServer = null;
            }
        }
        int protocolPort;
        int namingPort;
        String address = "localhost"; // bindingName;

        public void start() throws MalformedURLException, IOException {
            RMIServerSocketFactory serverFactory = new RMIServerSocketFactoryImpl(InetAddress.getByName(address));
            LocateRegistry.createRegistry(namingPort, null, serverFactory);
            StringBuffer url = new StringBuffer();
            url.append("service:jmx:");
            url.append("rmi://").append(address).append(':').append(protocolPort).append("/jndi/");
            url.append("rmi://").append(address).append(':').append(namingPort).append("/jmxrmi"); //append("/connector");
            Map<String, Object> env = new HashMap<String, Object>();
            env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, serverFactory);
            rmiServer = new RMIConnectorServer(
                        new JMXServiceURL(url.toString()),
                        env,
                        ManagementFactory.getPlatformMBeanServer()
                        );
            rmiServer.start();
        }

        static public class RMIServerSocketFactoryImpl implements RMIServerSocketFactory {

            private final InetAddress localAddress;

            public RMIServerSocketFactoryImpl(final InetAddress pAddress) {
                localAddress = pAddress;
            }

            public ServerSocket createServerSocket(final int pPort) throws IOException {
                return ServerSocketFactory.getDefault().createServerSocket(pPort, 0, localAddress);
            }

            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (obj == this) {
                    return true;
                }
                return obj.getClass().equals(getClass());
            }

            public int hashCode() {
                return RMIServerSocketFactoryImpl.class.hashCode();
            }
        }
    }
}
