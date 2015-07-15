package org.uli.conversationscope;


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
   @Scope("conversation")
   public CounterBean getCounterBean() {
      return new CounterBean();
   }
   //@Bean
   //public static CustomScopeConfigurer getCustomScopeConfigurer() {
   //   CustomScopeConfigurer csc = new CustomScopeConfigurer();
   //   Map<String, Object> scopes = new HashMap<String, Object>();
   //   scopes.put("conversation", new ConversationScope());
   //   csc.setScopes(scopes);
   //   return csc;
   //}
}
