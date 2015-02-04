package org.uli.jmx;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.WebApplicationInitializer;

@Slf4j
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.trace("->");
        log.trace("<-");
    }
}
