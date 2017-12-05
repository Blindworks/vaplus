package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.ProductStatistic;
import de.vaplus.api.entity.ProductStatisticCache;

public interface ProductStatisticControllerInterface extends Serializable{

	ProductStatistic factoryNewProductStatistic();

	ProductStatisticCache factoryNewProductStatisticCache();

	ProductStatistic saveProductStatistic(ProductStatistic productStatistic);

	void calculateProductStatisticCache(
			ProductStatistic selectedProductStatistic);

	List<? extends ProductStatistic> getProductStatisticList();

	void recalculateProductStatisticCache(
			ProductStatistic selectedProductStatistic);

	void recalculateAllProductStatisticCaches();

	List<? extends ProductStatistic> getOverviewProductStatisticList();

}
