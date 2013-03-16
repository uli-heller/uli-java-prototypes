ULI-JAVA-PROTOTYPES
===================

Einleitung
----------

In diesem Git-Projekt arbeite ich an diversen Java-Prototypen.
Grob sind das Test-Implementierungen von Dingen, die ich manchmal
in meine "richtigen" Projekte einbaue, manchmal auch nicht.

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
