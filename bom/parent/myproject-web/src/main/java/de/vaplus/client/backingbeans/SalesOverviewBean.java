package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.CommissionActivity;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.beans.PermissionBean;
import de.vaplus.client.beans.UserBean;
import de.vaplus.client.pojo.KategorySummary;
import de.vaplus.client.pojo.KategorySummaryItem;


@ManagedBean(name = "salesOverviewBean")
@SessionScoped
public class SalesOverviewBean implements Serializable {

	private static final long serialVersionUID = 2334569916238824747L;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private OrderControllerInterface orderController;

	@EJB
	private CommissionControllerInterface commissionController;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	@ManagedProperty(value="#{permissionBean}")
	private PermissionBean permissionBean;

	private User user;

	private VO vo;

	private Shop shop;
	
	private int month;
	
	private int year;
	
	private List<? extends BaseContract> contractList;
	
	private List<? extends Order> orderList;
	
	private List<? extends Invoice> invoiceList;
	
	private List<? extends CommissionActivity> commissionActivityList;

	private Map<Long, KategorySummary> kategorySummaryMap;
	
	private Map<BaseProduct, BigDecimal> productSummaryMap_newContract;
	private Map<BaseProduct, BigDecimal> productSummaryMap_extensionOfTerm;
	private Map<BaseProduct, BigDecimal> productSummaryMap_debitCreditChange;
	private Map<BaseProduct, BigDecimal> productSummaryMap_sales;
	
	private List<BaseProduct> summaryMapProducts;
	
	public SalesOverviewBean(){
	    resetFilter();
	}
	
	private void refresh(){
		orderList = null;
		setInvoiceList(null);
		contractList = null;
		commissionActivityList = null;
		updateCommissionCache();
		calculateSummary();
	}
	
	public void resetFilter(){
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    year = cal.get(Calendar.YEAR);
	    month = cal.get(Calendar.MONTH);

	    this.user = null;
	    this.vo = null;
	    this.shop = null;
	}
	
	private void calculateSummary(){
		
		// clear summary
		kategorySummaryMap = null;
		productSummaryMap_newContract = null;
		productSummaryMap_extensionOfTerm = null;
		productSummaryMap_debitCreditChange = null;
		productSummaryMap_sales = null;
		summaryMapProducts = null;
		
		Map<BaseProduct, BigDecimal> map;
		
		
		// sum contracts
		Iterator<? extends BaseContract> contractIterator = this.getContractList().iterator();
		BaseContract c;
		Iterator<? extends ContractItem> contractItemIterator;
		ContractItem contractItem;
		KategorySummary sum;
		KategorySummaryItem sumItem;
		ProductCategory cat;
		while(contractIterator.hasNext()){
			c = contractIterator.next();
			
			if(c.isCanceled())
				continue;
			
			if(c.isExtensionOfTerm()){
				map = getProductSummaryMap_extensionOfTerm();
			}else if(c.isDebidCreditChange()){
				map = getProductSummaryMap_debitCreditChange();
			}else{
				map = getProductSummaryMap_newContract();
			}
			
			// Tarif
			if(c.getCachedTariff().getBaseProduct() != null){
				cat = c.getCachedTariff().getBaseProduct().getProductCategory();
				sum = getKategorySummary(cat.getId(), cat.getName(), cat.getColor());
				sumItem = sum.getKategorySummaryItem(c.getCachedTariff().getBaseProduct().getId(), c.getCachedTariff().getBaseProduct().getName());
				sumItem.addItem(new BigDecimal(1), c.getCachedTariff().getProductCommission().getPrice());
				
				// add to Product Summary
				addToProductSummary(map, c.getCachedTariff().getBaseProduct(), new BigDecimal(1));
			}
			
			// Options
			contractItemIterator = c.getCachedTariffOptionList().iterator();
			while(contractItemIterator.hasNext()){
				contractItem = contractItemIterator.next();
				if(contractItem.getBaseProduct() != null){
					cat = contractItem.getBaseProduct().getProductCategory();
					sum = getKategorySummary(cat.getId(), cat.getName(), cat.getColor());
					sumItem = sum.getKategorySummaryItem(contractItem.getBaseProduct().getId(), contractItem.getBaseProduct().getName());
					sumItem.addItem(new BigDecimal(1), contractItem.getProductCommission().getPrice());
					
					addToProductSummary(map, contractItem.getBaseProduct(), new BigDecimal(1));
				}
				
			}
		}

		// sum order
		Iterator<? extends Order> orderIterator = this.getOrderList().iterator();
		Iterator<? extends OrderItem> orderItemIterator;
		OrderItem orderItem;
		Order o;
		map = getProductSummaryMap_sales();
		while(orderIterator.hasNext()){
			o = orderIterator.next();
			

			orderItemIterator = o.getOrderItemList().iterator();
			while(orderItemIterator.hasNext()){
				orderItem = orderItemIterator.next();
				if(orderItem.getProduct() != null){
					cat = orderItem.getProduct().getProductCategory();
					sum = getKategorySummary(cat.getId(), cat.getName(), cat.getColor());
					sumItem = sum.getKategorySummaryItem(orderItem.getProduct().getId(), orderItem.getProduct().getName());
					sumItem.addItem(orderItem.getQuantity(), orderItem.getCommission().getPrice());
					
					addToProductSummary(map, orderItem.getProduct(), orderItem.getQuantity());
				}
				
			}
		}
	}
	
