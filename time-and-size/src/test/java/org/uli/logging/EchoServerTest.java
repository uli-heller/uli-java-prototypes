package org.uli.logging;

import org.junit.Test;
import static org.junit.Assert.*;

public class EchoServerTest {

    @Test
    public void testEcho() {
        EchoServer echoServer = new EchoServer();
        String echo = echoServer.echo("UliWasHere!");
        assertEquals("UliWasHere!", echo);
	for (int i=0; i<20000; i++) {
	    echoServer.echo("This is a log message, i="+i);
	}
    }
}
