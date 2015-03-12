Static resources within GRETTY
==============================

This is a multi project build. I'm using Gretty to start a test environment
containing the output of the latest build.

## a

* Contains a static resource "test.txt"
* And another static resource "doc/test.txt" -> "doc/a/test.txt", copying is done at build time - see <common.gradle> for details

## b

* Is almost empty
* Depends on a

## This Works OK

Run:

```
cd b
gradle build
```

Deploy b/build/libs/b.war into a tomcat7

Browse to:

* http://localhost:8080/b/test.txt -> OK
* http://localhost:8080/b/doc/a/test.txt -> KO, 404 not found

## The Issue

Run:

```
cd b
gradle appRun
```

Browse to:

* http://localhost:8080/b/test.txt -> OK
* http://localhost:8080/b/doc/a/test.txt -> KO, 404 not found

Maybe Gretty doesn't pickup the files created by this correctly?

```
ext.jarTasks = tasks.withType(Jar);
jarTasks.all {
  into("META-INF/resources/doc/${project.name}") {
    from 'doc'
  }
}
```
