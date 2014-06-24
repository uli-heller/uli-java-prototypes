JAVAMELODY
==========

See <https://code.google.com/p/javamelody/wiki/UserGuide> for detailed documentation.

Quickstart
----------

### Starting Point: A Basic JSF Project

* build.gradle
* src/main/webapp/WEB-INF/web.xml
* src/main/webapp/hello.xhtml
* src/main/webapp/welcome.xhtml
* src/main/java/org/uli/HelloBean.java

### Add JavaMelody

#### build.gradle

    ...
    dependencies {
      ...
      runtime 'net.bull.javamelody:javamelody-core:1.51.0'
      runtime 'org.jrobin:jrobin:1.5.9'
    }
    
    repositories {
      mavenCentral()
    }
    ...

#### web.xml

    ...
    <filter>
      <filter-name>monitoring</filter-name>
      <filter-class>net.bull.javamelody.MonitoringFilter</filter-class>
    </filter>
    <filter-mapping>
      <filter-name>monitoring</filter-name>
      <url-pattern>/*</url-pattern>
    </filter-mapping>
    <listener>
      <listener-class>net.bull.javamelody.SessionListener</listener-class>
    </listener>
    ...

#### Build - Run - Test

    ../gradlew --daemon jettyRunWar
    # Browse to http://localhost:8080/javamelody
