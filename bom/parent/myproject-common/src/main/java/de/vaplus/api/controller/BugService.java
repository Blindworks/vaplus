package de.vaplus.api.controller;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.vaplus.api.controller.stub.BugStub;

@WebService
public interface BugService {

	@WebMethod
	public List<BugStub> getBugList() throws Exception;

	@WebMethod
	public BugStub factoryNewBug() throws Exception;

	@WebMethod
	public BugStub saveBug(BugStub bug) throws Exception;
}
