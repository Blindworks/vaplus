package de.vaplus.api.entity;

import java.math.BigDecimal;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface InvoiceItem extends Base{

	String getTitle();

	void setTitle(String title);

	BigDecimal getQuantity();

	void setQuantity(BigDecimal quantity);

	String getSerial();

	void setSerial(String serial);

	Product getProduct();

	void setProduct(Product product);

	Invoice getInvoice();

	void setInvoice(Invoice invoice);

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	BigDecimal getUnitPrice();

	void setUnitPrice(BigDecimal unitPrice);

	BigDecimal getUnitPurchasePrice();

	void setUnitPurchasePrice(BigDecimal unitPurchasePrice);

	BigDecimal getUnitGrossPrice();

	void setUnitGrossPrice(BigDecimal unitGrossPrice);

	String getSubTitle();

	void setSubTitle(String subTitle);

	boolean isBlockedItem();

	void setBlockedItem(boolean blockedItem);

	boolean isManualItem();

	void setManualItem(boolean manualItem);

}
