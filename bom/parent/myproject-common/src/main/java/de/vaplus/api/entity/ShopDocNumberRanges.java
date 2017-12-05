package de.vaplus.api.entity;

import java.io.Serializable;

public interface ShopDocNumberRanges extends Serializable{

	Shop getShop();

	void setShop(Shop shop);

	long getInvoiceNumber();

	void setInvoiceNumber(long invoiceNumber);

	long getOrderNumber();

	void setOrderNumber(long orderNumber);

	long getPickslipNumber();

	void setPickslipNumber(long pickslipNumber);

}
