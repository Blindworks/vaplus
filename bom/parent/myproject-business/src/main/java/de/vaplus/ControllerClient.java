package de.vaplus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;

import de.vaplus.api.ControllerClientInterface;
import de.vaplus.api.controller.ControllerService;
import de.vaplus.api.controller.stub.AuthResultStub;
import de.vaplus.api.controller.stub.BugStub;
import de.vaplus.api.controller.stub.FaqStub;
import de.vaplus.api.controller.stub.FeatureRequestStub;


@Named
@Stateless
public class ControllerClient implements ControllerClientInterface {

	private static final long serialVersionUID = 7088642315455541834L;

//	private ControllerServiceInterface controllerService;
	
	private ControllerService getWebservicePort() throws MalformedURLException{
		
		String wsdl = "http://controller.vaplus.de/ControllerServiceImpl.wsdl";

//        URL wsdlUrl = new URL("http://localhost:8080/VAPlus_Controller-war/"+servicename+"?wsdl");
        URL wsdlUrl = new URL(wsdl);
        QName qname = new QName("http://webservices.vaplus.de/","ControllerServiceImplService");
		Service service = Service.create(wsdlUrl, qname);
		ControllerService port = service.getPort(ControllerService.class);
		
		Map<String, Object> requestContext = ((BindingProvider)port).getRequestContext();
//		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
		
		Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
		requestHeaders.put("credentials", Collections.singletonList("813708y3.dej09ma3#9unfp9pa9#q3ir7dvcoqG.F3RCAFQ4"));
		requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
		
		return port;
		
	}
	
	@Override
	public List<FaqStub> getFaqList() throws Exception{
		
		ControllerService port = getWebservicePort();
		
		return port.getFaqList();
	}
	
	@Override
	public List<FeatureRequestStub> getFeatureRequestList() throws Exception{
		
		ControllerService port = getWebservicePort();
		
		return port.getFeatureRequestList();
	}

	@Override
	public List<BugStub> getBugList() throws Exception{
		
		ControllerService port = getWebservicePort();
		
		return port.getBugList();
	}


	@Override
	public BugStub factoryNewBug() {
		return new BugStub();
	}

	@Override
	public void saveBug(BugStub bug) throws Exception {
		
		ControllerService port = getWebservicePort();
		
		port.saveBug(bug);
	}


	@Override
	public FeatureRequestStub factoryNewFeatureRequest() {
		return new FeatureRequestStub();
	}


	@Override
	public void saveFeatureRequest(FeatureRequestStub featureRequest) throws Exception {
		
		ControllerService port = getWebservicePort();
		
		port.saveFeatureRequest(featureRequest);
	}

	
	@Override
	public AuthResultStub authInstance(String instanceId, String param, String paramHash){

		try {
			ControllerService port = getWebservicePort();
			return port.authInstance(instanceId, param, paramHash);
		} catch (Exception e) {
			return null;
		}
	}
}
