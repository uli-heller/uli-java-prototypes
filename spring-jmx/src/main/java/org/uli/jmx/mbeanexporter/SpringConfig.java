package org.uli.jmx.mbeanexporter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jmx.export.MBeanExporter;

@Configuration
public class SpringConfig {
    @Bean
    MyLombokBean myLombokBean() {
        return new MyLombokBean();
    }
    
    // With this, all properties get exported
    @Bean @Lazy(false)
    MBeanExporter mbeanExporter() {
        MBeanExporter mbe = new MBeanExporter();
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("bean:name=myLombokBean", myLombokBean());
        beans.put("bean:name=mySingleton", MySingleton.getInstance());
        mbe.setBeans(beans );
        return mbe;
    }
}
