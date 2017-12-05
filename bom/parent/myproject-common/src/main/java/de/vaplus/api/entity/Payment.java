package de.vaplus.api.entity;

public interface Payment extends StatusBase{

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	boolean isSystemPayment();

	boolean isDirect();

	void setDirect(boolean direct);

	String getInvoiceText();

	void setInvoiceText(String invoiceText);

	int getTermOfPayment();

	void setTermOfPayment(int termOfPayment);


}
