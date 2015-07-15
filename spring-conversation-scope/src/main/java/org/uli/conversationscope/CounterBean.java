// Copied from: http://cagataycivici.wordpress.com/2010/02/17/port-jsf-2-0s-viewscope-to-spring-3-0/
package org.uli.conversationscope;
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.SessionScope;
 
@Component
@Scope("conversation")
public class CounterBean {
 
    private int counter = 0;
 
    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
 
    public void increment() {
        counter++;
    }
    
    SessionScope s;
}
