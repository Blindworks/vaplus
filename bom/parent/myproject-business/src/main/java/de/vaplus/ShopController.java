package de.vaplus;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopDocNumberRanges;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.Stock;
import de.vaplus.client.eao.NumberRangeEao;
import de.vaplus.client.eao.ShopEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.ShopGroupEntity;
import de.vaplus.client.entity.VOEntity;


@Stateless
public class ShopController implements ShopControllerInterface {

	
	@EJB
	private FileControllerInterface fileController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private PropertyControllerInterface propertyController;
	
	@EJB
	private StockControllerInterface stockController;
	
    @Inject
    private ShopEao shopEao;

    @Inject
    private VOEao voEao;

	@Inject
    private NumberRangeEao numberRangeEao;

	@Override
	public Shop getShop(long id) {
		return shopEao.getShop(id);
	}

	@Override
	public List<? extends Shop> getShopList() {
		return shopEao.getShopList();
	}

	@Override
	public List<? extends Shop> getShopList(boolean showDisabled) {
		return shopEao.getShopList(showDisabled);
	}

	@Override
	public List<? extends ShopGroup> getShopGroupList() {
		return shopEao.getShopGroupList();
	}

	@Override
	public ShopGroup getShopGroup(long id) {
		return shopEao.getShopGroup(id);
	}

	@Override
	public List<? extends ShopAlias> getShopAliasList() {
		return shopEao.getShopAliasList();
	}

	@Override
	public List<? extends ShopGroup> getShopGroupList(boolean showDisabled) {
		return shopEao.getShopGroupList(showDisabled);
	}

	@Override
	public Shop saveShop(Shop shop) {
		
		boolean newShop = false;
    	
    	if(! (shop instanceof ShopEntity))
    		return shop;
    	
    	if(shop.getId() == 0)
    		newShop = true;
    		
    	
		shop =  shopEao.saveShop((ShopEntity) shop);
		
		if(newShop)
			shop = this.createStockForShop(shop);

		commissionController.updateShopComissionHistory((ShopEntity) shop);
		
		return shop;
    	
	}

	@Override
	public ShopGroup saveShopGroup(ShopGroup shopGroup) {
    	
    	if(! (shopGroup instanceof ShopGroupEntity))
    		return shopGroup;
    	
		shopGroup =  shopEao.saveShopGroup((ShopGroupEntity) shopGroup);

		return shopGroup;
    	
	}


	@Override
	public Shop getShopByUUID(String uuid){
		return shopEao.getShopByUUID(uuid);
	}

	@Override
	public Shop factoryNewShop() {
		ShopEntity shop = new ShopEntity();
		shop.setUuid(UUID.randomUUID().toString());
		return shop;
	}

	@Override
	public ShopGroup factoryNewShopGroup() {
		ShopGroupEntity shopGroup = new ShopGroupEntity();
		return shopGroup;
	}

	@Override
	public Shop factoryExternalDummyShop() {
		ShopEntity shop = new ShopEntity();
		shop.setId(-1);
		shop.setUuid(propertyController.getStringProperty("externalCalendarId", "0"));
		return shop;
	}

	@Override
	public Long getShopCount() {
		return shopEao.getShopCount();
	}

	@Override
	public void createFirstShop() {
		ShopEntity shop = new ShopEntity();
		shop.setName("Hauptfiliale");
		shop.setEnabled(true);
		shop.setEffectiveDate(new Date());
		shop.setPointGoal(new BigDecimal(0));
		shop.setUuid(UUID.randomUUID().toString());
		shopEao.saveShop(shop);
	}

	@Override
	public Shop updateShopImage(Shop shop, InputStream is, String contentType, long size) {
		
		DBFile shopImage = shop.getShopImage();
		
		shopImage = fileController.updateDBFile(shopImage, is, contentType, size);
		
		shop.setShopImage(shopImage);
		
		return shopEao.saveShop((ShopEntity) shop);
	}
	
	@Override
	public Shop addVOToShopPermission(Shop shop, VO vo){
		
		if(shop == null || vo == null)
			return shop;
		
		ShopEntity shopEntity = (ShopEntity) shop;
		
		((List<VO>)shopEntity.getShopVOPermissionList()).add(vo);
		
		return shopEao.saveShop((ShopEntity) shop);
	}
	
	@Override
	public Shop removeVOFromShopPermission(Shop shop, VO vo){
		
		if(shop == null || vo == null)
			return shop;
		
		ShopEntity shopEntity = (ShopEntity) shop;
		
		Iterator<? extends VO> i = shopEntity.getShopVOPermissionList().iterator();
		VO targetVO = null;
		while(i.hasNext()){
			targetVO = i.next();
			if(targetVO.equals(vo))
				i.remove();
		}
		
		return shopEao.saveShop((ShopEntity) shop);
	}
	
	@Override
	public List<? extends VO> getAvailableShopPermissionVOList(Shop shop){
		
//		System.out.println("getAvailableShopPermissionVOList");
		
		List<VOEntity> voList = voEao.getVOList(null);
		
//		System.out.println("full voList size: "+voList.size());
		
		if(voList == null)
			return null;

		voList.removeAll(shop.getShopVOList());
		voList.removeAll(shop.getShopVOPermissionList());
		

//		System.out.println("resulting voList size: "+voList.size());
		
		return voList;
	}



	@Override
	public Shop getShopByAlias(String alias) {
		if(alias == null || alias.trim().length() == 0)
			return null;
		
		return shopEao.getShopByAlias(alias.trim());
	}

	@Override
	public ShopAlias saveShopAlias(ShopAlias shopAlias) {
		return shopEao.saveShopAlias(shopAlias);
	}

	@Override
	public ShopAlias factoryNewShopAlias(String alias) {
		ShopAliasEntity shopAlias = new ShopAliasEntity();
		shopAlias.setAlias(alias);
    	return shopAlias;
	}

	@Override
	public Shop refreshShop(Shop shop) {
		if(shop == null || shop.getId() == 0)
			return shop;
		return shopEao.refreshShop(shop);
	}
	
	@Override
	public Shop createStockForShop(Shop shop){
		if(shop.getStock() != null)
			return shop;
		
		Stock stock = stockController.factoryNewStock();
		stock.setName("Lager "+shop.getName());
		stock.setEnabled(true);
		stock.setAddress(shop.getAddress());
		stock = stockController.saveStock(stock);
		shop.setStock(stock);
		this.saveShop(shop);
		
		return shop;
	}

	@Override
	public void deleteShopAlias(ShopAlias shopAlias) {
		shopEao.deleteShopAlias(shopAlias);
	}
	
	@Override
	public ShopDocNumberRanges getShopDocNumberRanges(Shop shop) {
		return numberRangeEao.getShopDocNumberRanges(shop);
	}

	@Override
	public ShopDocNumberRanges updateShopDocNumberRanges(ShopDocNumberRanges shopDocNumberRanges) {
		return numberRangeEao.updateShopDocNumberRanges(shopDocNumberRanges);
	}
	
}
