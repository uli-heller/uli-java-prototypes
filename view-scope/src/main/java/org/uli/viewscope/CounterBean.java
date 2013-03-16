package org.uli.viewscope;
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
 
@Component
@Scope("view")
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
}
