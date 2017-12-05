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
import javax.persistence.Embedded;
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

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Shop")
@NamedQueries({
	@NamedQuery(
	        name = "Shop.listAll",
	        query = "SELECT s FROM ShopEntity s WHERE s.deleted = false",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),@NamedQuery(
            name = "Shop.listEnabled",
            query = "SELECT s FROM ShopEntity s WHERE s.enabled = :enabled AND s.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	),
    @NamedQuery(
            name = "Shop.findByUUID",
            query = "SELECT s FROM ShopEntity s Where s.uuid = :uuid AND s.enabled = :enabled AND s.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
                }
    ),
    @NamedQuery(
            name = "Shop.findByAlias",
            query = "SELECT s FROM ShopEntity s join s.aliasList a Where a.alias = :alias AND s.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    )
})
public class ShopEntity extends StatusBaseEntity implements Shop {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3643855431105136788L;

	@Column(name="uuid", nullable = false)
	private String uuid;
	
	@Column(name="name", nullable = false)
	private String name;

	@Column(name="pointGoal", nullable = false, precision = 10, scale = 4)
	private BigDecimal pointGoal;

	@Column(name="contactPerson")
	private String contactPerson;

	@Column(name="tel")
	private String tel;

	@Column(name="fax")
	private String fax;

	@Column(name="email")
	private String email;

	@Column(name="color", nullable = true, length=7)
	private String color;


	@Column(name="calendarNote")
	private String calendarNote;

	@Column(name="businessHours_start_0", nullable = true, length=5)
	private String businessHours_start_0;

	@Column(name="businessHours_start_1", nullable = true, length=5)
	private String businessHours_start_1;

	@Column(name="businessHours_start_2", nullable = true, length=5)
	private String businessHours_start_2;

	@Column(name="businessHours_start_3", nullable = true, length=5)
	private String businessHours_start_3;

	@Column(name="businessHours_start_4", nullable = true, length=5)
	private String businessHours_start_4;

	@Column(name="businessHours_start_5", nullable = true, length=5)
	private String businessHours_start_5;

	@Column(name="businessHours_start_6", nullable = true, length=5)
	private String businessHours_start_6;

	@Column(name="businessHours_end_0", nullable = true, length=5)
	private String businessHours_end_0;

	@Column(name="businessHours_end_1", nullable = true, length=5)
	private String businessHours_end_1;

	@Column(name="businessHours_end_2", nullable = true, length=5)
	private String businessHours_end_2;

	@Column(name="businessHours_end_3", nullable = true, length=5)
	private String businessHours_end_3;

	@Column(name="businessHours_end_4", nullable = true, length=5)
	private String businessHours_end_4;

	@Column(name="businessHours_end_5", nullable = true, length=5)
	private String businessHours_end_5;

	@Column(name="businessHours_end_6", nullable = true, length=5)
	private String businessHours_end_6;
	
	@Column(name="drafterLine", nullable = true, length=5)
	private String drafterLine;

	@Column(name="docFooterLine1", nullable = true, length=5)
	private String docFooterLine1;

	@Column(name="docFooterLine2", nullable = true, length=5)
	private String docFooterLine2;

	@Column(name="docFooterLine3", nullable = true, length=5)
	private String docFooterLine3;

//	@Column(name="invoiceNumber")
//	private long invoiceNumber;
//
//	@Column(name="orderNumber")
//	private long orderNumber;
//
//	@Column(name="pickslipNumber")
//	private long pickslipNumber;
	
	// Crosscan Data
	
	@Column(name="crosscanData_authID", nullable = true)
	private String crosscanData_authID;

	@Column(name="crosscanData_storeID", nullable = true, length=5)
	private String crosscanData_storeID;

	// Account to correct the cashRegister
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="settlingAccount_id", nullable = true)
    private PaymentAccountEntity settlingAccount;

	
	// Jahresziel
	
	// Aquirierter Umsatz bei verträgen Laufzeit mal Monat.

	
	@ManyToOne
    @JoinColumn(name="state_id", nullable = false)
	private StateEntity state;

	@ManyToOne
    @JoinColumn(name="shopImage_id")
    private DBFileEntity shopImage;

	@Embedded
	private AddressEmbeddable address;
	
	@ManyToOne
    @JoinColumn(name="stock_id", nullable = true)
	private StockEntity stock;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="liveCommissionCache_id")
    private CommissionCacheEntity liveCommissionCache;

    @OrderBy("year DESC, month DESC")
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommissionCacheEntity> commissionCacheList;

	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY)
	private List<VOEntity> shopVOList;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="ShopVOPermission",
		joinColumns={@JoinColumn(name="shopID", referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="voID", referencedColumnName="id")})
	private List<VOEntity> shopVOPermissionList;


	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ProductCategorySalesCacheEntity> productCategorySalesCacheList;

	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "shop", orphanRemoval=true)
    private List<ShopAliasEntity> aliasList;

	public ShopEntity() {
		super();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getColor() {
		return color;
	}

	@Override
	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString(){
		return this.getName();
	}
	@Override
	public Address getAddress() {
		if(address == null)
			address = new AddressEmbeddable();
		return address;
	}

	@Override
	public void setAddress(Address address) {
		this.address = (AddressEmbeddable) address;
	}

	@Override
	public BigDecimal getPointGoal() {
		return pointGoal;
	}
	
	@Override
	public void setPointGoal(BigDecimal pointGoal) {
		this.pointGoal = pointGoal;
	}

	@Override
	public List<? extends CommissionCache> getCommissionCacheList() {
		if(commissionCacheList == null)
			commissionCacheList = new ArrayList<CommissionCacheEntity>();
		return commissionCacheList;
	}
	
	@Override
	public CommissionCache getLiveCommissionCache() {
		if(liveCommissionCache == null)
			liveCommissionCache = new CommissionCacheEntity();
		return liveCommissionCache;
	}

	@Override
	@Transient
	public CommissionCache getCommissionCache(int year, int month) {

		CommissionCache commissionCache = CommissionController.getCommissionCache(getCommissionCacheList(), year, month);

		if(commissionCache == null){
			commissionCache = new CommissionCacheEntity();
		}
		return commissionCache;
	}
	

	@Override
	public DBFile getShopImage() {
		return shopImage;
	}

	@Override
	public void setShopImage(DBFile shopImage) {
		this.shopImage = (DBFileEntity) shopImage;
	}

	@Override
	public List<? extends VO> getShopVOList() {
		return shopVOList;
	}
	
	public void setShopVOList(List<VOEntity> shopVOList) {
		this.shopVOList = shopVOList;
	}

	@Override
	public String getContactPerson() {
		return contactPerson;
	}

	@Override
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Override
	public String getTel() {
		return tel;
	}

	@Override
	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public List<? extends VO> getShopVOPermissionList() {
		if(shopVOPermissionList == null)
			shopVOPermissionList = new ArrayList<VOEntity>();
		return shopVOPermissionList;
	}

	public void setShopVOPermissionList(List<? extends VO> shopVOPermissionList) {
		this.shopVOPermissionList = (List<VOEntity>) shopVOPermissionList;
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
	public String getBusinessHours_start_0() {
		return businessHours_start_0;
	}

	@Override
	public void setBusinessHours_start_0(String businessHours_start_0) {
		this.businessHours_start_0 = businessHours_start_0;
	}

	@Override
	public String getBusinessHours_start_1() {
		return businessHours_start_1;
	}

	@Override
	public void setBusinessHours_start_1(String businessHours_start_1) {
		this.businessHours_start_1 = businessHours_start_1;
	}

	@Override
	public String getBusinessHours_start_2() {
		return businessHours_start_2;
	}

	@Override
	public void setBusinessHours_start_2(String businessHours_start_2) {
		this.businessHours_start_2 = businessHours_start_2;
	}

	@Override
	public String getBusinessHours_start_3() {
		return businessHours_start_3;
	}

	@Override
	public void setBusinessHours_start_3(String businessHours_start_3) {
		this.businessHours_start_3 = businessHours_start_3;
	}

	@Override
	public String getBusinessHours_start_4() {
		return businessHours_start_4;
	}

	@Override
	public void setBusinessHours_start_4(String businessHours_start_4) {
		this.businessHours_start_4 = businessHours_start_4;
	}

	@Override
	public String getBusinessHours_start_5() {
		return businessHours_start_5;
	}

	@Override
	public void setBusinessHours_start_5(String businessHours_start_5) {
		this.businessHours_start_5 = businessHours_start_5;
	}

	@Override
	public String getBusinessHours_start_6() {
		return businessHours_start_6;
	}

	@Override
	public void setBusinessHours_start_6(String businessHours_start_6) {
		this.businessHours_start_6 = businessHours_start_6;
	}


	@Override
	public String getBusinessHours_end_0() {
		return businessHours_end_0;
	}

	@Override
	public void setBusinessHours_end_0(String businessHours_end_0) {
		this.businessHours_end_0 = businessHours_end_0;
	}

	@Override
	public String getBusinessHours_end_1() {
		return businessHours_end_1;
	}

	@Override
	public void setBusinessHours_end_1(String businessHours_end_1) {
		this.businessHours_end_1 = businessHours_end_1;
	}

	@Override
	public String getBusinessHours_end_2() {
		return businessHours_end_2;
	}

	@Override
	public void setBusinessHours_end_2(String businessHours_end_2) {
		this.businessHours_end_2 = businessHours_end_2;
	}

	@Override
	public String getBusinessHours_end_3() {
		return businessHours_end_3;
	}

	@Override
	public void setBusinessHours_end_3(String businessHours_end_3) {
		this.businessHours_end_3 = businessHours_end_3;
	}

	@Override
	public String getBusinessHours_end_4() {
		return businessHours_end_4;
	}

	@Override
	public void setBusinessHours_end_4(String businessHours_end_4) {
		this.businessHours_end_4 = businessHours_end_4;
	}

	@Override
	public String getBusinessHours_end_5() {
		return businessHours_end_5;
	}

	@Override
	public void setBusinessHours_end_5(String businessHours_end_5) {
		this.businessHours_end_5 = businessHours_end_5;
	}

	@Override
	public String getBusinessHours_end_6() {
		return businessHours_end_6;
	}

	@Override
	public void setBusinessHours_end_6(String businessHours_end_6) {
		this.businessHours_end_6 = businessHours_end_6;
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
		cache.setShop(this);
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
	public List<? extends ProductCategorySalesCacheEntity> getProductCategorySalesCacheList() {
		if(productCategorySalesCacheList == null)
			productCategorySalesCacheList = new LinkedList<ProductCategorySalesCacheEntity>();
		return productCategorySalesCacheList;
	}

	@Override
	public void setProductCategorySalesCacheList(
			List<? extends ProductCategorySalesCache> productCategorySalesCacheList) {
		this.productCategorySalesCacheList = (List<ProductCategorySalesCacheEntity>) productCategorySalesCacheList;
	}
	
	@Override
	public List<? extends ShopAlias> getAliasList() {
		if(aliasList == null)
			aliasList = new LinkedList<ShopAliasEntity>();
		return aliasList;
	}

	@Override
	public void setAliasList(List<? extends ShopAlias> aliasList) {
		this.aliasList = (List<ShopAliasEntity>) aliasList;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = (StateEntity) state;
	}

	@Override
	public String getCalendarNote() {
		return calendarNote;
	}

	@Override
	public void setCalendarNote(String calendarNote) {
		this.calendarNote = calendarNote;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public void setStock(Stock stock) {
		this.stock = (StockEntity) stock;
	}

	@Override
	public String getCrosscanData_authID() {
		return crosscanData_authID;
	}

	@Override
	public void setCrosscanData_authID(String crosscanData_authID) {
		this.crosscanData_authID = crosscanData_authID;
	}

	@Override
	public String getCrosscanData_storeID() {
		return crosscanData_storeID;
	}

	@Override
	public void setCrosscanData_storeID(String crosscanData_storeID) {
		this.crosscanData_storeID = crosscanData_storeID;
	}

	@Override
	public String getDrafterLine() {
		return drafterLine;
	}

	@Override
	public void setDrafterLine(String drafterLine) {
		this.drafterLine = drafterLine;
	}

	@Override
	public String getDocFooterLine1() {
		return docFooterLine1;
	}

	@Override
	public void setDocFooterLine1(String docFooterLine1) {
		this.docFooterLine1 = docFooterLine1;
	}

	@Override
	public String getDocFooterLine2() {
		return docFooterLine2;
	}

	@Override
	public void setDocFooterLine2(String docFooterLine2) {
		this.docFooterLine2 = docFooterLine2;
	}

	@Override
	public String getDocFooterLine3() {
		return docFooterLine3;
	}

	@Override
	public void setDocFooterLine3(String docFooterLine3) {
		this.docFooterLine3 = docFooterLine3;
	}

//	@Override
//	public long getInvoiceNumber() {
//		return invoiceNumber;
//	}
//
//	@Override
//	public void setInvoiceNumber(long invoiceNumber) {
//		this.invoiceNumber = invoiceNumber;
//	}
//
//	@Override
//	public long getOrderNumber() {
//		return orderNumber;
//	}
//
//	@Override
//	public void setOrderNumber(long orderNumber) {
//		this.orderNumber = orderNumber;
//	}
//
//	@Override
//	public long getPickslipNumber() {
//		return pickslipNumber;
//	}
//
//	@Override
//	public void setPickslipNumber(long pickslipNumber) {
//		this.pickslipNumber = pickslipNumber;
//	}

	public PaymentAccountEntity getSettlingAccount() {
		return settlingAccount;
	}

	public void setSettlingAccount(PaymentAccountEntity settlingAccount) {
		this.settlingAccount = settlingAccount;
	}
}
