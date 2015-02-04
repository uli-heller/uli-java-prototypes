package org.uli.jmx.annotationmbeanexporter;

import java.math.BigDecimal;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class MyJavaBean {
    // 2015-02-04 - spring-4.0.1, 3.2.13: Attributes aren't exported without this annotation
    @ManagedAttribute
    public Integer getInteger() {
        return integer;
    }
    // 2015-02-04 - spring-4.0.1, 3.2.13: Attributes aren't exported without this annotation
    @ManagedAttribute
    public void setInteger(Integer integer) {
        this.integer = integer;
    }
    @ManagedAttribute
    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }
    @ManagedAttribute
    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
    @ManagedAttribute
    public String getString() {
        return string;
    }
    @ManagedAttribute
    public void setString(String string) {
        this.string = string;
    }
    
    public String getWithoutAnnotation() {
        return withoutAnnotation;
    }
    public void setWithoutAnnotation(String withoutAnnotation) {
        this.withoutAnnotation = withoutAnnotation;
    }

    Integer integer;
    BigDecimal bigDecimal;
    String string;
    String withoutAnnotation;
}
