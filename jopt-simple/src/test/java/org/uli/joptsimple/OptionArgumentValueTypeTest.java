// http://pholser.github.io/jopt-simple/examples.html
package org.uli.joptsimple;

//import java.util.logging.Level;
//import org.apache.log4j.Level;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionArgumentValueTypeTest {
    @Test
    public void convertsArgumentsToJavaValueTypes() {
        OptionParser parser = new OptionParser();
        parser.accepts( "flag" );
        parser.accepts( "count" ).withRequiredArg().ofType( Integer.class );
        parser.accepts( "level" ).withOptionalArg().ofType( Level.class );

        OptionSet options = parser.parse( "--count", "3", "--level", "INFO" );

        assertTrue( options.has( "count" ) );
        assertTrue( options.hasArgument( "count" ) );
        Integer i = (Integer) options.valueOf("count");
        assertEquals( Integer.valueOf( 3 ), options.valueOf( "count" ) );

        assertTrue( options.has( "level" ) );
        assertTrue( options.hasArgument( "level" ) );
        assertEquals( Level.INFO, options.valueOf( "level" ) );
    }
    public enum Level {
      DEBUG, INFO, ERROR
    }
}

