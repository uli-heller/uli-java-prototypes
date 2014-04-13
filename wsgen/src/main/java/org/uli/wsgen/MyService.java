package org.uli.wsgen;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "MyService", targetNamespace = "org.uli.wsgen")
public class MyService {
    @WebMethod
    public BasicResult basicService() {
	return (BasicResult) null;
    }
    @WebMethod
    public ExtendedResult extendedService() {
	return (ExtendedResult) null;
    }
}
