package de.vaplus;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.eao.ProductEao;
import de.vaplus.client.entity.BaseProductCombinationEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.CellPhoneTariffEntity;
import de.vaplus.client.entity.LandlineTariffEntity;
import de.vaplus.client.entity.PrePaidTariffEntity;
import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.ProductOptionEntity;
import de.vaplus.client.entity.SupplierEntity;
import de.vaplus.client.entity.VOTypeEntity;
import de.vaplus.client.entity.VendorEntity;


@Stateless
public class ProductController implements ProductControllerInterface {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2143950939925289603L;
	
	@Inject
    private ProductEao eao;
	
	@EJB
	private TaxRateControllerInterface taxRateController;

	@Override
	public Vendor getVendorById(Long id) {
		return eao.getVendorById(id);
	}
	
	@Override
	public List<? extends Vendor> getVendorList() {
		return eao.getVendorList();
	}
	
	
	public void clearVendorDefaultSelection(){
		Iterator<VendorEntity> i = eao.getVendorList().iterator();
		VendorEntity v;
		while(i.hasNext()){
			v = i.next();
			v.setDefaultSelection(false);
			
			eao.saveVendor(v);
		}
	}

	@Override
	public Vendor saveVendor(Vendor vendor) {
		
		clearVendorDefaultSelection();
		
		vendor = eao.saveVendor((VendorEntity) vendor);
		
		return vendor;
	}

	@Override
	public Vendor factoryNewVendor() {
		return eao.factoryNewVendor();
	}

	@Override
	public Supplier getSupplierById(Long id) {
		return eao.getSupplierById(id);
	}
	
	@Override
	public List<? extends Supplier> getSupplierList() {
		return eao.getSupplierList(false);
	}

	@Override
	public List<? extends Supplier> getSupplierList(boolean showDisabled) {
		return eao.getSupplierList(showDisabled);
	}

	@Override
	public Supplier refreshSupplier(Supplier supplier) {
		if(supplier == null || supplier.getId() == 0)
			return supplier;
		return eao.refreshSupplier(supplier);
	}

	@Override
	public Supplier saveSupplier(Supplier supplier) {
		
		supplier = eao.saveSupplier((SupplierEntity) supplier);
		
		return supplier;
	}

	@Override
	public Supplier factoryNewSupplier() {
		return eao.factoryNewSupplier();
	}
	
	@Override
	public ProductCategory saveProductCategory(ProductCategory productCategory) {
		return eao.saveProductCategory((ProductCategoryEntity) productCategory);
	}

	@Override
	public ProductCategory factoryNewProductCategory() {
		return eao.factoryNewProductCategory();
	}

	@Override
	public ProductCategory getProductCategoryById(Long id) {
		return eao.getProductCategoryById(id);
	}

	@Override
	public List<? extends ProductCategory> getProductCategoryList() {
		return eao.getProductCategoryList();
	}

	@Override
	public List<? extends ProductCategory> getOverviewProductCategoryList() {
		return eao.getOverviewProductCategoryList();
	}
	
	@Override
	public BaseProduct getProductById(Long id) {
		return eao.getProductById(id);
	}


	@Override
	public List<? extends BaseProduct> getBaseProductList() {
		return eao.getBaseProductList();
	}

//	@Override
//	public List<? extends CellPhoneTariff> getCellPhoneTariffList(String orderBy, String sorting, boolean showDisabled) {
//		return eao.getBaseProductList(CellPhoneTariffEntity.class, orderBy, sorting, showDisabled);
//	}
//
//	@Override
//	public List<? extends LandlineTariff> getLandlineTariffList(String orderBy, String sorting, boolean showDisabled) {
//		return eao.getBaseProductList(LandlineTariffEntity.class, orderBy, sorting, showDisabled);
//	}

	@Override
	public List<? extends Product> getRetailProductList() {
		return eao.getRetailProductList();
	}

	@Override
	public List<? extends Product> getForeignRetailProductList() {
		return eao.getForeignRetailProductList();
	}

//	@Override
//	public List<? extends PrePaidTariff> getPrePaidTariffList(String orderBy, String sorting, boolean showDisabled) {
//		return eao.getBaseProductList(PrePaidTariffEntity.class, orderBy, sorting, showDisabled);
//	}

	@Override
	public List<? extends BaseProduct> getFilteredProductList(String productClassName, String name, ProductCategory category, int status, String orderBy, String sorting, boolean showDisabled) {
		
		Class<? extends BaseProductEntity> productClass;
		
		if(productClassName == null){
			productClass = null;
		}else if(productClassName.equalsIgnoreCase("cellPhoneTariff")){
			productClass = CellPhoneTariffEntity.class;
		} else if(productClassName.equalsIgnoreCase("landlineTariff")){
			productClass = LandlineTariffEntity.class;
		} else if(productClassName.equalsIgnoreCase("prepaidTariff")){
			productClass = PrePaidTariffEntity.class;
		} else if(productClassName.equalsIgnoreCase("productOption")){
			productClass = ProductOptionEntity.class;
		} else if(productClassName.equalsIgnoreCase("retail")){
			productClass = ProductEntity.class;
		} else{
			productClass = null;
		}
		
		return eao.getFilteredProductList(productClass, name, category, status);
	}

