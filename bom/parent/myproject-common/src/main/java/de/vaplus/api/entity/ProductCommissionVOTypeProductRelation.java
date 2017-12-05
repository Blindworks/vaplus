package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.embeddable.Commissionable;


public interface ProductCommissionVOTypeProductRelation extends StatusBase{

	BaseProduct getProduct();

	void setProduct(BaseProduct product);

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	ProductCommissionVOType getProductCommissionVOType();

	void setProductCommissionVOType(
			ProductCommissionVOType productCommissionVOType);

	List<? extends ProductOption> getProductOptionList();

	void setProductOptionList(List<? extends ProductOption> productOptionList);

	BigDecimal getSubsidyBudgetary();

	void setSubsidyBudgetary(BigDecimal subsidyBudgetary);


}
