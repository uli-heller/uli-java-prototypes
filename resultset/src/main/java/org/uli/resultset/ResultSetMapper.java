// Derived from
// http://www.codeproject.com/Tips/372152/Mapping-JDBC-ResultSet-to-Object-using-Annotations
package org.uli.resultset;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.beanutils.BeanUtils;

public class ResultSetMapper {

    public <T> List<T> mapResultSetToObject(ResultSet rs, Class<T> outputClass) {
        List<T> outputList = null;
        try {
            // make sure resultset is not null
            if (rs != null) {
                // check if outputClass has 'Entity' annotation
                // if (outputClass.isAnnotationPresent(Entity.class)) {
                // get the resultset metadata
                ResultSetMetaData rsmd = rs.getMetaData();
                // get all the attributes of outputClass
                Field[] fields = outputClass.getDeclaredFields();
                while (rs.next()) {
                    T bean = (T) outputClass.newInstance();
                    for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                        // getting the SQL column name
                        String columnLabel = rsmd.getColumnLabel(_iterator + 1);
                        // reading the value of the SQL column
                        Object columnValue = rs.getObject(_iterator + 1);
                        if (columnValue != null) {
                            // iterating over outputClass attributes to check if
                            // any attribute has 'Column' annotation with
                            // matching 'name' value
                            for (Field field : fields) {
                                String name = field.getName();
                                if (field.isAnnotationPresent(Column.class)) {
                                    Column column = field.getAnnotation(Column.class);
                                    name = column.name();
                                }
                                if (name.equalsIgnoreCase(columnLabel)) {
                                    //BeanUtils.setProperty(bean, field.getName(), columnValue);
                                    this.setProperty(bean, field.getName(), columnValue);
                                    break;
                                }
                            }
                        }
                    }
                    if (outputList == null) {
                        outputList = new ArrayList<T>();
                    }
                    outputList.add(bean);
                }
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outputList;
    }
    
    private final void setProperty(Object bean, String name, Object value) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PropertyDescriptor pd = new PropertyDescriptor(name, bean.getClass());
        pd.getWriteMethod().invoke(bean, value);
    }
}
