// Derived from
// http://www.codeproject.com/Tips/372152/Mapping-JDBC-ResultSet-to-Object-using-Annotations
package org.uli.perfidix;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanPropertySetter {
    public void setProperty(Object bean, String name, Object value) throws Exception {
        PropertyDescriptor pd = new PropertyDescriptor(name, bean.getClass());
        pd.getWriteMethod().invoke(bean, value);
    }
    public void setPropertyBeanUtils(Object bean, String name, Object value) throws Exception {
        BeanUtils.setProperty(bean, name, value);
    }
    public void setPropertySpring(Object bean, String name, Object value) throws Exception {
        BeanWrapper bw = new BeanWrapperImpl(bean);
        bw.setPropertyValue(name, value);
    }
}
