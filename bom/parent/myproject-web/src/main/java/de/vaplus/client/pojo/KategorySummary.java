package de.vaplus.client.pojo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KategorySummary {
	
	private String name;
	
	private String color;
	
	private Map<Long, KategorySummaryItem> itemMap;
	
	public KategorySummary(String name, String color){
		setName(name);
		setColor(color);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Long, KategorySummaryItem> getItemMap() {
		if(itemMap == null)
			itemMap = new HashMap<Long, KategorySummaryItem>();
		return itemMap;
	}

	public void setItemMap(Map<Long, KategorySummaryItem> itemMap) {
		this.itemMap = itemMap;
	}
	
	public KategorySummaryItem getKategorySummaryItem(long id, String name){
		if(! getItemMap().containsKey(id))
			getItemMap().put(id, new KategorySummaryItem(name));
		return getItemMap().get(id);
	}

	public BigDecimal getPriceSum(){
		BigDecimal sum = new BigDecimal(0);
		Iterator<Long> i = getItemMap().keySet().iterator();
		KategorySummaryItem item;
		while(i.hasNext()){
			item = getItemMap().get(i.next());
			sum.add(item.getPriceSum());
		}
		return sum;
	}
	
	public BigDecimal getCount(){
		BigDecimal count = new BigDecimal(0);
		Iterator<Long> i = getItemMap().keySet().iterator();
		KategorySummaryItem item;
		while(i.hasNext()){
			item = getItemMap().get(i.next());
			count = count.add(item.getCount());
		}
		return count;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
