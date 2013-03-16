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
public class V3WithGenerics {
    final Map<String, Wrapper<?>> map;

    public class Wrapper<T> {
        private T wrappedValue;
        public Wrapper(T wrappedValue) {
            this.wrappedValue = wrappedValue;
        }
        public T get() {
            return wrappedValue;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        T result = (T) map.get(name).get();
        return result;
    }

    private V3WithGenerics() {
        map = new HashMap<String,Wrapper<?>>();
        map.put("String", new Wrapper<String>("a string"));
        map.put("Boolean", new Wrapper<Boolean>(Boolean.valueOf(true)));
        map.put("Long", new Wrapper<Long>(42L));
    }

    private static class LazyHolder {
       private static final V3WithGenerics INSTANCE = new V3WithGenerics();
    }

    public static V3WithGenerics getInstance() {
       return LazyHolder.INSTANCE;
    }
}
