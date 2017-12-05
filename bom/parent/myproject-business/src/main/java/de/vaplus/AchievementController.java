package de.vaplus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.AchievementControllerInterface;
import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.OrderControllerInterface;
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
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.eao.AchievementEao;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.entity.AchievementEntity;
import de.vaplus.client.entity.AchievementTargetEntity;
import de.vaplus.client.entity.AchievementUserGoalAttainmentEntity;
import de.vaplus.client.entity.BaseProductEntity;

@Stateless
public class AchievementController implements AchievementControllerInterface{

	private static final long serialVersionUID = 2375425917464270374L;
	
	@Inject
    private AchievementEao eao;
	
	
	@EJB
	private ContractControllerInterface contractController;
	
	@EJB
	private OrderControllerInterface orderController;

	@EJB
	private ExceptionControllerInterface exceptionController;
	

	@Override
	public Achievement factoryNewAchievement() {
		AchievementEntity achievement = new AchievementEntity();
		achievement.setEnabled(true);
		return achievement;
	}

	@Override
	public AchievementTarget factoryNewAchievementTarget() {
		AchievementTargetEntity achievement = new AchievementTargetEntity();
		
		return achievement;
	}

	@Override
	public AchievementTarget factoryNewAchievementTarget(BigDecimal target, BigDecimal payout, String payoutText, BigDecimal commission) {
		AchievementTargetEntity achievement = new AchievementTargetEntity(target, payout, payoutText, commission);
		
		return achievement;
	}

	@Override
	public Achievement saveAchievement(Achievement achievement) {
		return eao.saveAchievement((AchievementEntity) achievement);
	}

	@Override
	public Achievement getActiveAchievement(long id) {
		return eao.getActiveAchievement(id);
	}

	@Override
	public List<? extends Achievement> getAchievementList() {
		return eao.getAchievementList();
	}

	@Override
	public List<? extends Achievement> getActiveAchievementList() {
		return eao.getAchievementList(new Date());
	}


	@Override
	public List<? extends Achievement> getActiveAchievementListForCompanyDashboard() {
		List<? extends Achievement> list = getActiveAchievementList();
		
		Iterator<? extends Achievement> i = list.iterator();
		while(i.hasNext()){
			if(! i.next().isShowOnCompanyDashboard())
				i.remove();
		}
		return list;
	}

	@Override
	public List<? extends Achievement> getOpenAchievementList() {
		return eao.getOpenAchievementList();
	}

	@Override
	public List<? extends Achievement> getClosedAchievementList() {
		return eao.getClosedAchievementList();
	}

	@Override
	public List<? extends Achievement> getAchievementList(int month, int year) {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.clear(Calendar.HOUR_OF_DAY);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		
		Date from = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		
		Date to = c.getTime();
		
		
		System.out.println("getAchievementList "+from+" - "+to);
		
		return eao.getAchievementList(from, to);
	}
	
	@Override
	public boolean isAchivementSource(Achievement achievement, User sourceUser, Shop sourceShop, VO sourceVO, List<? extends VO> sourceVOList){
		
		if(achievement == null)
			return false;
		
		Iterator<? extends ShopGroup> i1;
		ShopGroup shopGroup;
		
		Iterator<? extends VO> i2;
		VO vo;
		
		if(achievement.getSourceUserList().contains(sourceUser))
			return true;
		
		if(achievement.getSourceShopList().contains(sourceShop))
			return true;
		
		i1 = achievement.getSourceShopGroupList().iterator();
		while(i1.hasNext()){
			shopGroup = i1.next();
			
			if(shopGroup.getShopList().contains(sourceShop))
				return true;
		}

		
		i2 = achievement.getSourceVOList().iterator();
		while(i2.hasNext()){
			vo = i2.next();
			
			if(vo.equals(sourceVO))
				return true;
			
			if(sourceVOList != null && sourceVOList.contains(vo))
				return true;
		}
		
		
		return false;
	}
	
	@Override
	@Schedule( minute="0,15,30,45", hour="*", persistent=false)
	public void calculateAllActiveAchievementGoalAttainments(){
//		System.out.println("@Schedule START calculateAllActiveAchievementGoalAttainments");
		
		Date ltEffectiveDate = new Date();
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -7);
		
		Date gtExpiryDate = c.getTime();
		
