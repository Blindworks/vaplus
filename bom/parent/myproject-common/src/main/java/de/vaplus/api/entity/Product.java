package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

public interface Product extends BaseProduct{

	String getEan();

	void setEan(String ean);

	BigDecimal getPurchasePrice();

	void setPurchasePrice(BigDecimal purchasePrice);

	String getDescription();

	void setDescription(String description);

	boolean isBookableAsForeignWare();

	void setBookableAsForeignWare(boolean bookableAsForeignWare);

	boolean isSerialRequired();

	void setSerialRequired(boolean serialRequired);

	List<? extends ProductStockCache> getProductStockCacheList();

	BigDecimal getAvergePurchasePrice();

	BigDecimal getMinPurchasePrice();

	BigDecimal getMaxPurchasePrice();

	boolean isStockControlled();

	void setStockControlled(boolean stockControlled);


}
