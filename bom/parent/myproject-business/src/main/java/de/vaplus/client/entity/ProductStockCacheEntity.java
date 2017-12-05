package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.ProductStockCache;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserStats;


@Entity
@Table(name="ProductStockCache")
@IdClass(ProductStockCachePK.class)
public class ProductStockCacheEntity implements ProductStockCache{

	private static final long serialVersionUID = -1639406654255691759L;

	@Id
	@ManyToOne
    @JoinColumn(name="product_id", nullable = false)
	private ProductEntity product;

	@Id
	@ManyToOne
    @JoinColumn(name="stock_id", nullable = false)
	private StockEntity stock;

	@Column(name="quantity", nullable = false)
	private BigDecimal quantity;
	
	public ProductStockCacheEntity(){
		
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public StockEntity getStock() {
		return stock;
	}

	public void setStock(StockEntity stock) {
		this.stock = stock;
	}

	@Override
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	


}