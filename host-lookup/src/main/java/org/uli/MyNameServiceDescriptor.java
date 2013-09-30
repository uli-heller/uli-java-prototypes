/**
 * 
 */
package org.uli;

import sun.net.spi.nameservice.*;

public final class MyNameServiceDescriptor implements NameServiceDescriptor {

    public NameService createNameService() throws Exception {
        return new MyNameService();
    }

    public String getProviderName() {
        return "mine";
    }

    public String getType() {
        return "dns";
    }
}
