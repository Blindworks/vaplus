package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.interfaces.RankableByCommission;

public interface ShopGroup extends StatusBase, RankableByCommission, ActivityOwner {

	String getName();

	void setName(String name);

	List<? extends Shop> getShopList();

	void setShopList(List<? extends Shop> shopList);

	CommissionCache[] getCommissionCacheList(int year);

	List<? extends CommissionCache> getCommissionCacheList();

	CommissionCache getLiveCommissionCache();

	CommissionCache getCurrentCommissionCache();

	CommissionCache getLastCommissionCache();

	ProductCategorySalesCache getProductCategorySalesCache(
			ProductCategory category, int year, int month);

	BigDecimal getPointGoal();

	CommissionCache[] getCurrentRangeCommissionCacheList();

	List<? extends ProductCategorySalesCache> getCurrentRangeProductCategorySalesCacheList(
			ProductCategory category);

}
