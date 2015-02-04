package org.uli.jmx.annotationmbeanexporter;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

@Configuration
@EnableMBeanExport
public class SpringConfig {
    @Bean
    MyJavaBean myJavaBean() {
        return new MyJavaBean();
    }
    @Bean
    AnotherLombokBean anotherLombokBean() {
        return new AnotherLombokBean();
    }
    @Bean
    NonManagedLombokBean nonManagedLombokBean() {
        return new NonManagedLombokBean();
    }
}
