Microbenchmarks Based On Perfidix
=================================

This is a small prototype related to microbenchmarking.
Originally, I planned to use [Google Caliper](https://code.google.com/p/caliper/).
Unfortunately,  [Google Caliper](https://code.google.com/p/caliper/) isn't in a good shape
at the moment in my opinion:

* Version 1.0-beta-1 is the current version available via maven
* This version doesn't seem to support annotations, but on the other hand most examples do use annotations
* Switching to git master doesn't work, since that version doesn't build cleanly

In the end, I dropped the idea of using caliper and stumbled over
[Perfidix](http://disy.github.io/perfidix/). It is pretty easy to set up and
it has way less dependencies than [Google Caliper](https://code.google.com/p/caliper/).

Goal
----

I do have a class allowing you to set the properties of a java bean. You pass the instance,
the property name and the property value to the class and it sets the property for the instance.
Setting the property can be done in two different ways:

* based on standard java calls
* based on commons-beanutils

This is the implementation class:

* src/main/java/org/uli/perfidix/BeanPropertySetter.java

Looking at the implementation it is unclear which of these is faster.

Junit Tests
-----------

At first, I created junit tests for the class. These are the files belonging to the
junit tests:

* src/test/java/org/uli/perfidix/Person.java
* src/test/java/org/uli/perfidix/BeanPropertySetterTest.java

To run the junit tests, execute `gradle --daemon test`.

Benchmarks
----------

The benchmarks are implemented within these classes:

* src/test/java/org/uli/perfidix/BeanPropertySetterBenchmark.java
* src/test/java/org/uli/perfidix/MyBenchmark.java

You run the benchmarks by executing `gradle --daemon runBenchmark`.
Afterwards, the benchmark results are available in build/benchmarks.

Results
-------

Looking at the results shows:

* the method using standard java calls is faster when doing only a small number of executions (10)
* the method based on commons-beanutils is faster when doing a large number of executions (100, 1000)
