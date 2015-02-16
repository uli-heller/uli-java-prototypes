package org.uli.gcb;

import org.junit.Test;
import static org.junit.Assert.*;

public class GuavaCacheKoTest {
  @Test
  public void testCache() throws Exception {
      GuavaCacheKo gc = new GuavaCacheKo();
      assertEquals(0, gc.cnt);
      String s = gc.getPair(1, 2);
      assertEquals(1, gc.cnt);
      assertEquals("this pair is (1,2) : 1", s);
      String s2 = gc.getPair(2, 3);
      assertEquals(2, gc.cnt);
      assertEquals("this pair is (2,3) : 2", s2);
      /*String s1 =*/ gc.getPair(1, 2);
      //assertEquals(2, gc.cnt); // this fails
      //assertEquals("this pair is (1,2) : 1", s1); // this fails, too
  }
}
