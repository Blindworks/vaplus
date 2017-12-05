package de.vaplus;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.SalesControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.eao.OrderEao;
import de.vaplus.client.eao.SalesEao;
import de.vaplus.client.eao.ShopEao;
import de.vaplus.client.eao.UserEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.VOEntity;

@Stateless
public class SalesController implements SalesControllerInterface{


	@EJB
	private ExceptionControllerInterface exceptionController;

	@EJB
	private ContractControllerInterface contractController;
	
	@Inject
    private ContractEao contractEao;

	@Inject
    private OrderEao orderEao;

	@Inject
    private SalesEao salesEao;

	@Inject
    private UserEao userEao;

	@Inject
    private ShopEao shopEao;

	@Inject
    private VOEao voEao;
	
	@Asynchronous
	@Override
	public void updateAllSalesCache(){
		
//		System.out.println("updateAllSalesCache");
		
		try{

			int firstYear = contractController.getFirstContractYear();
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			
			c.set(Calendar.YEAR, firstYear);
			c.set(Calendar.DAY_OF_YEAR, 1);
			
			while(now.compareTo(c.getTime()) > 0){
				
				int month = c.get(Calendar.MONTH);
				int year = c.get(Calendar.YEAR);
				

//				System.out.println("updateSalesCache: "+month+" "+year);
				
				Iterator<UserEntity> ui = userEao.getUserList().iterator();
				while(ui.hasNext()){
					updateUserCategorySalesCache(ui.next(), month, year);
				}
				
				Iterator<ShopEntity> si = shopEao.getShopList().iterator();
				while(si.hasNext()){
					updateShopCategorySalesCache(si.next(), month, year);
				}
				
				Iterator<? extends VO> vi = voEao.getVOList().iterator();
				while(vi.hasNext()){
					updateVOCategorySalesCache(vi.next(), month, year);
				}

				c.add(Calendar.MONTH, 1);
			}
			
		}catch(Exception e){
			exceptionController.logException(e, null);
		}
	}
	
	
	@Schedule( minute="*/30", hour="*", persistent=false)
    public void updateAllCurrentSalesCache() {
//		System.out.println("@Schedule START updateAllCurrentSalesCache");
		
		try{

			Calendar c = Calendar.getInstance();
			
			c.setTime(new Date());
			int currentMonth = c.get(Calendar.MONTH);
			int currentYear = c.get(Calendar.YEAR);
			
			Iterator<UserEntity> ui = userEao.getUserList().iterator();
			while(ui.hasNext()){
				updateUserCategorySalesCache(ui.next(), currentMonth, currentYear);
			}
			
			Iterator<ShopEntity> si = shopEao.getShopList().iterator();
			while(si.hasNext()){
				updateShopCategorySalesCache(si.next(), currentMonth, currentYear);
			}
			
			Iterator<? extends VO> vi = voEao.getVOList().iterator();
			while(vi.hasNext()){
				updateVOCategorySalesCache(vi.next(), currentMonth, currentYear);
			}
			
		}catch(Exception e){
			exceptionController.logException(e, null);
		}

//		System.out.println("@Schedule END updateAllCurrentSalesCache");
    }

	@Override
	public void updateUserCategorySalesCache(User user, int month, int year){
		
		//clear
		List<? extends ProductCategorySalesCache> productCategorySalesCacheList = user.getProductCategorySalesCacheList();
		clearProductCategorySalesCacheList(productCategorySalesCacheList, month, year);
		
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		
		Date startOfMonth = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);

		Date endOfMonth = c.getTime();
		
		List<? extends BaseContract> list = contractEao.getContractList(null, null, user, null, startOfMonth, endOfMonth, false);
		Iterator<? extends BaseContract> i = list.iterator();
		Iterator<? extends ContractItem> i2;
		BaseContract contract;
		ContractItem contractItem;
		while(i.hasNext()){
			contract = i.next();
			
			if(contract.getCachedTariff() == null)
				continue;
			
			addProductToCategorySalesCache(user, month, year, contract.getCachedTariff().getBaseProduct(), new BigDecimal(1));
			i2 = contract.getCachedTariffOptionList().iterator();
			while(i2.hasNext()){
				contractItem = i2.next();
				addProductToCategorySalesCache(user, month, year, contractItem.getBaseProduct(), new BigDecimal(1));
			}
			
			
		}
		
		List<? extends Order> list2 = orderEao.getOrderList(null, user, null, startOfMonth, endOfMonth, false);
		Iterator<? extends Order> i3 = list2.iterator();
		Iterator<? extends OrderItem> i4;
		Order order;
		OrderItem orderItem;
		while(i3.hasNext()){
			order = i3.next();
			
			i4 = order.getOrderItemList().iterator();
			while(i4.hasNext()){
				orderItem = i4.next();
				addProductToCategorySalesCache(user, month, year, orderItem.getProduct(), orderItem.getQuantity());
			}
			
			
		}
		
