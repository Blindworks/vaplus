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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.JobTitle;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.api.entity.UserStats;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="User")
@NamedQueries({
    @NamedQuery(
        name = "User.findByAccoundType",
        query = "SELECT u FROM UserEntity u Where u.accountTypeEntity.id = :id AND u.deleted = false",
        hints = {
                @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
            }
    ),
    @NamedQuery(
            name = "User.findByName",
            query = "SELECT u FROM UserEntity u Where u.firstname = :firstname AND u.lastname = :lastname AND u.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "User.findByUUID",
            query = "SELECT u FROM UserEntity u Where u.uuid = :uuid AND u.enabled = true AND u.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "User.findByAlias",
            query = "SELECT u FROM UserEntity u join u.aliasList a Where a.alias = :alias AND u.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "User.findByEmail",
            query = "SELECT u FROM UserEntity u Where u.email = :email AND u.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "User.listEnabledUserNotSupervisor",
            query = "SELECT u FROM UserEntity u Where u.enabled = true AND u.deleted = false AND u.id > 1 ORDER BY u.firstname,u.lastname ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "User.listEnabledUser",
            query = "SELECT u FROM UserEntity u Where u.enabled = true AND u.deleted = false ORDER BY u.firstname,u.lastname ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "User.listUser",
            query = "SELECT u FROM UserEntity u Where u.deleted = false ORDER BY u.firstname,u.lastname ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "User.listSalesUser",
                    query = "SELECT u FROM UserEntity u Where u.enabled = true AND u.deleted = false AND u.id > 0 AND u.pointGoal > 0",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        )
})
public class UserEntity extends StatusBaseEntity implements User, SearchResult{
	
	private static final long serialVersionUID = 4348459400472602051L;

	@Column(name="uuid", nullable = false)
	private String uuid;

	@Column(name="title", nullable = false)
	private String title;

	@Column(name="firstname", nullable = false)
	private String firstname;

	@Column(name="lastname", nullable = false)
	private String lastname;

	@Column(name="email", nullable = false)
	private String email;

	@Column(name="pointGoal", nullable = false, precision = 10, scale = 4)
	private BigDecimal pointGoal;

	@Column(name="color", nullable = true, length=30)
	private String color;

	@Column(name="pointsPerCommission", nullable = false)
	private BigDecimal pointsPerCommission;

	@Column(name="weeklyWorkingTime", nullable = false)
	private int weeklyWorkingTime;

	@Column(name="weeklyWorkingDays", nullable = false)
	private int weeklyWorkingDays;

	@Column(name="vacationDays", nullable = false)
	private int vacationDays;

	@Column(name="supervisor", nullable = false)
	private boolean supervisor;

	@Column(name="failedLogins", nullable = false)
	private int failedLogins;

	@Column(name="entranceDate", nullable = false)
	private Date entranceDate;

	@Column(name="planingStartMonth", nullable = false)
	private int planingStartMonth;

	@Column(name="planingStartYear", nullable = false)
	private int planingStartYear;
	


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bday")
	private Date bday;
	
	@Column(name="stuffNumber")
	private String stuffNumber;

	@Column(name="tel")
	private String tel;

	@Column(name="basicSalary", nullable = false, precision = 10, scale = 2)
	private BigDecimal basicSalary;

	@Column(name="bonusSalary", nullable = false, precision = 10, scale = 2)
	private BigDecimal bonusSalary;

	@Column(name="minimalBonusPointGoal", nullable = false, precision = 10, scale = 4)
	private BigDecimal minimalBonusPointGoal;

	@ManyToOne
    @JoinColumn(name="employmentForm_id", nullable = false)
	private EmploymentFormEntity formOfEmployment;
	
	@Column(name="carGrossCatalogPrice", nullable = false, precision = 10, scale = 2)
	private BigDecimal carGrossCatalogPrice;

	@Column(name="homeOffice")
	private boolean homeOffice;

	@Column(name="distanceToWorkplace")
	private int distanceToWorkplace;
	
	@Column(name="privateCarUsage")
	private boolean privateCarUsage;
	
	

//	@Column(nullable = false)
//	private BigDecimal commission;
//
//	@Column(nullable = false)
//	private BigDecimal netPay;
//	
//	@Column(nullable = false)
//	private BigDecimal payoutMinimumLimit;


	@ManyToOne
    @JoinColumn(name="state_id", nullable = false)
	private StateEntity state;
	
	@ManyToOne
    @JoinColumn(name="jobTitle_id", nullable = false)
    private JobTitleEntity jobTitle;

	@ManyToOne
    @JoinColumn(name="userGroup_id", nullable = false)
    private UserGroupEntity userGroup;


