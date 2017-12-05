package de.vaplus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.AchievementControllerInterface;
import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.ProductStatisticControllerInterface;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.AchievementUserGoalAttainment;
import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.ProductStatistic;
import de.vaplus.api.entity.ProductStatisticCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.eao.AchievementEao;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.eao.ProductStatisticEao;
import de.vaplus.client.entity.AchievementEntity;
import de.vaplus.client.entity.AchievementTargetEntity;
import de.vaplus.client.entity.AchievementUserGoalAttainmentEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductStatisticCacheEntity;
import de.vaplus.client.entity.ProductStatisticEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;

@Stateless
public class ProductStatisticController implements ProductStatisticControllerInterface{

	private static final long serialVersionUID = 3172392130372103624L;

	@Inject
    private ProductStatisticEao eao;
	
	
	@EJB
	private ContractControllerInterface contractController;
	
	@EJB
	private OrderControllerInterface orderController;

	@EJB
	private ExceptionControllerInterface exceptionController;
	

	@Override
	public ProductStatistic factoryNewProductStatistic() {
		ProductStatisticEntity achievement = new ProductStatisticEntity();
		return achievement;
	}

	@Override
	public ProductStatisticCache factoryNewProductStatisticCache() {
		ProductStatisticCacheEntity achievement = new ProductStatisticCacheEntity();
		return achievement;
	}

	@Override
	public ProductStatistic saveProductStatistic(ProductStatistic productStatistic) {
		return eao.saveProductStatistic((ProductStatisticEntity) productStatistic);
	}

	@Schedule( minute="0,15,30,45", hour="*", persistent=false)
	public void calculateAllProductStatisticCaches(){
//		System.out.println("@Schedule START calculateAllProductStatisticCaches");
		
		Iterator<? extends ProductStatistic> i = getProductStatisticList().iterator();
		while(i.hasNext()){
			calculateProductStatisticCache(i.next());
		}

//		System.out.println("@Schedule END calculateAllProductStatisticCaches");
	}

	@Override
	public void calculateProductStatisticCache( ProductStatistic productStatistic) {
		Calendar c = Calendar.getInstance();
			
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
			
		calculateProductStatisticCache(productStatistic, month, year);
	}

	@Override
	public List<? extends ProductStatistic> getProductStatisticList() {
		return eao.getProductStatisticList();
	}

	@Override
	public List<? extends ProductStatistic> getOverviewProductStatisticList() {
		return eao.getOverviewProductStatisticList();
	}
	
	@Asynchronous
	@Override
	public void recalculateAllProductStatisticCaches(){
		Iterator<? extends ProductStatistic> i = getProductStatisticList().iterator();
//		System.out.println("recalculateAllProductStatisticCaches START size: "+getProductStatisticList().size());
		while(i.hasNext()){
			recalculateProductStatisticCache(i.next());
		}
//		System.out.println("recalculateAllProductStatisticCaches DONE");
	}

	@Override
	public void recalculateProductStatisticCache( ProductStatistic productStatistic) {

//		System.out.println("recalculateProductStatisticCache: "+productStatistic.getId());

		int firstYear = contractController.getFirstContractYear();
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.YEAR, firstYear);
		c.set(Calendar.DAY_OF_YEAR, 1);
		
