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
    private static final String PARAM_NAME_NAME="name";
    private static final String PARAM_NAME_LANGUAGE="language";
    private static final String PARAM_NAME_COUNTER="counter";

    // TODO
    private static final long serialVersionUID = 1L;

    @Inject Conversation conversation;
    @Inject UserContextBean userContextBean;
    @Inject CounterBean counterBean;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        conversation.begin();
        String name = getStringWithDefault(req.getParameter(PARAM_NAME_NAME), "default name");
        String language = getStringWithDefault(req.getParameter(PARAM_NAME_LANGUAGE), "default language");
        String counter =  getStringWithDefault(req.getParameter(PARAM_NAME_COUNTER), "0");
        userContextBean.setName(name);
        userContextBean.setLanguage(language);
        counterBean.setCounter(Integer.valueOf(counter));
        String url = req.getContextPath() + "/step1s.xhtml?cid="+conversation.getId();
        res.sendRedirect(url);
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
}