	@ManyToOne
    @JoinColumn(name="userImage_id")
    private DBFileEntity userImage;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="liveCommissionCache_id")
    private CommissionCacheEntity liveCommissionCache;
	
    @OrderBy("year DESC, month DESC")
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommissionCacheEntity> commissionCacheList;
	
	@Embedded
	private AddressEmbeddable address;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accountTypeEntity_id", nullable = false)
    private AccountTypeEntity accountTypeEntity;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval=true)
    private List<UserPermissionEntity> permissionList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval=true)
    private List<UserStatsEntity> statsList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval=true)
    private List<UserAliasEntity> aliasList;

	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ProductCategorySalesCacheEntity> productCategorySalesCacheList;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="UserAllowedShops",
	joinColumns={@JoinColumn(name="userID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="shopID", referencedColumnName="id")})
    private List<ShopEntity> allowedShops;

    @OrderBy("lastOpened DESC")
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	private List<UserCustomerHistoryEntity> customerHistoryList;
	
	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public UserEntity() {
		super();
	}
	
	@Override
    public JobTitle getJobTitle() {
		return jobTitle;
	}

	@Override
	public void setJobTitle(JobTitle jobTitle) {
		this.jobTitle = (JobTitleEntity) jobTitle;
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

	public AccountTypeEntity getAccountTypeEntity() {
		return accountTypeEntity;
	}

	@Override
	public String getAccountTypeId() {
		if(accountTypeEntity == null)
			return null;
		return accountTypeEntity.getId();
	}

	public void setAccountTypeEntity(AccountTypeEntity accountTypeEntity) {
		this.accountTypeEntity = accountTypeEntity;
	}

	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public BigDecimal getPointsPerCommission() {
		return pointsPerCommission;
	}

	@Override
	public void setPointsPerCommission(BigDecimal pointsPerCommission) {
		this.pointsPerCommission = pointsPerCommission;
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
	public boolean isSupervisor() {
		return supervisor;
	}

	@Override
	public void setSupervisor(boolean supervisor) {
		this.supervisor = supervisor;
	}

	@Override
	public int getFailedLogins() {
		return failedLogins;
	}

	@Override
	public void setFailedLogins(int failedLogins) {
		this.failedLogins = failedLogins;
	}

	@Override
	public int getWeeklyWorkingTime() {
		return weeklyWorkingTime;
	}

	@Override
	public void setWeeklyWorkingTime(int weeklyWorkingTime) {
		this.weeklyWorkingTime = weeklyWorkingTime;
	}

	@Override
	public int getWeeklyWorkingDays() {
		return weeklyWorkingDays;
	}

	@Override
	public void setWeeklyWorkingDays(int weeklyWorkingDays) {
		this.weeklyWorkingDays = weeklyWorkingDays;
	}

	@Override
	public int getVacationDays() {
		return vacationDays;
	}

	@Override
	public void setVacationDays(int vacationDays) {
		this.vacationDays = vacationDays;
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
	public DBFile getUserImage() {
		return userImage;
	}

	@Override
	public void setUserImage(DBFile userImage) {
		this.userImage = (DBFileEntity) userImage;
	}

	@Override
	public UserGroup getUserGroup() {
		return userGroup;
	}

	@Override
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = (UserGroupEntity) userGroup;
	}

	@Override
	public Date getBday() {
		return bday;
	}

	@Override
	public void setBday(Date bday) {
		this.bday = bday;
	}

	@Override
	public String getStuffNumber() {
		return stuffNumber;
	}

	@Override
	public void setStuffNumber(String stuffNumber) {
		this.stuffNumber = stuffNumber;
	}

	@Override
	public boolean isPrivateCarUsage() {
		return privateCarUsage;
	}

	@Override
	public void setPrivateCarUsage(boolean privateCarUsage) {
		this.privateCarUsage = privateCarUsage;
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
	public BigDecimal getBasicSalary() {
		if(basicSalary == null)
			basicSalary = new BigDecimal(0);
		return basicSalary;
	}

	@Override
	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}

	@Override
	public BigDecimal getBonusSalary() {
		if(bonusSalary == null)
			bonusSalary = new BigDecimal(0);
		return bonusSalary;
	}

	@Override
	public void setBonusSalary(BigDecimal bonusSalary) {
		this.bonusSalary = bonusSalary;
	}

	@Override
	public BigDecimal getMinimalBonusPointGoal() {
		if(minimalBonusPointGoal == null)
			minimalBonusPointGoal = new BigDecimal(0);
		return minimalBonusPointGoal;
	}

	@Override
	public void setMinimalBonusPointGoal(BigDecimal minimalBonusPointGoal) {
		this.minimalBonusPointGoal = minimalBonusPointGoal;
	}

	@Override
	public EmploymentForm getFormOfEmployment() {
		return formOfEmployment;
	}

	@Override
	public void setFormOfEmployment(EmploymentForm formOfEmployment) {
		this.formOfEmployment = (EmploymentFormEntity) formOfEmployment;
	}

	@Override
	public BigDecimal getCarGrossCatalogPrice() {
		if(carGrossCatalogPrice == null)
			carGrossCatalogPrice = new BigDecimal(0);
		return carGrossCatalogPrice;
	}

	@Override
	public void setCarGrossCatalogPrice(BigDecimal carGrossCatalogPrice) {
		this.carGrossCatalogPrice = carGrossCatalogPrice;
	}

	@Override
	public boolean isHomeOffice() {
		return homeOffice;
	}

	@Override
	public void setHomeOffice(boolean homeOffice) {
		this.homeOffice = homeOffice;
	}

	@Override
	public int getDistanceToWorkplace() {
		return distanceToWorkplace;
	}

	@Override
	public void setDistanceToWorkplace(int distanceToWorkplace) {
		this.distanceToWorkplace = distanceToWorkplace;
	}

	@Override
	@Transient
	public String getName() {
		return firstname +" "+lastname;
	}

	@Override
	public String toString(){
		return this.getName();
	}

	@Override
	public BigDecimal getPointGoal() {
		if(pointGoal == null)
			pointGoal = new BigDecimal(0);
		return pointGoal;
	}

	@Override
	public void setPointGoal(BigDecimal pointGoal) {
		this.pointGoal = pointGoal;
	}

	public List<UserPermissionEntity> getPermissionList() {
		if(permissionList == null)
			permissionList = new ArrayList<UserPermissionEntity>();
		return permissionList;
	}

	public void setPermissionList(List<UserPermissionEntity> permissionList) {
		this.permissionList = permissionList;
	}
	
	@Override
	public Date getEntranceDate() {
		return entranceDate;
	}

	@Override
	public void setEntranceDate(Date entranceDate) {
		this.entranceDate = entranceDate;
	}

	@Override
	public int getPlaningStartMonth() {
		return planingStartMonth;
	}

	@Override
	public void setPlaningStartMonth(int planingStartMonth) {
		this.planingStartMonth = planingStartMonth;
	}

	@Override
	public int getPlaningStartYear() {
		return planingStartYear;
	}

	@Override
	public void setPlaningStartYear(int planingStartYear) {
		this.planingStartYear = planingStartYear;
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
	public CommissionCache getCommissionCache(int year, int month) {

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
	public List<? extends UserStats> getStatsList() {
		if(statsList == null)
			statsList = new LinkedList<UserStatsEntity>();
		return statsList;
	}

	public void setStatsList(List<UserStatsEntity> statsList) {
		this.statsList = statsList;
	}

	@Override
	@Transient
	// return value in minutes
	public int getFullDayWorkingTime() {
		if(getWeeklyWorkingTime() == 0)
			return 0;
		
		return getWeeklyWorkingTime() * 60 / getWeeklyWorkingDays();
	}


	@Override
	@Transient
	public UserStats getStats(int year, int month) {
		
		Iterator<? extends UserStats> i = getStatsList().iterator();
		UserStatsEntity stats;
		while(i.hasNext()){
			stats = (UserStatsEntity) i.next();
			
			if(stats.getYear() != year)
				continue;
			if(stats.getMonth() != month)
				continue;
			return stats;
		}
		// no stats for timerange found
		
		stats = new UserStatsEntity(this, year, month);
		((List<UserStatsEntity>) getStatsList()).add(stats);
		return stats;
	}

	@Override
	public List<? extends UserAlias> getAliasList() {
		if(aliasList == null)
			aliasList = new LinkedList<UserAliasEntity>();
		return aliasList;
	}

	@Override
	public void setAliasList(List<? extends UserAlias> aliasList) {
		this.aliasList = (List<UserAliasEntity>) aliasList;
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
		cache.setUser(this);
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
	public List<? extends Shop> getAllowedShops() {
		if(allowedShops == null)
			allowedShops = new LinkedList<ShopEntity>();
		
		Iterator<ShopEntity> i = allowedShops.iterator();
		Shop shop;
		while(i.hasNext()){
			shop = i.next();
			if(shop.isDeleted() || !shop.isEnabled())
				i.remove();
		}
		
		return allowedShops;
	}

	@Override
	public void setAllowedShops(List<? extends Shop> allowedShops) {
		this.allowedShops = (List<ShopEntity>) allowedShops;
	}

	@Override
	public List<? extends UserCustomerHistory> getCustomerHistoryList() {
		if(customerHistoryList == null)
			customerHistoryList = new LinkedList<UserCustomerHistoryEntity>();
		return customerHistoryList;
	}

	public void setCustomerHistoryList(
			List<UserCustomerHistoryEntity> customerHistoryList) {
		this.customerHistoryList = customerHistoryList;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = (StateEntity) state;
	}
}