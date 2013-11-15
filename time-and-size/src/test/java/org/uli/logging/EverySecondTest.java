package org.uli.logging;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EverySecondTest {

    @BeforeClass
    static public void setUp() {
        setLog4jXml("src/test/resources/log4j-every-second.xml");
    }

    @AfterClass
    static public void tearDown() {
        setLog4jXml("src/test/resources/log4j.xml");
    }

    private static final void setLog4jXml(final String filename) {
        DOMConfigurator.configure(filename);
    }

    @Test
    public void testEcho() {
        try {
        final int LOOP_COUNT = 5;
        EchoServer echoServer = new EchoServer();
        String echo = echoServer.echo("UliWasHere!");
        assertEquals("UliWasHere!", echo);
        for (int i = 0; i < LOOP_COUNT; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                ;
            }
            echoServer.echo("This is a log message, i=" + i);
        }
        int count = glob(".", "logging-every-second.*");
        assertTrue(count > LOOP_COUNT - 2);
        assertTrue(count < LOOP_COUNT + 2);
        } finally {
            //delete(".", "logging-every-second.*");
        }
    }

    private final int glob(String folderName, String stringPattern) {
        Pattern pattern = Pattern.compile(stringPattern);
        File folder = new File(folderName);
        int count = 0;
        for (File file : folder.listFiles()) {
            if (pattern.matcher(file.getName()).matches()) {
                ++count;
            }
        }
        return count;
    }
    
    private final void delete(String folderName, String stringPattern) {
        Pattern pattern = Pattern.compile(stringPattern);
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            if (pattern.matcher(file.getName()).matches()) {
                file.delete();
            }
        }
    }
}
