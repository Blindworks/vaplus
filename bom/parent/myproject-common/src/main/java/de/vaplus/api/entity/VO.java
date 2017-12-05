package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.interfaces.RankableByCommission;


public interface VO extends StatusBase, RankableByCommission, ActivityOwner{

	String getName();

	void setName(String name);

	String getNumber();

	void setNumber(String number);

	Shop getShop();

	void setShop(Shop shop);

	Vendor getVendor();

	void setVendor(Vendor vendor);

	VOType getVoType();

	void setVoType(VOType voType);

	VOCommission getVOCommission(int year, int month);

	CommissionCache getCurrentCommissionCache();

	List<? extends CommissionCache> getCommissionCacheList();

	void setCommissionCacheList(
			List<? extends CommissionCache> commissionCacheList);

	BigDecimal getPointGoal();

	CommissionCache getLastCommissionCache();

	CommissionCache getLiveCommissionCache();

	CommissionCache getNextCommissionCache();

	ProductCategorySalesCache getProductCategorySalesCache(
			ProductCategory category, int year, int month);

	List<? extends ProductCategorySalesCache> getProductCategorySalesCacheList();

	void setProductCategorySalesCacheList(
			List<? extends ProductCategorySalesCache> productCategorySalesCacheList);

	CommissionCache[] getCommissionCacheList(int year);

	CommissionCache[] getCurrentRangeCommissionCacheList();

	List<? extends ProductCategorySalesCache> getCurrentRangeProductCategorySalesCacheList(
			ProductCategory category);

}
