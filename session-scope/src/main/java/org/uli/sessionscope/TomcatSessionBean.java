// Copied from: http://cagataycivici.wordpress.com/2010/02/17/port-jsf-2-0s-viewscope-to-spring-3-0/
package org.uli.sessionscope;
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Component
@Scope("session")
public class TomcatSessionBean {
    public String getSessionId() {
	FacesContext fCtx = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
	String sessionId = session.getId();
	return sessionId;
    }
}
