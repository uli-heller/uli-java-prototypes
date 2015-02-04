package org.uli.jmx.mbeanexporter;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MySingleton {
    private MySingleton() {}
    
    private static class MySingletonHolder {
        private static final MySingleton INSTANCE = new MySingleton();
    }

    public static MySingleton getInstance() {
        return MySingletonHolder.INSTANCE;
    }

    Integer integer;
    BigDecimal bigDecimal;
    String string;
}
