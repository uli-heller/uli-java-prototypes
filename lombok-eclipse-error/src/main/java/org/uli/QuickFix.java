package org.uli;

public class QuickFix {
   void t() {
      int i = getIt(); // error, try out quickfix on this
      // eclipse-jee-mars and eclipse-jee-mars-RC3
      // ... with lombok-1.16.4: quickfix shows up, but no code change happens
      // ... without lombok-1.16.4: quickfix works as expected
   }
   String getIt() {
      return "Got it";
   }
}