		try{
			Iterator<? extends Achievement> i = eao.getActiveAchievementList(ltEffectiveDate, gtExpiryDate).iterator();
			Achievement a;
			while(i.hasNext()){
				a = i.next();
				calculateAchievementGoalAttainment(a);
			}
		}catch(Exception e){
			exceptionController.logException(e, null);
		}
//		System.out.println("@Schedule END calculateAllActiveAchievementGoalAttainments");
	}
	
	private boolean isSelectedProduct(Achievement achievement, BaseProduct product){
		
		if(achievement == null || product == null)
			return false;
		
		if(achievement.getSelectedProductFilterList().contains(product))
			return false;
		
		if(achievement.getSelectedProductList().contains(product))
			return true;
		
		Iterator<? extends ProductCategory> i = achievement.getSelectedProductCategoryList().iterator();
		ProductCategory category;
		while(i.hasNext()){
			category = i.next();
			
			if(product.getProductCategory().equals(category))
				return true;
			
		}
		
		return false;
	}
	
	
	@Override
	public void calculateAchievementGoalAttainment(Achievement achievement){
		
		if(achievement == null)
			return;
		
		boolean debug = false;
		
		System.out.println("calculateAchievementGoalAttainment: "+achievement.getId());
		
		if(achievement.getId() == 158)
			debug = true;
//		
//		if(achievement.getId() == 43)
//			debug = true;
		
		
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
		
		boolean foundContractItem; 

		boolean tariffAlreadyProcessed;
		List<ProductOption> alreadyProcessedProductOption;
		
		achievement.clearGoalAttainment();

		if(debug)
			System.out.println("A getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
		
		if(debug)
			System.out.println("calculateAchievementGoalAttainment: "+achievement.getId()+" "+achievement.getName());

		if(debug)
			System.out.println("get Contracts: "+achievement.getEffectiveDate()+" "+achievement.getExpiryDate());
		
		List<? extends BaseContract> contractList = contractController.getContractList(null, null, null, achievement.getEffectiveDate(), achievement.getExpiryDate());
		
		if(debug)
			System.out.println("got "+contractList.size()+" Contracts");
		
		Iterator<? extends BaseContract> contractIterator = contractList.iterator();
		BaseContract contract;
		
		contractLoop:
		while(contractIterator.hasNext()){
			contract = contractIterator.next();
			
			if(debug)
				System.out.println("check Contract "+contract.getId());
			
			if(contract.isCanceled()){
				if(debug)
					System.out.println("Contract is canceled");
				continue;
			}
			
			tariffAlreadyProcessed = false;
			alreadyProcessedProductOption = new ArrayList<ProductOption>();
			
			if(! isAchivementSource(achievement, contract.getUser(), contract.getShop(), contract.getVo(), null)){
				if(debug)
					System.out.println("Contract is not Achivement Source");
				continue;
			}

			if(contract.isExtensionOfTerm() && ! achievement.isExtensionOfTheTerm()){
				if(debug)
					System.out.println("Contract is ExtensionOfTerm and achievement is not");
				continue;
			}

			if(contract.isDebidCreditChange() && ! achievement.isDebidCreditChange()){
				if(debug)
					System.out.println("Contract is DC Change and achievement is not");
				continue;
			}

			if(! contract.isExtensionOfTerm() && ! contract.isDebidCreditChange() &&  ! achievement.isNewContract()){
				if(debug)
					System.out.println("Contract is new Contract achievement is not");
				continue;
			}
			
			if(debug)
				System.out.println("Contract is AchivementSource: "+contract.getId()+" "+contract.getEffectiveDate()+" - "+contract.getUser() +" - "+ contract.getShop() +" - "+ contract.getVo());
				

			if(debug){
				System.out.println("AA getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
				System.out.println("AA getSumGoalAttainment: "+achievement.getSumGoalAttainment());
				System.out.println("AA getAquiredRevenueGoalAttainment: "+achievement.getAquiredRevenueGoalAttainment());
				System.out.println("AA getContractedRevenueGoalAttainment: "+achievement.getContractedRevenueGoalAttainment());
			}
			
			
			// Product Combination Blackist
			// if product and ALL Options are SAME -> SKIP
			productCombinationEntityIterator = achievement.getSelectedProductCombinationFilterList().iterator();
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
				
				// Product and All of its Options ARE SAME -> SKIP Contract
				continue contractLoop;
					
			}
			

			if(debug){
				System.out.println("B getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
				System.out.println("B getSumGoalAttainment: "+achievement.getSumGoalAttainment());
				System.out.println("B getAquiredRevenueGoalAttainment: "+achievement.getAquiredRevenueGoalAttainment());
				System.out.println("B getContractedRevenueGoalAttainment: "+achievement.getContractedRevenueGoalAttainment());
			}

			// Product Combination White
			// if product and ALL Options found -> process
			productCombinationEntityIterator = achievement.getSelectedProductCombinationList().iterator();
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
				
				// Product and All of its Options found -> SKIP Contract
				tariffAlreadyProcessed = true;
				
				
				achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
				

				
				
				if(! contract.isExtensionOfTerm()){
					achievement.addAquiredRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
				}else{
					achievement.addContractedRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
				}
				
				if(contract.getCachedTariff().getBaseProduct() != null)
					achievement.addSumGoalAttainment(contract.getUser(), contract.getCachedTariff().getBaseProduct().getCommission().getGrossPrice());
				
				// process Product Options
				productOptionIterator = productCombination.getProductOptionList().iterator();
				while(productOptionIterator.hasNext()){
					productOption = productOptionIterator.next();
					
					alreadyProcessedProductOption.add(productOption);

					achievement.addSumGoalAttainment(contract.getUser(), productOption.getCommission().getGrossPrice());
				}
				
			}

			if(debug){
				System.out.println("D getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
				System.out.println("D getSumGoalAttainment: "+achievement.getSumGoalAttainment());
				System.out.println("D getAquiredRevenueGoalAttainment: "+achievement.getAquiredRevenueGoalAttainment());
				System.out.println("D getContractedRevenueGoalAttainment: "+achievement.getContractedRevenueGoalAttainment());
			}
			
			if(debug)
				System.out.println("check isSelectedProduct");
			

			// Base Tariff
			if(!tariffAlreadyProcessed && isSelectedProduct(achievement, contract.getCachedTariff().getBaseProduct())){
				

				if(debug)
					System.out.println("isSelectedProduct OK");
				
				if(debug){
					System.out.println("Add Sum for Base Tariff");
					
					if(! contract.isExtensionOfTerm()){
						System.out.println("Add AU "+contract.getGeneratedRevenue());
					}else{
						System.out.println("Add GU "+contract.getGeneratedRevenue());
						achievement.addContractedRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
					}
					
					if(contract.getCachedTariff().getBaseProduct() != null)
						System.out.println("Add Sum "+contract.getCachedTariff().getBaseProduct().getCommission().getGrossPrice());
				}
				
//				System.out.println("Tarif is selected product: "+contract.getCachedTariff().getProductName());
				achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
				
				if(! contract.isExtensionOfTerm()){
					achievement.addAquiredRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
				}else{
					achievement.addContractedRevenueGoalAttainment(contract.getUser(), contract.getGeneratedRevenue());
				}
				
				if(contract.getCachedTariff().getBaseProduct() != null)
					achievement.addSumGoalAttainment(contract.getUser(), contract.getCachedTariff().getBaseProduct().getCommission().getGrossPrice());
				
			}
			

			if(debug){
				System.out.println("E getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
				System.out.println("E getSumGoalAttainment: "+achievement.getSumGoalAttainment());
				System.out.println("E getAquiredRevenueGoalAttainment: "+achievement.getAquiredRevenueGoalAttainment());
				System.out.println("E getContractedRevenueGoalAttainment: "+achievement.getContractedRevenueGoalAttainment());
			}
			
			// Product Options
			contractItemIterator = contract.getCachedTariffOptionList().iterator();
			while(contractItemIterator.hasNext()){
				contractItem = contractItemIterator.next();
				
				if(contractItem.getBaseProduct() != null && alreadyProcessedProductOption.contains(contractItem.getBaseProduct()))
					continue;
				
				if(isSelectedProduct(achievement, contractItem.getBaseProduct())){
//					System.out.println("Tarif Option is selected product: "+contractItem.getProductName());
					achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));

					if(contractItem.getBaseProduct() != null)
						achievement.addSumGoalAttainment(contract.getUser(), contractItem.getBaseProduct().getCommission().getGrossPrice());
				}
			}

			if(debug){
				System.out.println("F getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
				System.out.println("F getSumGoalAttainment: "+achievement.getSumGoalAttainment());
				System.out.println("F getAquiredRevenueGoalAttainment: "+achievement.getAquiredRevenueGoalAttainment());
				System.out.println("F getContractedRevenueGoalAttainment: "+achievement.getContractedRevenueGoalAttainment());
			}
			

			// Retail Products in Contract
			contractRetailItemIterator = contract.getContractRetailItemList().iterator();
			while(contractRetailItemIterator.hasNext()){
				contractRetailItem = contractRetailItemIterator.next();
				
				if(contractRetailItem.getProduct() == null)
					continue;
				
				if(isSelectedProduct(achievement, contractRetailItem.getProduct())){
//					System.out.println("Tarif Option is selected product: "+contractItem.getProductName());
					achievement.addPieceGoalAttainment(contract.getUser(), new BigDecimal(1));
					
					

					if(contractRetailItem.getProduct() != null)
						achievement.addSumGoalAttainment(contract.getUser(), contractRetailItem.getProduct().getCommission().getGrossPrice());
				}
			}
			

			if(debug){
				System.out.println("G getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
				System.out.println("G getSumGoalAttainment: "+achievement.getSumGoalAttainment());
				System.out.println("G getAquiredRevenueGoalAttainment: "+achievement.getAquiredRevenueGoalAttainment());
				System.out.println("G getContractedRevenueGoalAttainment: "+achievement.getContractedRevenueGoalAttainment());
			}
				
		}
		
		
		
		/*
		 * PROCESS ORDER
		 */

		List<? extends Order> orderList = orderController.getOrderList(null, null, achievement.getEffectiveDate(), achievement.getExpiryDate());
		Iterator<? extends Order> orderIterator = orderList.iterator();
		Order order;
		
		orderLoop:
		while(orderIterator.hasNext()){
			order = orderIterator.next();
			
			if(! isAchivementSource(achievement, order.getUser(), order.getShop(), null, null))
				continue;

//			if(order.isCanceled())
//				continue;
			
			// Retail Products in Order
			orderItemIterator = order.getOrderItemList().iterator();
			while(orderItemIterator.hasNext()){
				orderItem = orderItemIterator.next();
				
				if(orderItem.getProduct() == null)
					continue;
				
				
				if(isSelectedProduct(achievement, orderItem.getProduct())){
					achievement.addPieceGoalAttainment(order.getUser(), new BigDecimal(1));
					
					

					if(orderItem.getProduct() != null)
						achievement.addSumGoalAttainment(order.getUser(), orderItem.getProduct().getCommission().getGrossPrice());
				}
			}
			
		}
		
		if(debug)
			System.out.println("Final getPieceGoalAttainment: "+achievement.getPieceGoalAttainment());
		
		achievement = this.saveAchievement(achievement);
		
//		System.out.println("get Order: "+achievement.getEffectiveDate()+" "+achievement.getExpiryDate());
		
	}


	@Override
	public Achievement cloneAchievement(Achievement achievement) {
		
		Iterator<? extends AchievementTarget> i;
		
		AchievementEntity clone = (AchievementEntity) achievement;
		eao.detachAchievement(clone);
		clone.setId(0);
		clone.setName(clone.getName()+" (Kopie)");
		
		clone.getUserGoalAttainmentList().clear();
		
		if(clone.getTotalPieceTarget() != null)
			clone.setTotalPieceTarget( cloneAchievementTarget( clone.getTotalPieceTarget() ) );
		
		if(clone.getTotalSumTarget() != null)
			clone.setTotalSumTarget( cloneAchievementTarget( clone.getTotalSumTarget() ) );
		
		if(clone.getTotalAquiredRevenueTarget() != null)
			clone.setTotalAquiredRevenueTarget( cloneAchievementTarget( clone.getTotalAquiredRevenueTarget() ) );
		
		if(clone.getTotalContractedRevenueTarget() != null)
			clone.setTotalContractedRevenueTarget( cloneAchievementTarget( clone.getTotalContractedRevenueTarget() ) );
		
		i = clone.getPieceTargetList().iterator();
		clone.setPieceTargetList(null);
		while(i.hasNext()){
			((List<AchievementTarget>) clone.getPieceTargetList()).add(cloneAchievementTarget(i.next()));
		}
		
		i = clone.getSumTargetList().iterator();
		clone.setSumTargetList(null);
		while(i.hasNext()){
			((List<AchievementTarget>) clone.getSumTargetList()).add(cloneAchievementTarget(i.next()));
		}
		
		i = clone.getAquiredRevenueTargetList().iterator();
		clone.setAquiredRevenueTargetList(null);
		while(i.hasNext()){
			((List<AchievementTarget>) clone.getAquiredRevenueTargetList()).add(cloneAchievementTarget(i.next()));
		}
		
		i = clone.getContractedRevenueTargetList().iterator();
		clone.setContractedRevenueTargetList(null);
		while(i.hasNext()){
			((List<AchievementTarget>) clone.getContractedRevenueTargetList()).add(cloneAchievementTarget(i.next()));
		}
		
		
		return clone;
	}

	@Override
	public AchievementTarget cloneAchievementTarget(AchievementTarget achievementTarget) {
		
		AchievementTargetEntity clone = (AchievementTargetEntity) achievementTarget;
		eao.detachAchievementTarget(clone);
		clone.setId(0);
		
		return clone;
	}
}