	@Override
	public BaseProduct saveProduct(BaseProduct product) {
		return eao.saveProduct((BaseProductEntity) product);
	}

	@Override
	public BaseProduct factoryNewRetailProduct() {
		Product p = eao.factoryNewRetailProduct();
		p.setTaxRate(taxRateController.getDefaultTaxRate());
		return p;
	}

	@Override
	public BaseProduct factoryNewTariffProduct() {
		BaseProduct p = eao.factoryNewTariffProduct();
		p.setTaxRate(taxRateController.getDefaultTaxRate());
		return p;
	}

	@Override
	public BaseProduct factoryNewCellPhoneTariffProduct() {
		BaseProduct p = eao.factoryNewCellPhoneTariffProduct();
		p.setTaxRate(taxRateController.getDefaultTaxRate());
		return p;
	}

	@Override
	public BaseProduct factoryNewLandlineTariffProduct() {
		BaseProduct p = eao.factoryNewLandlineTariffProduct();
		p.setTaxRate(taxRateController.getDefaultTaxRate());
		return p;
	}

	@Override
	public BaseProduct factoryNewPrePaidTariffProduct() {
		BaseProduct p = eao.factoryNewPrePaidTariffProduct();
		p.setTaxRate(taxRateController.getDefaultTaxRate());
		return p;
	}

	@Override
	public ProductOption factoryNewProductOption() {
		ProductOption p = eao.factoryNewProductOption();
		p.setTaxRate(taxRateController.getDefaultTaxRate());
		return p;
	}

	@Override
	public List<? extends Tariff> getTariffList(String orderBy, String sorting, boolean showDisabled) {
		return eao.getTariffList();
	}

	@Override
	public List<? extends Tariff> getTariffList(VO vo) {
		return eao.getTariffList(vo);
	}


	@Override
	public List<? extends Tariff> getTariffList(VOType voType) {
		return eao.getTariffListByVOType(voType);
	}

	@Override
	public List<? extends ProductOption> getProductOptionList() {
		return eao.getProductOptionList();
	}

	@Override
	public List<? extends ProductOption> getProductOptionList(VO vo) {
		return eao.getProductOptionList(vo);
	}

	@Override
	public List<? extends ProductOption> getProductOptionList(VOType voType) {
		return eao.getProductOptionList(voType);
	}

	@Override
	public BaseProduct getProductByName(String name) {
		return eao.getProductByName(name);
	}

	@Override
	public Vendor getVendorByName(String name) {
		return eao.getVendorByName(name, false);
	}

	@Override
	public Vendor getVendorByName(String name, boolean createIfNotExists) {
		return eao.getVendorByName(name, createIfNotExists);
	}

	@Override
	public ProductCategory getProductCategoryByName(String name) {
		return eao.getProductCategoryByName(name, false);
	}

	@Override
	public ProductCategory getProductCategoryByName(String name, boolean createIfNotExists) {
		return eao.getProductCategoryByName(name, createIfNotExists);
	}

	@Override
	public Vendor getDefaultVendor() {
		return eao.getDefaultVendor();
	}

	@Override
	public Product getProductByEAN(String ean) {
		return eao.getProductByEAN(ean);
	}

	@Override
	public BaseProduct cloneProduct(BaseProduct product) {
		BaseProductEntity clone = (BaseProductEntity) product;
		eao.detachProduct(clone);
		clone.setId(0);
		clone.setName(clone.getName()+" (Kopie)");
		
		return clone;
	}

	@Override
	public void addVOTypeToVoTypePermissionList(BaseProduct p, VOType voType) {
		if(p == null || voType == null)
			return;
		
		Iterator<? extends VOType> i = p.getVoTypePermissionList().iterator();
		VOType other;
		while(i.hasNext()){
			other = i.next();
			if(other.equals(voType))
				return;
		}
		((List<VOTypeEntity>)p.getVoTypePermissionList()).add((VOTypeEntity) voType);
	}

	@Override
	public BaseProductCombination factoryNewBaseProductCombination() {
		return new BaseProductCombinationEntity();
	}

	@Override
	public BaseProductCombination saveBaseProductCombination(BaseProductCombination productCombination) {
		return eao.saveProductCombination(productCombination);
	}

	@Override
	public List<? extends BaseProductCombination> getProductCombinationList() {
		return eao.getProductCombinationList();
	}

	@Override
	public BaseProductCombination getProductCombination(long id) {
		return eao.getProductCombination(id);
	}

}
