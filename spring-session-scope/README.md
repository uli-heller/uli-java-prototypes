SESSION-SCOPE
=============

Some tests repated to web applications using session scope.

* Build: `../gradlew war`
* Run: `../gradlew jettyRun`

When jetty is running, head your browser to <http://localhost:8080/spring-session-scope>.

Clicking on "New counter page (existing session)" opens a new
browser window/tab reusing the existing session. Opening multiple
of these lead to "shared counters".

Clicking on "New counter page (new session)" opens a new
browser window/tab using a new session. Each new window will have
a fresh counter. Unfortunately, old windows will now work any more.

Persistent Sessions
-------------------

1. Deploy the war to a tomcat
2. Start the tomcat
3. Head your browser to <http://localhost:8080/spring-session-scope>
4. Click on "New counter page (new session)"
5. Click on "Count" -> Counter:1
6. Click on "Count" again -> Counter:2
7. Stop the tomcat
8. Start the tomcat, wait 10 seconds
9. Click on "Count" again -> Counter:3


