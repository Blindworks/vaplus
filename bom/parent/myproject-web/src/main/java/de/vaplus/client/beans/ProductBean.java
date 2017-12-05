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
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.applicationbeans.ResourceBean;
import de.vaplus.client.pojo.SideBarProduct;
import de.vaplus.helper.TaxHelper;

@ManagedBean(name="productBean")
@SessionScoped
public class ProductBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -437367847947323508L;

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private VOControllerInterface voController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private ResourceBean resourceBean;

	@Inject
	private FacesContext facesContext;

	@ManagedProperty(value="#{propertyBean}")
	private PropertyBean propertyBean;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
    
	private Vendor selectedVendor;
    
	private BaseProduct selectedProduct;
    
	private ProductCategory selectedProductCategory;
	
	private boolean vendorListEditable;
	
	private boolean productCategoryListEditable;
	
	private boolean productListEditable;
	
	private String productListFilterName;
	
	private String productListFilterTyp;
	
	private ProductCategory productListFilterKategory;
	
	private int productListFilterStatus;
	
	private Date cachedSideBarProductListGenerationTime;
	
	private BaseProductCombination selectedProductCombination;
	
	private boolean productCombinationListEditable;
	
	public ProductBean() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Vendor
	 */

	public List<? extends Vendor> getVendorList(){
		return productController.getVendorList();
	}

	public Vendor getVendor(long id){
		return productController.getVendorById(id);
	}
	
	public String saveVendor(){
		
		selectedVendor = productController.saveVendor(selectedVendor);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Filiale erfolgreich gespeichert.", null));
		
		return "vendorList";
	}
	
	public Vendor getSelectedVendor() {
		if(selectedVendor == null)
			selectedVendor = productController.factoryNewVendor();
		return selectedVendor;
	}

	public void setSelectedVendor(Vendor selectedVendor) {
		this.selectedVendor = selectedVendor;
	}
	
	/*
	 * ProductCategory
	 */
	
	public List<? extends ProductCategory> getProductCategoryList(){
		return productController.getProductCategoryList();
	}
	
	public List<? extends ProductCategory> getProductCategoryListWithout(ProductCategory productCategory){
		List<? extends ProductCategory> list = productController.getProductCategoryList();
		
		Iterator<? extends ProductCategory> i = list.iterator();
		ProductCategory c;
		while(i.hasNext()){
			c = i.next();
			
			if(c.equals(productCategory))
				i.remove();
		}
		
		return list;
	}
	
	public List<? extends ProductCategory> getOverviewProductCategoryList(){
		return resourceBean.getOverviewProductCategoryList();
	}
	
	public String saveProductCategory(){
		
		selectedProductCategory = productController.saveProductCategory(selectedProductCategory);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Produkt Kategorie erfolgreich gespeichert.", null));
		
		return "productCategoryList";
	}
	
	public ProductCategory getSelectedProductCategory() {
		if(selectedProductCategory == null)
			selectedProductCategory = productController.factoryNewProductCategory();
		return selectedProductCategory;
	}

	public void setSelectedProductCategory(ProductCategory selectedProductCategory) {
		this.selectedProductCategory = selectedProductCategory;
	}
	
	/*
	 * Product
	 */

	public PropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public List<? extends BaseProduct> getProductList(){
		return productController.getBaseProductList();
	}

	public List<? extends Product> getRetailProductList(){
		return productController.getRetailProductList();
	}

	public List<? extends BaseProduct> getTariffList(){
		return productController.getTariffList("title", "ASC", true);
	}

	public List<? extends BaseProduct> getProductOptionList(){
		return productController.getProductOptionList();
	}