		while(now.compareTo(c.getTime()) > 0){
			
			int month = c.get(Calendar.MONTH);
			int year = c.get(Calendar.YEAR);
			
			calculateProductStatisticCache(productStatistic, month, year);

			c.add(Calendar.MONTH, 1);
		}
		
	}

	private void calculateProductStatisticCache( ProductStatistic productStatistic, int month, int year) {
//		System.out.println("recalculateProductStatisticCache: "+productStatistic.getId()+" "+month+" "+year);

		productStatistic.clearPieces(month, year);

		// get ProductStatisticCache
		ProductStatisticCache shopCache = null;
		ProductStatisticCache userCache = null;
		ProductStatisticCache voCache = null;
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		
		Date startOfMonth = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);

		Date endOfMonth = c.getTime();

		boolean foundContractItem; 
		boolean tariffAlreadyProcessed;
		List<ProductOption> alreadyProcessedProductOption;
		
		Iterator<? extends OrderItem> orderItemIterator;
		OrderItem orderItem;

		Iterator<? extends ContractRetailItem> contractRetailItemIterator;
		ContractRetailItem contractRetailItem;
		
		Iterator<? extends ContractItem> contractItemIterator;
		ContractItem contractItem;

		Iterator<? extends BaseProductCombination> productCombinationEntityIterator;
		BaseProductCombination productCombination;

		Iterator<? extends ProductOption> productOptionIterator;
		ProductOption productOption;
		
		// CONTRACTS
		List<? extends BaseContract> list = contractController.getContractList(null, null, null, startOfMonth, endOfMonth);
		Iterator<? extends BaseContract> contractIterator = list.iterator();
		Iterator<? extends ContractItem> i2;
		BaseContract contract;
		
		

		contractLoop:
		while(contractIterator.hasNext()){
			contract = contractIterator.next();
			
			if(contract.isCanceled())
				continue;
			
			tariffAlreadyProcessed = false;
			alreadyProcessedProductOption = new ArrayList<ProductOption>();
			
			if(contract.isExtensionOfTerm() && ! productStatistic.isExtensionOfTheTerm())
				continue;

			if(! contract.isExtensionOfTerm() && ! productStatistic.isNewContract())
				continue;

			if(contract.isDebidCreditChange() && ! productStatistic.isDebidCreditChange())
				continue;
			

			
			// get ProductStatisticCache
			shopCache = productStatistic.getProductStatisticCache(month, year, contract.getShop());
			userCache = productStatistic.getProductStatisticCache(month, year, contract.getUser());
			voCache = productStatistic.getProductStatisticCache(month, year, contract.getVo());
			
			
			
			
			// Product Combination Blackist
			// if product and ALL Options are SAME -> SKIP
			productCombinationEntityIterator = productStatistic.getSelectedProductCombinationFilterList().iterator();
			productCombinationLoop:
			while(productCombinationEntityIterator.hasNext()){
				
//				System.out.println("Check Product : "+contract.getEffectiveDate()+" - "+contract.getUser() +" - "+ contract.getShop() +" - "+ contract.getVo());
				
				productCombination = productCombinationEntityIterator.next();
				
				
				// Check Tarif
				if(! productCombination.getProduct().equals(contract.getCachedTariff().getBaseProduct()))
					continue;
				
				// Check ProductOption count
				if(productCombination.getProductOptionList().size() != contract.getCachedTariffOptionList().size())
					continue;
				

				
				// Check ProductOptions
				productOptionIterator = productCombination.getProductOptionList().iterator();
				while(productOptionIterator.hasNext()){
					productOption = productOptionIterator.next();
					
					// search ProductOption in CachedTariffOptionList
					contractItemIterator = contract.getCachedTariffOptionList().iterator();
					foundContractItem = false;
					while(contractItemIterator.hasNext()){
						contractItem = contractItemIterator.next();
						
						if(contractItem.getBaseProduct() == null)
							continue productCombinationLoop;
						
						// Produkt Option found
						if(productOption.equals(contractItem.getBaseProduct())){
							foundContractItem = true;
							break;
						}
					}
					
					// Produkt Option not found
					if(!foundContractItem)
						continue productCombinationLoop;
				}
				
				// Product ans All of its Options ARE SAME -> SKIP Contract
				continue contractLoop;
					
			}
			

			// Product Combination White
			// if product and ALL Options found -> process
			productCombinationEntityIterator = productStatistic.getSelectedProductCombinationList().iterator();
			productCombinationLoop:
			while(productCombinationEntityIterator.hasNext()){
				
				productCombination = productCombinationEntityIterator.next();
				
				// Check Tarif
				if(! productCombination.getProduct().equals(contract.getCachedTariff().getBaseProduct()))
					continue;
				
				// Check ProductOptions
				productOptionIterator = productCombination.getProductOptionList().iterator();
				while(productOptionIterator.hasNext()){
					productOption = productOptionIterator.next();
					
					// search ProductOption in CachedTariffOptionList
					contractItemIterator = contract.getCachedTariffOptionList().iterator();
					foundContractItem = false;
					while(contractItemIterator.hasNext()){
						contractItem = contractItemIterator.next();
						
						if(contractItem.getBaseProduct() == null)
							continue productCombinationLoop;
						
						// Produkt Option found
						if(productOption.equals(contractItem.getBaseProduct())){
							foundContractItem = true;
							break;
						}
					}
					
					// Produkt Option not found
					if(!foundContractItem)
						continue productCombinationLoop;
				}
				
				// Product and All of its Options found -> USE IT
				
				tariffAlreadyProcessed = true;

				shopCache.addPieces(1);
				userCache.addPieces(1);
				voCache.addPieces(1);
				
				
				// process Product Options
				productOptionIterator = productCombination.getProductOptionList().iterator();
				while(productOptionIterator.hasNext()){
					productOption = productOptionIterator.next();
					alreadyProcessedProductOption.add(productOption);
				}
				
			}

			

			// Base Tariff
			if(!tariffAlreadyProcessed && isSelectedProduct(productStatistic, contract.getCachedTariff().getBaseProduct())){

				shopCache.addPieces(1);
				userCache.addPieces(1);
				voCache.addPieces(1);
				
			}
			
			
			// Product Options
			contractItemIterator = contract.getCachedTariffOptionList().iterator();
			while(contractItemIterator.hasNext()){
				contractItem = contractItemIterator.next();
				
				if(contractItem.getBaseProduct() != null && alreadyProcessedProductOption.contains(contractItem.getBaseProduct()))
					continue;
				
				if(isSelectedProduct(productStatistic, contractItem.getBaseProduct())){

					shopCache.addPieces(1);
					userCache.addPieces(1);
					voCache.addPieces(1);
				}
			}


			// Retail Products in Contract
			contractRetailItemIterator = contract.getContractRetailItemList().iterator();
			while(contractRetailItemIterator.hasNext()){
				contractRetailItem = contractRetailItemIterator.next();
				
				if(contractRetailItem.getProduct() == null)
					continue;
				
				if(isSelectedProduct(productStatistic, contractRetailItem.getProduct())){

					shopCache.addPieces(1);
					userCache.addPieces(1);
					voCache.addPieces(1);
				}
			}
				
		}
		
		

		// ORDERS
		
		List<? extends Order> list2 = orderController.getOrderList(null, null, startOfMonth, endOfMonth);
		Iterator<? extends Order> i3 = list2.iterator();
		Iterator<? extends OrderItem> i4;
		Order order;
		while(i3.hasNext()){
			order = i3.next();

			// get ProductStatisticCache
			shopCache = productStatistic.getProductStatisticCache(month, year, order.getShop());
			userCache = productStatistic.getProductStatisticCache(month, year, order.getUser());
			
			i4 = order.getOrderItemList().iterator();
			while(i4.hasNext()){
				orderItem = i4.next();
				
				if(isSelectedProduct(productStatistic, orderItem.getProduct())){
					shopCache.addPieces(1);
					userCache.addPieces(1);
				}
				
			}
			
		}
		
		saveProductStatistic(productStatistic);
		
	}
	
	private boolean isSelectedProduct(ProductStatistic productStatistic, BaseProduct product){
	
		if(productStatistic == null || product == null)
			return false;
		
		if(productStatistic.getSelectedProductFilterList().contains(product))
			return false;
		
		if(productStatistic.getSelectedProductList().contains(product))
			return true;
		
		Iterator<? extends ProductCategory> i = productStatistic.getSelectedProductCategoryList().iterator();
		ProductCategory category;
		while(i.hasNext()){
			category = i.next();
			
			if(product.getProductCategory().equals(category))
				return true;
			
		}
		
		return false;
	}

