/**
 * 
 */
package org.uli.javagenerics;

import java.util.HashMap;
import java.util.Map;


/**
 * @author uli
 *
 */
public class V2WithGenerics {
    @SuppressWarnings("rawtypes")
    final Map map;
    
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        T result = (T) map.get(name);
        return result;
    }

    public Boolean getBoolean(String name) {
        return this.<Boolean>get(name);
    }
    public String getString(String name) {
        return this.<String>get(name);
    }
    public Long getLong(String name) {
        return this.<Long>get(name);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private V2WithGenerics() {
        map = new HashMap();
        map.put("String", "a string");
        map.put("Boolean", Boolean.valueOf(true));
        map.put("Long", 42L);
    }

    private static class LazyHolder {
       private static final V2WithGenerics INSTANCE = new V2WithGenerics();
    }

    public static V2WithGenerics getInstance() {
       return LazyHolder.INSTANCE;
    }
}