	public User getUser() {
		if(user == null){
			if(! permissionBean.hasPermission("contract_list","read")){
				setUser(userBean.getActiveUser());
			}
		}
		
		return user;
	}
	
	public void setUser(User user) {
		
		if(permissionBean.hasPermission("contract_list","read")){
			this.user = user;
		}else{
			this.user = userBean.getActiveUser();
		}
		refresh();
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
		refresh();
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
		refresh();
	}
	
	public VO getVo() {
		return vo;
	}

	public void setVo(VO vo) {
		this.vo = vo;
		refresh();
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
		refresh();
	}

	public KategorySummary getKategorySummary(long id, String name, String color) {
		if(! getKategorySummaryMap().containsKey(id))
			getKategorySummaryMap().put(id, new KategorySummary(name, color));
		return getKategorySummaryMap().get(id);
	}

	public Map<Long, KategorySummary> getKategorySummaryMap() {
		if(kategorySummaryMap == null)
			kategorySummaryMap = new HashMap<Long, KategorySummary>();
		return kategorySummaryMap;
	}

//	public List<KategorySummary> getKategorySummaryList() {
//		
//		getKategorySummaryMap().
//		
//		if(kategorySummaryMap == null)
//			kategorySummaryMap = new HashMap<Long, KategorySummary>();
//		return kategorySummaryMap;
//	}

	public void setKategorySummaryMap(Map<Long, KategorySummary> kategorySummaryMap) {
		this.kategorySummaryMap = kategorySummaryMap;
	}


	public Map<BaseProduct, BigDecimal> getProductSummaryMap_newContract() {
		if(productSummaryMap_newContract == null)
			productSummaryMap_newContract = new HashMap<BaseProduct, BigDecimal>();
		return productSummaryMap_newContract;
	}


	public Map<BaseProduct, BigDecimal> getProductSummaryMap_extensionOfTerm() {
		if(productSummaryMap_extensionOfTerm == null)
			productSummaryMap_extensionOfTerm = new HashMap<BaseProduct, BigDecimal>();
		return productSummaryMap_extensionOfTerm;
	}


	public Map<BaseProduct, BigDecimal> getProductSummaryMap_debitCreditChange() {
		if(productSummaryMap_debitCreditChange == null)
			productSummaryMap_debitCreditChange = new HashMap<BaseProduct, BigDecimal>();
		return productSummaryMap_debitCreditChange;
	}


	public Map<BaseProduct, BigDecimal> getProductSummaryMap_sales() {
		if(productSummaryMap_sales == null)
			productSummaryMap_sales = new HashMap<BaseProduct, BigDecimal>();
		return productSummaryMap_sales;
	}

	public List<BaseProduct> getSummaryMapProducts() {
		if(summaryMapProducts == null)
			summaryMapProducts = new LinkedList<BaseProduct>();
		return summaryMapProducts;
	}


