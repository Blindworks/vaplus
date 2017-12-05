package de.vaplus.client.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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

import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.Supplier;
import de.vaplus.client.beans.ShopBean;
import de.vaplus.client.beans.UserBean;

@ManagedBean(name = "stockMovementBean")
@SessionScoped
public class StockMovementBean implements Serializable {

	private static final long serialVersionUID = 6101170244366039469L;
	
	private static final String FORM_FIELD_EAN = "ean";
	private static final String FORM_FIELD_SERIAL = "serial";
	private static final String FORM_FIELD_QUANTITY = "quantity";
	private static final String FORM_FIELD_PURCHASEPRICE = "purchasePrice";

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private StockControllerInterface stockController;
	
	@Inject
	private FacesContext facesContext;

	
    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;
	
    @ManagedProperty(value="#{shopBean}")
    private ShopBean shopBean;
	
	private String ean;
	private boolean eanProductFound;
	private Product product;
	private String serial;
	
	private BigDecimal quantity;
	private BigDecimal purchasePrice;
	
	private String nextFormField;

	private StockPickslip stockPickslipImport;

	private StockPickslip stockPickslipExport;
	
	private Stock selectedStock;
	
	private Supplier selectedSupplier;
	
	private StockPickslip selectedStockPickslip;
	
	private StockPickslip pickslipReturning;
	
	private StockPickslip pickslipReimport;
	
	private List<? extends StockPickslip> stockPickslipList;
	
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
	
	public void clearPickslipList(){
		stockPickslipList = null;
	}
	
	public void clearProduct(){
		this.ean = null;
		this.eanProductFound = false;
		this.product = null;
		this.serial = null;
		this.quantity = null;
		this.purchasePrice = null;
		nextFormField = FORM_FIELD_EAN;
	}
	
	public void newStockImport(){
		stockPickslipImport = stockController.factoryNewStockPickslip();
		stockPickslipImport.setStock(shopBean.getActiveShop().getStock());
		stockPickslipImport.setDst_stock(shopBean.getActiveShop().getStock());
		stockPickslipImport.setEffectiveDate(new Date());
	}
	
	public void newStockExport(){
		stockPickslipExport = stockController.factoryNewStockPickslip();
		stockPickslipExport.setStock(shopBean.getActiveShop().getStock());
		stockPickslipExport.setSrc_stock(shopBean.getActiveShop().getStock());
		stockPickslipExport.setEffectiveDate(new Date());
	}
	
	public void loadProductByEan(){
		
		if(ean == null || ean.length() == 0){
			clearProduct();
			return;
		}
		
		product = productController.getProductByEAN(ean);
		
		if(product == null){
			setEan(null);
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Artikel nicht gefunden!", null));
			setEanProductFound(false);
			nextFormField = FORM_FIELD_EAN;
		}else{
			setEanProductFound(true);
		}
		
		initProduct();
	}
	
	public void initProduct(){
		
		if(product != null){

			this.setQuantity(new BigDecimal(1));
			this.setPurchasePrice(new BigDecimal(0));
			this.setSerial(null);
			
			if(this.product.isSerialRequired()){
				nextFormField = FORM_FIELD_SERIAL;
			}else{
				nextFormField = FORM_FIELD_QUANTITY;
			}
		}
	}
	
	public boolean checkSerial(StockPickslip pickslip){
		return checkSerial(pickslip, true);
	}
		
