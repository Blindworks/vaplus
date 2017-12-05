package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.AchievementController;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.AchievementUserGoalAttainment;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductStatistic;
import de.vaplus.api.entity.ProductStatisticCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.entity.commission.vendor.VendorCommissionAccountingItemEntity;

@Entity
@Table(name="ProductStatistic")
@NamedQueries({
    @NamedQuery(
        name = "ProductStatistic.getList",
              	query = "SELECT p FROM ProductStatisticEntity p Where p.deleted = false ORDER BY p.weight, p.name ASC",
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    ),
    @NamedQuery(
        name = "ProductStatistic.getOverviewList",
              	query = "SELECT p FROM ProductStatisticEntity p Where p.deleted = false AND p.showOnOverview = true ORDER BY p.weight, p.name ASC",
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    )
})
public class ProductStatisticEntity extends BaseEntity implements ProductStatistic{
	
	private static final long serialVersionUID = -4263980144502842853L;

	@Column(name="name")
	private String name;
	
	@Column(name="showOnOverview")
	private boolean showOnOverview;

	@Column(name="weight", nullable = false)
	private int weight;

	@Column(name="newContract", nullable = false)
	private boolean newContract;

	@Column(name="extensionOfTheTerm", nullable = false)
	private boolean extensionOfTheTerm;

