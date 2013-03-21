// http://stackoverflow.com/questions/347056/restricting-jmx-to-localhost
package org.uli.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JmxServlet extends HttpServlet {
    static private final String BINDING_ADDRESS="127.0.0.1";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static LocalJMXPort.jmxHandle jmxHandle = null;;

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

    private synchronized void start(int port) {
        if (jmxHandle != null) {
            jmxHandle.stop();
        }
        LocalJMXPort localJMXPort = new LocalJMXPort(port);
        jmxHandle = localJMXPort.start();
    }

    private synchronized void stop() {
        if (jmxHandle != null) {
            jmxHandle.stop();
            jmxHandle = null;
        }
    }

    static public class LocalJMXPort {
        int port;
        
        public LocalJMXPort(int port) {
            this.port = port;
        }

        public jmxHandle start() {
            jmxHandle jmxHandle = new jmxHandle();
            try {
                JMXConnectorServer rmiServer;
                System.setProperty("java.rmi.server.hostname",  BINDING_ADDRESS);
                // Create an instance of our own socket factory (see below)
                RMISocketFactory factory = new LocalHostSocketFactory();
                // Set it as default
                RMISocketFactory.setSocketFactory(factory);
                // Create our registry
                LocateRegistry.createRegistry(port);
                // Get the MBeanServer and setup a JMXConnectorServer
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + BINDING_ADDRESS +":" + port + "/jndi/rmi://"+ BINDING_ADDRESS + ":" + port + "/jmxrmi");
                rmiServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
                rmiServer.start();
                jmxHandle = new jmxHandle(rmiServer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jmxHandle;
        }

        static class jmxHandle {
            JMXConnectorServer rmiServer;
            protected jmxHandle() {
                rmiServer = null;
            }
            protected jmxHandle(JMXConnectorServer rmiServer) {
                this.rmiServer = rmiServer;
            }
            
            public synchronized void stop() {
                if (this.rmiServer != null) {
                    try {
                        this.rmiServer.stop();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    this.rmiServer = null;
                }
            }
        }

        static private class LocalHostSocketFactory extends RMISocketFactory {
            public ServerSocket createServerSocket(int port) throws IOException {
                ServerSocket ret = new ServerSocket();
                ret.bind(new InetSocketAddress("localhost", port));
                return ret;
            }

            public Socket createSocket(String host, int port) throws IOException {
                return new Socket(host, port);
            }
        }
    }
}