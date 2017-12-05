package de.vaplus.client.applicationbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.CellPhoneTariff;
import de.vaplus.api.entity.LandlineTariff;
import de.vaplus.api.entity.PrePaidTariff;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.pojo.SideBarProduct;
import de.vaplus.comparator.GoalAttainmentRankingComparator;


@Singleton
@ConcurrencyManagement(javax.ejb.ConcurrencyManagementType.CONTAINER)
public class ResourceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532301334019992988L;

	@EJB
	private UserControllerInterface userController;
	
	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private ProductControllerInterface productController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private FileControllerInterface fileController;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private ContractControllerInterface contractController;


	@EJB
	private VOControllerInterface voController;

	List<? extends User> salesUserList;

	List<? extends Shop> salesShopList;

	List<? extends Shop> shopList;

	List<? extends Shop> shopListFull;
	
	private List<SideBarProduct> sideBarProductList;
	
	private List<SideBarProduct> groupedSideBarProductList;

	private List<? extends ProductCategory> overviewProductCategoryList;
	
	private long fileConsumption = -1;
	
	private long renewableContractCount = -1;
	
	public ResourceBean(){
	}

	@PostConstruct
	@Schedule(hour="*", minute="*/15", second="0", persistent=false)
	public void refreshLists(){
		refreshShopList();
		refreshSalesUserList();
		refreshSalesShopList();
		refreshSideBarProductList();
		refreshOverviewProductCategoryList();
		updateFileConsumption();
		updateRenewableContractCount();
	}

	@Lock(javax.ejb.LockType.READ)
	public List<? extends User> getSalesUserList(){
		if(salesUserList == null)
			refreshSalesUserList();
		return salesUserList;
	}
	
	private void refreshSalesUserList(){
		salesUserList = userController.getSalesUserList();
		
		Collections.sort(salesUserList, new GoalAttainmentRankingComparator());
	}


	@Lock(javax.ejb.LockType.READ)
	public List<? extends Shop> getSalesShopList() {
		if(salesShopList == null)
			refreshSalesShopList();
		return salesShopList;
	}
	
	private void refreshSalesShopList(){
		salesShopList = getShopList();
		
		Collections.sort(salesShopList, new GoalAttainmentRankingComparator());
	}
	

	@Lock(javax.ejb.LockType.READ)
	public List<? extends Shop> getShopList() {
		return getShopList(false);
	}

	@Lock(javax.ejb.LockType.READ)
	public List<? extends Shop> getShopList(boolean showDisables) {
		if(shopList == null)
			refreshSalesShopList();
		if(shopListFull == null)
			refreshSalesShopList();
		
		if(showDisables)
			return shopListFull;
		
		return shopList;
	}
	
	private void refreshShopList(){
		shopList = shopController.getShopList(false);
		shopListFull = shopController.getShopList(true);
	}

	@Lock(javax.ejb.LockType.READ)
	public List<SideBarProduct> getSideBarProductList(){
		if(sideBarProductList == null)
			refreshSideBarProductList();
		return sideBarProductList;
	}

	@Lock(javax.ejb.LockType.READ)
	public List<SideBarProduct> getGroupedSideBarProductList(){
		if(groupedSideBarProductList == null)
			refreshSideBarProductList();
		return groupedSideBarProductList;
	}
	
	@Lock(javax.ejb.LockType.READ)
	public List<? extends ProductCategory> getOverviewProductCategoryList(){
		if(overviewProductCategoryList == null)
			refreshOverviewProductCategoryList();
		
		return overviewProductCategoryList;
	}

	@Lock(javax.ejb.LockType.WRITE)
	private void refreshOverviewProductCategoryList(){
		overviewProductCategoryList = productController.getOverviewProductCategoryList();
	}
	
	@Lock(javax.ejb.LockType.READ)
	public long getFileConsumption(){
		if(fileConsumption < 0L)
			updateFileConsumption();
		return fileConsumption;
	}
	
	@Lock(javax.ejb.LockType.WRITE)
	public void updateFileConsumption(){
		fileConsumption = fileController.getFileConsumption();
	}


	
	@Lock(javax.ejb.LockType.READ)
	public long getRenewableContractCount(){
		if(renewableContractCount < 0L)
			updateRenewableContractCount();
		return renewableContractCount;
	}
	
	@Lock(javax.ejb.LockType.WRITE)
	public void updateRenewableContractCount() {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		Date expiryDateFrom = c.getTime();

		c.add(Calendar.DAY_OF_YEAR, (int) propertyController.getLongProperty("extensionOfTermLimit", 90L));

		Date expiryDateTo = c.getTime();
		
		renewableContractCount = contractController.getRenewableContractCount(expiryDateFrom, expiryDateTo);
	}
	

	@Lock(javax.ejb.LockType.WRITE)
	public void refreshSideBarProductList(){
		
		sideBarProductList = new ArrayList<SideBarProduct>();
		groupedSideBarProductList = new ArrayList<SideBarProduct>();

		/*
		Iterator<? extends BaseProduct> i = productController.getBaseProductList().iterator();
		BaseProduct p;
		SideBarProduct rp;
		Iterator<? extends VOType> i2;
		VOType voType;
		Commissionable commission;
		BigDecimal points = null;
		while(i.hasNext()){
			p = i.next();
			commission = commissionController.factoryNewCommission();

			
			if(p instanceof CellPhoneTariff){

//				if(propertyBean.isShowGroupedProductsOnSideBar()){
					// groupedSideBarProductList START
					i2 = voController.getVOTypeList(p.getVendor()).iterator();
	
					rp = new SideBarProduct();
					rp.setProduct(p);
					
					while(i2.hasNext()){
						voType = i2.next();
						commission = commissionController.calculateCommission(p, voType);

//						points = commissionController.calculateUserPointsPerCommission(userBean.getActiveUser(), commission.getCommission());
						points = commission.getCommission();

						if(points == null){
							points = commission.getPoints();
						}
						
						if(points.compareTo(new BigDecimal(0)) > 0)
							rp.getPoints().put(voType.getShortName(), points);
					}
					
					if(rp.getPoints().size() > 0)
						groupedSideBarProductList.add(rp);
					
					// groupedSideBarProductList END
					
//				}else{
					// sideBarProductList START
					i2 = voController.getVOTypeList(p.getVendor()).iterator();
					while(i2.hasNext()){
						voType = i2.next();
						rp = new SideBarProduct();
						rp.setProduct(p);
						commission = commissionController.calculateCommission(p, voType);

						points = commission.getCommission();
//						points = commissionController.calculateUserPointsPerCommission(userBean.getActiveUser(), commission.getCommission());

						if(points == null){
							points = commission.getPoints();
						}
						
						rp.getPoints().put(voType.getShortName(), points);
						if(commission.getPoints().compareTo(new BigDecimal(0)) > 0)
							sideBarProductList.add(rp);
					}	
					// sideBarProductList END
//				}
			}else if(p instanceof LandlineTariff || p instanceof PrePaidTariff){
				rp = new SideBarProduct();
				rp.setProduct(p);

				points = p.getCommission().getCommission();
//				points = commissionController.calculateUserPointsPerCommission(userBean.getActiveUser(), p.getCommission().getCommission());
				
				if(points == null){
					points = p.getCommission().getPoints();
				}


				if(points.compareTo(new BigDecimal(0)) > 0){
					rp.getPoints().put("",points);
					sideBarProductList.add(rp);
					groupedSideBarProductList.add(rp);
				}
			}else if(p instanceof Product){
				rp = new SideBarProduct();
				rp.setProduct(p);
				
				points = p.getCommission().getPoints();
				
				if(points == null){
					points = p.getCommission().getPoints();
				}


				if(points.compareTo(new BigDecimal(0)) > 0){
					rp.getPoints().put("",points);
					sideBarProductList.add(rp);
					groupedSideBarProductList.add(rp);
				}
			}
		
		}
		
		
		
		
		// Add Special Conditions
		ProductCommissionVOType productCommissionVOType;
		List<? extends ProductCommissionVOType> voTypeList = voController.getVOProductCommissionTypeList();
		Iterator<? extends ProductCommissionVOType> voType_i = voTypeList.iterator();
		while(voType_i.hasNext()){
			productCommissionVOType = voType_i.next();
			
			Iterator<? extends ProductCommissionVOTypeProductRelation> pRel_i = productCommissionVOType.getProductCommissionList().iterator();
			ProductCommissionVOTypeProductRelation rel;
			
			while(pRel_i.hasNext()){
				rel = pRel_i.next();
				
				if(rel.getProductOptionList() == null || rel.getProductOptionList().size() == 0){
					continue;
				}
				
				rp = new SideBarProduct();
				rp.setProduct(rel.getProduct());
				rp.setProductOptionList(rel.getProductOptionList());

				points = rel.getCommission().getCommission();
//				points = commissionController.calculateUserPointsPerCommission(userBean.getActiveUser(), rel.getCommission().getCommission());
				
				if(points == null){
					points = rel.getCommission().getPoints();
				}


				if(points.compareTo(new BigDecimal(0)) > 0){
					rp.getPoints().put("",points);
					sideBarProductList.add(rp);
					groupedSideBarProductList.add(rp);
				}
			}
			
		}
		

	    Collections.sort(sideBarProductList);
	    Collections.sort(groupedSideBarProductList);
	*/
	}


}
