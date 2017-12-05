package de.vaplus.api.entity;

import java.util.List;

public interface CommissionTypeAssociation extends Base{

	Vendor getVendor();

	BaseProduct getProduct();

	void setProduct(BaseProduct product);

	List<? extends ProductOption> getProductOptionList();

	void setProductOptionList(List<? extends ProductOption> productOptionList);

//	boolean isChargeback();

//	void setChargeback(boolean chargeback);

	boolean isExtensionOfTheTerm();

	void setExtensionOfTheTerm(boolean extensionOfTheTerm);

	void setVendor(Vendor vendor);

	boolean hasProduct(BaseProduct p);

	boolean hasProductOption(ProductOption po);

	String getCommissionText();

	String getCommissionSubText();

}
