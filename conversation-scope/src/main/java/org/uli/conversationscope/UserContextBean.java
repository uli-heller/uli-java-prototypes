// http://www.byteslounge.com/tutorials/java-ee-cdi-conversationscoped-example
package org.uli.conversationscope;

import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named
@ConversationScoped
public class UserContextBean implements Serializable {

  private static final long serialVersionUID = 4771270804699990999L;
  
  private String name;
  private String language;

/**
 * @return the name
 */
public String getName() {
    return name;
}

/**
 * @param name the name to set
 */
public void setName(String name) {
    this.name = name;
}

/**
 * @return the language
 */
public String getLanguage() {
    return language;
}

/**
 * @param language the language to set
 */
public void setLanguage(String language) {
    this.language = language;
}
  
}
