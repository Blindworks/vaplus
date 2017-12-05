package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.AquiredRevenueLevel;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ContractedRevenueLevel;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;

@ManagedBean(name="VOBean")
@SessionScoped
public class VOBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;

	@EJB
	private VOControllerInterface voController;

	@EJB
	private CommissionControllerInterface commissionController;

	@EJB
	private ProductControllerInterface productController;

	@Inject
	private FacesContext facesContext;

	@ManagedProperty(value="#{shopBean}")
	private ShopBean shopBean;

	private VO selectedVO;
	
	private VOType selectedVOType;
	
	private Vendor selectedVendor;
	
	private RevenueLevel selectedRevenueLevel;
	
	private boolean aquiredRevenueMatrixEditable;
	
	private boolean contractedRevenueMatrixEditable;
	
	private boolean voListEditable;
	
	private boolean voTypeListEditable;
	
	private List<? extends RevenueLevelVOType> editableVOTypeRevenueLevelList;
	
	private Map<VOType,Boolean> voTypeEditable;
	
	private Map<VOType,Boolean> productCommissionListEditable;
	
	private BaseProduct selectedProduct;
	
	private ProductCommissionVOTypeProductRelation selectedProductCommissionVOTypeProductRelation;
	
	
	public VOBean() {
		// TODO Auto-generated constructor stub
	}


