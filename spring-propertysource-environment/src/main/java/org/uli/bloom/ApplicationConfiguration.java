package org.uli.bloom;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:bloom.properties")
public class ApplicationConfiguration {
    @Inject
    private Environment environment;

    @Bean
    public MessageBean messageBean() {
        return new MessageBean(environment.getProperty("message"));
    }
}
