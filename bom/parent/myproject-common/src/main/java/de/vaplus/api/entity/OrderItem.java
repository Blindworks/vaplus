package de.vaplus.api.entity;

import java.math.BigDecimal;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface OrderItem extends Base{

	String getTitle();

	void setTitle(String title);

	BigDecimal getQuantity();

	void setQuantity(BigDecimal quantity);

	String getSerial();

	void setSerial(String serial);

	Product getProduct();

	void setProduct(Product product);

	Order getOrder();

	void setOrder(Order order);

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	BigDecimal getUnitPrice();

	void setUnitPrice(BigDecimal unitPrice);

	BigDecimal getUnitGrossPrice();

	void setUnitGrossPrice(BigDecimal unitGrossPrice);

}