	public boolean checkSerial(StockPickslip pickslip, boolean autoInsert){
		
		if(product == null){
			return true;
		}
		
		if(!product.isSerialRequired()){
			return true;
		}

		if(this.serial == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine Seriennummer angegeben!", null));
			this.setSerial(null);
			nextFormField = FORM_FIELD_SERIAL;
			return false;
		}

		if(this.serial.length() < 4){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Seriennummer muss mind. 4 Zeichen lang sein!", null));
			this.setSerial(null);
			nextFormField = FORM_FIELD_SERIAL;
			return false;
		}
		
		// EXPORT | check if serial on Stock
		if(pickslip.getStock().equals(pickslip.getSrc_stock())){
			
			if(!stockController.isProductOnStock(pickslip.getSrc_stock(), this.product, this.serial)){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Das Produkt mit dieser Seriennummer ist in diesem Lager nicht vorhanden!", null));
				this.setSerial(null);
				nextFormField = FORM_FIELD_SERIAL;
				return false;
			}
			
			
		}

		
		
		//check if serial already found
		
		Iterator<? extends StockMovement> i = pickslip.getStockMovementList().iterator();
		StockMovement movement = null;
		while(i.hasNext()){
			movement = i.next();
			
			if(movement.getProduct().getId() == this.product.getId()){
				
				if(movement.getProduct().isSerialRequired()){
					
					if(movement.getSerial().equalsIgnoreCase(this.getSerial())){
						facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Artikel mit dieser IMEI/Seriennummer bereits hinzugefügt!", null));
						nextFormField = FORM_FIELD_SERIAL;
						this.setSerial(null);
						return false;
					}
				}
			}
		}
		
		nextFormField = FORM_FIELD_PURCHASEPRICE;

		// Export no purchase price needed so insert directly
		if(pickslip.getStock().equals(pickslip.getSrc_stock())){
			checkPurchasePrice(pickslip, autoInsert);
		}

		
		return true;
	}
	
	public boolean checkQuantity(StockPickslip pickslip){
		return checkQuantity(pickslip, true);
	}
	
	public boolean checkQuantity(StockPickslip pickslip, boolean autoInsert){

		if(this.quantity == null || this.quantity.compareTo(new BigDecimal(0)) == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Anzahl angegeben!", null));
			nextFormField = FORM_FIELD_QUANTITY;
			return false;
		}

		if(this.quantity.compareTo(new BigDecimal(0)) < 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anzahl muss positiv sein!", null));
			nextFormField = FORM_FIELD_QUANTITY;
			return false;
		}
		nextFormField = FORM_FIELD_PURCHASEPRICE;
		
		// Export no purchase price needed so insert directly
		if(pickslip.getStock().equals(pickslip.getSrc_stock())){
			checkPurchasePrice(pickslip, autoInsert);
		}

		return true;
	}
	

	public boolean checkPurchasePrice(StockPickslip pickslip){
		return checkPurchasePrice(pickslip, true);
	}
	
	private boolean checkPurchasePrice(StockPickslip pickslip, boolean autoInsert){
		
		
		
		if(pickslip == null){
			System.out.println("checkPurchasePrice StockPickslip is null");
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Interner Fehler bei der Verarbeitung!", null));
			nextFormField = FORM_FIELD_PURCHASEPRICE;
			return false;
		}

		// EXPORT | add average purchase price for export items
		if(pickslip.getStock().equals(pickslip.getSrc_stock())){
			
			if(stockController.getStockLevel(pickslip.getStock(), this.product).compareTo(new BigDecimal(0)) == 0){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Dieses Produkt ist in diesem Lager nicht vorhanden.", null));
				this.clearProduct();
				return false;
			}
			
			this.purchasePrice = stockController.getAveragePurchasePrice(this.getProduct());
		}


		if(this.purchasePrice == null || this.purchasePrice.compareTo(new BigDecimal(0)) == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Einkaufspreis angegeben!", null));
			nextFormField = FORM_FIELD_PURCHASEPRICE;
			return false;
		}

		if(this.purchasePrice.compareTo(new BigDecimal(0)) < 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Einkaufspreis muss positiv sein!", null));
			nextFormField = FORM_FIELD_PURCHASEPRICE;
			return false;
		}

		
		nextFormField = FORM_FIELD_EAN;
		
		if(autoInsert){
			addProductToPickslip(pickslip);
		}
		
		return true;
	}
	
	public void checkProduct(StockPickslip pickslip){
		
		loadProductByEan();

		
		if(this.product != null){
			if(this.product.isSerialRequired()){
				if(this.getSerial() != null && this.getSerial().length() > 0){
					addProductToPickslip(pickslip);
				}
			}else{
				addProductToPickslip(pickslip);
			}
		}
	}
	
