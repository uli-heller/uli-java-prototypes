SPRING-JMX
==========

Das Exportieren von JavaBeans an JMX ist mit Spring recht einfach.
Grundsätzlich gibt es zwei Mechanismen:

* MBeanExporter
* AnnotationMBeanExporter

MBeanExporter
-------------

Hierbei muß eine _Instanz vom MBean-Exporter erzeugt und mit Instanzen
der zu exportierenden JavaBeans versorgt werden. Dies geschieht bspw.
mit diesem JavaConfig-Snippet:

``` java
@Bean
MyLombokBean myLombokBean() {
   return new MyLombokBean();
}

@Bean @Lazy(false)
MBeanExporter mbeanExporter() {
  MBeanExporter mbe = new MBeanExporter();
  Map<String, Object> beans = new HashMap<String, Object>();
  beans.put("bean.name=myLombokBean", myLombokBean());
  beans.put("bean.name=mySingleton",  MySingleton.getInstance());
  mbe.setBeans(beans);
  return mbe;
}
```

Es werden alle aufgelisteten JavaBeans per JMX exportiert.
Alle Properties sind als JMX-Attribute definiert und können gelesen
und modifiziert werden.

AnnotationMBeanExporter
-----------------------

Hierbei werden alle SpringBeans exportiert, die mit `@ManagedResource`
markiert sind. Die JavaConfig sieht dann grob so aus:

``` java
@Configuration
@EnableMBeanExport
public class SpringConfig {
   @Bean
    MyJavaBean myJavaBean() {
        return new MyJavaBean();
    }
    @Bean
    AnotherLombokBean anotherLombokBean() {
        return new AnotherLombokBean();
    }
}
```

Es werden alle SpringBeans exportiert, die mit `@ManagedResource` markiert sind.
Für Lese- und Modifikationszugriff müssen die betreffenden Getter und Setter
mit `@ManagedAttribute` markiert sein.

Kompilierung und Test
---------------------

* Kompilieren mit: `./gradlew build`
* Start mit: `./gradlew appRun`
* Test mit
    * <localhost:8080/localhost-jmx>
    * `jconsole localhost:22334`

Probleme
--------

* Funktioniert nicht mit dem Gradle-Jetty-Plugin
* Logging funktioniert nicht mit `gradle appRun`
* AnnotationMBeanExporter
    * Exportiert alle Beans, die mit @ManagedResource markiert sind
    * Exportiert nur die Methoden, die mit @ManagedAttribute markiert sind
* MBeanExporter
    * Exportiert nur die "aufgeführten" Beans
    * Für diese werden automatisch alle Properties exportiert
//* Nur @ManagedAttribute wird via JMX exportiert, einfache Properties werden nicht exportiert
//* Lombok-@Data geht garnicht
