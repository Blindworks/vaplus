package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.entity.Product;

@ManagedBean(name = "retailProductBean")
@SessionScoped
public class RetailProductBean implements Serializable {

	private static final long serialVersionUID = 6101170244366039469L;

	@EJB
	private ProductControllerInterface productController;
	
	@Inject
	private FacesContext facesContext;
	
	private String ean;
	private boolean eanProductFound;
	private Product product;
	private String serial;
	private boolean error;
	private boolean finish;
	private boolean foreignWare;
	
	public String getEan() {
		return ean;
	}
	
	public void setEan(String ean) {
		this.ean = ean;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public boolean isEanProductFound() {
		return eanProductFound;
	}

	public void setEanProductFound(boolean eanProductFound) {
		this.eanProductFound = eanProductFound;
	}
	
	public void clearProduct(){
		this.ean = null;
		this.eanProductFound = false;
		this.error = false;
		this.finish = false;
		this.product = null;
		this.serial = null;
	}
	
	private void loadProductByEan(){
		if(ean == null)
			return;
		
		product = productController.getProductByEAN(ean);
		
		if(product == null){
			setError(true);
			setEan(null);
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Artikel nicht gefunden!", null));
			setEanProductFound(false);
		}else{
			
			if(this.isForeignWare() && ! product.isBookableAsForeignWare()){
				product = null;
				
				setError(true);
				setEan(null);
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Artikel keine Fremd Ware!", null));
				setEanProductFound(false);
			}else{
				setError(false);
				setEanProductFound(true);
			}
		}
	}
	
	private void checkSerial(){
		setError(false);
	}
	
	public void checkProduct(){
		
		loadProductByEan();
		
		if(this.product != null){
			if(this.product.isSerialRequired()){
				if(this.getSerial() != null && this.getSerial().length() > 0){
					this.setFinish(true);
				}
			}else{
				this.setFinish(true);
			}
		}
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	
	public boolean isForeignWare() {
		return foreignWare;
	}

	public void setForeignWare(boolean foreignWare) {
		this.foreignWare = foreignWare;
	}

	public List<? extends Product> getProductList(){
		List<? extends Product> list;
		if(this.isForeignWare()){
			list = productController.getForeignRetailProductList();
		}
		else{
			list = productController.getRetailProductList();
		}
		
		return list;
	}
}
