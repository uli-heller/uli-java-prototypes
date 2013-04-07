/**
 * 
 */
package org.uli.bloom;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class MessageBeanTest2 {
    @Inject
    MessageBean messageBean;

    @Test
    public void testMessage() {
        assertEquals("Uli was here!", messageBean.getMessage());
    }

    @Configuration
    @PropertySource("classpath:bloom.properties")
    static class ApplicationConfiguration {
        @Inject
        private Environment environment;

        @Bean
        public MessageBean messageBean() {
            return new MessageBean(environment.getProperty("message"));
        }
    }
}
