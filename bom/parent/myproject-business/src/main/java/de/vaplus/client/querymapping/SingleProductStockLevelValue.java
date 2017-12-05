package de.vaplus.client.querymapping;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;


@Entity
@SqlResultSetMapping(
        name = "SingleProductStockLevelValueMapping",
		entities = {
	        @EntityResult(
	            entityClass = SingleProductStockLevelValue.class,
	            fields = {
	                @FieldResult(name = "id", column = "id"),
	                @FieldResult(name = "stock_id", column = "stock_id"),
	                @FieldResult(name = "serial", column = "serial"),
	                @FieldResult(name = "stockName", column = "name"),
	                @FieldResult(name = "purchasePrice", column = "purchasePrice")
	            }
	        )
        }
)
public class SingleProductStockLevelValue implements SingleProductStockLevelValueInterface{
	
	@Id
	private int id;
	
	private long stock_id;
	
	private String serial;
	
	private String stockName;
	
	private BigDecimal purchasePrice;

	@Override
	public long getStock_id() {
		return stock_id;
	}

	@Override
	public void setStock_id(long stock_id) {
		this.stock_id = stock_id;
	}

	@Override
	public String getSerial() {
		return serial;
	}

	@Override
	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Override
	public String getStockName() {
		return stockName;
	}

	@Override
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@Override
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	@Override
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}


}
