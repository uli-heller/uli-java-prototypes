ULI-JAVA-PROTOTYPES
===================

Einleitung
----------

In diesem Git-Projekt arbeite ich an diversen Java-Prototypen.
Grob sind das Test-Implementierungen von Dingen, die ich manchmal
in meine "richtigen" Projekte einbaue, manchmal auch nicht.

Diese Prototypen gibt's:

* [conversation-scope](conversation-scope) ... Verwendung vom CDI-Conversation-Scope

* [javamelody](javamelody) ... JEE-Monitoring

* [jopt-simple](jopt-simple) ... Auswerten von Kommandozeilen-Optionen

* [jsf-view-scope](jsf-view-scope) ... Beispiel von JSF-View-Scope

* [localhost-jmx](localhost-jmx) ... JMX-Netzzugriff aktivieren für Localhost und ohne Setzen von Java-Properties

* [lombok-jpa](lombok-jpa) ... JPA in Verbindung mit Lombok

* [lombok-spring-jpa](lombok-spring-jpa) ... JPA und Spring in Verbindung mit Lombok

* [lookup](lookup) ... "Nachschlagen" von "Konfigurations"-Parametern

* [perfidix-microbenchmark](perfidix-microbenchmark) ... Ausführungszeiten und Speicherverbrauch abstoppen mit Perfidix

* [resultset](resultset) ... Wandlung eines JDBC-ResultSets in List<Entity>

* [request-response-logger](request-response-logger) ... Protokollierung der Anfragen und Antworten in einer WebApp

* [spring-data-jpa](spring-data-jpa) ... Verwendung von SpringDataJPA

* [spring-data-jpa-cacheable](spring-data-jpa-cacheable) ... Verwendung von SpringDataJPA mit GuavaCache

* [view-scope](view-scope) ... Zusammenspiel von JSF-View-Scope und SpringBeans

Dies sind "gescheiterte" Prototypen:

* [caliper-microbenchmark](caliper-microbenchmark) ... Anfang 2014 liegt keine vernünftig funktionierende Version von "caliper" vor; Alternative: [perfidix-microbenchmark](perfidix-microbenchmark)

* [openwebbeans](openwebbeans) ... sind Ende 2013 unter Tomcat nicht an's Laufen zu bekommen

Kompilierung
------------

* Gradle Wrapper: `./gradlew assemble`

* Vorinstalliertes Gradle: `gradle assemble`

Eclipse
-------

* Eclipse-Projekte erzeugen: `./gradlew eclipse`

* Eclipse starten

* Eclipse-Projekte importieren

    * File - Import

    * General - Existing Projects into Workspace - Next

    * Select root directory: uli-java-prototypes

    * Projects: Alle selektieren - Finish


Git
---

Gedächtnisstütze für mich:

```sh
git init uli-java-prototypes
cd uli-java-prototypes
...
git commit -m "was-auch-immer" .
# Neues Repo auf GitHub angelegt
git remote add origin git@github.com:uli-heller/uli-java-prototypes.git
git push -u origin master
```
