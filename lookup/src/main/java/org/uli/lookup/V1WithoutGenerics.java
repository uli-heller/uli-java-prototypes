/**
 * 
 */
package org.uli.lookup;

import java.util.HashMap;
import java.util.Map;


/**
 * @author uli
 *
 */
public class V1WithoutGenerics {
    @SuppressWarnings("rawtypes")
    final Map map;
    
    public Object get(String name) {
        Object result = map.get(name);
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private V1WithoutGenerics() {
        map = new HashMap();
        map.put("String", "a string");
        map.put("Boolean", Boolean.valueOf(true));
        map.put("Long", 42L);
    }

    private static class LazyHolder {
       private static final V1WithoutGenerics INSTANCE = new V1WithoutGenerics();
    }

    public static V1WithoutGenerics getInstance() {
       return LazyHolder.INSTANCE;
    }
}
