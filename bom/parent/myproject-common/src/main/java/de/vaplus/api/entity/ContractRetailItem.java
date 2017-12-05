package de.vaplus.api.entity;

public interface ContractRetailItem extends Base{

	String getSerial();

	void setSerial(String serial);

	Product getProduct();

	void setProduct(Product product);

	BaseContract getBaseContract();

	void setBaseContract(BaseContract baseContract);

}
