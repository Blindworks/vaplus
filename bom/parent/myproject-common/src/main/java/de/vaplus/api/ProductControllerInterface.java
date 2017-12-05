package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

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

public interface ProductControllerInterface extends Serializable{

	List<? extends Vendor> getVendorList();

	Vendor saveVendor(Vendor vendor);

	Vendor factoryNewVendor();

	ProductCategory saveProductCategory(ProductCategory productCategory);

	ProductCategory factoryNewProductCategory();

	List<? extends ProductCategory> getProductCategoryList();

	List<? extends BaseProduct> getBaseProductList();

	List<? extends Product> getRetailProductList();

	BaseProduct saveProduct(BaseProduct product);

	BaseProduct factoryNewRetailProduct();

	BaseProduct factoryNewTariffProduct();

	ProductCategory getProductCategoryById(Long id);

	Vendor getVendorById(Long id);

	BaseProduct getProductById(Long id);

	ProductOption factoryNewProductOption();

	List<? extends Tariff> getTariffList(String orderBy, String sorting, boolean showDisabled);

	List<? extends ProductOption> getProductOptionList();

	BaseProduct getProductByName(String name);

	Vendor getVendorByName(String name);

	ProductCategory getProductCategoryByName(String name);

	Vendor getVendorByName(String name, boolean createIfNotExists);

	ProductCategory getProductCategoryByName(String name,
			boolean createIfNotExists);

	Vendor getDefaultVendor();

	List<? extends Tariff> getTariffList(VO vo);

	List<? extends ProductOption> getProductOptionList(VO vo);

	BaseProduct factoryNewCellPhoneTariffProduct();

	BaseProduct factoryNewLandlineTariffProduct();

	BaseProduct factoryNewPrePaidTariffProduct();

	List<? extends Tariff> getTariffList(VOType voType);

	List<? extends ProductOption> getProductOptionList(VOType voType);

//	List<? extends CellPhoneTariff> getCellPhoneTariffList(String orderBy,
//			String sorting, boolean showDisabled);
//
//	List<? extends LandlineTariff> getLandlineTariffList(String orderBy,
//			String sorting, boolean showDisabled);

//	List<? extends Product> getRetailProductList();

//	List<? extends PrePaidTariff> getPrePaidTariffList(String orderBy,
//			String sorting, boolean showDisabled);

	Product getProductByEAN(String ean);

	BaseProduct cloneProduct(BaseProduct product);

	List<? extends Product> getForeignRetailProductList();

	List<? extends BaseProduct> getFilteredProductList(
			String productClass, String name,
			ProductCategory category, int status, String orderBy,
			String sorting, boolean showDisabled);

	void addVOTypeToVoTypePermissionList(BaseProduct p, VOType voType);

	List<? extends ProductCategory> getOverviewProductCategoryList();

	BaseProductCombination factoryNewBaseProductCombination();

	BaseProductCombination saveBaseProductCombination(
			BaseProductCombination selectedProductCombination);

	List<? extends BaseProductCombination> getProductCombinationList();

	BaseProductCombination getProductCombination(long id);

	Supplier getSupplierById(Long id);

	List<? extends Supplier> getSupplierList();

	Supplier saveSupplier(Supplier supplier);

	Supplier factoryNewSupplier();

	List<? extends Supplier> getSupplierList(boolean showDisabled);

	Supplier refreshSupplier(Supplier selectedSupplier);


}
