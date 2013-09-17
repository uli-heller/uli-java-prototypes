/**
 * 
 */
package org.uli.bloom;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class MessageBeanTest {
    @Inject
    MessageBean messageBean;
    
    @Test
    public void testMessage() {
        assertEquals("Uli was here!", messageBean.getMessage());
    }
}
