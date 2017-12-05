package de.vaplus.api;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopDocNumberRanges;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.VO;

public interface ShopControllerInterface extends Serializable {

	List<? extends Shop> getShopList();

	Shop saveShop(Shop shop);

	Shop factoryNewShop();

	Shop getShop(long id);

	Shop getShopByUUID(String uuid);

	Long getShopCount();

	void createFirstShop();

	Shop addVOToShopPermission(Shop shop, VO vo);

	List<? extends VO> getAvailableShopPermissionVOList(Shop shop);

	Shop removeVOFromShopPermission(Shop shop, VO vo);

	Shop updateShopImage(Shop shop, InputStream is, String contentType,
			long size);

	Shop factoryExternalDummyShop();

	List<? extends Shop> getShopList(boolean showDisabled);

	ShopGroup factoryNewShopGroup();

	List<? extends ShopGroup> getShopGroupList();

	List<? extends ShopGroup> getShopGroupList(boolean showDisabled);

	ShopGroup saveShopGroup(ShopGroup selectedShopGroup);

	Shop getShopByAlias(String alias);

	ShopAlias saveShopAlias(ShopAlias shopAlias);

	ShopAlias factoryNewShopAlias(String alias);

	ShopGroup getShopGroup(long id);

	Shop refreshShop(Shop selectedShop);

	Shop createStockForShop(Shop shop);

	List<? extends ShopAlias> getShopAliasList();

	void deleteShopAlias(ShopAlias shopAlias);

	ShopDocNumberRanges getShopDocNumberRanges(Shop shop);

	ShopDocNumberRanges updateShopDocNumberRanges(ShopDocNumberRanges shopDocNumberRanges);

}
