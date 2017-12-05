package de.vaplus.client.pojo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ProductOption;

public class SideBarProduct implements Comparable<SideBarProduct>{
	
	private BaseProduct product;
	
	private List<? extends ProductOption> productOptionList;
	
	private Map<String,BigDecimal> points;

	public BaseProduct getProduct() {
		return product;
	}
	
	public boolean isSinglePoint(){
		return getPoints().size() == 1 ? true : false;
	}
	
	public String getFirstPointName(){
		return getPoints().size() == 0 ? "" : getPoints().keySet().iterator().next();
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public Map<String,BigDecimal> getPoints() {
		if(points == null)
			points = new HashMap<String,BigDecimal>();
		return points;
	}

	public void setPoints(Map<String,BigDecimal> points) {
		this.points = points;
	}
	
	public BigDecimal getHighestPoints(){
		BigDecimal highest = new BigDecimal(0);
		
		Iterator<String> i = points.keySet().iterator();
		BigDecimal p;
		while(i.hasNext()){
			p = points.get(i.next());
			
			if(p.compareTo(highest) > 0)
				highest = p;
		}
		
		return highest;
	}

	@Override
	public int compareTo(SideBarProduct o) {
		return o.getHighestPoints().compareTo(this.getHighestPoints());
	}

	public List<? extends ProductOption> getProductOptionList() {
		return productOptionList;
	}

	public void setProductOptionList(List<? extends ProductOption> productOptionList) {
		this.productOptionList = productOptionList;
	}
	
}