	@Column(name="debidCreditChange", nullable = false)
	private boolean debidCreditChange;


	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="ProductStatisticSelectedProductCategory",
	joinColumns={@JoinColumn(name="productStatisticID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductCategoryID", referencedColumnName="id")})
	private List<ProductCategoryEntity> selectedProductCategoryList;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="ProductStatisticSelectedProduct",
	joinColumns={@JoinColumn(name="productStatisticID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductID", referencedColumnName="id")})
	private List<BaseProductEntity> selectedProductList;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="ProductStatisticSelectedProductCombination",
	joinColumns={@JoinColumn(name="productStatisticID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductCombinationID", referencedColumnName="id")})
	private List<BaseProductCombinationEntity> selectedProductCombinationList;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="ProductStatisticSelectedProductFilter",
	joinColumns={@JoinColumn(name="productStatisticID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductID", referencedColumnName="id")})
	private List<BaseProductEntity> selectedProductFilterList;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="ProductStatisticSelectedProductCombinationFilter",
	joinColumns={@JoinColumn(name="productStatisticID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductCombinationFilterID", referencedColumnName="id")})
	private List<BaseProductCombinationEntity> selectedProductCombinationFilterList;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "productStatistic")
	private List<ProductStatisticCacheEntity> productStatisticCacheList; 
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}
	

	@Override
	public List<? extends ProductCategory> getSelectedProductCategoryList() {
		if(selectedProductCategoryList == null)
			selectedProductCategoryList = new LinkedList<ProductCategoryEntity>();
		return selectedProductCategoryList;
	}

	@Override
	public void setSelectedProductCategoryList(
			List<? extends ProductCategory> selectedProductCategoryList) {
		this.selectedProductCategoryList = (List<ProductCategoryEntity>) selectedProductCategoryList;
	}

	@Override
	public List<? extends BaseProduct> getSelectedProductList() {
		if(selectedProductList == null)
			selectedProductList = new LinkedList<BaseProductEntity>();
		return selectedProductList;
	}

	@Override
	public void setSelectedProductList(List<? extends BaseProduct> selectedProductList) {
		this.selectedProductList = (List<BaseProductEntity>) selectedProductList;
	}

	@Override
	public List<? extends BaseProductCombination> getSelectedProductCombinationList() {
		if(selectedProductCombinationList == null)
			selectedProductCombinationList = new LinkedList<BaseProductCombinationEntity>();
		return selectedProductCombinationList;
	}

	@Override
	public void setSelectedProductCombinationList(List<? extends BaseProductCombination> selectedProductCombinationList) {
		this.selectedProductCombinationList = (List<BaseProductCombinationEntity>) selectedProductCombinationList;
	}

	@Override
	public List<? extends BaseProductCombination> getSelectedProductCombinationFilterList() {
		if(selectedProductCombinationFilterList == null)
			selectedProductCombinationFilterList = new LinkedList<BaseProductCombinationEntity>();
		return selectedProductCombinationFilterList;
	}

	@Override
	public void setSelectedProductCombinationFilterList(List<? extends BaseProductCombination> selectedProductCombinationFilterList) {
		this.selectedProductCombinationFilterList = (List<BaseProductCombinationEntity>) selectedProductCombinationFilterList;
	}

	@Override
	public List<? extends BaseProduct> getSelectedProductFilterList() {
		if(selectedProductFilterList == null)
			selectedProductFilterList = new LinkedList<BaseProductEntity>();
		return selectedProductFilterList;
	}

	@Override
	public void setSelectedProductFilterList(List<? extends BaseProduct> selectedProductFilterList) {
		this.selectedProductFilterList = (List<BaseProductEntity>) selectedProductFilterList;
	}


	@Override
	public boolean isNewContract() {
		return newContract;
	}

	@Override
	public void setNewContract(boolean newContract) {
		this.newContract = newContract;
	}

	@Override
	public boolean isShowOnOverview() {
		return showOnOverview;
	}

	@Override
	public void setShowOnOverview(boolean showOnOverview) {
		this.showOnOverview = showOnOverview;
	}

	@Override
	public boolean isExtensionOfTheTerm() {
		return extensionOfTheTerm;
	}

	@Override
	public void setExtensionOfTheTerm(boolean extensionOfTheTerm) {
		this.extensionOfTheTerm = extensionOfTheTerm;
	}

	@Override
	public boolean isDebidCreditChange() {
		return debidCreditChange;
	}

	@Override
	public void setDebidCreditChange(boolean debidCreditChange) {
		this.debidCreditChange = debidCreditChange;
	}


	@Override
	public List<? extends ProductStatisticCache> getProductStatisticCacheList() {
		if(productStatisticCacheList == null)
			productStatisticCacheList = new LinkedList<ProductStatisticCacheEntity>();
		return productStatisticCacheList;
	}

	@Override
	public void setProductStatisticCacheList(List<? extends ProductStatisticCache> productStatisticCacheList) {
		this.productStatisticCacheList = (List<ProductStatisticCacheEntity>) productStatisticCacheList;
	}
	

	@Override
	public ProductStatisticCache getCurrentProductStatisticCache(ActivityOwner owner) {
		ProductStatisticCache cache, tmp;
		Calendar c = Calendar.getInstance();
		
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		
		if(owner instanceof ShopGroupEntity){
			ShopGroupEntity shopGroup = (ShopGroupEntity) owner;
			Iterator<? extends Shop> i = shopGroup.getShopList().iterator();
			Shop shop;
			
			cache = new ProductStatisticCacheEntity();
			cache.setMonth(month);
			cache.setYear(year);
			
			while(i.hasNext()){
				shop = i.next();
				tmp = getProductStatisticCache(month, year, shop);
				cache.addPieces(tmp.getPieces());
			}
		}else{
			cache = getProductStatisticCache(month, year, owner);
		}
		
		return cache;
	}


	@Override
	public ProductStatisticCache getProductStatisticCache(int month, int year, ActivityOwner owner) {
		
//		System.out.println("getProductStatisticCache "+month+" "+year+" "+owner.getClass()+" "+owner.getId());
		
		Iterator<? extends ProductStatisticCache> i = getProductStatisticCacheList().iterator();
		ProductStatisticCacheEntity cache = null;
		while(i.hasNext()){
			cache = (ProductStatisticCacheEntity) i.next();

//			System.out.println("check cache "+cache.getMonth()+" "+cache.getYear()+" u:"+cache.getUser()+" "+" s:"+cache.getShop()+" "+" v:"+cache.getVo()+" ");
			
			if(cache.getMonth() != month){
				cache = null;
//				System.out.println("wrong month");
				continue;
			}
			if(cache.getYear() != year){
				cache = null;
//				System.out.println("wrong year");
				continue;
			}

			if(owner instanceof UserEntity){
				if(cache.getUser() == null || cache.getUser().getId() != owner.getId()){
					cache = null;
//					System.out.println("wrong user");
					continue;
				}
			}
			if(owner instanceof ShopEntity){
				if(cache.getShop() == null || cache.getShop().getId() != owner.getId()){
					cache = null;
//					System.out.println("wrong shop");
					continue;
				}
			}
			if(owner instanceof VOEntity){
				if(cache.getVo() == null || cache.getVo().getId() != owner.getId()){
					cache = null;
//					System.out.println("wrong vo");
					continue;
				}
			}
			
			break;
		}
		
		if(cache == null){
			cache = new ProductStatisticCacheEntity();
			cache.setMonth(month);
			cache.setYear(year);


			if(owner instanceof UserEntity){
				cache.setUser((User) owner);
			}
			if(owner instanceof ShopEntity){
				cache.setShop((Shop) owner);
			}
			if(owner instanceof VOEntity){
				cache.setVo((VO) owner);
			}
			
			cache.setProductStatistic(this);
			((List<ProductStatisticCacheEntity>)getProductStatisticCacheList()).add(cache);
		}
		
//		System.out.println("get ProductStatisticCache: "+cache.getId());
		
		return cache;
	}
	

	@Override
	public void clearPieces(int month, int year){
		
		Iterator<? extends ProductStatisticCache> i = getProductStatisticCacheList().iterator();
		ProductStatisticCacheEntity cache = null;
		while(i.hasNext()){
			cache = (ProductStatisticCacheEntity) i.next();
			if(cache.getMonth() != month)
				continue;
			if(cache.getYear() != year)
				continue;

			cache.clearPieces();
		}
		
	}
}