//	public List<? extends BaseProduct> getProductList(boolean showDisabled){
//		return productController.getBaseProductList(null, null, showDisabled);
//	}
//	public List<? extends BaseProduct> getProductList(String orderBy, String sorting){
//		return productController.getBaseProductList(orderBy, sorting, false);
//	}
//	public List<? extends BaseProduct> getProductList(String orderBy, String sorting, boolean showDisabled){
//		return productController.getBaseProductList(orderBy, sorting, showDisabled);
//	}


	public List<? extends BaseProduct> getFilteredProductList(){
		
		return productController.getFilteredProductList(productListFilterTyp, productListFilterName, productListFilterKategory, productListFilterStatus, "name", "asc", true);
		
//		return productController.getBaseProductList(null, null, false);
	}
	
	public String deleteProduct(){
		
		selectedProduct.setDeleted();
		selectedProduct = productController.saveProduct(selectedProduct);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Produkt gelöscht.", null));
		
		return "productList";
	}
	
	public String deleteProductCombination(){
		
		selectedProductCombination.setDeleted();
		selectedProductCombination = productController.saveBaseProductCombination(selectedProductCombination);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Produkt Kombination gelöscht.", null));
		
		return "productCombinationList";
	}

	
	public String saveProduct(){

		selectedProduct = productController.saveProduct(selectedProduct);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Produkt erfolgreich gespeichert.", null));
		
		return "productList";
	}
	
	public BaseProduct getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(BaseProduct selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public void setCloneProduct(BaseProduct selectedProduct) {
		this.selectedProduct = productController.cloneProduct(selectedProduct);
	}
	

	public String getSelectedProductType() {
		if(this.selectedProduct == null)
			return null;
		if(this.isProductOption())
			return "productOption";
		if(this.isCellPhoneTariffProduct())
			return "cellPhoneTariff";
		if(this.isLandlineTariffProduct())
			return "landlineTariff";
		if(this.isPrePaidTariffProduct())
			return "prepaidTariff";
		if(this.isRetailProduct())
			return "retail";
		
		return null;
	}
	
	public void setSelectedProductType(String selectedProductType) {
		
		if(selectedProductType.equalsIgnoreCase("retail")){
			this.changeToRetailProduct();
		}
		else if(selectedProductType.equalsIgnoreCase("tariff")){
			this.changeToTariffProduct();
		}
		else if(selectedProductType.equalsIgnoreCase("cellPhoneTariff")){
			this.changeToCellPhoneTariffProduct();
		}
		else if(selectedProductType.equalsIgnoreCase("landlineTariff")){
			this.changeToLandlineTariffProduct();
		}
		else if(selectedProductType.equalsIgnoreCase("prepaidTariff")){
			this.changeToPrePaidTariffProduct();
		}
		else if(selectedProductType.equalsIgnoreCase("productOption")){
			this.changeToProductOption();
		}
	}

	public String newProduct(){
		
		if(this.voController.getVOList().size() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte zuerst mind. eine VO anlegen.", null));
			return "productList";
		}
		
		this.selectedProduct = productController.factoryNewCellPhoneTariffProduct();
		return "product";
	}
	
	public void changeToRetailProduct(){
		if(this.isRetailProduct())
			return;
		
		BaseProduct product = selectedProduct;
		this.selectedProduct = productController.factoryNewRetailProduct();
		selectedProduct.setBaseProductValues(product);
	}
	
	public void changeToTariffProduct(){
		if(this.isTariffProduct())
			return;
		
		BaseProduct product = selectedProduct;
		this.selectedProduct = productController.factoryNewTariffProduct();
		selectedProduct.setBaseProductValues(product);
	}
	
	public void changeToCellPhoneTariffProduct(){
		if(this.isCellPhoneTariffProduct())
			return;
		
		BaseProduct product = selectedProduct;
		this.selectedProduct = productController.factoryNewCellPhoneTariffProduct();
		selectedProduct.setBaseProductValues(product);
	}
	
	public void changeToPrePaidTariffProduct(){
		if(this.isPrePaidTariffProduct())
			return;
		
		BaseProduct product = selectedProduct;
		this.selectedProduct = productController.factoryNewPrePaidTariffProduct();
		selectedProduct.setBaseProductValues(product);
	}
	
	public void changeToLandlineTariffProduct(){
		if(this.isLandlineTariffProduct())
			return;
		
		BaseProduct product = selectedProduct;
		this.selectedProduct = productController.factoryNewLandlineTariffProduct();
		selectedProduct.setBaseProductValues(product);
	}
	
	public void changeToProductOption(){
		if(this.isProductOption())
			return;
		
		BaseProduct product = selectedProduct;
		this.selectedProduct = productController.factoryNewProductOption();
		selectedProduct.setBaseProductValues(product);
	}
	
	public String getProductType(){

		
		if(this.isRetailProduct()){
			return "Retail Product";
		}
		else if(this.isCellPhoneTariffProduct()){
			return "Mobilfunk Tarif";
		}
		else if(this.isLandlineTariffProduct()){
			return "Festnetz Tarif";
		}
		else if(this.isPrePaidTariffProduct()){
			return "PrePaid Tarif";
		}
		else if(this.isTariffProduct()){
			return "Tarif";
		}
		else if(this.isProductOption()){
			return "Produkt Option";
		}
		
		return "";
	}
	
	public String getProductType(BaseProduct product){

		
		if(this.isRetailProduct(product)){
			return "Retail Product";
		}
		else if(this.isCellPhoneTariffProduct(product)){
			return "Mobilfunk Tarif";
		}
		else if(this.isLandlineTariffProduct(product)){
			return "Festnetz Tarif";
		}
		else if(this.isPrePaidTariffProduct(product)){
			return "PrePaid Tarif";
		}
		else if(this.isTariffProduct(product)){
			return "Tarif";
		}
		else if(this.isProductOption(product)){
			return "Produkt Option";
		}
		
		return "";
	}
	
	public boolean isRetailProduct(){
		return isRetailProduct(selectedProduct);
	}
	
	public boolean isRetailProduct(BaseProduct product){
		return product instanceof Product;
	}
	
	public boolean isProductOption(){
		return isProductOption(selectedProduct);
	}
	
	public boolean isProductOption(BaseProduct product){
		return product instanceof ProductOption;
	}
	
	public boolean isTariffProduct(){
		return isTariffProduct(selectedProduct);
	}
	
	public boolean isTariffProduct(BaseProduct product){
		return product instanceof Tariff;
	}
	
	public boolean isCellPhoneTariffProduct(){
		return isCellPhoneTariffProduct(selectedProduct);
	}
	
	public boolean isCellPhoneTariffProduct(BaseProduct product){
		return product instanceof CellPhoneTariff;
	}
	
	public boolean isLandlineTariffProduct(){
		return isLandlineTariffProduct(selectedProduct);
	}
	
	public boolean isLandlineTariffProduct(BaseProduct product){
		return product instanceof LandlineTariff;
	}
	
	public boolean isPrePaidTariffProduct(){
		return isPrePaidTariffProduct(selectedProduct);
	}
	
	public boolean isPrePaidTariffProduct(BaseProduct product){
		return product instanceof PrePaidTariff;
	}
	
	
	public boolean isShowPercentagePrice(BaseProduct product){
		if(isProductOption(product) && ((ProductOption)product).isPercentagePrice())
			return true;
		
		return false;
	}
	
	public void importProducts(){
		
		readXml("/Users/dennisknopp/Downloads/Tarife_2015-04-02.xlsx");
	}
	

	private void readXml(String srcPath)
	{
		File src_file;
		XSSFWorkbook src_wb = null;
		XSSFSheet src_sheet;
		XSSFRow row; 
		
		int rowNum = 0;
		

		try {
			src_file = new File(srcPath);

			src_wb = new XSSFWorkbook(src_file);
			
			src_sheet = src_wb.getSheetAt(0);
			
	 
			Iterator<Row> rows = src_sheet.rowIterator();
	 
			while (rows.hasNext())
			{
				rowNum++;
				row = (XSSFRow) rows.next();
				if(row == null)
					break;
				
				if(rowNum < 5)
					continue;
				
				if(row.getCell(1) == null)
					continue;
				
				BaseProduct p = productController.getProductByName(row.getCell(0).getStringCellValue());
				
				if(p != null)
					continue;
				
				if(row.getCell(9) != null && row.getCell(9).getStringCellValue().equalsIgnoreCase("1"))
					p = productController.factoryNewProductOption();
				else
					p = productController.factoryNewTariffProduct();
				

				Vendor vendor = productController.getVendorByName(row.getCell(4).getStringCellValue(), true);
				p.setVendor(vendor);
				
				ProductCategory cat = productController.getProductCategoryByName(row.getCell(3).getStringCellValue(), true);
				p.setProductCategory(cat);
				
//				21:22:15,204 INFO  [stdout] (default task-75) #1 VDSl 100000
//				21:22:15,204 INFO  [stdout] (default task-75) #2 0,30
//				21:22:15,204 INFO  [stdout] (default task-75) #3 Vodafone
//				21:22:15,204 INFO  [stdout] (default task-75) #4 Optionen
//				21:22:15,204 INFO  [stdout] (default task-75) #5 aktiv
//				21:22:15,204 INFO  [stdout] (default task-75) #6 
//				21:22:15,204 INFO  [stdout] (default task-75) #7 
//				21:22:15,205 INFO  [stdout] (default task-75) #8 nein
//				21:22:15,205 INFO  [stdout] (default task-75) #9 0.0
				
				p.setName(row.getCell(0).getStringCellValue());
				p.getCommission().setPoints(new BigDecimal(row.getCell(1).getStringCellValue().replace(",", ".")));
				
				p = productController.saveProduct(p);
				p.setEnabled(true);
				
//				System.out.println("#1 "+row.getCell(0).getStringCellValue());
//				System.out.println("#2 "+row.getCell(1).getStringCellValue());
//				System.out.println("#3 "+row.getCell(2).getStringCellValue());
//				System.out.println("#4 "+row.getCell(3).getStringCellValue());
//				System.out.println("#5 "+row.getCell(4).getStringCellValue());
//				System.out.println("#6 "+row.getCell(5).getStringCellValue());
//				System.out.println("#7 "+row.getCell(6).getStringCellValue());
//				System.out.println("#8 "+row.getCell(7).getStringCellValue());
//				if(row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING)
//					System.out.println("#9 "+row.getCell(8).getStringCellValue());
//				if(row.getCell(8).getCellType() == Cell.CELL_TYPE_NUMERIC)
//					System.out.println("#9 "+row.getCell(8).getNumericCellValue());
				
				
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(src_wb != null)
					src_wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	
	}

	public boolean isVendorListEditable() {
		return vendorListEditable;
	}

	public void toggleVendorListEditable() {
		vendorListEditable = vendorListEditable ? false : true;
	}

	public boolean isProductCategoryListEditable() {
		return productCategoryListEditable;
	}

	public void toggleProductCategoryListEditable() {
		productCategoryListEditable = productCategoryListEditable ? false : true;
	}

	public boolean isProductListEditable() {
		return productListEditable;
	}

	public void toggleProductListEditable() {
		productListEditable = productListEditable ? false : true;
	}
	
	public BigDecimal getMinimumPrice(Product product){
		if(product == null)
			return new BigDecimal(0);
		return TaxHelper.addPercentage(product.getAvergePurchasePrice(), propertyBean.getMinimumPricePremium());
	}
	
	public BigDecimal getMinimumGrossPrice(Product product){
		if(product == null)
			return new BigDecimal(0);
		return TaxHelper.addTax(getMinimumPrice(product), product.getTaxRate().getTax());
	}

	public String getProductListFilterName() {
		return productListFilterName;
	}

	public void setProductListFilterName(String productListFilterName) {
		this.productListFilterName = productListFilterName;
	}

	public String getProductListFilterTyp() {
		return productListFilterTyp;
	}

	public void setProductListFilterTyp(String productListFilterTyp) {
		this.productListFilterTyp = productListFilterTyp;
	}

	public ProductCategory getProductListFilterKategory() {
		return productListFilterKategory;
	}

	public void setProductListFilterKategory(
			ProductCategory productListFilterKategory) {
		this.productListFilterKategory = productListFilterKategory;
	}

	public int getProductListFilterStatus() {
		return productListFilterStatus;
	}

	public void setProductListFilterStatus(int productListFilterStatus) {
		this.productListFilterStatus = productListFilterStatus;
	}

	public BaseProductCombination getSelectedProductCombination() {
		if(selectedProductCombination == null)
			selectedProductCombination = productController.factoryNewBaseProductCombination();
		return selectedProductCombination;
	}

	public void setSelectedProductCombination(BaseProductCombination selectedProductCombination) {
		this.selectedProductCombination = selectedProductCombination;
	}

	public String saveProductCombination() {
		selectedProductCombination = productController.saveBaseProductCombination(selectedProductCombination);
		return "productCombinationList";
	}

	public List<? extends BaseProductCombination> getProductCombinationList() {
		return productController.getProductCombinationList();
	}

	public boolean isProductCombinationListEditable() {
		return productCombinationListEditable;
	}

	public void toggleProductCombinationListEditable() {
		productCombinationListEditable = productCombinationListEditable ? false : true;
	}

}
