package de.vaplus.api.entity;

import java.util.List;

public interface ExternalCustomer extends Base{

	Customer getCustomer();

	void setCustomer(Customer customer);

	String getCustomerId();

	void setCustomerId(String customerId);

	String getPassword();

	void setPassword(String password);

	List<? extends BaseContract> getContractList();

	void setContractList(List<? extends BaseContract> contractList);

}
