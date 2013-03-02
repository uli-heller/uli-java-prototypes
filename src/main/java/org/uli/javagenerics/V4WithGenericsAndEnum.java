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
public class V4WithGenericsAndEnum {
    final Map<String, Object> map;

    static public class Wrapper<T> {
        public Wrapper() {
        }
        @SuppressWarnings("unchecked")
        public T get(Object value) {
            return (T) value;
        }
    }

    static public class Element<T> {
        public final static Element<String> STRING_ELEMENT = new Element<String>("String", new Wrapper<String>());
        public final static Element<Boolean> BOOLEAN_ELEMENT = new Element<Boolean>("Boolean", new Wrapper<Boolean>());
        public final static Element<Long> LONG_ELEMENT = new Element<Long>("Long", new Wrapper<Long>());
        
        private String name;
        private Wrapper<T> wrapper;
        
        private Element(String name, Wrapper<T> wrapper) {
            this.name = name;
            this.wrapper = wrapper;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Wrapper<T> getWrapper() {
            return this.wrapper;
        }
    }

    public <T> T get(Element<T> element) {
        T result = (T) element.getWrapper().get(map.get(element.getName()));
        return result;
    }

    private V4WithGenericsAndEnum() {
        map = new HashMap<String, Object>();
        map.put(Element.STRING_ELEMENT.getName(), "a string");
        map.put(Element.BOOLEAN_ELEMENT.getName(), Boolean.valueOf(true));
        map.put(Element.LONG_ELEMENT.getName(), 42L);
    }

    private static class LazyHolder {
       private static final V4WithGenericsAndEnum INSTANCE = new V4WithGenericsAndEnum();
    }

    public static V4WithGenericsAndEnum getInstance() {
       return LazyHolder.INSTANCE;
    }
}
