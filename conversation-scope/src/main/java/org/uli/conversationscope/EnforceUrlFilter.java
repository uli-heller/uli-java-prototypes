package org.uli.conversationscope;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

// Derived from
// http://stackoverflow.com/questions/11349064/jsessionid-how-to-avoid-jsessionid-xxx-on-the-first-call-to-a-page-it-works
public class EnforceUrlFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(EnforceUrlFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
                ServletException {
        boolean allowFilterChain = redirectToUrl((HttpServletRequest) req, (HttpServletResponse) res);
        // I'm doing this because if I execute the request completely, it will
        // perform a pretty heavy lookup operation. No need to do it twice.
        if (allowFilterChain) chain.doFilter(req, res);
    }

    public boolean redirectToUrl(HttpServletRequest req, HttpServletResponse res) {
        HttpSession s = req.getSession();
        if (s.isNew()) {
            if (!(req.isRequestedSessionIdFromCookie() && req.isRequestedSessionIdFromURL())) {
                String qs = req.getQueryString();
                String uri = req.getRequestURI();
                if (qs != null) {
                    uri = uri + "?" + qs;
                }
                String newUrl = res.encodeURL(uri);
                try {
                    res.sendRedirect(newUrl);
                    return false;
                } catch (IOException e) {
                    log.error("Error sending redirect. ", e.getMessage());
                }
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
}
