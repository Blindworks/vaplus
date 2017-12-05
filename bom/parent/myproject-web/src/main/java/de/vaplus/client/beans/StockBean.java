package de.vaplus.client.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.CellPhoneTariff;
import de.vaplus.api.entity.LandlineTariff;
import de.vaplus.api.entity.PrePaidTariff;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.pojo.SideBarProduct;
import de.vaplus.client.querymapping.SingleProductStockLevelValueInterface;
import de.vaplus.helper.TaxHelper;

@ManagedBean(name="stockBean")
@SessionScoped
public class StockBean implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6754747123026842257L;

	@EJB
	private StockControllerInterface stockController;

	@Inject
	private FacesContext facesContext;

	private Stock selectedStock;

	private Stock viewStock;

	private Product selectedProduct;
	
	private List<? extends StockMovement> selectedProductStockMovementList;
	
	private boolean stockListEditable;
	
	public StockBean() {
		// TODO Auto-generated constructor stub
	}


	public List<? extends Stock> getStockList(){
		return stockController.getStockList();
	}

	public List<? extends Stock> getStockListWithoutStock(Stock stock){
		 List<? extends Stock> list = stockController.getStockList();
		 
		 list.remove(stock);
		 
		 return list;
	}
	
	public List<? extends Stock> getStockList(boolean showDisabled){
		return stockController.getStockList(showDisabled);
	}

	public String saveStock(){
		
		selectedStock = stockController.saveStock(selectedStock);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Lager erfolgreich gespeichert.", null));
		
		return "stockList";
	}
	
	public Stock getSelectedStock() {
		if(selectedStock == null)
			selectedStock = stockController.factoryNewStock();
		return selectedStock;
	}

	public void setSelectedStock(Stock selectedStock) {
		this.selectedStock = stockController.refreshStock(selectedStock);
	}

	
	public String deleteStock(){
		
		selectedStock.setDeleted();
		selectedStock = stockController.saveStock(selectedStock);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Lager gelöscht.", null));
		
		return "/settings/stockList";
	}

	public boolean isStockListEditable() {
		return stockListEditable;
	}

	public void toggleStockListEditable() {
		stockListEditable = stockListEditable ? false : true;
	}


	public Product getSelectedProduct() {
		return selectedProduct;
	}


	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
		selectedProductStockMovementList = null;
	}


	public List<? extends StockMovement> getSelectedProductStockMovementList() {
		if(selectedProductStockMovementList == null)
			selectedProductStockMovementList = stockController.getStockMovementList(getSelectedProduct());
		return selectedProductStockMovementList;
	}

	/*
	 * Returns filtered StockMovement
	 * Only Stock Serials are Returned and in which Stock
	 */
	public List<? extends SingleProductStockLevelValueInterface> getSelectedSingleProductStockLevel(){
		return stockController.getStockLevelForSingleProduct(getSelectedProduct());
	}


	public void setSelectedProductStockMovementList(List<? extends StockMovement> selectedProductStockMovementList) {
		this.selectedProductStockMovementList = selectedProductStockMovementList;
	}
	
	public BigDecimal getStockLevel(Stock stock, Product product){
		return stockController.getStockLevel(stock, product);
	}


	public Stock getViewStock() {
		return viewStock;
	}


	public void setViewStock(Stock viewStock) {
		this.viewStock = viewStock;
	}
}
