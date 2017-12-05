package de.vaplus.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public interface ProductStockCache extends Serializable {

	BigDecimal getQuantity();

}
