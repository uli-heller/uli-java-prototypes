// Derived from
// http://www.codeproject.com/Tips/372152/Mapping-JDBC-ResultSet-to-Object-using-Annotations
package org.uli.perfidix;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanPropertySetter {
    public BeanPropertySetter() {
        this(Implementation.JAVA);
    }
    
    public BeanPropertySetter(Implementation impl) {
        this.impl = impl;
    }
    
    public enum Implementation {
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
