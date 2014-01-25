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

Can be fixed by using myfaces instead of mojarra:

* Mojarra

     compile 'org.glassfish:javax.faces:2.2.5'

* Myfaces

     compile 'org.apache.myfaces.core:myfaces-api:2.1.13'
     compile 'org.apache.myfaces.core:myfaces-impl:2.1.13'

### Absent Code attribute in method that is not native or abstract in class file javax/enterprise/util/AnnotationLiteral

TBD
