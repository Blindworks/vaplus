package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.backingbeans.RetailProductBean;

@ManagedBean(name = "orderBean")
@SessionScoped
public class OrderBean implements Serializable {

	private static final long serialVersionUID = -7048650747422791041L;
	
	private static final String FORM_FIELD_EAN = "ean";
	private static final String FORM_FIELD_SERIAL = "serial";
	private static final String FORM_FIELD_QUANTITY = "quantity";
	private static final String FORM_FIELD_PURCHASEPRICE = "purchasePrice";

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private OrderControllerInterface orderController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	

	@Inject
	private FacesContext facesContext;

	@ManagedProperty(value="#{retailProductBean}")
	private RetailProductBean retailProductBean;

	@ManagedProperty(value="#{productBean}")
	private ProductBean productBean;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	@ManagedProperty(value="#{shopBean}")
	private ShopBean shopBean;
	
	@ManagedProperty(value="#{customerBean}")
	private CustomerBean customerBean;

	private Order selectedOrder;
	private OrderItem selectedOrderItem;
	

	public void setNextFormField(String s) {
	}

	public Order getSelectedOrder() {
		
		if(selectedOrder != null && !selectedOrder.getShop().equals(shopBean.getActiveShop()))
			clearSelectedOrder();
		
		if(selectedOrder == null){
			selectedOrder = orderController.factoryOrder();
			selectedOrder.setEnabled(true);
			selectedOrder.setEffectiveDate(new Date());
			selectedOrder.setUser(userBean.getActiveUser());
			selectedOrder.setShop(shopBean.getActiveShop());
			selectedOrder.setCustomer(customerBean.getCustomer());
		}
		return selectedOrder;
	}
	
	
	public void clearSelectedOrder(){
		this.setSelectedOrder(null);
	}

	public void setSelectedOrder(Order selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	

	public ProductBean getProductBean() {
		return productBean;
	}

	public void setProductBean(ProductBean productBean) {
		this.productBean = productBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}

	public CustomerBean getCustomerBean() {
		return customerBean;
	}

	public void setCustomerBean(CustomerBean customerBean) {
		this.customerBean = customerBean;
	}

	public RetailProductBean getRetailProductBean() {
		return retailProductBean;
	}

	public void setRetailProductBean(RetailProductBean retailProductBean) {
		this.retailProductBean = retailProductBean;
	}

	public void addRetailProduct(){
		addRetailProduct(getSelectedOrder());
	}

	public void addRetailProduct(Order order){
		
		if(retailProductBean.getProduct() == null)
			return;
		
		try {
			orderController.addRetailItemToOrder(order, retailProductBean.getProduct(), retailProductBean.getSerial(), new BigDecimal(1));
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		
		retailProductBean.clearProduct();
		calculateCommission(order);
	}

	public void addProductLine(String title, BigDecimal quantity, String serial, Commissionable commission, BigDecimal unitPrice) {
		
		orderController.addOrderItemToOrder(selectedOrder, title, quantity, serial, commission, unitPrice, false);
		
		calculateCommission();
	}
	
	public void removeRetailItemFromOrder(Order order, OrderItem orderItem){
		orderController.removeRetailItemFromOrder(order, orderItem);
		calculateCommission(order);
	}

	public String saveOrder(){
		if(this.getSelectedOrder() == null || this.getSelectedOrder().getOrderItemList().size() == 0){
			this.clearSelectedOrder();
			return "/customer/activities";
		}

		calculateCommission();
	
		try {
			selectedOrder = orderController.saveOrder(selectedOrder);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), null));
			return "";
		}

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Verkauf erfolgreich gespeichert.", null));
		
		this.clearSelectedOrder();
		
		return "/customer/activities";
	}

	
	public void calculateCommission(){
		calculateCommission(this.getSelectedOrder());
	}
	
	public void calculateCommission(Order order){
		orderController.calculateCommission(order);
//		orderController.calculteSubsidy(order);
		orderController.calculateCommission(order);
	}

	public OrderItem getSelectedOrderItem() {
		return selectedOrderItem;
	}

	public void setSelectedOrderItem(OrderItem selectedOrderItem) {
		this.selectedOrderItem = selectedOrderItem;
	}
	
	public void checkSelectedOrderItem(){

		BigDecimal minPrice = productBean.getMinimumGrossPrice(selectedOrderItem.getProduct());
		BigDecimal maxPrice = selectedOrderItem.getProduct().getCommission().getGrossPrice();
		
		if(selectedOrderItem.getUnitGrossPrice().compareTo(maxPrice) > 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preis grösser als VK.", null));
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Preis wurde angepasst.", null));
			selectedOrderItem.setUnitGrossPrice(maxPrice);
		}
		
		if(selectedOrderItem.getUnitGrossPrice().compareTo(minPrice) < 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preis kleiner als Mindestpreis", null));
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Preis wurde angepasst.", null));
			selectedOrderItem.setUnitGrossPrice(minPrice);
		}
		
	}
	
	public void newOrder() throws IOException {
		clearSelectedOrder();
		
		facesContext.getExternalContext().redirect("/customer/order.xhtml");
	}
	

}
