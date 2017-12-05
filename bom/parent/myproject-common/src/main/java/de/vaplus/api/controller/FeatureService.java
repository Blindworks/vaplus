	package de.vaplus.api.controller;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.vaplus.api.controller.stub.FeatureRequestStub;

@WebService
public interface FeatureService {

	@WebMethod
	public FeatureRequestStub factoryNewFeatureRequest() throws Exception;

	@WebMethod
	public FeatureRequestStub saveFeatureRequest(FeatureRequestStub featureRequest) throws Exception;

	@WebMethod
	public List<FeatureRequestStub> getFeatureRequestList() throws Exception;
}
