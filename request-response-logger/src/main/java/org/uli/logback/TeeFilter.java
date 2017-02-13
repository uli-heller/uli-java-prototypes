/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package org.uli.logback;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeeFilter implements Filter {

    boolean active;
    private final static String LOG_REQUEST_PAYLOAD = "logRequestPayload";
    private final static String LOG_RESPONSE_PAYLOAD = "logResponsePayload";

    private final Logger logger = LoggerFactory.getLogger(TeeFilter.class);
    private static boolean fHttpServletResponseHasGetHeaderNames = false; // Initialized via init()
    static private boolean fLogRequestPayload = true;
    static private boolean fLogResponsePayload = true;
    @Override
    public void destroy() {
        // NOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (active && request instanceof HttpServletRequest) {
            try {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);

                logger.debug("Request - [HTTP METHOD:{}] [SERVLET PATH:{}] [QUERY STRING:{}] [PATH INFO:{}] [REQUEST_PARAMETERS:{}] [REMOTE ADDRESS:{}]",
                        httpServletRequest.getMethod(),
                        httpServletRequest.getServletPath(),
                        httpServletRequest.getQueryString(),
                        httpServletRequest.getPathInfo(),
                        requestMap,
                        httpServletRequest.getRemoteAddr()
                        );
                TeeHttpServletRequest teeRequest = new TeeHttpServletRequest(httpServletRequest);
                TeeHttpServletResponse teeResponse = new TeeHttpServletResponse(httpServletResponse);
                if (fLogRequestPayload) {
                    logger.debug("Request - [PAYLOAD/BODY: {}]", buffer2String(teeRequest.getInputBuffer()));
                }

                // System.out.println("BEFORE TeeFilter. filterChain.doFilter()");
                filterChain.doFilter(teeRequest, teeResponse);
                // System.out.println("AFTER TeeFilter. filterChain.doFilter()");

                teeResponse.finish();
                // let the output contents be available for later use by
                // logback-access-logging
                teeRequest.setAttribute(AccessConstants.LB_OUTPUT_BUFFER, teeResponse.getOutputBuffer());
                if (fLogResponsePayload) {
                    logger.debug("Response - [PAYLOAD/CONTENT: {}]", buffer2String(teeResponse.getOutputBuffer()));
                }
                logHeaders(" <-resonse- ", headersToMap(httpServletResponse));
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } catch (ServletException e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

    private String buffer2String(byte[] buffer) {
        String result;
        if (buffer == null) {
            result="<null>";
        } else {
            result = new String(buffer);
        }
        return result;
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
    
    private Map<String, List<String>> headersToMap(HttpServletResponse r) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String includeListAsStr = filterConfig.getInitParameter(AccessConstants.TEE_FILTER_INCLUDES_PARAM);
        String excludeListAsStr = filterConfig.getInitParameter(AccessConstants.TEE_FILTER_EXCLUDES_PARAM);
        String localhostName = getLocalhostName();
        fLogRequestPayload = parseBoolean(filterConfig.getInitParameter(LOG_REQUEST_PAYLOAD), true);
        fLogResponsePayload = parseBoolean(filterConfig.getInitParameter(LOG_RESPONSE_PAYLOAD), true);

        active = computeActivation(localhostName, includeListAsStr, excludeListAsStr);
        if (active)
            System.out.println("TeeFilter will be ACTIVE on this host [" + localhostName + "]");
        else
            System.out.println("TeeFilter will be DISABLED on this host [" + localhostName + "]");

    }

    private boolean parseBoolean(String s, boolean dflt) {
        boolean result = dflt;
        if (s != null && s.length() > 0) {
            Boolean v = Boolean.parseBoolean(s);
            result = v.booleanValue();
        }
        return result;
    }

    static List<String> extractNameList(String nameListAsStr) {
        List<String> nameList = new ArrayList<String>();
        if (nameListAsStr == null) {
            return nameList;
        }

        nameListAsStr = nameListAsStr.trim();
        if (nameListAsStr.length() == 0) {
            return nameList;
        }

        String[] nameArray = nameListAsStr.split("[,;]");
        for (String n : nameArray) {
            n = n.trim();
            nameList.add(n);
        }
        return nameList;
    }

    static String getLocalhostName() {
        String hostname = "127.0.0.1";

        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return hostname;
    }

    static boolean computeActivation(String hostname, String includeListAsStr, String excludeListAsStr) {
        List<String> includeList = extractNameList(includeListAsStr);
        List<String> excludeList = extractNameList(excludeListAsStr);
        boolean inIncludesList = mathesIncludesList(hostname, includeList);
        boolean inExcludesList = mathesExcludesList(hostname, excludeList);
        return inIncludesList && (!inExcludesList);
    }

    static boolean mathesIncludesList(String hostname, List<String> includeList) {
        if (includeList.isEmpty())
            return true;
        return includeList.contains(hostname);
    }

    static boolean mathesExcludesList(String hostname, List<String> excludesList) {
        if (excludesList.isEmpty())
            return false;
        return excludesList.contains(hostname);
    }

}