	public BaseProduct[] getProductSummaryMapProducts() {
		BaseProduct[] products = getSummaryMapProducts().toArray(new BaseProduct[getSummaryMapProducts().size()]);
		
		Arrays.sort(products, new Comparator<BaseProduct>() {
	        @Override
	        public int compare(BaseProduct first, BaseProduct second) {
	            return first.getName().toLowerCase().compareTo(second.getName().toLowerCase());
	        }
	    });
		
		return products;
	}
	
	private void addToProductSummary(Map<BaseProduct, BigDecimal> map, BaseProduct product, BigDecimal num){
		
		if(product == null || num == null)
			return;
		
		if(! getSummaryMapProducts().contains(product))
			getSummaryMapProducts().add(product);
		
		if(! getProductSummaryMap_newContract().containsKey(product))
			getProductSummaryMap_newContract().put(product, new BigDecimal(0));
		
		if(! getProductSummaryMap_extensionOfTerm().containsKey(product))
			getProductSummaryMap_extensionOfTerm().put(product, new BigDecimal(0));
		
		if(! getProductSummaryMap_debitCreditChange().containsKey(product))
			getProductSummaryMap_debitCreditChange().put(product, new BigDecimal(0));
		
		if(! getProductSummaryMap_sales().containsKey(product))
			getProductSummaryMap_sales().put(product, new BigDecimal(0));
		
		map.put(product, map.get(product).add(num));
	}

	private Date getFrom(){

		// All Month workaround
		int m = month;
		if(m == -1)
			m = 0;

	    Calendar cal = Calendar.getInstance();
	    cal.set(year, m, 1, 0, 0, 0);
	    return cal.getTime();
	}
	
	private Date getTo(){

		// All Month workaround
		int m = month;
		if(m == -1)
			m = 11;

	    Calendar cal = Calendar.getInstance();
	    cal.set(year, m, 1, 0, 0, 0);
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.MILLISECOND, -1);
	    
	    return cal.getTime();
	}
	
	private void updateCommissionCache(){
		
		if(user != null){
			updateCommissionCache(user);
		}else{
			Iterator<? extends User> i = userBean.getUserList().iterator();
			User u;
			while(i.hasNext()){
				u = i.next();
				updateCommissionCache(u);
			}
		}
	}

	private void updateCommissionCache(User user){
		
		if(month >= 0){
			commissionController.updateUserCommissionCache(user, month, year);
		}else{
			// No Month selected
			for(int i = 0; i <= 11; i++){
				commissionController.updateUserCommissionCache(user, i, year);
			}
		}
		
		
	}

	
	public List<Integer> getYearList(){
		int firstYear = contractController.getFirstContractYear();
		
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    int actYear = cal.get(Calendar.YEAR);
	    
	    List<Integer> yearList = new LinkedList<Integer>();
		
		for(int i = firstYear; i <= actYear + 2; i++){
			yearList.add(i);
		}
		
		return yearList;
	}
	
	public List<? extends BaseContract> getContractList() {
		if(contractList == null)
			contractList = contractController.getContractList(shop, vo, user, getFrom(), getTo());
		
		return contractList;
	}
	
	public List<? extends CommissionActivity> getCommissionActivityList() {
		if(commissionActivityList == null)
			commissionActivityList = contractController.getCommissionActivityList(shop, user, getFrom(), getTo());
		
		return commissionActivityList;
	}
	
	public List<? extends Order> getOrderList() {
		if(orderList == null)
			orderList = orderController.getOrderList(shop, user, getFrom(), getTo());
		
		return orderList;
	}

	public List<? extends Invoice> getInvoiceList() {
		if(invoiceList == null)
			invoiceList = orderController.getInvoiceList(shop, user, getFrom(), getTo());
		
		return invoiceList;
	}

	public void setInvoiceList(List<? extends Invoice> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public PermissionBean getPermissionBean() {
		return permissionBean;
	}

	public void setPermissionBean(PermissionBean permissionBean) {
		this.permissionBean = permissionBean;
	}

}
