package de.vaplus.api.entity;

import java.util.List;

public interface BaseProductCombination extends Base {

	BaseProduct getProduct();

	void setProduct(BaseProduct product);

	void setProductOptionList(List<? extends ProductOption> productOptionList);

	List<? extends ProductOption> getProductOptionList();

}
