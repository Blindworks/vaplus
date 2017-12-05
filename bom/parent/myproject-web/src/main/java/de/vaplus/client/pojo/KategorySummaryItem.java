package de.vaplus.client.pojo;

import java.math.BigDecimal;

public class KategorySummaryItem {
	
	private String name;
	
	private BigDecimal count;
	
	private BigDecimal priceSum;
	
	public KategorySummaryItem(String name){
		setName(name);
		setCount(new BigDecimal(0));
		setPriceSum(new BigDecimal(0));
	}
	
	public void addItem(BigDecimal count, BigDecimal price){
		this.count = this.count.add(count);
		priceSum.add(price);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public BigDecimal getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(BigDecimal priceSum) {
		this.priceSum = priceSum;
	}
	

}
