package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductStockCache;
import de.vaplus.api.entity.Stock;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Product")
public class ProductEntity extends BaseProductEntity implements Product{

	private static final long serialVersionUID = -1205535549975626074L;
	
	@Column(name="ean", length=13, nullable = true)
	private String ean;

	@Column(name="purchasePrice", precision = 10, scale = 2, nullable = false)
	private BigDecimal purchasePrice;
	
	@Column(name="avergePurchasePrice", precision = 10, scale = 4, nullable = false)
	private BigDecimal avergePurchasePrice;
	
	@Column(name="minPurchasePrice", precision = 10, scale = 4, nullable = false)
	private BigDecimal minPurchasePrice;
	
	@Column(name="maxPurchasePrice", precision = 10, scale = 4, nullable = false)
	private BigDecimal maxPurchasePrice;
	
	@Column(name="stockQuantity", precision = 10, scale = 4, nullable = false)
	private BigDecimal stockQuantity;

	@Lob 
	@Column(name="description", nullable = true)
	private String description;

	@Column(name="bookableAsForeignWare", nullable = false)
	private boolean bookableAsForeignWare;

	@Column(name="serialRequired", nullable = false)
	private boolean serialRequired;
	
	// Bestandsführung
	@Column(name="stockControlled", nullable = false)
	private boolean stockControlled;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductStockCacheEntity> ProductStockCacheList;

	public ProductEntity() {
		super();
	}

	@Override
	public String getEan() {
		return ean;
	}

	@Override
	public void setEan(String ean) {
		this.ean = ean;
	}

	@Override
	public BigDecimal getPurchasePrice() {
		if(purchasePrice == null)
			purchasePrice = new BigDecimal(0);
		return purchasePrice;
	}

	@Override
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Override
	public BigDecimal getAvergePurchasePrice() {
		return avergePurchasePrice;
	}

	public void setAvergePurchasePrice(BigDecimal avergePurchasePrice) {
		this.avergePurchasePrice = avergePurchasePrice;
	}

	@Override
	public BigDecimal getMinPurchasePrice() {
		return minPurchasePrice;
	}

	public void setMinPurchasePrice(BigDecimal minPurchasePrice) {
		this.minPurchasePrice = minPurchasePrice;
	}

	@Override
	public BigDecimal getMaxPurchasePrice() {
		return maxPurchasePrice;
	}

	public void setMaxPurchasePrice(BigDecimal maxPurchasePrice) {
		this.maxPurchasePrice = maxPurchasePrice;
	}

	public BigDecimal getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(BigDecimal stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isBookableAsForeignWare() {
		return bookableAsForeignWare;
	}

	@Override
	public void setBookableAsForeignWare(boolean bookableAsForeignWare) {
		this.bookableAsForeignWare = bookableAsForeignWare;
	}

	@Override
	public boolean isSerialRequired() {
		return serialRequired;
	}

	@Override
	public void setSerialRequired(boolean serialRequired) {
		this.serialRequired = serialRequired;
	}

	@Override
	public List<? extends ProductStockCache> getProductStockCacheList() {
		if(ProductStockCacheList == null)
			ProductStockCacheList = new LinkedList<ProductStockCacheEntity>();
		return ProductStockCacheList;
	}

	public void setProductStockCacheList(List<ProductStockCacheEntity> productStockCacheList) {
		ProductStockCacheList = productStockCacheList;
	}
	
	@Transient
	public ProductStockCacheEntity getProductStockCache(Stock stock){
		Iterator<? extends ProductStockCache> i = getProductStockCacheList().iterator();
		ProductStockCacheEntity cache;
		while(i.hasNext()){
			cache = (ProductStockCacheEntity) i.next();
			if(cache.getStock().getId() == stock.getId())
				return cache;
		}
		
		cache = new ProductStockCacheEntity();
		cache.setStock((StockEntity) stock);
		cache.setProduct(this);
		ProductStockCacheList.add(cache);
		
		return cache;
	}

	@Override
	public boolean isStockControlled() {
		return stockControlled;
	}

	@Override
	public void setStockControlled(boolean stockControlled) {
		this.stockControlled = stockControlled;
	}

}