		userEao.saveUser((UserEntity) user);
		 
	}
	
	private void addProductToCategorySalesCache(User user, int month, int year, BaseProduct product, BigDecimal quantity){
		
		if(product == null || user == null)
			return;
		
		ProductCategorySalesCache cache = user.getProductCategorySalesCache(product.getProductCategory(), year, month);
		
		cache.setPieces(cache.getPieces().add(quantity));
		
	}
	

	@Override
	public void updateShopCategorySalesCache(Shop shop, int month, int year){

		
		//clear
		List<? extends ProductCategorySalesCache> productCategorySalesCacheList = shop.getProductCategorySalesCacheList();
		clearProductCategorySalesCacheList(productCategorySalesCacheList, month, year);
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		
		Date startOfMonth = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);

		Date endOfMonth = c.getTime();
		
		List<? extends BaseContract> list = contractEao.getContractList(shop, null, null, null, startOfMonth, endOfMonth, false);
		Iterator<? extends BaseContract> i = list.iterator();
		Iterator<? extends ContractItem> i2;
		BaseContract contract;
		ContractItem contractItem;
		while(i.hasNext()){
			contract = i.next();
			
			addProductToCategorySalesCache(shop, month, year, contract.getCachedTariff().getBaseProduct(), new BigDecimal(1));
			i2 = contract.getCachedTariffOptionList().iterator();
			while(i2.hasNext()){
				contractItem = i2.next();
				addProductToCategorySalesCache(shop, month, year, contractItem.getBaseProduct(), new BigDecimal(1));
			}
			
			
		}
		
		List<? extends Order> list2 = orderEao.getOrderList(shop, null, null, startOfMonth, endOfMonth, false);
		Iterator<? extends Order> i3 = list2.iterator();
		Iterator<? extends OrderItem> i4;
		Order order;
		OrderItem orderItem;
		while(i3.hasNext()){
			order = i3.next();
			
			i4 = order.getOrderItemList().iterator();
			while(i4.hasNext()){
				orderItem = i4.next();
				addProductToCategorySalesCache(shop, month, year, orderItem.getProduct(), orderItem.getQuantity());
			}
			
			
		}
		
		shopEao.saveShop((ShopEntity) shop);
		 
	}
	

	@Override
	public void updateVOCategorySalesCache(VO vo, int month, int year){
		
		//clear
		List<? extends ProductCategorySalesCache> productCategorySalesCacheList = vo.getProductCategorySalesCacheList();
		clearProductCategorySalesCacheList(productCategorySalesCacheList, month, year);
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		
		Date startOfMonth = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);

		Date endOfMonth = c.getTime();
		
		List<? extends BaseContract> list = contractEao.getContractList(null, vo, null, null, startOfMonth, endOfMonth, false);
		Iterator<? extends BaseContract> i = list.iterator();
		Iterator<? extends ContractItem> i2;
		BaseContract contract;
		ContractItem contractItem;
		while(i.hasNext()){
			contract = i.next();
			
			addProductToCategorySalesCache(vo, month, year, contract.getCachedTariff().getBaseProduct(), new BigDecimal(1));
			i2 = contract.getCachedTariffOptionList().iterator();
			while(i2.hasNext()){
				contractItem = i2.next();
				addProductToCategorySalesCache(vo, month, year, contractItem.getBaseProduct(), new BigDecimal(1));
			}
			
			
		}
		
		voEao.saveVO((VOEntity) vo);
		 
	}
	
	private void addProductToCategorySalesCache(Shop shop, int month, int year, BaseProduct product, BigDecimal quantity){
		
		if(product == null)
			return;
		
		ProductCategorySalesCache cache = shop.getProductCategorySalesCache(product.getProductCategory(), year, month);
		
		cache.setPieces(cache.getPieces().add(quantity));
		
	}
	
	private void addProductToCategorySalesCache(VO vo, int month, int year, BaseProduct product, BigDecimal quantity){
		
		ProductCategorySalesCache cache = vo.getProductCategorySalesCache(product.getProductCategory(), year, month);
		
		cache.setPieces(cache.getPieces().add(quantity));
		
	}
	
	private void clearProductCategorySalesCacheList(List<? extends ProductCategorySalesCache> productCategorySalesCacheList, int month, int year){
		Iterator<? extends ProductCategorySalesCache> i = productCategorySalesCacheList.iterator();
		ProductCategorySalesCache cache;
		while(i.hasNext()){
			cache = i.next();
			
			if(cache.getMonth() != month)
				continue;
			
			if(cache.getYear() != year)
				continue;
			
			cache.setPieces(new BigDecimal(0));
		}
	}
}