//	
//	
//	@Override
//	public Achievement getActiveAchievement(long id) {
//		return eao.getActiveAchievement(id);
//	}
//
//	@Override
//	public List<? extends Achievement> getAchievementList() {
//		return eao.getAchievementList();
//	}
//
//	@Override
//	public List<? extends Achievement> getActiveAchievementList() {
//		return eao.getAchievementList(new Date());
//	}
//
//
//	@Override
//	public List<? extends Achievement> getActiveAchievementListForCompanyDashboard() {
//		List<? extends Achievement> list = getActiveAchievementList();
//		
//		Iterator<? extends Achievement> i = list.iterator();
//		while(i.hasNext()){
//			if(! i.next().isShowOnCompanyDashboard())
//				i.remove();
//		}
//		return list;
//	}
//
//	@Override
//	public List<? extends Achievement> getOpenAchievementList() {
//		return eao.getOpenAchievementList();
//	}
//
//	@Override
//	public List<? extends Achievement> getClosedAchievementList() {
//		return eao.getClosedAchievementList();
//	}
//
//	@Override
//	public List<? extends Achievement> getAchievementList(int month, int year) {
//		
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.YEAR, year);
//		c.set(Calendar.MONTH, month);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		c.clear(Calendar.HOUR_OF_DAY);
//		c.clear(Calendar.MINUTE);
//		c.clear(Calendar.SECOND);
//		c.clear(Calendar.MILLISECOND);
//		
//		Date from = c.getTime();
//
//		c.add(Calendar.MONTH, 1);
//		c.add(Calendar.MILLISECOND, -1);
//		
//		Date to = c.getTime();
//		
//		
//		System.out.println("getAchievementList "+from+" - "+to);
//		
//		return eao.getAchievementList(from, to);
//	}
//	
//	@Override
//	public boolean isAchivementSource(Achievement achievement, User sourceUser, Shop sourceShop, VO sourceVO, List<? extends VO> sourceVOList){
//		
//		if(achievement == null)
//			return false;
//		
//		Iterator<? extends ShopGroup> i1;
//		ShopGroup shopGroup;
//		
//		Iterator<? extends VO> i2;
//		VO vo;
//		
//		if(achievement.getSourceUserList().contains(sourceUser))
//			return true;
//		
//		if(achievement.getSourceShopList().contains(sourceShop))
//			return true;
//		
//		i1 = achievement.getSourceShopGroupList().iterator();
//		while(i1.hasNext()){
//			shopGroup = i1.next();
//			
//			if(shopGroup.getShopList().contains(sourceShop))
//				return true;
//		}
//
//		
//		i2 = achievement.getSourceVOList().iterator();
//		while(i2.hasNext()){
//			vo = i2.next();
//			
//			if(vo.equals(sourceVO))
//				return true;
//			
//			if(sourceVOList != null && sourceVOList.contains(vo))
//				return true;
//		}
//		
//		
//		return false;
//	}
//	
//	@Override
//	@Schedule( minute="0,15,30,45", hour="*", persistent=false)
//	public void calculateAllActiveAchievementGoalAttainments(){
//		
//		Date ltEffectiveDate = new Date();
//		
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DAY_OF_YEAR, -7);
//		
//		Date gtExpiryDate = c.getTime();
//		
//		try{
//			Iterator<? extends Achievement> i = eao.getActiveAchievementList(ltEffectiveDate, gtExpiryDate).iterator();
//			Achievement a;
//			while(i.hasNext()){
//				a = i.next();
//				calculateAchievementGoalAttainment(a);
//			}
//		}catch(Exception e){
//			exceptionController.logException(e, null);
//		}
//	}
//	

