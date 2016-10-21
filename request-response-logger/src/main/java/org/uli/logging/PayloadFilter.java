// http://stackoverflow.com/questions/8933054/how-to-log-response-content-from-a-java-web-server
package org.uli.logging;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(PayloadFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        RequestWrapper myRequestWrapper = new RequestWrapper(
                httpServletRequest);
        String body = myRequestWrapper.getBody();
        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8"); // Or whatever default.
                                                    // UTF-8 is good for World
                                                    // Domination.
        }

        logger.debug(
                "Request - [HTTP METHOD:{}] [SERVLET PATH:{}] [QUERY STRING:{}] [PATH INFO:{}] [REQUEST_PARAMETERS:{}] [REQUEST BODY:{}] [REMOTE ADDRESS:{}]",
                httpServletRequest.getMethod(),
                httpServletRequest.getServletPath(),
                httpServletRequest.getQueryString(),
                httpServletRequest.getPathInfo(),
                getTypesafeRequestMap(httpServletRequest), body,
                httpServletRequest.getRemoteAddr());
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(
                (HttpServletResponse) response);
        logHeaders(" -request-> ", headersToMap(httpServletRequest));
        try {
            chain.doFilter(myRequestWrapper, responseCopier);
            responseCopier.flushBuffer();
        } finally {
            byte[] copy = responseCopier.getCopy();
            logger.debug("Response - [RESPONSE:{}]",
                    new String(copy, response.getCharacterEncoding()));
            logHeaders(" <-resonse- ", headersToMap(responseCopier));
        }
    }

    @Override
    public void destroy() {
    }

    private Map<String, String> getTypesafeRequestMap(
            HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<String, String>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue = request.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
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
        return m;
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

    public class HttpServletResponseCopier extends HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter writer;
        private ServletOutputStreamCopier copier;

        public HttpServletResponseCopier(HttpServletResponse response)
                throws IOException {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException(
                        "getWriter() has already been called on this response.");
            }

            if (outputStream == null) {
                outputStream = getResponse().getOutputStream();
                copier = new ServletOutputStreamCopier(outputStream);
            }

            return copier;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException(
                        "getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                copier = new ServletOutputStreamCopier(
                        getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(copier,
                        getResponse().getCharacterEncoding()), true);
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (outputStream != null) {
                copier.flush();
            }
        }

        public byte[] getCopy() {
            if (copier != null) {
                return copier.getCopy();
            } else {
                return new byte[0];
            }
        }

    }

    public class ServletOutputStreamCopier extends ServletOutputStream {

        private OutputStream outputStream;
        private ByteArrayOutputStream copy;

        public ServletOutputStreamCopier(OutputStream outputStream) {
            this.outputStream = outputStream;
            this.copy = new ByteArrayOutputStream(1024);
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            copy.write(b);
        }

        public byte[] getCopy() {
            return copy.toByteArray();
        }

    }

    public class RequestWrapper extends HttpServletRequestWrapper {
        private final String body;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                InputStream inputStream = request.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    stringBuilder.append("");
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
            }
            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    body.getBytes());
            ServletInputStream servletInputStream = new ServletInputStream() {
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
            return servletInputStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(
                    new InputStreamReader(this.getInputStream()));
        }

        public String getBody() {
            return this.body;
        }
    }
}
