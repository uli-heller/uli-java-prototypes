sprint-boot
===========

Quellen:

* http://spring.io/guides/gs/spring-boot/
* https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-web-jsp

Historie
--------

* 2014-08-27
    * Beginn der Historie

Probleme
--------

### Unable to read TLD "META-INF/c.tld"

Das Problem tritt bei mir immer auf, wenn ich versuche, eine
JSP im Browser zu laden.

Abhilfe über build.gradle:

```
...
apply plugin: 'war'
...
  providedCompile("org.springframework.boot:spring-boot-starter-tomcat")
...
  providedCompile("org.apache.tomcat.embed:tomcat-embed-jasper")
...
```

### Aktivieren des Loggings
