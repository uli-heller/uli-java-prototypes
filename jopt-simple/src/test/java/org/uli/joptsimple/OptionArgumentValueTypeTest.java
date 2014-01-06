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

	/*
        assertTrue( options.has( "level" ) );
        assertTrue( options.hasArgument( "level" ) );
	Level k = Level.valueOf("INFO");
        --
        -- joptsimple.OptionArgumentConversionException: Cannot parse argument 'INFO' of option ['level']
	--   at joptsimple.AbstractOptionSpec.convertWith(AbstractOptionSpec.java:94)
	--   at joptsimple.ArgumentAcceptingOptionSpec.convert(ArgumentAcceptingOptionSpec.java:276)
	--   at joptsimple.OptionSet.valuesOf(OptionSet.java:220)
	--   at joptsimple.OptionSet.valueOf(OptionSet.java:169)
	--   at joptsimple.OptionSet.valueOf(OptionSet.java:150)
	--   at org.uli.joptsimple.OptionArgumentValueTypeTest.convertsArgumentsToJavaValueTypes(OptionArgumentValueTypeTest.java:31)
	--   at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        -- ...
        -- Caused by: joptsimple.internal.ReflectionException: java.lang.IllegalAccessException: Class joptsimple.internal.Reflection can not access a member of class org.uli.joptsimple.Level with modifiers "public static"
	--   at joptsimple.internal.Reflection.reflectionException(Reflection.java:144)
	--   at joptsimple.internal.Reflection.invoke(Reflection.java:122)
	--   at joptsimple.internal.MethodInvokingValueConverter.convert(MethodInvokingValueConverter.java:48)
	--   at joptsimple.internal.Reflection.convertWith(Reflection.java:128)
	--   at joptsimple.AbstractOptionSpec.convertWith(AbstractOptionSpec.java:91)
	--   ... 47 more
        -- Caused by: java.lang.IllegalAccessException: Class joptsimple.internal.Reflection can not access a member of class org.uli.joptsimple.Level with modifiers "public static"
	--   at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:101)
	--   at java.lang.reflect.AccessibleObject.slowCheckMemberAccess(AccessibleObject.java:295)
	--   at java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:287)
	--   at java.lang.reflect.Method.invoke(Method.java:476)
	--   at joptsimple.internal.Reflection.invoke(Reflection.java:119)
	--   ... 50 more
        --
        Level l = (Level) options.valueOf("level");
        assertEquals( Level.INFO, options.valueOf( "level" ) );
        */
    }
}

enum Level {
    DEBUG, INFO, ERROR
}