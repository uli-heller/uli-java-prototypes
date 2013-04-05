package org.uli.builder;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

public class RectangleTest {
    @Test
    public void basicTest () {
	Rectangle r = new Rectangle.Builder().width(4.0).height(2.0).opacity(0.5).build();
	assertEquals(4.0, r.getWidth(), 0.1);
	assertEquals(2.0, r.getHeight(), 0.1);
	assertEquals(0.5, r.getOpacity(), 0.1);
    }
}
