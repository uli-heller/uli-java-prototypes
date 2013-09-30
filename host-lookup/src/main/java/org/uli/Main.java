package org.uli;

import java.net.InetAddress;

public class Main {

    public static void main(String args[]) throws Exception {
        try {
            System.setProperty("sun.net.spi.nameservice.provider.1", "dns,mine");
            InetAddress ia = InetAddress.getByName(args[0]);
            System.out.println(ia);
        } catch (Exception e) {
            System.out.println("in exception handler" + e);
            e.printStackTrace();
        }
    }
}
