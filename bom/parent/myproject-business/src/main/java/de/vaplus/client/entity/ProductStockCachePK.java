package de.vaplus.client.entity;

import java.io.Serializable;

public class ProductStockCachePK implements Serializable {

	private static final long serialVersionUID = 6820388342115048325L;
	
	private long product;
	private long stock;
	
	public long getProduct() {
		return product;
	}
	
	public void setProduct(long product) {
		this.product = product;
	}
	
	public long getStock() {
		return stock;
	}
	
	public void setStock(long stock) {
		this.stock = stock;
	}
	
    public int hashCode() {
        return (int) product + (int) stock;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ProductStockCachePK)) return false;
        ProductStockCachePK pk = (ProductStockCachePK) obj;
        return pk.product == product && pk.stock == stock;
    }
}
