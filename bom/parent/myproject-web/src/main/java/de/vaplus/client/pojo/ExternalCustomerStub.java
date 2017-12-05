package de.vaplus.client.pojo;

import java.util.ArrayList;
import java.util.List;

import de.vaplus.api.entity.BaseContract;

public class ExternalCustomerStub {
	private String customerId;
	private String password;
	private List<BaseContract> contractList;
	
	private boolean containsExtensionOfTheTermContract;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<BaseContract> getContractList() {
		if(contractList == null)
			contractList = new ArrayList<BaseContract>();
		return contractList;
	}
	public void setContractList(List<BaseContract> contractList) {
		this.contractList = contractList;
	}
	public boolean isContainsExtensionOfTheTermContract() {
		return containsExtensionOfTheTermContract;
	}
	public void setContainsExtensionOfTheTermContract(
			boolean containsExtensionOfTheTermContract) {
		this.containsExtensionOfTheTermContract = containsExtensionOfTheTermContract;
	}
}
