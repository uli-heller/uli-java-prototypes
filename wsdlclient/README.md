WSDL2JAVA - Creating Stubs
==========================

Preparations
------------

* Create a build description similar to 'build.gradle'
* Copy the WSDL into src/main/resources/wsdl, for example 'wsaa.wsdl'

Wsdl2Java
---------

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

Looking at the generated stubs, I see:

* CXF and Axis2 stubs are similar
* Axis stubs are totally different

CXF seems to be actively maintained, there are several versions
released this year (2014). Axis2 seems to be pretty stable, the last
version has been released two years ago (2012). Axis looks outdated,
the last version has been released eight years ago (2006); you have to
search a bit to find the download at
<http://archive.apache.org/dist/ws/axis/1_4/>.
