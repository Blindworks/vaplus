package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;

@Entity
@Table(name="ShopGroup")
@NamedQueries({
	@NamedQuery(
	        name = "ShopGroup.listAll",
	        query = "SELECT s FROM ShopGroupEntity s WHERE s.deleted = false ORDER BY s.name",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),@NamedQuery(
            name = "ShopGroup.listEnabled",
            query = "SELECT s FROM ShopGroupEntity s WHERE s.enabled = :enabled AND s.deleted = false ORDER BY s.name",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	)
})
public class ShopGroupEntity extends StatusBaseEntity implements ShopGroup  {

	private static final long serialVersionUID = -3981649512844018424L;

	@Column(name="name", nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="ShopGroupMember",
	joinColumns={@JoinColumn(name="shopGroupID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="shopID", referencedColumnName="id")})
    private List<ShopEntity> shopList;
	
	@Transient
	private List<? extends CommissionCache> commissionCacheList;
	
	@Transient
	private CommissionCache liveCommissionCache;
	
	@Transient
	private CommissionCache currentCommissionCache;
	
	@Transient
	private CommissionCache lastCommissionCache;
	
	@Transient
	private BigDecimal pointGoal;
	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<? extends Shop> getShopList() {
		if(shopList == null)
			shopList = new LinkedList<ShopEntity>();
		return shopList;
	}

	@Override
	public void setShopList(List<? extends Shop> shopList) {
		this.shopList = (List<ShopEntity>) shopList;
	}
	

	@Transient
	@Override
	public List<? extends CommissionCache> getCommissionCacheList() {
		if(commissionCacheList == null){
			commissionCacheList = new ArrayList<CommissionCacheEntity>();
			
			
			Iterator<? extends Shop> i = getShopList().iterator();
			Shop s;
			while(i.hasNext()){
				s = i.next();
				if(! s.isEnabled())
					continue;
				if(s.isDeleted())
					continue;
				
				commissionCacheList = CommissionController.mergeCommissionCacheList(commissionCacheList, s.getCommissionCacheList());
			}
			
		}
		
		return commissionCacheList;
	}

	@Override
	@Transient
	public CommissionCache[] getCommissionCacheList(int year) {
		
		return CommissionController.getCommissionArray(getCommissionCacheList(), year);
	}

	@Override
	@Transient
	public CommissionCache[] getCurrentRangeCommissionCacheList() {
		return CommissionController.getCurrentRangeCommissionCacheArray(getCommissionCacheList());
	}
	

	
	@Override
	@Transient
	public CommissionCache getLiveCommissionCache() {
		if(liveCommissionCache == null){
			liveCommissionCache = new CommissionCacheEntity();
			
			Iterator<? extends Shop> i = getShopList().iterator();
			Shop s;
			while(i.hasNext()){
				s = i.next();
				if(! s.isEnabled())
					continue;
				if(s.isDeleted())
					continue;
	
				liveCommissionCache.setPointGoal(liveCommissionCache.getPointGoal().add( s.getLiveCommissionCache().getPointGoal()));
				liveCommissionCache.setExtensionOfTheTermSum(liveCommissionCache.getExtensionOfTheTermSum() + s.getLiveCommissionCache().getExtensionOfTheTermSum());
				liveCommissionCache.setNewContractSum(liveCommissionCache.getNewContractSum() + s.getCurrentCommissionCache().getNewContractSum());
				liveCommissionCache.setCommission(liveCommissionCache.getCommission().addCommissionable( s.getLiveCommissionCache().getCommission()));
			}
		
		}
		return liveCommissionCache;
	}
	
	@Override
	@Transient
	public CommissionCache getCurrentCommissionCache() {
		if(currentCommissionCache == null){
			currentCommissionCache = new CommissionCacheEntity();
			
			Iterator<? extends Shop> i = getShopList().iterator();
			Shop s;
			while(i.hasNext()){
				s = i.next();
				if(! s.isEnabled())
					continue;
				if(s.isDeleted())
					continue;
				
				currentCommissionCache.setPointGoal(currentCommissionCache.getPointGoal().add( s.getCurrentCommissionCache().getPointGoal()));
				currentCommissionCache.setExtensionOfTheTermSum(currentCommissionCache.getExtensionOfTheTermSum() + s.getCurrentCommissionCache().getExtensionOfTheTermSum());
				currentCommissionCache.setNewContractSum(currentCommissionCache.getNewContractSum() + s.getCurrentCommissionCache().getNewContractSum());
				currentCommissionCache.setCommission(currentCommissionCache.getCommission().addCommissionable( s.getCurrentCommissionCache().getCommission()));
			}
		}
		return currentCommissionCache;
	}




	@Override
	@Transient
	public CommissionCache getLastCommissionCache() {
		if(lastCommissionCache == null){
			lastCommissionCache = new CommissionCacheEntity();
			
			Iterator<? extends Shop> i = getShopList().iterator();
			Shop s;
			while(i.hasNext()){
				s = i.next();
				if(! s.isEnabled())
					continue;
				if(s.isDeleted())
					continue;
				
				lastCommissionCache.setPointGoal(lastCommissionCache.getPointGoal().add( s.getLastCommissionCache().getPointGoal()));
				lastCommissionCache.setExtensionOfTheTermSum(lastCommissionCache.getExtensionOfTheTermSum() + s.getLastCommissionCache().getExtensionOfTheTermSum());
				lastCommissionCache.setNewContractSum(lastCommissionCache.getNewContractSum() + s.getCurrentCommissionCache().getNewContractSum());
				lastCommissionCache.setCommission(lastCommissionCache.getCommission().addCommissionable( s.getLastCommissionCache().getCommission()));
			}
		}
		
		return lastCommissionCache;
	}
	


	@Override
	@Transient
	public ProductCategorySalesCache getProductCategorySalesCache(ProductCategory category, int year, int month) {
		
		ProductCategorySalesCache productCategorySalesCache = new ProductCategorySalesCacheEntity();

		productCategorySalesCache.setMonth(month);
		productCategorySalesCache.setYear(year);
	
		ProductCategorySalesCache cache2;
		
		Iterator<? extends Shop> i = getShopList().iterator();
		Shop s;
		while(i.hasNext()){
			s = i.next();
			if(! s.isEnabled())
				continue;
			if(s.isDeleted())
				continue;
			
			cache2 = s.getProductCategorySalesCache(category, year, month);
			
			productCategorySalesCache.setPieces(productCategorySalesCache.getPieces().add(cache2.getPieces()));
			
		}
	
		return productCategorySalesCache;
	}
	


	@Override
	@Transient
	public List<? extends ProductCategorySalesCache> getCurrentRangeProductCategorySalesCacheList(ProductCategory category) {
		
		List<ProductCategorySalesCache> list = new LinkedList<ProductCategorySalesCache>();
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -9);
		
		// create Rage
		String[] range = new String[12];

		for(int i = 0; i < range.length; i++) {
			
			list.add(getProductCategorySalesCache(category, c.get(Calendar.YEAR), c.get(Calendar.MONTH)));
			c.add(Calendar.MONTH, 1);
			
		}
	
		return list;
	}

	@Transient
	@Override
	public BigDecimal getPointGoal() {
		if(pointGoal == null){
			pointGoal = new BigDecimal(0);
			
			Iterator<? extends Shop> i = getShopList().iterator();
			Shop s;
			while(i.hasNext()){
				s = i.next();
				if(! s.isEnabled())
					continue;
				if(s.isDeleted())
					continue;
				
				pointGoal = pointGoal.add( s.getPointGoal());
			}
			
		}
		
		return pointGoal;
	}
}
