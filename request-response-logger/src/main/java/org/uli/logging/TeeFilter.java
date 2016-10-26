// http://www.wetfeetblog.com/servlet-filer-to-log-request-and-response-details-and-payload/431
// http://logback.qos.ch/recipes/captureHttp.html
package org.uli.logging;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeeFilter implements Filter {

    private final static String LOG_REQUEST_PAYLOAD = "logRequestPayload";
    private final static String LOG_RESPONSE_PAYLOAD = "logResponsePayload";

    private final Logger logger = LoggerFactory.getLogger(TeeFilter.class);
    private static boolean fHttpServletResponseHasGetHeaderNames = false; // Initialized via init()
    private static Method mSetContentLengthLong = null; // Initialized via init()
    static private boolean fLogRequestPayload = true;
    static private boolean fLogResponsePayload = true;
    private final static Map<String, List<String>> EMPTY=new HashMap<String, List<String>>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //int effectiveMajorVersion = filterConfig.getServletContext().getEffectiveMajorVersion();
        //int effectiveMinorVersion = filterConfig.getServletContext().getEffectiveMinorVersion();
        int majorVersion = filterConfig.getServletContext().getMajorVersion();
        int minorVersion = filterConfig.getServletContext().getMinorVersion();
        TeeFilter.fHttpServletResponseHasGetHeaderNames = majorVersion < 3 ? false : true; // Only available for servlet 3 and upwards
        if ((majorVersion == 3 && minorVersion >= 1) || majorVersion > 3) {
            try {
                mSetContentLengthLong = HttpServletResponse.class.getMethod("setContentLengthLong", long.class);
            } catch (NoSuchMethodException e) {
                logger.info("Unexpected exception - nosuchmethod:", e);
            } catch (SecurityException e) {
                logger.info("Unexpected exception - security:", e);
            }
        }
        fLogRequestPayload = parseBoolean(filterConfig.getInitParameter(LOG_REQUEST_PAYLOAD), true);
        fLogResponsePayload = parseBoolean(filterConfig.getInitParameter(LOG_RESPONSE_PAYLOAD), true);
    }

    private boolean parseBoolean(String s, boolean dflt) {
        boolean result = dflt;
        if (s != null && s.length() > 0) {
            Boolean v = Boolean.parseBoolean(s);
            result = v.booleanValue();
        }
        return result;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
            if (fLogRequestPayload) {
                httpServletRequest = new BufferedRequestWrapper(httpServletRequest);
            }
            if (fLogResponsePayload) {
                httpServletResponse = new BufferedResponseWrapper(httpServletResponse);
            }
            logger.debug("Request - [HTTP METHOD:{}] [SERVLET PATH:{}] [QUERY STRING:{}] [PATH INFO:{}] [REQUEST_PARAMETERS:{}] [REMOTE ADDRESS:{}]",
                        httpServletRequest.getMethod(),
                        httpServletRequest.getServletPath(),
                        httpServletRequest.getQueryString(),
                        httpServletRequest.getPathInfo(),
                        requestMap,
                        httpServletRequest.getRemoteAddr()
            );
            if (fLogRequestPayload) {
                logger.debug("Request - [PAYLOAD/BODY: {}]", ((BufferedRequestWrapper)httpServletRequest).getRequestBody());
            }
            logHeaders(" -request-> ", headersToMap(httpServletRequest));
            chain.doFilter(httpServletRequest, httpServletResponse);
            logger.debug("Response");
            if (fLogResponsePayload) {
                logger.debug("Response - [PAYLOAD/CONTENT: {}]", ((BufferedResponseWrapper)httpServletResponse).getContent());
            }
            logHeaders(" <-resonse- ", headersToMap(httpServletResponse));
        } catch (Throwable a) {
            logger.error("Error when trying to log request and response", a);
        }
    }
    private Map<String, List<String>> headersToMap(HttpServletRequest r) {
        Map<String, List<String>> m = new HashMap<String, List<String>>();
        Enumeration<String> names = r.getHeaderNames();
        while (names.hasMoreElements()) {
          String name = names.nextElement();
          List<String> valuesList = new LinkedList<String>();
          Enumeration<String> values = r.getHeaders(name);
          while (values.hasMoreElements()) {
            String v = values.nextElement();
            valuesList.add(v);
          }
          m.put(name, valuesList);
        }
        return m;
    }

    private Map<String, List<String>> headersToMap(HttpServletResponse r) {
        Map<String, List<String>> result = EMPTY;
        if (fHttpServletResponseHasGetHeaderNames) {
          Map<String, List<String>> m = new HashMap<String, List<String>>();
          Collection<String> names = r.getHeaderNames();
          for (String name : names) {
            List<String> valuesList = new LinkedList<String>();
            Collection<String> values = r.getHeaders(name);
            for (String v : values) {
              valuesList.add(v);
            }
            m.put(name, valuesList);
          }
          result = m;
        }
        return result;
    }

      private void logHeaders(String pfx, Map<String, List<String>> headers) {
        Set<String> namesSet = headers.keySet();
        List<String> namesList = new ArrayList<String>(namesSet);
        Collections.sort(namesList);
        for (String name : namesList) {
          List<String> values = headers.get(name);
          logger.debug(pfx + " " + name + "=" + values);
        }
      }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<String, String>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue = request.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }

    @Override
    public void destroy() {
    }

    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private ByteArrayInputStream bais = null;
        private ByteArrayOutputStream baos = null;
        private BufferedServletInputStream bsis = null;
        private byte[] buffer = null;

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            // Read InputStream and store its content in a buffer.
            InputStream is = req.getInputStream();
            this.baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int letti;
            while ((letti = is.read(buf)) > 0) {
                this.baos.write(buf, 0, letti);
            }
            this.buffer = this.baos.toByteArray();
        }

        @Override
        public ServletInputStream getInputStream() {
            this.bais = new ByteArrayInputStream(this.buffer);
            this.bsis = new BufferedServletInputStream(this.bais);
            return this.bsis;
        }

        String getRequestBody() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream(), Charset.defaultCharset()));
            String line = null;
            StringBuilder inputBuffer = new StringBuilder();
            do {
                line = reader.readLine();
                if (null != line) {
                    inputBuffer.append(line.trim());
                }
            } while (line != null);
            reader.close();
            return inputBuffer.toString().trim();
        }
    }

    private static final class BufferedServletInputStream extends ServletInputStream {

        private ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }
    }

    public static class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }
    }
    
    public static class BufferedResponseWrapper extends HttpServletResponseWrapper {
        private final Logger logger = LoggerFactory.getLogger(BufferedResponseWrapper.class);

        HttpServletResponse original;
        TeeServletOutputStream tee;
        ByteArrayOutputStream bos;

        public BufferedResponseWrapper(HttpServletResponse response) {
            super(response);
            original = response;
        }

        public String getContent() {
            if (bos == null) {
                return "";
            }

            try {
                return bos.toString(Charset.defaultCharset().name());
            } catch (UnsupportedEncodingException e) {
                logger.error("Strange error related to character encoding", e);
                return "ERROR - UliWasHere!";
            }
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (tee == null) {
                bos = new ByteArrayOutputStream();
                tee = new TeeServletOutputStream(original.getOutputStream(), bos);
            }
            return tee;
        }
    }
}
