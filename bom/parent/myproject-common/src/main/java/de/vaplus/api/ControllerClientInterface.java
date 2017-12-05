package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.controller.stub.AuthResultStub;
import de.vaplus.api.controller.stub.BugStub;
import de.vaplus.api.controller.stub.FaqStub;
import de.vaplus.api.controller.stub.FeatureRequestStub;

public interface ControllerClientInterface extends Serializable {

	List<FaqStub> getFaqList() throws Exception;
	
	List<BugStub> getBugList() throws Exception;

	List<FeatureRequestStub> getFeatureRequestList() throws Exception;

	BugStub factoryNewBug();

	void saveBug(BugStub bug) throws Exception;

	FeatureRequestStub factoryNewFeatureRequest();

	void saveFeatureRequest(FeatureRequestStub featureRequest) throws Exception;

	AuthResultStub authInstance(String instanceId, String param, String paramHash);


}
