package org.uli.crash;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrashServlet extends HttpServlet implements ServletContextListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("->");
        response.setContentType("text/html");
        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();
        if (pathInfo == null) {
            send(out, "Was soll das?");
        } else {
            if (pathInfo.startsWith("/start")) {
                log.debug(".: START");
                send(out, "Gestartet - pathInfo=" + pathInfo);
            } else if (pathInfo.startsWith("/stop")) {
                log.debug(".: STOP");
                send(out, "Gestoppt - pathInfo=" + pathInfo);
            }
        }
        log.debug("<-");
    }

    @Override
    public void destroy() {
        log.debug("->");
        log.debug("<-");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("->");
        log.debug("<-");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("->");
        log.debug("<-");
    }

    private void send(PrintWriter out, String text) {
        out.println("<HTML>");
        out.println("<HEAD><TITLE>CrashServlet - Response</TITLE></HEAD>");
        out.println("<BODY>");
        out.println("<PRE>");
        out.println(text);
        out.println("</PRE>");
        out.println("</BODY>");
        out.println("</HTML>");
    }
}
