package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.ControllerClientInterface;
import de.vaplus.api.controller.stub.FaqStub;

@ManagedBean(name="faqBean")
@SessionScoped
public class FaqBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;

	
	@EJB
	private ControllerClientInterface controllerClient;
    
	private FaqStub selectedFaq;
	
	public FaqBean() {
	}
	
	public List<FaqStub> getFaqList() throws Exception{
		return controllerClient.getFaqList();
	}

	public FaqStub getSelectedFaq() {
		return selectedFaq;
	}

	public void setSelectedFaq(FaqStub selectedFaq) {
		this.selectedFaq = selectedFaq;
	}
	
	

}
