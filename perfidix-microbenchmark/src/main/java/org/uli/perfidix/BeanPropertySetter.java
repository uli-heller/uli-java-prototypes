// Derived from
// http://www.codeproject.com/Tips/372152/Mapping-JDBC-ResultSet-to-Object-using-Annotations
package org.uli.perfidix;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BeanPropertySetter {
    public BeanPropertySetter() {
        this(Implementation.JAVA);
    }
    
    public BeanPropertySetter(Implementation impl) {
        this.impl = impl;
    }

    private static class methodCacheKey {
        final private Class clazz;
        final private String propertyName;
        
        public methodCacheKey(final Class clazz, final String propertyName) {
            this.clazz = clazz;
            this.propertyName = propertyName;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
            result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            methodCacheKey other = (methodCacheKey) obj;
            if (clazz == null) {
                if (other.clazz != null) return false;
            } else if (!clazz.equals(other.clazz)) return false;
            if (propertyName == null) {
                if (other.propertyName != null) return false;
            } else if (!propertyName.equals(other.propertyName)) return false;
            return true;
        }
    }
    
    static final ConcurrentMap<methodCacheKey, Method> methodCache = new ConcurrentHashMap<methodCacheKey, Method>();
    static final LoadingCache<methodCacheKey, Method> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .build(new CacheLoader<methodCacheKey, Method>() {
                    public Method load(methodCacheKey key) throws Exception {
                        PropertyDescriptor pd = new PropertyDescriptor(key.propertyName, key.clazz);
                        Method m = pd.getWriteMethod();
                        return m;
                    }
                });
    
    public enum Implementation {
        JAVA_GUAVA_CACHE {
            @Override
            public void setProperty(Object bean, String name, Object value) throws Exception {
                Class<?> clazz = bean.getClass();
                methodCacheKey mck = new methodCacheKey(clazz, name);
                Method m = loadingCache.get(mck);
                m.invoke(bean, value);
            }
        },
        JAVA_METHOD_CACHE {
            @Override
            public void setProperty(Object bean, String name, Object value) throws Exception {
                Class<?> clazz = bean.getClass();
                methodCacheKey mck = new methodCacheKey(clazz, name);
                Method m = methodCache.get(mck);
                if (m == null) {
                    PropertyDescriptor pd = new PropertyDescriptor(name, bean.getClass());
                    m = pd.getWriteMethod();
                    methodCache.put(mck, m);
                }
                m.invoke(bean, value);
            }
        },
        JAVA {
            @Override
            public void setProperty(Object bean, String name, Object value) throws Exception {
                PropertyDescriptor pd = new PropertyDescriptor(name, bean.getClass());
                pd.getWriteMethod().invoke(bean, value);
            }
        },
        BEANUTILS {
            @Override
            public void setProperty(Object bean, String name, Object value) throws Exception {
                BeanUtils.setProperty(bean, name, value);
            }
        },
        SPRING {
            public void setProperty(Object bean, String name, Object value) throws Exception {
                BeanWrapper bw = new BeanWrapperImpl(bean);
                bw.setPropertyValue(name, value);
            }
        };
        
        public abstract void setProperty(Object bean, String name, Object value) throws Exception;
    }
    
    private final Implementation impl;

    public void setProperty(Object bean, String name, Object value) throws Exception {
        this.impl.setProperty(bean, name, value);
    }
}
