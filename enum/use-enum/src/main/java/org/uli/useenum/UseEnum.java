/**
 * 
 */
package org.uli.useenum;

import org.uli.javaenum.UliEnum;

/**
 * @author uli
 *
 */
public class UseEnum {
   public static void main(String[] args) {
      System.out.println("USE-ENUM");
      for (UliEnum e : UliEnum.values()) {
         System.out.println("name=" + e.name()+", ordinal="+e.ordinal());
      }
      UliEnum e1 = UliEnum.E1;
      System.out.println("E1: name="+e1.name()+", ordinal="+e1.ordinal());
   }
}
