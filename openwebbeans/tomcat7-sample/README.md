Apache OpenWebBean
==================

Note: This is work in progress. Currently, nothing works ;)

Issues
------

### java.lang.ClassNotFoundException: javax.enterprise.context.spi.Contextual

Can be fixed by copying the jar "javaee-api-6.0.jar"
into the lib folder of the tomcat folder.

### java.lang.ClassNotFoundException: com.sun.faces.config.ConfigureListener

Can be fixed by adding the mojarra jar to "build.gradle":

   compile 'org.glassfish:javax.faces:2.2.5'

### Absent Code attribute in method that is not native or abstract in class file javax/enterprise/inject/IllegalProductException

TBD
