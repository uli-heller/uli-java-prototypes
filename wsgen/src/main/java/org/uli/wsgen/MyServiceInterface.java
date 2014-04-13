package org.uli.wsgen;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "MyService", targetNamespace = "http://wsgen.uli.org/")
public interface MyServiceInterface {
    @WebMethod
    public BasicResult basicService();
    @WebMethod
    public ExtendedResult extendedService();
}
