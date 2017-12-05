package de.vaplus.client.pojo;

import java.util.HashMap;
import java.util.Map;

public class ExternalCustomerList {
	
	private int contractCount;
	private Map<String, ExternalCustomerStub> customerList;
	
	public int getContractCount() {
		return contractCount;
	}
	public void setContractCount(int contractCount) {
		this.contractCount = contractCount;
	}
	
	public Object[] getCustomerIdList(){
		return getCustomerList().keySet().toArray();
	}
	
	private Map<String, ExternalCustomerStub> getCustomerList() {
		if(customerList == null)
			customerList = new HashMap<String, ExternalCustomerStub>();
		return customerList;
	}
	
	public ExternalCustomerStub getCustomerList(String customerId) {
		if(getCustomerList().get(customerId) == null)
			getCustomerList().put(customerId, new ExternalCustomerStub());
		return getCustomerList().get(customerId);
	}
}
