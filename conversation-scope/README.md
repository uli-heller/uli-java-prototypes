CONVERSATION SCOPE
==================

This is a small prototype using conversation scope java beans
in a web application. I'm testing the web application within
Tomcat-7.0.37 at the moment.

Building And Deploying The Project
----------------------------------

```
cd conversation-scope
../gradlew war
cp build/libs/conversation-scope.war /opt/apache-7.0.37/webapps/.
```

Using The Showcase
------------------

After deploying the project, you're able to look at the showcase.
The starting point is this: <http://localhost:8080/conversation-scope/>.
It shows a static html page. The page has a couple of links.
Each of this link is headed towards a servlet and contains specific
initialization parameters for the java beans.

The servlet

* creates a new conversation
* reads the parameters from the url
* stores the parameters within the java beans
* redirects the browser to a jsf page showing the content of the java beans

The main benefit of the conversation scope is the fact that you can
open multiple browser windows/tabs, each having a unique set of java beans.

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
