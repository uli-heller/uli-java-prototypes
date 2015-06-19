// Copied from: http://cagataycivici.wordpress.com/2010/02/17/port-jsf-2-0s-viewscope-to-spring-3-0/
package org.uli.sessionscope;
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

@Component
@Scope("session")
public class TomcatSessionBean implements Serializable {
    public String getSessionId() {
	FacesContext fCtx = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
	String sessionId = session.getId();
	return sessionId;
    }
    public void invalidate() throws IOException {
	FacesContext fCtx = FacesContext.getCurrentInstance();
        ExternalContext ec = fCtx.getExternalContext();
	ec.invalidateSession();
	ec.redirect(ec.getRequestContextPath() + "/counter.xhtml");
    }
}
