package de.vaplus.client.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.AchievementControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.ProductStatisticControllerInterface;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ProductStatistic;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

@ManagedBean(name = "productStatisticBean")
@SessionScoped
public class ProductStatisticBean implements Serializable {

	private static final long serialVersionUID = 4136486989510030130L;

	@EJB
	private ProductStatisticControllerInterface productStatisticController;

	@Inject
	private FacesContext facesContext;
	
	private boolean productStatisticListEditable;
	
	private ProductStatistic selectedProductStatistic;

	private List<? extends ProductStatistic> productStatisticList;
	private List<? extends ProductStatistic> overviewProductStatisticList;
	
	public ProductStatisticBean(){
//		 System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}

	private void clear(){
		productStatisticList = null;
		selectedProductStatistic = null;
		overviewProductStatisticList = null;
	}

	public boolean isProductStatisticListEditable() {
		return productStatisticListEditable;
	}

	public void toggleProductStatisticListEditable() {
		productStatisticListEditable = productStatisticListEditable ? false : true;
	}

	public ProductStatistic getSelectedProductStatistic() {
		if(selectedProductStatistic == null)
			selectedProductStatistic = productStatisticController.factoryNewProductStatistic();
		return selectedProductStatistic;
	}

	public void setSelectedProductStatistic(ProductStatistic selectedProductStatistic) {
		this.selectedProductStatistic = selectedProductStatistic;
	}
	
	public List<? extends ProductStatistic> getProductStatisticList(){
		if(productStatisticList == null)
			productStatisticList = productStatisticController.getProductStatisticList();
		return productStatisticList;
	}
	
	public List<? extends ProductStatistic> getOverviewProductStatisticList(){
		if(overviewProductStatisticList == null)
			overviewProductStatisticList = productStatisticController.getOverviewProductStatisticList();
		return overviewProductStatisticList;
	}

	public String saveProductStatistic() {
		
		selectedProductStatistic = productStatisticController.saveProductStatistic(getSelectedProductStatistic());
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Produkt Statistik Einstellung gespeichert.", null));

		productStatisticController.calculateProductStatisticCache(getSelectedProductStatistic());
		
		clear();
		
		return "productStatisticList";
	}

	public String deleteProductStatistic() {
		getSelectedProductStatistic().setDeleted();
		saveProductStatistic();
		return "productStatisticList";
	}

	public void recalculateAllProductStatisticCaches(){
		productStatisticController.recalculateAllProductStatisticCaches();
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuberechnung der Produkt Statistiken gestartet.", null));
	}

}
