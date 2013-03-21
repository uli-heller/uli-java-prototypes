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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmxServlet extends HttpServlet {
    static private final String BINDING_ADDRESS="127.0.0.1";

    Logger logger = LoggerFactory.getLogger(JmxServlet.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static LocalJMXPort.jmxHandle jmxHandle = null;;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	logger.debug("->");
	response.setContentType("text/html");
        String pathInfo = request.getPathInfo();
	PrintWriter out = response.getWriter();
        int port = 11223;
        if (pathInfo != null) {
	    send(out, "Was soll das?");
	} else {
            if (pathInfo.startsWith("/start")) {
                String portString = request.getParameter("port");
                if (portString != null) {
                    port = Integer.parseInt(portString);
                }
                start(port);
		send(out, "Gestartet - port="+port);
            } else if (pathInfo.startsWith("/stop")) {
		send(out, "Gestoppt");
                stop();
            }
        }
	logger.debug("<-");
    }

    private void send(PrintWriter out, String text) {
        out.println("<HTML>");
        out.println("<HEAD><TITLE>JmxServlet - Response</TITLE></HEAD>");
        out.println("<BODY>");
        out.println("<PRE>");
	out.println(text);
        out.println("</PRE>");
        out.println("</BODY>");
        out.println("</HTML>");
    }

    private synchronized void start(int port) {
	logger.debug("-> {}", port);
        if (jmxHandle != null) {
            jmxHandle.stop();
        }
        LocalJMXPort localJMXPort = new LocalJMXPort(port);
        jmxHandle = localJMXPort.start();
	logger.debug("<-");
    }

    private synchronized void stop() {
	logger.debug("->");
        if (jmxHandle != null) {
            jmxHandle.stop();
            jmxHandle = null;
        }
	logger.debug("<-");
    }

    static public class LocalJMXPort {
	Logger logger = LoggerFactory.getLogger(LocalJMXPort.class);
        int port;
        
        public LocalJMXPort(int port) {
	    logger.debug("-> port={}");
            this.port = port;
	    logger.debug("<-");
        }

        public jmxHandle start() {
	    logger.debug("->");
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
	    logger.debug("<- jmxHandle={}", jmxHandle);
            return jmxHandle;
        }

        static class jmxHandle {
	    Logger logger = LoggerFactory.getLogger(jmxHandle.class);
            JMXConnectorServer rmiServer;
            protected jmxHandle() {
                rmiServer = null;
            }
            protected jmxHandle(JMXConnectorServer rmiServer) {
                this.rmiServer = rmiServer;
            }
            
            public synchronized void stop() {
		logger.debug("->");
                if (this.rmiServer != null) {
                    try {
                        this.rmiServer.stop();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    this.rmiServer = null;
                }
		logger.debug("<-");
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
