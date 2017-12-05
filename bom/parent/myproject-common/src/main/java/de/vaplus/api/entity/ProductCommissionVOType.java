package de.vaplus.api.entity;

import java.util.List;

public interface ProductCommissionVOType extends VOType{

	List<? extends ProductCommissionVOTypeProductRelation> getProductCommissionList();

	void setProductCommissionList(
			List<? extends ProductCommissionVOTypeProductRelation> productCommissionList);


}
