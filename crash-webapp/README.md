CRASH-WEBAPP
============

Eine Mini-Webapp zum Test von [CRaSH](http://www.crashub.org)

Bauen
-----

```
../gradlew build
# war-Datei liegt unter build/libs/crash-webapp.war
```

Testen
------

```
../gradlew appRun
# Test mit Browser: http://localhost:8080/crash-webapp/
telnet localhost 5000
```

Minimal-Einbindung
------------------

Für eine minimale Einbindung muß man diese Schritte unternehmen:

1. Erweitern von "build.gradle": `compile 'org.crashub:crash.connectors.telnet:+'`
2. Erweitern von "web.xml"
    * Listener: org.crsh.plugin.WebPluginLifeCycle
    * ContextParam: crash.mountpointconfig.conf=classpath:/crash/
    * ContextParam: crash.mountpointconfig.cmd=classpath:/crash/commands/

Danach kann man mit `telnet localhost 5000` eine Verbindung zu CRaSH aufnehmen:

```
uli@mypc:~$ telnet localhost 5000
Trying ::1...
Connected to localhost.
Escape character is '^]'.

   _____     ________                 _______    ____ ____
 .'     `.  |        `.             .'       `. |    |    | 1.3.1
|    |    | |    |    |  .-------.  |    |    | |    |    |
|    |____| |    `   .' |   _|    |  .    '~_ ` |         |
|    |    | |    .   `.  .~'      | | `~_    `| |         |
|    |    | |    |    | |    |    | |    |    | |    |    |
 `._____.'  |____|____| `.________|  `._____.'  |____|____|

Follow and support the project on http://www.crashub.org
Welcome to mypc + !
It is Sat Feb 14 09:27:31 CET 2015 now

% 
```

Groovy-Script als CRaSH-Befehl
------------------------------

Man kann relativ einfach ein eigenes Groovy-Script als CRaSH-Befehl einbinden:

* Erweitern von "crash.mountpointconfig.cmd"
    * Von: classpath:/crash/commands/
    * Nach: classpath:/crash/commands/;classpath:/org/uli/crash/commands/
* Erstellen eines GroovyScripts
    * Pfad: src/main/resources/org/uli/crash/commands/hello.groovy
    * Inhalt: `return "hello world!";`

Danach kann man das Script direkt aufrufen:

```
uli@mypc:~$ telnet localhost 5000
Trying ::1...
Connected to localhost.
Escape character is '^]'.

   _____     ________                 _______    ____ ____
 .'     `.  |        `.             .'       `. |    |    | 1.3.1
|    |    | |    |    |  .-------.  |    |    | |    |    |
|    |____| |    `   .' |   _|    |  .    '~_ ` |         |
|    |    | |    .   `.  .~'      | | `~_    `| |         |
|    |    | |    |    | |    |    | |    |    | |    |    |
 `._____.'  |____|____| `.________|  `._____.'  |____|____|

Follow and support the project on http://www.crashub.org
Welcome to mypc + !
It is Sat Feb 14 10:03:21 CET 2015 now

% hello
hello world!
```

Man kann im Script auch Argumente auswerten:

```
def greeting = args.size()==0 ? "world" : args.join(",");
return "Hello ${greeting}!";
```

Ausführen von System-Kommandos
------------------------------

Hierzu wird ein Skript abgelegt unter "src/main/resources/org/uli/crash/commands/exec.groovy":

```
def outSb = new StringBuilder();
def errSb = new StringBuilder();
def proc = args.execute();
proc.consumeProcessOutput(outSb, errSb);
proc.waitFor();
def result = new StringBuilder();
if (outSb) {
  result.append('out:\n').append(outSb);
}
if (errSb) {
  result.append('err:\n').append(errSb);
}
return result;
```

Das kann man dann so nutzen:

```
% exec pwd
out:
/home/uli/git/uli-java-prototypes/crash-webapp

% exec ls . /error
out:
.:
bin
build
build.gradle
crash-webapp-gretty.log
README.md
src
err:
ls: Zugriff auf /error nicht möglich: Datei oder Verzeichnis nicht gefunden

%
```
