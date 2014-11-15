LOMBOK und JPA
==============

In diesem Projekt verwende ich Lombok in Kombination mit Hibernate JPA. Primäres Ziel: "Beweisen", dass man Lombok-Entities ziemlich gleichartig wie "normale" Java-Entities verwenden kann.

* src/main/java/org/uli/lombokjpa/TraditionalPerson.java ... Java-Entity-Klasse
* src/main/java/org/uli/lombokjpa/LombokPerson.java ... Lombok-Entity-Klasse
* src/main/java/org/uli/lombokjpa/AnotherLombokPerson.java ... noch eine Lombok-Entity-Klasse
* src/test/java ... diverse JUnit-Tests, die wechselseitig die Entity-Klassen verwenden und sicherstellen, dass die Ergebnisse gleichartig sind

Die Klassen LombokPerson und AnotherLombokPerson unterscheiden sich so:

* AnotherLombokVersion enthält etwas weniger Java-Code
* AnotherLombokVersion enthält einen Builder
* AnotherLombokVersion basiert auf der [hier](https://groups.google.com/forum/#!topic/project-lombok/oLqKQ8FVN2E) vorgestellten Variante
