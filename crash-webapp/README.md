CRASH-WEBAPP
============

Eine Mini-Webapp zum Test von [CRaSH](http://www.crashub.org)

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
# Test mit http://localhost:8080/crash-webapp/
```