//	
//	
//	@Override
//	public void calculateAchievementGoalAttainment(Achievement achievement){
//		
//		if(achievement == null)
//			return;
//		
//		boolean debug = false;
//		
////		System.out.println("calculateAchievementGoalAttainment: "+achievement.getId());
//		
////		if(achievement.getId() == 41)
////			debug = true;
////		
////		if(achievement.getId() == 43)
////			debug = true;
//		
//		
//		Iterator<? extends OrderItem> orderItemIterator;
//		OrderItem orderItem;
//
//		Iterator<? extends ContractRetailItem> contractRetailItemIterator;
//		ContractRetailItem contractRetailItem;
//		
//		Iterator<? extends ContractItem> contractItemIterator;
//		ContractItem contractItem;
//
//		Iterator<? extends BaseProductCombination> productCombinationEntityIterator;
//		BaseProductCombination productCombination;
//
//		Iterator<? extends ProductOption> productOptionIterator;
//		ProductOption productOption;
//		
//		boolean foundContractItem; 
//
//		boolean tariffAlreadyProcessed;
//		List<ProductOption> alreadyProcessedProductOption;
//		
//		achievement.clearGoalAttainment();
//
//		if(debug)
//			System.out.println("A getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//		
////		System.out.println("calculateAchievementGoalAttainment: "+achievement.getId()+" "+achievement.getName());
//
////		System.out.println("get Contracts: "+achievement.getEffectiveDate()+" "+achievement.getExpiryDate());
//		List<? extends BaseContract> contractList = contractController.getContractList(null, null, null, achievement.getEffectiveDate(), achievement.getExpiryDate());
//		Iterator<? extends BaseContract> contractIterator = contractList.iterator();
//		BaseContract contract;
//		
//		contractLoop:
//		while(contractIterator.hasNext()){
//			contract = contractIterator.next();
//			
//			if(contract.isCanceled())
//				continue;
//			
//			tariffAlreadyProcessed = false;
//			alreadyProcessedProductOption = new ArrayList<ProductOption>();
//			
//			if(! isAchivementSource(achievement, contract.getUser(), contract.getShop(), contract.getVo(), null))
//				continue;
//
//			if(contract.isExtensionOfTerm() && ! achievement.isExtensionOfTheTerm())
//				continue;
//
//			if(! contract.isExtensionOfTerm() && ! achievement.isNewContract())
//				continue;
//
//			if(contract.isDebidCreditChange() && ! achievement.isDebidCreditChange())
//				continue;
//			
//			if(debug)
//				System.out.println("Contract is AchivementSource: "+contract.getId()+" "+contract.getEffectiveDate()+" - "+contract.getUser() +" - "+ contract.getShop() +" - "+ contract.getVo());
//				
//
//			if(debug)
//				System.out.println("AA getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//			
//			
//			// Product Combination Blackist
//			// if product and ALL Options are SAME -> SKIP
//			productCombinationEntityIterator = achievement.getSelectedProductCombinationFilterList().iterator();
//			productCombinationLoop:
//			while(productCombinationEntityIterator.hasNext()){
//				
////				System.out.println("Check Product : "+contract.getEffectiveDate()+" - "+contract.getUser() +" - "+ contract.getShop() +" - "+ contract.getVo());
//				
//				productCombination = productCombinationEntityIterator.next();
//				
//				
//				// Check Tarif
//				if(! productCombination.getProduct().equals(contract.getCachedTariff().getBaseProduct()))
//					continue;
//				
//				// Check ProductOption count
//				if(productCombination.getProductOptionList().size() != contract.getCachedTariffOptionList().size())
//					continue;
//				
//
//				
//				// Check ProductOptions
//				productOptionIterator = productCombination.getProductOptionList().iterator();
//				while(productOptionIterator.hasNext()){
//					productOption = productOptionIterator.next();
//					
//					// search ProductOption in CachedTariffOptionList
//					contractItemIterator = contract.getCachedTariffOptionList().iterator();
//					foundContractItem = false;
//					while(contractItemIterator.hasNext()){
//						contractItem = contractItemIterator.next();
//						
//						if(contractItem.getBaseProduct() == null)
//							continue productCombinationLoop;
//						
//						// Produkt Option found
//						if(productOption.equals(contractItem.getBaseProduct())){
//							foundContractItem = true;
//							break;
//						}
//					}
//					
//					// Produkt Option not found
//					if(!foundContractItem)
//						continue productCombinationLoop;
//				}
//				
//				// Product ans All of its Options ARE SAME -> SKIP Contract
//				continue contractLoop;
//					
//			}
//			
//
//			if(debug)
//				System.out.println("B getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//
//			// Product Combination White
//			// if product and ALL Options found -> process
//			productCombinationEntityIterator = achievement.getSelectedProductCombinationList().iterator();
//			productCombinationLoop:
//			while(productCombinationEntityIterator.hasNext()){
//				
//				productCombination = productCombinationEntityIterator.next();
//				
//				// Check Tarif
//				if(! productCombination.getProduct().equals(contract.getCachedTariff().getBaseProduct()))
//					continue;
//				
//				// Check ProductOptions
//				productOptionIterator = productCombination.getProductOptionList().iterator();
//				while(productOptionIterator.hasNext()){
//					productOption = productOptionIterator.next();
//					
//					// search ProductOption in CachedTariffOptionList
//					contractItemIterator = contract.getCachedTariffOptionList().iterator();
//					foundContractItem = false;
//					while(contractItemIterator.hasNext()){
//						contractItem = contractItemIterator.next();
//						
//						if(contractItem.getBaseProduct() == null)
//							continue productCombinationLoop;
//						
//						// Produkt Option found
//						if(productOption.equals(contractItem.getBaseProduct())){
//							foundContractItem = true;
//							break;
//						}
//					}
//					
//					// Produkt Option not found
//					if(!foundContractItem)
//						continue productCombinationLoop;
//				}
//				
//				// Product ans All of its Options found -> SKIP Contract
//				tariffAlreadyProcessed = true;
//				
//				
//				achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
//				
//				if(! contract.isExtensionOfTerm()){
//					achievement.addAquiredRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
//				}else{
//					achievement.addContractedRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
//				}
//				
//				if(contract.getCachedTariff().getBaseProduct() != null)
//					achievement.addSumGoalAttainment(contract.getUser(), contract.getCachedTariff().getBaseProduct().getCommission().getGrossPrice());
//				
//				// process Product Options
//				productOptionIterator = productCombination.getProductOptionList().iterator();
//				while(productOptionIterator.hasNext()){
//					productOption = productOptionIterator.next();
//					
//					alreadyProcessedProductOption.add(productOption);
//
//					achievement.addSumGoalAttainment(contract.getUser(), productOption.getCommission().getGrossPrice());
//				}
//				
//			}
//
//			if(debug)
//				System.out.println("D getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//			
//			if(debug)
//				System.out.println("check isSelectedProduct");
//			
//
//			// Base Tariff
//			if(!tariffAlreadyProcessed && isSelectedProduct(achievement, contract.getCachedTariff().getBaseProduct())){
//				
//
//				if(debug)
//					System.out.println("isSelectedProduct OK");
//				
////				System.out.println("Tarif is selected product: "+contract.getCachedTariff().getProductName());
//				achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
//				
//				if(! contract.isExtensionOfTerm()){
//					achievement.addAquiredRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
//				}else{
//					achievement.addContractedRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
//				}
//				
//				if(contract.getCachedTariff().getBaseProduct() != null)
//					achievement.addSumGoalAttainment(contract.getUser(), contract.getCachedTariff().getBaseProduct().getCommission().getGrossPrice());
//				
//			}
//			
//
//			if(debug)
//				System.out.println("E getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//			
//			// Product Options
//			contractItemIterator = contract.getCachedTariffOptionList().iterator();
//			while(contractItemIterator.hasNext()){
//				contractItem = contractItemIterator.next();
//				
//				if(contractItem.getBaseProduct() != null && alreadyProcessedProductOption.contains(contractItem.getBaseProduct()))
//					continue;
//				
//				if(isSelectedProduct(achievement, contractItem.getBaseProduct())){
////					System.out.println("Tarif Option is selected product: "+contractItem.getProductName());
//					achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
//
//					if(contractItem.getBaseProduct() != null)
//						achievement.addSumGoalAttainment(contract.getUser(), contractItem.getBaseProduct().getCommission().getGrossPrice());
//				}
//			}
//
//			if(debug)
//				System.out.println("F getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//			
//
//			// Retail Products in Contract
//			contractRetailItemIterator = contract.getContractRetailItemList().iterator();
//			while(contractRetailItemIterator.hasNext()){
//				contractRetailItem = contractRetailItemIterator.next();
//				
//				if(contractRetailItem.getProduct() == null)
//					continue;
//				
//				if(isSelectedProduct(achievement, contractRetailItem.getProduct())){
////					System.out.println("Tarif Option is selected product: "+contractItem.getProductName());
//					achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
//					
//					
//
//					if(contractRetailItem.getProduct() != null)
//						achievement.addSumGoalAttainment(contract.getUser(), contractRetailItem.getProduct().getCommission().getGrossPrice());
//				}
//			}
//			
//
//			if(debug)
//				System.out.println("G getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//				
//		}
//		
//		
//		
//		/*
//		 * PROCESS ORDER
//		 */
//
//		List<? extends Order> orderList = orderController.getOrderList(null, null, achievement.getEffectiveDate(), achievement.getExpiryDate());
//		Iterator<? extends Order> orderIterator = orderList.iterator();
//		Order order;
//		
//		orderLoop:
//		while(orderIterator.hasNext()){
//			order = orderIterator.next();
//			
//			if(! isAchivementSource(achievement, order.getUser(), order.getShop(), null, null))
//				continue;
//
////			if(order.isCanceled())
////				continue;
//			
//			// Retail Products in Order
//			orderItemIterator = order.getOrderItemList().iterator();
//			while(orderItemIterator.hasNext()){
//				orderItem = orderItemIterator.next();
//				
//				if(orderItem.getProduct() == null)
//					continue;
//				
//				
//				if(isSelectedProduct(achievement, orderItem.getProduct())){
//					achievement.addPieceGoalAttainment(order.getUser(), new BigDecimal(1));
//					
//					
//
//					if(orderItem.getProduct() != null)
//						achievement.addSumGoalAttainment(order.getUser(), orderItem.getProduct().getCommission().getGrossPrice());
//				}
//			}
//			
//		}
//		
//		if(debug)
//			System.out.println("Final getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
//		
//		achievement = this.saveAchievement(achievement);
//		
////		System.out.println("get Order: "+achievement.getEffectiveDate()+" "+achievement.getExpiryDate());
//		
//	}
//
//
//	@Override
//	public Achievement cloneAchievement(Achievement achievement) {
//		
//		Iterator<? extends AchievementTarget> i;
//		
//		AchievementEntity clone = (AchievementEntity) achievement;
//		eao.detachAchievement(clone);
//		clone.setId(0);
//		clone.setName(clone.getName()+" (Kopie)");
//		
//		clone.getUserGoalAttainmentList().clear();
//		
//		if(clone.getTotalPieceTarget() != null)
//			clone.setTotalPieceTarget( cloneAchievementTarget( clone.getTotalPieceTarget() ) );
//		
//		if(clone.getTotalSumTarget() != null)
//			clone.setTotalSumTarget( cloneAchievementTarget( clone.getTotalSumTarget() ) );
//		
//		if(clone.getTotalAquiredRevenueTarget() != null)
//			clone.setTotalAquiredRevenueTarget( cloneAchievementTarget( clone.getTotalAquiredRevenueTarget() ) );
//		
//		if(clone.getTotalContractedRevenueTarget() != null)
//			clone.setTotalContractedRevenueTarget( cloneAchievementTarget( clone.getTotalContractedRevenueTarget() ) );
//		
//		i = clone.getPieceTargetList().iterator();
//		clone.setPieceTargetList(null);
//		while(i.hasNext()){
//			((List<AchievementTarget>) clone.getPieceTargetList()).add(cloneAchievementTarget(i.next()));
//		}
//		
//		i = clone.getSumTargetList().iterator();
//		clone.setSumTargetList(null);
//		while(i.hasNext()){
//			((List<AchievementTarget>) clone.getSumTargetList()).add(cloneAchievementTarget(i.next()));
//		}
//		
//		i = clone.getAquiredRevenueTargetList().iterator();
//		clone.setAquiredRevenueTargetList(null);
//		while(i.hasNext()){
//			((List<AchievementTarget>) clone.getAquiredRevenueTargetList()).add(cloneAchievementTarget(i.next()));
//		}
//		
//		i = clone.getContractedRevenueTargetList().iterator();
//		clone.setContractedRevenueTargetList(null);
//		while(i.hasNext()){
//			((List<AchievementTarget>) clone.getContractedRevenueTargetList()).add(cloneAchievementTarget(i.next()));
//		}
//		
//		
//		return clone;
//	}
//
//	@Override
//	public AchievementTarget cloneAchievementTarget(AchievementTarget achievementTarget) {
//		
//		AchievementTargetEntity clone = (AchievementTargetEntity) achievementTarget;
//		eao.detachAchievementTarget(clone);
//		clone.setId(0);
//		
//		return clone;
//	}
}
