LOCALHOST-JMX
=============

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