//	public List<? extends VO> getAllVendorVOList(){
//		return voController.getVOList(null);
//	}

	public List<? extends VO> getVOList(){
		return voController.getVOList();
	}
	
	public List<? extends VO> getPermittedShopVOList(){
		return voController.getVOList(shopBean.getActiveShop(), true);
	}
	
	public List<? extends VO> getPermittedShopVOList(Shop shop){
		return voController.getVOList(shop, true);
	}
	
	public List<? extends VO> getVOList(Vendor vendor){
		return voController.getVOList(vendor);
	}
	
	public List<? extends VOType> getVOTypeList(){
		return voController.getVOTypeList(this.getSelectedVendor());
	}
	
	public List<? extends VOType> getVOTypeList(Vendor vendor){
		return voController.getVOTypeList(vendor);
	}
	
	public List<? extends RevenueLevelVOType> getEditableVOTypeRevenueLevelList(){
		if(editableVOTypeRevenueLevelList == null)
			editableVOTypeRevenueLevelList = voController.getVOTypeRevenueLevelList(this.getSelectedVendor());
		return editableVOTypeRevenueLevelList;
	}

	
	public String getVOTypeShortType(VOType voType){
		if(voType instanceof RevenueLevelVOType){
			return "AU/GU";
		}
		if(voType instanceof ProductCommissionVOType){
			return "SoKo.";
		}
		return "";
	}
	
	public List<? extends ProductCommissionVOType> getProductCommissionVOTypeList(){
		return (List<? extends ProductCommissionVOType>) voController.getVOProductCommissionTypeList();
	}
	
	public List<? extends RevenueLevel> getRevenueLevelList(){
		return commissionController.getRevenueLevelList();
	}
	
	public List<? extends AquiredRevenueLevel> getAquiredRevenueLevelList(){
		return commissionController.getAquiredRevenueLevelList(this.getSelectedVendor());
	}
	
	
	public List<? extends ContractedRevenueLevel> getContractedRevenueLevelList(){
		return commissionController.getContractedRevenueLevelList(this.getSelectedVendor());
	}


	public VO getSelectedVO() {
		if(selectedVO == null){
			selectedVO = voController.factoryNewVO();
			selectedVO.setVendor(this.getSelectedVendor());
		}
		return selectedVO;
	}


	public void setSelectedVO(VO selectedVO) {
		this.selectedVO = selectedVO;
	}


	public ShopBean getShopBean() {
		return shopBean;
	}


	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}


	public VOType getSelectedVOType() {
		return selectedVOType;
	}


	public RevenueLevel getSelectedRevenueLevel() {
		return selectedRevenueLevel;
	}


	public void setSelectedRevenueLevel(RevenueLevel selectedRevenueLevel) {
		this.selectedRevenueLevel = selectedRevenueLevel;
	}


	public boolean isAquiredRevenueMatrixEditable() {
		return aquiredRevenueMatrixEditable;
	}


	public void toggleAquiredRevenueMatrixEditable() {
		aquiredRevenueMatrixEditable = aquiredRevenueMatrixEditable ? false : true;
	}

	public boolean isContractedRevenueMatrixEditable() {
		return contractedRevenueMatrixEditable;
	}

	public void toggleContractedRevenueMatrixEditable() {
		contractedRevenueMatrixEditable = contractedRevenueMatrixEditable ? false : true;
	}

	public void reloadAquiredRevenueMatrix() {
		reloadRevenueMatrix();
		toggleAquiredRevenueMatrixEditable();
	}

	public void reloadContractedRevenueMatrix() {
		reloadRevenueMatrix();
		toggleContractedRevenueMatrixEditable();
	}

	public void reloadRevenueMatrix() {
		editableVOTypeRevenueLevelList = voController.getVOTypeRevenueLevelList(this.getSelectedVendor());
	}

	public void saveAquiredRevenueMatrix() {

		saveRevenueMatrix();
		
		toggleAquiredRevenueMatrixEditable();
	}
	
	public void saveContractedRevenueMatrix() {

		saveRevenueMatrix();
		
		toggleContractedRevenueMatrixEditable();
	}

	private void saveRevenueMatrix() {

		Iterator<? extends RevenueLevelVOType> i = this.getEditableVOTypeRevenueLevelList().iterator();
		RevenueLevelVOType voType;
		while(i.hasNext()){
			voType = i.next();
			voType = (RevenueLevelVOType) voController.saveVOType((VOType) voType);
		}

		editableVOTypeRevenueLevelList = voController.getVOTypeRevenueLevelList(this.getSelectedVendor());
	}


	public String saveVO() {
		voController.saveVO(selectedVO);
		return "voManagement";
	}


	public String saveVOType() {
		voController.saveVOType(selectedVOType);
		return "voManagement";
	}
	
	public String saveRevenueLevel() {
		commissionController.saveRevenueLevel(selectedRevenueLevel);
		return "revenueLevelMatrix";
	}
	
	public String deleteRevenueLevel() {
		commissionController.deleteRevenueLevel(selectedRevenueLevel);
		editableVOTypeRevenueLevelList = voController.getVOTypeRevenueLevelList(this.getSelectedVendor());
		return "revenueLevelMatrix";
	}

	public void setSelectedVOType(VOType selectedVOType) {
		this.selectedVOType = selectedVOType;
	}
	
	public void newVOTypeRevenueLevel(){
		this.selectedVOType = voController.factoryNewVOTypeRevenueLevel();
		this.selectedVOType.setVendor(this.getSelectedVendor());
	}
	
	public void newVOTypeProductCommission(){
		this.selectedVOType = voController.factoryNewVOTypeProductCommission();
		this.selectedVOType.setVendor(this.getSelectedVendor());
	}
	
	public void newAquiredRevenueLevel(){
		this.selectedRevenueLevel = commissionController.factoryNewAquiredRevenueLevel();
		this.selectedRevenueLevel.setVendor(this.getSelectedVendor());
	}
	
	public void newContractedRevenueLevel(){
		this.selectedRevenueLevel = commissionController.factoryNewContractedRevenueLevel();
		this.selectedRevenueLevel.setVendor(this.getSelectedVendor());
	}

	public void removeProductCommissionVOTypeProductRelation(ProductCommissionVOType voType, ProductCommissionVOTypeProductRelation relation){
		((List<ProductCommissionVOTypeProductRelation>) ((ProductCommissionVOType)voType).getProductCommissionList()).remove(relation);

		voController.saveVOType(voType);
	}
	
	public void saveProductCommissionVOType(ProductCommissionVOType voType){
		voController.saveVOType(voType);
		toggleVOTypeEditable(voType);
	}

	public boolean isProductCommissionListEditable(VOType voType) {
		if(productCommissionListEditable == null)
			productCommissionListEditable = new HashMap<VOType,Boolean>();
		
		if(productCommissionListEditable.get(voType) == null)
			productCommissionListEditable.put(voType, false);
		
		return productCommissionListEditable.get(voType);
	}



	public void toggleProductCommissionListEditable(VOType voType) {
		productCommissionListEditable.put(voType, 
				productCommissionListEditable.get(voType) ? false : true
				);
	}


	public boolean isVOTypeEditable(VOType voType) {
		if(voTypeEditable == null)
			voTypeEditable = new HashMap<VOType,Boolean>();
		
		if(voTypeEditable.get(voType) == null)
			voTypeEditable.put(voType, false);
		
		return voTypeEditable.get(voType);
	}


	public void toggleVOTypeEditable(VOType voType) {
		
		if(voTypeEditable == null)
			voTypeEditable = new HashMap<VOType,Boolean>();
		
		if(voTypeEditable.get(voType) == null){
			voTypeEditable.put(voType, true);
		}
		
		voTypeEditable.put(voType, 
				voTypeEditable.get(voType) ? false : true
		);
	}


	public BaseProduct getSelectedProduct() {
		return selectedProduct;
	}


	public void setSelectedProduct(BaseProduct selectedProduct) {
		this.selectedProduct = selectedProduct;
	}


	public boolean isVoListEditable() {
		return voListEditable;
	}


	public void toggleVoListEditable() {
		voListEditable = voListEditable ? false : true;
	}


	public boolean isVoTypeListEditable() {
		return voTypeListEditable;
	}


	public void toggleVoTypeListEditable() {
		voTypeListEditable = voTypeListEditable ? false : true;
	}


	public Vendor getSelectedVendor() {
		if(selectedVendor == null)
			selectedVendor = productController.getDefaultVendor();
		return selectedVendor;
	}


	public void setSelectedVendor(Vendor selectedVendor) {
		this.selectedVendor = selectedVendor;
		reloadRevenueMatrix();
		
	}


	public ProductCommissionVOTypeProductRelation getSelectedProductCommissionVOTypeProductRelation() {
		return selectedProductCommissionVOTypeProductRelation;
	}


	public void newProductCommissionVOTypeProductRelation(VOType voType) {
//		System.out.println("newProductCommissionVOTypeProductRelation");
		selectedVOType = voType;
		this.selectedProductCommissionVOTypeProductRelation = commissionController.factoryNewProductCommissionVOTypeProductRelation();
	}
	
	public List<? extends Tariff> getTariffList(){
		return productController.getTariffList(selectedVOType);
	}
	
	public List<? extends ProductOption> getProductOptionList(){
		return productController.getProductOptionList(selectedVOType);
	}
	
	public void saveProductCommissionVOTypeProductRelation(){
		

		
		if(selectedProductCommissionVOTypeProductRelation.getProduct() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produkt Kondition enthält kein Produkt.", null));
			return;
		}
		
		if(selectedProductCommissionVOTypeProductRelation.getId() == 0){
		
			// check if Relation exists
			ProductCommissionVOTypeProductRelation existingRelation = commissionController.getProductCommissionVOTypeProductRelation(
					(ProductCommissionVOType)selectedVOType,
					selectedProductCommissionVOTypeProductRelation.getProduct(),
					selectedProductCommissionVOTypeProductRelation.getProductOptionList(),
					true
					);
			
			
			if(existingRelation != null){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produkt Kondition bereits vorhanden.", null));
				return;
			}
			
			// init Commission
			selectedProductCommissionVOTypeProductRelation.getCommission();
			
			// Add to VOType
			selectedProductCommissionVOTypeProductRelation.setProductCommissionVOType((ProductCommissionVOType)selectedVOType);
			((List<ProductCommissionVOTypeProductRelation>) ((ProductCommissionVOType)selectedVOType).getProductCommissionList()).add(selectedProductCommissionVOTypeProductRelation);
		}
		
		voController.saveVOType(selectedVOType);
		
//		selectedProductCommissionVOTypeProductRelation = commissionController.saveProductCommissionVOTypeProductRelation(selectedProductCommissionVOTypeProductRelation);
		
		
//		((ProductCommissionVOType)selectedVOType).getProductCommissionList();
	}
	
	public void allowAllProductsForSelectedVoType(){
		if(selectedVOType == null)
			return;
		
		voController.allowAllProductsForVoType(selectedVOType);
	}
	
	public String deleteVO(){
		
		selectedVO.setDeleted();
		selectedVO = voController.saveVO(selectedVO);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "VO gelöscht.", null));
		
		return "/settings/voManagement";
	}
	
	public String deleteVOType(){
		
		selectedVOType.setDeleted();
		selectedVOType = voController.saveVOType(selectedVOType);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "VO Gruppe gelöscht.", null));
		
		return "/settings/voManagement";
	}

}
