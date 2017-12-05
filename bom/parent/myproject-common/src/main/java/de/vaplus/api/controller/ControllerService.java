package de.vaplus.api.controller;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.vaplus.api.controller.stub.AuthResultStub;
import de.vaplus.api.controller.stub.BugStub;
import de.vaplus.api.controller.stub.FaqStub;
import de.vaplus.api.controller.stub.FeatureRequestStub;

@WebService
public interface ControllerService {

	@WebMethod
	public AuthResultStub authInstance(String instanceId, String param, String paramHash) throws Exception;


	@WebMethod
	public List<BugStub> getBugList() throws Exception;

	@WebMethod
	public BugStub factoryNewBug() throws Exception;

	@WebMethod
	public BugStub saveBug(BugStub bug) throws Exception;


	@WebMethod
	public List<FaqStub> getFaqList() throws Exception;


	@WebMethod
	public FeatureRequestStub factoryNewFeatureRequest() throws Exception;

	@WebMethod
	public FeatureRequestStub saveFeatureRequest(FeatureRequestStub featureRequest) throws Exception;

	@WebMethod
	public List<FeatureRequestStub> getFeatureRequestList() throws Exception;
}
