package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOCommission;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="VO")
@NamedQueries({
	@NamedQuery(
            name = "VO.getList",
            query = "SELECT v FROM VOEntity v WHERE v.deleted = false ORDER BY v.shop.name",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	),
	@NamedQuery(
            name = "VO.getByNumber",
            query = "SELECT v FROM VOEntity v WHERE v.number = :number AND v.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	),
	@NamedQuery(
            name = "VO.getListByVendor",
            query = "SELECT v FROM VOEntity v WHERE v.vendor = :vendor AND v.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	)
})
public class VOEntity extends StatusBaseEntity implements VO {
	
	private static final long serialVersionUID = 4303214716117643638L;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name="number", nullable = false)
	private String number;

	@ManyToOne
    @JoinColumn(name="voType_id", nullable = false)
    private VOTypeEntity voType;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable = false)
    private ShopEntity shop;

	@ManyToOne
    @JoinColumn(name="vendor_id", nullable = false)
    private VendorEntity vendor;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="shopVOPermissionList")
	private List<ShopEntity> permittedShopShopList;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="liveCommissionCache_id")
    private CommissionCacheEntity liveCommissionCache;
	
    @OrderBy("year DESC, month DESC")
    @JoinTable(name = "VO_CommissionCache", 
    	joinColumns = {@JoinColumn(name = "VOEntity_id", referencedColumnName = "id")}, 
    	inverseJoinColumns = { @JoinColumn(name = "commissionCacheList_id", referencedColumnName = "id")})
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CommissionCacheEntity> commissionCacheList;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="vo")
	private List<VOCommissionEntity> voCommissionList;

	@OneToMany(mappedBy="vo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<ProductCategorySalesCacheEntity> productCategorySalesCacheList;

	public VOEntity() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public Vendor getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(Vendor vendor) {
		this.vendor = (VendorEntity) vendor;
	}

	@Override
	public VOType getVoType() {
		return voType;
	}

	@Override
	public void setVoType(VOType voType) {
		this.voType = (VOTypeEntity) voType;
	}

	public List<VOCommissionEntity> getVOCommissionList() {
		if(voCommissionList == null)
			voCommissionList = new LinkedList<VOCommissionEntity>();
		return voCommissionList;
	}

	public void setVOCommissionList(List<VOCommissionEntity> voCommissionList) {
		this.voCommissionList = voCommissionList;
	}

	@Override
	public VOCommission getVOCommission(int year, int month) {
		Iterator<VOCommissionEntity> i = getVOCommissionList().iterator();
		VOCommissionEntity c;
		while(i.hasNext()){
			c = i.next();
			if(c.getMonth() == month && c.getYear() == year)
				return c;
		}
		
		c = new VOCommissionEntity();
		c.setVo(this);
		c.setYear(year);
		c.setMonth(month);
		getVOCommissionList().add(c);
		
		return c;
	}

	@Override
	public String toString(){
		return this.getName();
	}


	@Override
	@Transient
	public CommissionCache getCurrentCommissionCache() {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		
		CommissionCache commissionCache = CommissionController.getCommissionCache(getCommissionCacheList(), year, month);
		
		if(commissionCache == null){
			commissionCache = new CommissionCacheEntity();
			commissionCache.setYear(year);
			commissionCache.setMonth(month);
			((List<CommissionCache>)getCommissionCacheList()).add(commissionCache);
		}

		return commissionCache;
	}

	@Override
	public List<? extends CommissionCache> getCommissionCacheList() {
		if(commissionCacheList == null)
			commissionCacheList = new ArrayList<CommissionCacheEntity>();
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
	public void setCommissionCacheList(
			List<? extends CommissionCache> commissionCacheList) {
		this.commissionCacheList = (List<CommissionCacheEntity>) commissionCacheList;
	}

	@Override
	public BigDecimal getPointGoal() {
		return new BigDecimal(0);
	}

	@Override
	public CommissionCache getLiveCommissionCache() {
		if(liveCommissionCache == null)
			liveCommissionCache = new CommissionCacheEntity();
		return liveCommissionCache;
	}

	public void setLiveCommissionCache(CommissionCacheEntity liveCommissionCache) {
		this.liveCommissionCache = liveCommissionCache;
	}

	@Override
	@Transient
	public CommissionCache getLastCommissionCache() {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		int month = c.get(Calendar.MONTH) -1;
		int year = c.get(Calendar.YEAR);
		
		if(month < 0){
			month = 0;
			year --;
		}
		
		CommissionCache commissionCache = CommissionController.getCommissionCache(getCommissionCacheList(), year, month);

		if(commissionCache == null){
			commissionCache = new CommissionCacheEntity();
		}
		return commissionCache;
	}
	
	@Override
	@Transient
	public CommissionCache getNextCommissionCache() {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		int month = c.get(Calendar.MONTH) +1;
		int year = c.get(Calendar.YEAR);
		
		if(month > 11){
			month = 0;
			year++;
		}

		CommissionCache commissionCache = CommissionController.getCommissionCache(getCommissionCacheList(), year, month);
		
		if(commissionCache == null){
			commissionCache = new CommissionCacheEntity();
			commissionCache.setYear(year);
			commissionCache.setMonth(month);
			((List<CommissionCache>)getCommissionCacheList()).add(commissionCache);
		}
		return commissionCache;
	}


	@Override
	public ProductCategorySalesCache getProductCategorySalesCache(ProductCategory category, int year, int month) {
		Iterator<? extends ProductCategorySalesCache> i = getProductCategorySalesCacheList().iterator();
		ProductCategorySalesCache cache;
		while(i.hasNext()){
			cache = i.next();
			
			if(cache.getYear() != year)
				continue;
			
			if(cache.getMonth() != month)
				continue;
			
			if(! cache.getProductCategory().equals(category))
				continue;
			
			return cache;
		}
		
		cache = new ProductCategorySalesCacheEntity(category, year, month);
		cache.setVo(this);
		((List<ProductCategorySalesCacheEntity>)getProductCategorySalesCacheList()).add((ProductCategorySalesCacheEntity) cache);
		return cache;
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

	@Override
	public List<? extends ProductCategorySalesCache> getProductCategorySalesCacheList() {
		if(productCategorySalesCacheList == null)
			productCategorySalesCacheList = new LinkedList<ProductCategorySalesCacheEntity>();
		return productCategorySalesCacheList;
	}

	@Override
	public void setProductCategorySalesCacheList(
			List<? extends ProductCategorySalesCache> productCategorySalesCacheList) {
		this.productCategorySalesCacheList = (List<ProductCategorySalesCacheEntity>) productCategorySalesCacheList;
	}
}

