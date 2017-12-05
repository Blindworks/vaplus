package de.vaplus.api.entity;

import java.util.List;

public interface ProductStatistic extends Base{

	String getName();

	void setName(String name);

	int getWeight();

	void setWeight(int weight);

	List<? extends ProductCategory> getSelectedProductCategoryList();

	List<? extends BaseProduct> getSelectedProductList();

	void setSelectedProductList(List<? extends BaseProduct> selectedProductList);

	List<? extends BaseProductCombination> getSelectedProductCombinationList();

	void setSelectedProductCombinationList(
			List<? extends BaseProductCombination> selectedProductCombinationList);

	List<? extends BaseProductCombination> getSelectedProductCombinationFilterList();

	void setSelectedProductCombinationFilterList(
			List<? extends BaseProductCombination> selectedProductCombinationFilterList);

	List<? extends BaseProduct> getSelectedProductFilterList();

	void setSelectedProductFilterList(
			List<? extends BaseProduct> selectedProductFilterList);

	void setSelectedProductCategoryList(
			List<? extends ProductCategory> selectedProductCategoryList);

	boolean isNewContract();

	void setNewContract(boolean newContract);

	boolean isExtensionOfTheTerm();

	void setExtensionOfTheTerm(boolean extensionOfTheTerm);

	boolean isDebidCreditChange();

	void setDebidCreditChange(boolean debidCreditChange);

	boolean isShowOnOverview();

	void setShowOnOverview(boolean showOnOverview);

	void setProductStatisticCacheList(
			List<? extends ProductStatisticCache> productStatisticCacheList);

	List<? extends ProductStatisticCache> getProductStatisticCacheList();

	ProductStatisticCache getProductStatisticCache(int month, int year,
			ActivityOwner owner);

	void clearPieces(int month, int year);

	ProductStatisticCache getCurrentProductStatisticCache(ActivityOwner owner);

}
