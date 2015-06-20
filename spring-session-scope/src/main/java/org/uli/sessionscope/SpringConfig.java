package org.uli.sessionscope;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * @author X112640
 *
 */
@Configuration
@Lazy
public class SpringConfig {
   @Bean(name="counterBean")
   @Scope("session")
   public CounterBean getCounterBean() {
      return new CounterBean();
   }

   @Bean(name="tomcatSessionBean")
   @Scope("session")
   public TomcatSessionBean getTomcatSessionBean() {
      return new TomcatSessionBean();
   }
}
