package org.uli.jsfviewscope;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
 
@ManagedBean
@ViewScoped
public class CounterBean implements Serializable {
 
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