	private void addProductToPickslip(StockPickslip pickslip){
		
		
		if(this.product == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kein Produkt gewählt!", null));
			return;
		}
		
		if(!checkSerial(pickslip, false)){
			return;
		}
		
		if(!checkQuantity(pickslip, false)){
			return;
		}
		
		if(!checkPurchasePrice(pickslip, false)){
			return;
		}
		
		// EXPORT | check if quantity on stock
		if(pickslip.getStock().equals(pickslip.getSrc_stock())){
			
			if(stockController.getStockLevel(pickslip.getStock(), this.product).compareTo(new BigDecimal(0)) == 0){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Dieses Produkt ist in diesem Lager nicht vorhanden.", null));
				this.clearProduct();
				return;
			}
		}
		
		
		Iterator<? extends StockMovement> i = pickslip.getStockMovementList().iterator();
		StockMovement movement = null;
		while(i.hasNext()){
			movement = i.next();
			
			if(movement.getProduct().getId() == this.product.getId()){

				// single product
				if(movement.getProduct().isSerialRequired()){
					
					
					if(movement.getSerial().equalsIgnoreCase(this.getSerial())){
						facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Artikel mit dieser IMEI/Seriennummer bereits in Liste!", null));
						this.setSerial(null);

						return;
					}else{
						movement = null;
						break;
					}
					
				}else{
					// add more items to Movement
					break;
				}
				
			}else{
				movement = null;
			}
		}
		
		if(movement != null){
			movement.setQuantity(movement.getQuantity().add(getQuantity()));
		}else{
			movement = stockController.factoryNewStockMovement();
			movement.setProduct(getProduct());
			movement.setQuantity(getQuantity());
			movement.setSerial(getSerial());
			movement.setPurchasePrice(getPurchasePrice());
			
			((List<StockMovement>)pickslip.getStockMovementList()).add(movement);
		}
		

		if(pickslip.getStock().equals(pickslip.getSrc_stock())){
			
			if(stockController.getStockLevel(pickslip.getStock(), this.product).compareTo(movement.getQuantity()) < 0){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Dieses Produkt ist nicht in genügender Menge in diesem Lager vorhanden. Die Anzahl wurde angepasst.", null));
				movement.setQuantity(stockController.getStockLevel(pickslip.getStock(), this.product));
			}
		}

		clearProduct();
	}
	
	public void removeStockMovementFromPickslip(StockPickslip pickslip, StockMovement m){
		Iterator<? extends StockMovement> i = pickslip.getStockMovementList().iterator();
		StockMovement movement;
		
		while(i.hasNext()){
			movement = i.next();
			
			if(movement.equals(m)){
				i.remove();
				break;
			}
		}
		
		
		clearProduct();
	}

	public List<? extends Product> getProductList(){
		List<? extends Product> list;
		list = productController.getRetailProductList();
		
		return list;
	}

	public StockPickslip getStockPickslipImport() {
		if(stockPickslipImport == null)
			this.newStockImport();
		return stockPickslipImport;
	}

	public void setStockPickslipImport(StockPickslip stockPickslip) {
		this.stockPickslipImport = stockPickslip;
	}

	public StockPickslip getStockPickslipExport() {
		if(stockPickslipExport == null)
			this.newStockExport();
		return stockPickslipExport;
	}

	public void setStockPickslipExport(StockPickslip stockPickslipExport) {
		this.stockPickslipExport = stockPickslipExport;
	}

