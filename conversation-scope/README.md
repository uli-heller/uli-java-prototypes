CONVERSATION SCOPE
==================

This is a small prototype using conversation scope java beans
in a web application. I'm testing the web application within
Tomcat-7.0.37 at the moment.

Issues
------

### Redirect Without "cid=..."

Within the initial servlet, it is unclear if you are required to add the "cid"
when doing redirects. According to my tests, these are the findings:

* when the cid is added, the java beans are populated correctly
  (res.sendRedirect(req.getContextPath() + "/step1s.xhtml?cid="+conversation.getId());)
* without the cid, the java beans are empty
  (res.sendRedirect(req.getContextPath() + "/step1s.xhtml");)

So: You do have to specify the cid when doing redirects!

### Weld-1.1 vs. Weld 2.1

When using Weld-1.1.17.Final, I'm getting exceptions like this:

```
javax.enterprise.context.ContextNotActiveException: Conversation Context not active when method called on conversation Transient conversation
	org.jboss.weld.context.conversation.ConversationImpl.verifyConversationContextActive(ConversationImpl.java:197)
	org.jboss.weld.context.conversation.ConversationImpl.touch(ConversationImpl.java:159)
	org.jboss.weld.context.conversation.ConversationImpl.<init>(ConversationImpl.java:72)
	org.jboss.weld.bean.builtin.ConversationBean.create(ConversationBean.java:48)
	org.jboss.weld.bean.builtin.ConversationBean.create(ConversationBean.java:17)
	org.jboss.weld.context.AbstractContext.get(AbstractContext.java:103)
	org.jboss.weld.bean.proxy.ContextBeanInstance.getInstance(ContextBeanInstance.java:90)
	org.jboss.weld.bean.proxy.ProxyMethodHandler.invoke(ProxyMethodHandler.java:104)
	org.jboss.weld.proxies.Conversation$1990511612$Proxy$_$$_WeldClientProxy.begin(Conversation$1990511612$Proxy$_$$_WeldClientProxy.java)
	org.uli.conversationscope.UrlToUserContextBeanServlet.doGet(UrlToUserContextBeanServlet.java:37)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:621)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:728)
```

The exception doesn't happen when using Weld-2.1.2.Final.

### Circular dependencies

Circular dependencies between java beans don't seem to raise any issue.

```
// within CounterBean
@Inject UserContextBean userContextBean;

// within UserContextBean
@Inject CounterBean counterBean;
```
