package de.vaplus.client.querymapping;

import java.math.BigDecimal;

public interface SingleProductStockLevelValueInterface {

	public long getStock_id();

	public void setStock_id(long stock_id);

	public String getSerial();

	public void setSerial(String serial);

	String getStockName();

	void setStockName(String stockName);

	BigDecimal getPurchasePrice();

	void setPurchasePrice(BigDecimal purchasePrice);
	

}
