package de.vaplus.api;

import java.io.Serializable;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;

public interface SalesControllerInterface extends Serializable{

	void updateUserCategorySalesCache(User user, int month, int year);

	void updateShopCategorySalesCache(Shop shop, int month, int year);

	void updateVOCategorySalesCache(VO vo, int month, int year);

	void updateAllSalesCache();

}
