package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface StockMovement extends Base{

	StockPickslip getStockPickslip();

	void setStockPickslip(StockPickslip stockPickslip);

	Product getProduct();

	void setProduct(de.vaplus.api.entity.Product product);

	BigDecimal getQuantity();

	void setQuantity(BigDecimal quantity);

	String getSerial();

	void setSerial(String serial);

	BigDecimal getPurchasePrice();

	void setPurchasePrice(BigDecimal purchasePrice);


}
