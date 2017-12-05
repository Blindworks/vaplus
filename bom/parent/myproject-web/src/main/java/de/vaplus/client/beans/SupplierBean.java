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
import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.pojo.SideBarProduct;
import de.vaplus.helper.TaxHelper;

@ManagedBean(name="supplierBean")
@SessionScoped
public class SupplierBean implements Serializable {
	
	private static final long serialVersionUID = -5961840365407223159L;

	@EJB
	private ProductControllerInterface productController;

	@Inject
	private FacesContext facesContext;

	private Supplier selectedSupplier;
	
	private boolean supplierListEditable;
	
	public SupplierBean() {
		// TODO Auto-generated constructor stub
	}


	public List<? extends Supplier> getSupplierList(){
		return productController.getSupplierList();
	}
	
	public List<? extends Supplier> getSupplierList(boolean showDisabled){
		return productController.getSupplierList(showDisabled);
	}

	public String saveSupplier(){
		
		selectedSupplier = productController.saveSupplier(selectedSupplier);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Lieferant erfolgreich gespeichert.", null));
		
		return "supplierList";
	}
	
	public Supplier getSelectedSupplier() {
		if(selectedSupplier == null)
			selectedSupplier = productController.factoryNewSupplier();
		return selectedSupplier;
	}

	public void setSelectedSupplier(Supplier selectedSupplier) {
		this.selectedSupplier = productController.refreshSupplier(selectedSupplier);
	}

	
	public String deleteSupplier(){
		
		selectedSupplier.setDeleted();
		selectedSupplier = productController.saveSupplier(selectedSupplier);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Lieferant gelöscht.", null));
		
		return "/settings/supplierList";
	}

	public boolean isSupplierListEditable() {
		return supplierListEditable;
	}

	public void toggleSupplierListEditable() {
		supplierListEditable = supplierListEditable ? false : true;
	}
}
