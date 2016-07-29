/**
 * 
 */
package org.uli.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author uli
 * 
 */
public class SampleServlet extends HttpServlet {

   private static final long serialVersionUID = 1L;
   static private final String resultPageStart = ""
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> \n"
            + "<html> \n"
            + "<head> \n"
            + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n"
            + "<title>IPL - CheckUrl</title> \n"
            + "</head> \n" + "<body> \n";
   static private final String resultPageEnd = ""
            + "<p>If you see this page, the web app seems to work quite well.</p>"
            + "</body> \n" + "</html>";

   private Logger logger = LoggerFactory.getLogger(SampleServlet.class);

   protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
      logger.debug("request=" + request);
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println(resultPageStart);
      flushAndWait(out, 1000L);
      Map<String, String> requestMap = new HashMap<String, String>();
      Map<String, String> m = new HashMap<String, String>();
      requestMap.put("authType", request.getAuthType());
      requestMap.put("contentType", request.getContentType());
      requestMap.put("characterEncoding", request.getCharacterEncoding());
      requestMap.put("localAddress", request.getLocalAddr());
      String scheme = request.getScheme(); // http
      String serverName = request.getServerName(); // hostname.com
      int serverPort = request.getServerPort(); // 80
      String contextPath = request.getContextPath(); // /mywebapp
      String servletPath = request.getServletPath(); // /servlet/MyServlet
      String pathInfo = request.getPathInfo(); // /a/b;c=123
      String queryString = request.getQueryString(); // d=789
      requestMap.put("scheme", scheme);
      requestMap.put("serverName", serverName);
      requestMap.put("serverPort", Integer.toString(serverPort));
      requestMap.put("contextPath", contextPath);
      requestMap.put("servletPath", servletPath);
      requestMap.put("pathInfo", pathInfo);
      requestMap.put("queryString", queryString);
      out.println(mapToList("Request:", requestMap));
      flushAndWait(out, 2000L);
      m.put("ServletInfo", this.getServletInfo());
      m.put("ServletName", this.getServletName());
      ServletContext servletContext = this.getServletContext();
      m.put("ServletContext.contextPath", servletContext.getContextPath());
      out.println(headers("RequestHeaders:", request));
      out.println(mapToList("Servlet:", m));
      out.println(attributes("ServletContext.Attributes:"));
      out.println(initParameters("Init Parameters:"));
      out.println(mapToList("System Properties:", p2m(System.getProperties())));
      out.println(resultPageEnd);
      logger.debug("response=" + response);
   }

   private String headers(String headline, HttpServletRequest request) {
      Enumeration<String> hnames = request.getHeaderNames();
      Map<String, String> m = new HashMap<String, String>();
      while (hnames.hasMoreElements()) {
         String n = hnames.nextElement();
         String v = request.getHeader(n);
         m.put(n, v);
      }
      return mapToList(headline, m);
   }

   private String attributes(String headline) {
      ServletContext servletContext = this.getServletContext();
      Enumeration<String> anames = servletContext.getAttributeNames();
      Map<String, String> m = new HashMap<String, String>();
      while (anames.hasMoreElements()) {
         String n = anames.nextElement();
         Object v = servletContext.getAttribute(n);
         try {
            m.put(n, (String) v);
         } catch (Exception e) {
            m.put(n, v.toString());
         }
      }
      return mapToList(headline, m);
   }

   private String initParameters(String headline) {
      ServletConfig servletConfig = this.getServletConfig();
      ServletContext servletContext = servletConfig.getServletContext();
      Enumeration<String> pnames = servletContext.getInitParameterNames();
      Map<String, String> m = new HashMap<String, String>();
      while (pnames.hasMoreElements()) {
         String n = pnames.nextElement();
         m.put(n, servletContext.getInitParameter(n));
      }
      return mapToList(headline, m);
   }

   private String mapToList(String headline, Map<String, ?> m) {
      StringBuffer sb = new StringBuffer();
      sb.append("<font size=\"12px\">");
      sb.append(headline);
      sb.append("</font>");
      sb.append("<ul>");
      Set<String> keySet = (Set<String>) m.keySet();
      List<String> keyList = new ArrayList<String>(keySet);
      Collections.sort(keyList);
      for (String k : keyList) {
         sb.append("<li>");
         sb.append(k);
         sb.append(" = ");
         sb.append(m.get(k));
         sb.append("</li>");
      }
      sb.append("</ul>");
      return sb.toString();
   }

   @SuppressWarnings("unchecked")
   private final Map<String, String> p2m(Properties p) {
      return (Map<String, String>) (Map<?, ?>) p;
   }

   private final void flushAndWait(PrintWriter out, long wait) {
       out.flush();
       try {
        Thread.sleep(wait);
    } catch (InterruptedException e) {
        logger.error("Cannot sleep");
    }
   }
}