	public BigDecimal getQuantity() {
		if(quantity == null)
			quantity = new BigDecimal(1);
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPurchasePrice() {
		if(purchasePrice == null)
			purchasePrice = new BigDecimal(1);
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getNextFormField() {
		if(nextFormField == null)
			nextFormField = FORM_FIELD_EAN;
		return nextFormField;
	}

	public void setNextFormField(String s) {
	}
	
	public void savePickslipImport(){
		
		if(this.getStockPickslipImport().getSupplier() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte Lieferanten auswählen.", null));
			return;
		}
		
		try {
			if(savePickslip(this.getStockPickslipImport())){
				this.setStockPickslipImport(null);
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Wareneingang erfolgreich verbucht.", null));
			}
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return;
		}
	}
	
	public void savePickslipExport(){
		
		if(this.getStockPickslipExport().getDst_stock() == null && this.getStockPickslipExport().getSupplier() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte Ziel Lager auswählen.", null));
			return;
		}
		
		try {
			if(savePickslip(this.getStockPickslipExport())){
				this.setStockPickslipExport(null);
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Warenausgang erfolgreich verbucht.", null));
			}
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return;
		}
	}
		
	private boolean savePickslip(StockPickslip stockPickslip) throws Exception{
		
		if(stockPickslip == null)
			return false;
		
		if(stockPickslip.getStockMovementList().size() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte erst Artikel einfügen.", null));
			return false;
		}

		stockPickslip.setUser(userBean.getActiveUser());
		stockPickslip.setShop(shopBean.getActiveShop());
		stockPickslip.setEnabled(true);
		
		stockPickslip = stockController.saveStockPickslip(stockPickslip);

		clearPickslipList();
		
		return true;
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

	public List<? extends StockPickslip> getStockPickslipList() {
		if(stockPickslipList == null)
			stockPickslipList = stockController.getStockPickslipList(getSelectedStock(), getSelectedSupplier()) ;
		return stockPickslipList;
	}

	public void setStockPickslipList(List<? extends StockPickslip> stockPickslipList) {
		this.stockPickslipList = stockPickslipList;
	}

	public Stock getSelectedStock() {
		return selectedStock;
	}

	public void setSelectedStock(Stock selectedStock) {
		this.selectedStock = selectedStock;
		clearPickslipList();
	}

	public Supplier getSelectedSupplier() {
		return selectedSupplier;
	}

	public void setSelectedSupplier(Supplier selectedSupplier) {
		this.selectedSupplier = selectedSupplier;
		clearPickslipList();
	}

	public StockPickslip getSelectedStockPickslip() {
		return selectedStockPickslip;
	}

	public void setSelectedStockPickslip(StockPickslip selectedStockPickslip) {
		this.selectedStockPickslip = selectedStockPickslip;
	}

	public void setPickslipReturningNumber(String numberString){
//		long number;
//		
//		try{
//			number = Long.valueOf(numberString);
//		}catch(Exception e){
//			return;
//		}
//		
		pickslipReturning = stockController.getStockPickslipByNumber(numberString);
	}
	
	public String getPickslipReturningNumber(){
		return null;
	}
	
	public StockPickslip getPickslipReimport(){
		return pickslipReimport;
	}
	
	public String loadPickslipReturning(){
		if(pickslipReturning == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Lieferschein nicht gefunden!", null));
			return "";
		}
		if(! pickslipReturning.getDst_stock().equals(shopBean.getActiveShop().getStock()) || pickslipReturning.getSrc_stock() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Lieferschein nicht für den Wareneingang in dieses Lager geeignet!", null));
			return "";
		}
		
		preparePickslipReimport();
		
		
		return "reimport.xhtml";
	}
	
	private void preparePickslipReimport(){

		if(pickslipReturning == null){
			return;
		}
		if(! pickslipReturning.getDst_stock().equals(shopBean.getActiveShop().getStock()) || pickslipReturning.getSrc_stock() == null){
			return;
		}

		
		pickslipReimport = stockController.factoryNewStockPickslip();
		pickslipReimport.setStock(shopBean.getActiveShop().getStock());
		pickslipReimport.setDst_stock(shopBean.getActiveShop().getStock());
		pickslipReimport.setSrc_stock(pickslipReturning.getSrc_stock());
		pickslipReimport.setEffectiveDate(new Date());
		
		pickslipReimport.setNote(pickslipReturning.getNote());
		
		pickslipReimport.setPickslipReference(pickslipReturning);
		
		Iterator<? extends StockMovement> i = getNotImportedMovements(pickslipReturning).iterator();
		StockMovement movement = null;
		StockMovement movement_new = null;
		while(i.hasNext()){
			movement = i.next();
			
			movement_new = stockController.factoryNewStockMovement();
			movement_new.setSerial(movement.getSerial());
			movement_new.setProduct(movement.getProduct());
			movement_new.setPurchasePrice(movement.getPurchasePrice());
			movement_new.setQuantity(movement.getQuantity());
			movement_new.setStockPickslip(pickslipReimport);
			
			((List<StockMovement>)pickslipReimport.getStockMovementList()).add(movement_new);
			
		}
		
	}
	
	public void checkIfPickslipReturningLogged(){

		if(pickslipReturning == null || ! pickslipReturning.getDst_stock().equals(shopBean.getActiveShop().getStock()) || pickslipReturning.getSrc_stock() == null){
			
			try {
    			if(!facesContext.getExternalContext().isResponseCommitted()){
					facesContext.getExternalContext().redirect("in.xhtml");
					facesContext.responseComplete();
    			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private List<? extends StockMovement> getNotImportedMovements(StockPickslip exportPickslip){
		
		exportPickslip = stockController.refreshPickslip(exportPickslip);
		
		
		List<? extends StockMovement> stockMovementReferenceList = exportPickslip.getStockMovementList();
		
		Iterator<? extends StockPickslip> iPickslip = exportPickslip.getPickslipReimportList().iterator();
		StockPickslip p;
		Iterator<? extends StockMovement> iMovement;
		StockMovement m, mReference;
		Iterator<? extends StockMovement> iMovementReference;
		
		// iterate through imported Pickslips
		while(iPickslip.hasNext()){
			p = iPickslip.next();
			iMovement = p.getStockMovementList().iterator();
			
			// iterate through movement lists
			while(iMovement.hasNext()){
				m = iMovement.next();
				iMovementReference = stockMovementReferenceList.iterator();
				
				// remove containing movements from referenceList
				while(iMovementReference.hasNext()){
					mReference = iMovementReference.next();
					
					if(mReference.getProduct() != m.getProduct())
						continue;
					
					if(mReference.getProduct().isSerialRequired() && ! mReference.getSerial().equalsIgnoreCase(m.getSerial()))
						continue;
					
					// subtract quantity
					mReference.setQuantity( mReference.getQuantity().subtract(m.getQuantity()));
					
					// if quantity lower or equal 0 -> remove it
					iMovementReference.remove();
				}
			}
			
		}
		
		return stockMovementReferenceList;
	}

	
	public String savePickslipReimport(){
		
		try {
			if(savePickslip(pickslipReimport)){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Waren-Reimport erfolgreich verbucht.", null));
				
				// check if pickslip is completely reimported 
				List<? extends StockMovement> l = getNotImportedMovements(pickslipReimport.getPickslipReference());
				System.out.println("check if pickslip is completely reimported");
				System.out.println("List size: "+l.size());
				if(l.size() == 0){
					pickslipReimport.getPickslipReference().setCompletelyReImported(true);
					savePickslip(pickslipReimport.getPickslipReference());
				}
				
				Iterator<? extends StockMovement> i = l.iterator();
				StockMovement m;
				while(i.hasNext()){
					m = i.next();
					System.out.println("not imported: "+m.getProduct()+" "+m.getSerial());
				}

				pickslipReimport = null;
				return "in.xhtml";
			}
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return "";
		}

		
		return "";
	}
	
	public List<? extends StockMovement> getOpenMovementsList(){
		List<? extends StockMovement> openMovementsList = new LinkedList<StockMovement>();
		
		Iterator<? extends StockPickslip> i = stockController.getOpenStockPickslipExports().iterator();
		StockPickslip p;
		while(i.hasNext()){
			p = i.next();
			
			((List<StockMovement>)openMovementsList).addAll(getNotImportedMovements(p));
			
		}
		
		return openMovementsList;
	}
	
	public void clearExportSupplier(javax.faces.event.AjaxBehaviorEvent event) throws javax.faces.event.AbortProcessingException{
		this.getStockPickslipExport().setSupplier(null);
	}
	
	public void clearExportStock(javax.faces.event.AjaxBehaviorEvent event) throws javax.faces.event.AbortProcessingException{
		this.getStockPickslipExport().setDst_stock(null);
	}
	
}
