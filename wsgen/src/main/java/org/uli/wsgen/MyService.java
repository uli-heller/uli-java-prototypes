package org.uli.wsgen;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class MyService implements MyServiceInterface {
    @Override
    public BasicResult basicService() {
	return (BasicResult) null;
    }
    @Override
    public ExtendedResult extendedService() {
	return (ExtendedResult) null;
    }
}
