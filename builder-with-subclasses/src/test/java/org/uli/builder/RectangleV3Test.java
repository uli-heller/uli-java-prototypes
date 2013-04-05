package org.uli.builder;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

public class RectangleV3Test {
    @Test
    public void basicTest () {
	RectangleV3 r = RectangleV3.builder().width(4.0).height(2.0).opacity(0.5).build();
	assertEquals(4.0, r.getWidth(), 0.1);
	assertEquals(2.0, r.getHeight(), 0.1);
	assertEquals(0.5, r.getOpacity(), 0.1);
    }

    @Test
    public void reverseTest () {
	RectangleV3 r = RectangleV3.builder().opacity(0.6).height(10.0).width(3.0).build();
	assertEquals(3.0, r.getWidth(), 0.1);
	assertEquals(10.0, r.getHeight(), 0.1);
	assertEquals(0.6, r.getOpacity(), 0.1);
    }
}
