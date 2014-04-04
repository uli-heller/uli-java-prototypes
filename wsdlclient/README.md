WSDL First Web Service Client
=============================

Challenges
----------

* Gradle build
* Eclipse IDE
* Identify the service class
* Identify all required interfaces
* Adjust the package name

Instructions
------------

* Create a build description similar to 'build.gradle'
* Copy the WSDL into src/main/resources/wsdl, for example 'wsaa.wsdl'

Wsdl2Java - Creating Stubs
--------------------------

There are several ways to create the java stub classes:

* CXF
* Axis
* Axis2

Within our build.gradle, all three ways are included.
You select the stub creation by activating one of these:

```
def stubGenerator = cxf;
//def stubGenerator = axis;
//def stubGenerator = axis2;
```
