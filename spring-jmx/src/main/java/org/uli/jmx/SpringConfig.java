package org.uli.jmx;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Lazy;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;

@Configuration
@EnableMBeanExport
public class SpringConfig {
    @Bean
    MyJavaBean myJavaBean() {
        return new MyJavaBean();
    }
    @Bean
    MyLombokBean myLombokBean() {
        return new MyLombokBean();
    }
    @Bean
    AnotherLombokBean anotherLombokBean() {
        return new AnotherLombokBean();
    }
    
    // With this, all properties get exported
    @Bean @Lazy(false)
    MBeanExporter mbeanExporter() {
        MBeanExporter mbe = new AnnotationMBeanExporter();
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("bean:name=myLombokBean", myLombokBean());
        mbe.setBeans(beans );
        return mbe;
    }
}
