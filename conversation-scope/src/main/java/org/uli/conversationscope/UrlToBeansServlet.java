/**
 * 
 */
package org.uli.conversationscope;

import java.io.IOException;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author uli
 *
 */
public class UrlToBeansServlet extends HttpServlet {
    private static final String PARAMETER_NAME_NAME="name";
    private static final String PARAMETER_NAME_LANGUAGE="language";
    private static final String PARAMETER_NAME_COUNTER="counter";

    private static final String PARAMETER_NAME_CONVERSATION_ID="cid";

    private static final String QUERY_PARAMETER_START="?";
    private static final String QUERY_PARAMETER_SEPARATOR="&";

    // TODO
    private static final long serialVersionUID = 1L;

    @Inject Conversation conversation;
    @Inject UserContextBean userContextBean;
    @Inject CounterBean counterBean;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        conversation.begin();
        String name = getStringWithDefault(req.getParameter(PARAMETER_NAME_NAME), "default name");
        String language = getStringWithDefault(req.getParameter(PARAMETER_NAME_LANGUAGE), "default language");
        String counter =  getStringWithDefault(req.getParameter(PARAMETER_NAME_COUNTER), "0");
        userContextBean.setName(name);
        userContextBean.setLanguage(language);
        counterBean.setCounter(Integer.valueOf(counter));
        redirect(res, "step1s.xhtml");
    }

    public void initConversation(){
        if (conversation.isTransient()) {
          conversation.begin();
        }
    }

    private String getStringWithDefault(String s, String d) {
        String result = s;
        if (s == null || s.trim().length() <= 0) {
            result = d;
        }
        return result;
    }
    
    private void redirect(HttpServletResponse res, String url) throws IOException {
        String newUrl = addQueryParameter(url, PARAMETER_NAME_CONVERSATION_ID, conversation.getId());
        newUrl = res.encodeRedirectURL(newUrl); // ... required when session id is stored in url
        res.sendRedirect(newUrl);
    }
    
    // TODO: Handling of special characters
    private String addQueryParameter(String url, String name, String value) {
        String sep = QUERY_PARAMETER_START;
        if (url.contains(QUERY_PARAMETER_START)) {
            sep = QUERY_PARAMETER_SEPARATOR;
        }
        String newUrl = url + sep + name + "=" + value;
        return newUrl;
    }
}
