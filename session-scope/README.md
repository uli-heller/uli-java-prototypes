SESSION-SCOPE
=============

Some tests repated to web applications using session scope.

* Build: `../gradlew war`
* Run: `../gradlew runJetty`

When jetty is running, head your browser to <http://localhost:8080/session-scope>.

Clicking on "New counter page (existing session)" opens a new
browser window/tab reusing the existing session. Opening multiple
of these lead to "shared counters".

Clicking on "New counter page (new session)" opens a new
browser window/tab using a new session. Each new window will have
a freh counter. Unfortunately, old windows will now work any more.

