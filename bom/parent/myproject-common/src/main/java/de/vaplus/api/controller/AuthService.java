package de.vaplus.api.controller;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.vaplus.api.controller.stub.AuthResultStub;

@WebService
public interface AuthService {

	@WebMethod
	public AuthResultStub authInstance(String instanceId, String param, String paramHash) throws Exception;
}
