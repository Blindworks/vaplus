package de.vaplus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.entity.AquiredRevenueLevel;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.CellPhoneTariff;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.ContractedRevenueLevel;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.PrePaidTariff;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.VOTypeCommissionRevenueLevel;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.eao.CommissionEao;
import de.vaplus.client.eao.ShopEao;
import de.vaplus.client.eao.UserEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.CommissionCacheEntity;
import de.vaplus.client.entity.ContractItemEntity;
import de.vaplus.client.entity.ProductCommissionVOTypeEntity;
import de.vaplus.client.entity.ProductCommissionVOTypeProductRelationEntity;
import de.vaplus.client.entity.ProductOptionEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.VOEntity;
import de.vaplus.client.entity.commission.AquiredRevenueLevelEntity;
import de.vaplus.client.entity.commission.ContractedRevenueLevelEntity;
import de.vaplus.client.entity.commission.RevenueLevelEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import de.vaplus.helper.TaxHelper;


@Stateless
public class CommissionController implements CommissionControllerInterface {

	private static final long serialVersionUID = -5792665690268719074L;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private ExceptionControllerInterface exceptionController;
	
	@Inject
    private CommissionEao commissionEao;
	
	@Inject
    private VOEao voEao;
	
	@Inject
    private UserEao userEao;
	
	@Inject
    private ShopEao shopEao;

	@Override
	public void updateUserComissionHistory(User user){
		
		System.out.println("updateUserComissionHistory: "+user.getName() );
		
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();

		c.setTime(new Date());
		int currentMonth = c.get(Calendar.MONTH);
		int currentYear = c.get(Calendar.YEAR);
		
		c.setTime(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Date startOfMonth = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 59);
		Date endOfMonth = c.getTime();


		c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.MONTH, 1);

		Date startNextOfMonth = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		Date endOfNextMonth = c.getTime();

		Commissionable liveCommission = commissionEao.getUserCommissionSum(user, startOfMonth, now);
		
		Commissionable commission = commissionEao.getUserCommissionSum(user, startOfMonth, endOfMonth);
		
		Commissionable nextCommission = commissionEao.getUserCommissionSum(user, startNextOfMonth, endOfNextMonth);

		CommissionCache commissionCache = user.getCurrentCommissionCache();
		commissionCache.setPointGoal(user.getPointGoal());
		commissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, null, user, startOfMonth, endOfMonth));
		commissionCache.setNewContractSum(contractController.getNewContractSum(null, null, user, startOfMonth, endOfMonth));
		commissionCache.setCommission(commission);
		
		
		CommissionCache liveCommissionCache = user.getLiveCommissionCache();
		liveCommissionCache.setPointGoal(user.getPointGoal());
		liveCommissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, null, user, startOfMonth, now));
		liveCommissionCache.setNewContractSum(contractController.getNewContractSum(null, null, user, startOfMonth, now));
		liveCommissionCache.setCommission(liveCommission);
		liveCommissionCache.setMonth(currentMonth);
		liveCommissionCache.setYear(currentYear);
		
		CommissionCache nextCommissionCache = user.getNextCommissionCache();
		nextCommissionCache.setPointGoal(user.getPointGoal());
		nextCommissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, null, user, startNextOfMonth, endOfNextMonth));
		nextCommissionCache.setNewContractSum(contractController.getNewContractSum(null, null, user, startNextOfMonth, endOfNextMonth));
		nextCommissionCache.setCommission(nextCommission);
		
		user = userEao.saveUser((UserEntity) user);
	}
	
	@Override
	public void updateUserCommissionCache(User user, int month, int year){
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		
		Date startOfMonth = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);

		Date endOfMonth = c.getTime();
		
		Commissionable commission = commissionEao.getUserCommissionSum(user, startOfMonth, endOfMonth);
		CommissionCache commissionCache = user.getCommissionCache(year, month);
		commissionCache.setCommission(commission);
		commissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, null, user, startOfMonth, endOfMonth));
		
		user = userEao.saveUser((UserEntity) user);
	}
	
	@Override
	public void updateShopComissionHistory(Shop shop){

		System.out.println("updateShopComissionHistory: "+shop.getName() );
		
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(new Date());
		int currentMonth = c.get(Calendar.MONTH);
		int currentYear = c.get(Calendar.YEAR);
		
		
		c.setTime(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		Date startOfMonth = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		Date endOfMonth = c.getTime();
		

		c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.MONTH, 1);

		Date startNextOfMonth = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		Date endOfNextMonth = c.getTime();
		

		Commissionable liveCommission = commissionEao.getShopCommissionSum(shop, startOfMonth, now);

		Commissionable commission = commissionEao.getShopCommissionSum(shop, startOfMonth, endOfMonth);
		
		Commissionable nextCommission = commissionEao.getShopCommissionSum(shop, startNextOfMonth, endOfNextMonth);

		CommissionCache commissionCache = shop.getCurrentCommissionCache();
		commissionCache.setPointGoal(shop.getPointGoal());
		commissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(shop, null, null, startOfMonth, endOfMonth));
		commissionCache.setNewContractSum(contractController.getNewContractSum(shop, null, null, startOfMonth, endOfMonth));
		commissionCache.setCommission(commission);
		

		CommissionCache liveCommissionCache = shop.getLiveCommissionCache();
		liveCommissionCache.setPointGoal(shop.getPointGoal());
		liveCommissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(shop, null, null, startOfMonth, now));
		liveCommissionCache.setNewContractSum(contractController.getNewContractSum(shop, null, null, startOfMonth, now));
		liveCommissionCache.setCommission(liveCommission);
		liveCommissionCache.setMonth(currentMonth);
		liveCommissionCache.setYear(currentYear);
		
		CommissionCache nextCommissionCache = shop.getNextCommissionCache();
		nextCommissionCache.setPointGoal(shop.getPointGoal());
		nextCommissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(shop, null, null, startNextOfMonth, endOfNextMonth));
		nextCommissionCache.setNewContractSum(contractController.getNewContractSum(shop, null, null, startNextOfMonth, endOfNextMonth));
		nextCommissionCache.setCommission(nextCommission);

		shop = shopEao.saveShop((ShopEntity) shop);
	}
	

	@Override
	public void updateVOComissionHistory(VO vo){

		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(new Date());
		int currentMonth = c.get(Calendar.MONTH);
		int currentYear = c.get(Calendar.YEAR);
		
		
		c.setTime(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		Date startOfMonth = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		Date endOfMonth = c.getTime();
		

		c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.MONTH, 1);

		Date startNextOfMonth = c.getTime();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		Date endOfNextMonth = c.getTime();
		

		Commissionable liveCommission = commissionEao.getVOCommissionSum(vo, startOfMonth, now);

		CommissionCache liveCommissionCache = vo.getLiveCommissionCache();
		liveCommissionCache.setPointGoal(vo.getPointGoal());
		liveCommissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, vo, null, startOfMonth, now));
		liveCommissionCache.setNewContractSum(contractController.getNewContractSum(null, vo, null, startOfMonth, now));
		liveCommissionCache.setCommission(liveCommission);
		liveCommissionCache.setMonth(currentMonth);
		liveCommissionCache.setYear(currentYear);

		Commissionable commission = commissionEao.getVOCommissionSum(vo, startOfMonth, endOfMonth);

		CommissionCache commissionCache = vo.getCurrentCommissionCache();
		commissionCache.setPointGoal(vo.getPointGoal());
		commissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, vo, null, startOfMonth, endOfMonth));
		commissionCache.setNewContractSum(contractController.getNewContractSum(null, vo, null, startOfMonth, endOfMonth));
		commissionCache.setCommission(commission);
		
		
		Commissionable nextCommission = commissionEao.getVOCommissionSum(vo, startNextOfMonth, endOfNextMonth);
		
		CommissionCache nextCommissionCache = vo.getNextCommissionCache();
		nextCommissionCache.setPointGoal(vo.getPointGoal());
		nextCommissionCache.setExtensionOfTheTermSum(contractController.getExtensionOfTheTermContractSum(null, vo, null, startNextOfMonth, endOfNextMonth));
		nextCommissionCache.setNewContractSum(contractController.getNewContractSum(null, vo, null, startNextOfMonth, endOfNextMonth));
		nextCommissionCache.setCommission(nextCommission);

		vo = voEao.saveVO((VOEntity) vo);
	}
	
	@Schedule( minute="0", hour="1", persistent=false)
	@Override
    public void updateAllComissionHistory() {
		System.out.println("@Schedule START updateAllComissionHistory");
		
		try{
		
		
			Iterator<UserEntity> ui = userEao.getUserList().iterator();
			while(ui.hasNext()){
				updateUserComissionHistory(ui.next());

//				System.out.println("@Schedule START updateAllComissionHistory -> updateUserComissionHistory");
			}
			
			Iterator<ShopEntity> si = shopEao.getShopList().iterator();
			while(si.hasNext()){
				updateShopComissionHistory(si.next());

//				System.out.println("@Schedule START updateAllComissionHistory -> updateShopComissionHistory");
			}
			
			Iterator<? extends VO> vi = voEao.getVOList().iterator();
			while(vi.hasNext()){
				updateVOComissionHistory(vi.next());

//				System.out.println("@Schedule START updateAllComissionHistory -> updateVOComissionHistory");
			}
		
		}catch(Exception e){
			exceptionController.logException(e, null);
		}

		System.out.println("@Schedule END updateAllComissionHistory");
    }
	
	public static CommissionCache getCommissionCache(List<? extends CommissionCache> commissionCacheList, int year, int month){

		Iterator<? extends CommissionCache> it = commissionCacheList.iterator();
		CommissionCache commission = null;
		while(it.hasNext()){
			commission = it.next();
			
			if(commission.getYear() != year )
				continue;
			
			if(commission.getMonth() != month )
				continue;
			
			return commission;
		}
		
		return null;
	}
	
	public static CommissionCache[] getCommissionArray(List<? extends CommissionCache> commissionCacheList, int year){
		
		CommissionCache[] commissionCacheArray = new CommissionCache[12];

		Iterator<? extends CommissionCache> it = commissionCacheList.iterator();
		CommissionCache commission = null;
		while(it.hasNext()){
			commission = it.next();
			
			if(commission.getYear() != year )
				continue;

			commissionCacheArray[commission.getMonth() ] = commission;
		}
		
		for(int i = 0; i < commissionCacheArray.length; i++) {
			if(commissionCacheArray[i] == null)
				commissionCacheArray[i] = new CommissionCacheEntity();
		}
		
		return commissionCacheArray;
	}


	public static CommissionCache[] getCurrentRangeCommissionCacheArray(List<? extends CommissionCache> commissionCacheList){
			
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -9);
			
			// create Rage
			String[] range = new String[12];

			for(int i = 0; i < range.length; i++) {
				
				range[i] = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH));
				c.add(Calendar.MONTH, 1);
				
			}
			
			
			
			CommissionCache[] commissionCacheArray = new CommissionCache[12];

			Iterator<? extends CommissionCache> it = commissionCacheList.iterator();
			CommissionCache commission = null;
			String dateString;
			while(it.hasNext()){
				commission = it.next();
				
				dateString = String.valueOf(commission.getYear()) + String.valueOf(commission.getMonth());
				
				if(! Arrays.asList(range).contains(dateString))
					continue;

				commissionCacheArray[ Arrays.asList(range).indexOf(dateString) ] = commission;
			}
			
			for(int i = 0; i < commissionCacheArray.length; i++) {
				if(commissionCacheArray[i] == null){
					commissionCacheArray[i] = new CommissionCacheEntity();

					commissionCacheArray[i].setYear(Integer.valueOf(  range[i].substring(0, 4)  ));
					commissionCacheArray[i].setMonth(Integer.valueOf(  range[i].substring(4)  ));
				}
			}
			
			return commissionCacheArray;
	}

	@Override
	public List<? extends RevenueLevel> getRevenueLevelList() {
		return commissionEao.getRevenueLevelList();
	}

	@Override
	public RevenueLevel factoryNewAquiredRevenueLevel() {
		return new AquiredRevenueLevelEntity();
	}

	@Override
	public RevenueLevel factoryNewContractedRevenueLevel() {
		return new ContractedRevenueLevelEntity();
	}

	@Override
	public RevenueLevel saveRevenueLevel(RevenueLevel selectedRevenueLevel) {
		return commissionEao.saveRevenueLevel((RevenueLevelEntity) selectedRevenueLevel);
	}

	@Override
	public void deleteRevenueLevel(RevenueLevel revenueLevel) {
		commissionEao.deleteRevenueLevel((RevenueLevelEntity) revenueLevel);
	}

	@Override
	public List<? extends AquiredRevenueLevel> getAquiredRevenueLevelList(Vendor vendor) {
		return commissionEao.getAquiredRevenueLevelList(vendor);
	}

	@Override
	public List<? extends ContractedRevenueLevel> getContractedRevenueLevelList(Vendor vendor) {
		return commissionEao.getContractedRevenueLevelList(vendor);
	}


	@Override
	public AquiredRevenueLevel getAquiredRevenueLevel(BigDecimal revenue, BigDecimal tax, Vendor vendor) {

		BigDecimal grossRevenue = TaxHelper.addTax(revenue, tax);
		
		return commissionEao.getAquiredRevenueLevel(grossRevenue.setScale(2, RoundingMode.HALF_UP), vendor);
	}

	@Override
	public ContractedRevenueLevel getContractedRevenueLevel(BigDecimal revenue, BigDecimal tax, Vendor vendor) {
		
		BigDecimal grossRevenue = TaxHelper.addTax(revenue, tax);
		
		return commissionEao.getContractedRevenueLevel(grossRevenue.setScale(2, RoundingMode.HALF_UP), vendor);
	}
	
	@Override
	public BigDecimal calculatePointsPerProfit(BigDecimal profit){
		
		BigDecimal pointsPerCommission = propertyController.getDecimalProperty("pointsPerProfit", new BigDecimal(0));

		if(profit == null || pointsPerCommission == null)
			return new BigDecimal(0);
		
		
		return pointsPerCommission.multiply(profit).setScale(2, RoundingMode.HALF_UP);
	}
	
	@Override
	public BigDecimal calculatePoints(BigDecimal commission, VOType votype){
		
		BigDecimal pointsPerCommission = votype.getPointsPerCommission();

		if(commission == null || pointsPerCommission == null)
			return new BigDecimal(0);
		
		
		return pointsPerCommission.multiply(commission).setScale(2, RoundingMode.HALF_UP);
	}
	
	@Override
	public BigDecimal calculateRevenuePoints(BigDecimal commission, VOTypeCommissionRevenueLevel revenueLevel){
		
		
		BigDecimal points = revenueLevel.getPoints();
		
		if(points == null)
			points = calculatePoints(commission, revenueLevel.getRevenueLevelVOType());
		
		return points;
	}
	
	
//	@Override
//	public BigDecimal calculateAquiredCommissionRevenuePoints(BigDecimal revenue, RevenueLevelVOType voType){
//
//		RevenueLevel revenueLevel;
//		
//		revenueLevel = getAquiredRevenueLevel(revenue, voType.getVendor());
//		
//		VOTypeCommissionRevenueLevel voTypeCommissionRevenueLevel = voType.getVoTypeCommissionRevenueLevel(revenueLevel);
//
//		return voTypeCommissionRevenueLevel.getCommission();
//	}
//	
//	@Override
//	public BigDecimal calculateContractedCommissionRevenuePoints(BigDecimal revenue, RevenueLevelVOType voType){
//		
//		RevenueLevel revenueLevel;
//
//		revenueLevel = getContractedRevenueLevel(revenue, voType.getVendor());
//		
//		VOTypeCommissionRevenueLevel voTypeCommissionRevenueLevel = voType.getVoTypeCommissionRevenueLevel(revenueLevel);
//		
//		return voTypeCommissionRevenueLevel.getCommission();
//	}

	@Override
	public Commissionable calculateCommission(BaseProduct product, VOType voType) {
		Commissionable commission = new CommissionableEmbeddable();

		if(voType == null){

			commission = commission.addCommissionable(product.getCommission());			
			
		}else if(voType instanceof RevenueLevelVOType){

			commission = commission.addCommissionable(product.getCommission());
			

			RevenueLevel revenueLevel = getAquiredRevenueLevel(product.getCommission().getPrice(), product.getTaxRate().getTax(), voType.getVendor());
			
			VOTypeCommissionRevenueLevel commissionRevenueLevel = ((RevenueLevelVOType)voType).getVoTypeCommissionRevenueLevel(revenueLevel);
			
			
			BigDecimal revenueCommission = commissionRevenueLevel.getCommission();
			
			BigDecimal revenuePoints = calculateRevenuePoints(revenueCommission, commissionRevenueLevel);

			commission.setPoints( commission.getPoints().add(revenuePoints)  );
			commission.setCommission( commission.getCommission().add(revenueCommission)  );
			
		}else if(voType instanceof ProductCommissionVOType){
			
			// Get Commission PUSH from product
			commission = commission.addCommissionable(product.getCommission());
			
			commission = commission.addCommissionable(getProductTypeCommission( (ProductCommissionVOType) voType, product, null, true));
			
			
		}
		
		return commission;
	}
	
	@Override
	public void calculateCommission(BaseContract contract){
		
		
		// CLEAR Commission
		contract.setFinalCommission(null);
		contract.setRevenueStepCommission(null);
		
		BigDecimal generatedRevenuebyproductOptions = new BigDecimal(0);
		
		
		// TARIFF
		if(contract.getTariff() != null){
			// CLEAR Commission
			contract.getCachedTariff().setVoCommission(null);
			contract.getCachedTariff().getVoCommission();
			
			// Set Commissionable from product
			contract.setFinalCommission( 
					contract.getFinalCommission().addCommissionable( contract.getCachedTariff().getProductCommission() ) 
			);
//			contract.getFinalCommission().setVat(contract.getFinalCommission().getVat().add(contract.getTariff().getVat()));
			
			// Revenue Level
			// Set Revenue price
			contract.getRevenueStepCommission().setTax(contract.getTariff().getTaxRate().getTax());
			if(contract.getTariff() instanceof CellPhoneTariff){
				contract.getRevenueStepCommission().setPrice(
						contract.getRevenueStepCommission().getPrice().add(contract.getCachedTariff().getProductCommission().getPrice())
					);
				contract.getRevenueStepCommission().setVat(
						contract.getRevenueStepCommission().getVat().add(contract.getCachedTariff().getProductCommission().getVat())
					);
			}
			
		}
		
		// PRODUCT OPTIONS
		Iterator<? extends ContractItem> i = null;
		ContractItem contractItem;
		
		// start with percentage price Options
		Collections.sort((List<ContractItemEntity>)contract.getCachedTariffOptionList());
		
		i =  contract.getCachedTariffOptionList().iterator();
		while(i.hasNext()){
			contractItem = i.next();

			// CELL PHONE SPECIAL
			if(contractItem.getBaseProduct() != null && ((ProductOptionEntity)contractItem.getBaseProduct()).isForceExtensionOfTheTerm()){
				contract.setExtensionOfTerm(true);
			}
			
			// CLEAR Commission
			contractItem.setVoCommission(null);
			contractItem.getVoCommission();
			
			contract.setFinalCommission( 
					contract.getFinalCommission().addCommissionable( contractItem.getProductCommission() , ((ProductOption)contractItem.getBaseProduct()).isPercentagePrice()) 
			);
			contract.getFinalCommission().setVat(contract.getFinalCommission().getVat().add(contractItem.getProductCommission().getVat()));
			
			// Revenue Level
			if(contractItem.getBaseProduct() != null && ((ProductOption)contractItem.getBaseProduct()).isRevenueCommissionRelevant()){

				contract.setRevenueStepCommission(
						contract.getRevenueStepCommission().addCommissionable(contractItem.getProductCommission(), ((ProductOption)contractItem.getBaseProduct()).isPercentagePrice())
				);
//				contract.setRevenueStepCommission(
//						contract.getRevenueStepCommission().addCommissionable(contractItem.getProductCommission(), ((ProductOption)contractItem.getBaseProduct()).isPercentagePrice())
//				);
			}else{
				
				// add Generated Revenue
				generatedRevenuebyproductOptions = generatedRevenuebyproductOptions.add(((ProductOption)contractItem.getBaseProduct()).getGeneratedRevenue());
			}

			
		}
		
//		System.out.println("contract.getFinalCommission().getPoints(): "+contract.getFinalCommission().getPoints());
		
		
		
		// add Revenue Level
		
		if(contract.getVo() != null && contract.getVo().getVoType() instanceof RevenueLevelVOType){

			contract.getRevenueStepCommission().setCommission(new BigDecimal(0));
			contract.getRevenueStepCommission().setPoints(new BigDecimal(0));
			
			
			// only cellPhoneTariff
			if(contract.getRevenueStepCommission().getPrice().compareTo(new BigDecimal(0)) != 0){
				RevenueLevel revenueLevel;
				
				if(contract.isExtensionOfTerm()){
					// extension of term
					revenueLevel = getContractedRevenueLevel(contract.getRevenueStepCommission().getPrice(), contract.getTariff().getTaxRate().getTax(), contract.getVo().getVendor());
				}
				else{
					// new contract
					revenueLevel = getAquiredRevenueLevel(contract.getRevenueStepCommission().getPrice(), contract.getTariff().getTaxRate().getTax(), contract.getVo().getVendor());
				}
				
//				System.out.println("GOT RevenueLevel: "+revenueLevel.getName());
	
				VOTypeCommissionRevenueLevel commissionRevenueLevel = ((RevenueLevelVOType) contract.getVo().getVoType()).getVoTypeCommissionRevenueLevel(revenueLevel);
				contract.getRevenueStepCommission().setCommission(commissionRevenueLevel.getCommission());

//				System.out.println("GOT VOTypeCommissionRevenueLevel Points: "+commissionRevenueLevel.getPoints());
				
				contract.getRevenueStepCommission().setPoints(
					calculateRevenuePoints(contract.getRevenueStepCommission().getCommission(), commissionRevenueLevel)
				);
				

//				System.out.println("contract.getRevenueStepCommission().getPoints(): "+contract.getRevenueStepCommission().getPoints());
				
				// ADD Points and Commission to FinalCommission
				contract.getFinalCommission().setPoints(
						contract.getFinalCommission().getPoints().add(
								contract.getRevenueStepCommission().getPoints()
						)
				);
				contract.getFinalCommission().setCommission(
						contract.getFinalCommission().getCommission().add(
								contract.getRevenueStepCommission().getCommission()
						)
				);
			}
			
		}
		
		// add Product VOType Commission
		else if(contract.getVo() != null && contract.getVo().getVoType() instanceof ProductCommissionVOType){
			
			
			ProductCommissionVOTypeProductRelation voCommission = null;
			if(contract.getCachedTariff() != null){

				// try to get best VOType Commission
				voCommission = getProductCommissionVOTypeProductRelation(
						(ProductCommissionVOType) contract.getVo().getVoType(),
						contract.getCachedTariff(),
						contract.getCachedTariffOptionList(),
						false
				);
				
				if(voCommission != null){
				
					contract.getCachedTariff().setVoCommission(
							voCommission.getCommission()
					);
					
					contract.setSubsidyBudgetary(voCommission.getSubsidyBudgetary());
					
	
					// ADD Points and Commission to FinalCommission
					contract.getFinalCommission().setPoints(
							contract.getFinalCommission().getPoints().add(
									contract.getCachedTariff().getVoCommission().getPoints()
							)
					);
					contract.getFinalCommission().setCommission(
							contract.getFinalCommission().getCommission().add(
									contract.getCachedTariff().getVoCommission().getCommission()
							)
					);
					
					
					i =  contract.getCachedTariffOptionList().iterator();
					while(i.hasNext()){
						contractItem = i.next();
						
						// check if Option already in VOType Commission
						if(voCommission != null && voCommission.getProductOptionList().contains(contractItem.getBaseProduct())){
							continue;
						}
						
						
						contractItem.setVoCommission(
								getProductTypeCommission( (ProductCommissionVOType) contract.getVo().getVoType(), contractItem.getBaseProduct(), null, true)
						);
						
						// ADD Points and Commission to FinalCommission
						contract.getFinalCommission().setPoints(
								contract.getFinalCommission().getPoints().add(
										contractItem.getVoCommission().getPoints()
								)
						);
						contract.getFinalCommission().setCommission(
								contract.getFinalCommission().getCommission().add(
										contractItem.getVoCommission().getCommission()
								)
						);
					}
					
					
				}
			}

			

			
		}
		
		// generated Revenue
		contract.setGeneratedRevenue(new BigDecimal(0));
		
		
		if(contract.getTariff() != null){
			
			if(contract.getTariff() instanceof PrePaidTariff){
				contract.setGeneratedRevenue(
						((PrePaidTariff) contract.getTariff()).getGeneratedRevenue()
				);
			}
			else if(contract.getTariff().getMinimumTermOfContract() > 0){
				contract.setGeneratedRevenue( 
					contract.getFinalCommission().getGrossPrice().setScale(2, RoundingMode.HALF_UP).multiply( 
							new BigDecimal( contract.getTariff().getMinimumTermOfContract() )
					) 
				);
			}
		}
		
		contract.setGeneratedRevenue(
				contract.getGeneratedRevenue().add(generatedRevenuebyproductOptions)
		);
		
		
		// Special User Points
		BigDecimal userPoints = calculateUserPointsPerCommission(contract.getUser(), contract.getFinalCommission().getCommission());
		if(userPoints != null && userPoints.compareTo(new BigDecimal(0)) > 0){
			
//			System.out.println("Special User Points: "+userPoints);
			
			contract.getFinalCommission().setPoints(userPoints);
		}
		
	}

	

	@Override
	public void calculateCommission(Order order){
		
		
		// CLEAR Commission
		order.setCommission(null);

		// PRODUCT OPTIONS
		Iterator<? extends OrderItem> i = null;
		OrderItem orderItem;

		i =  order.getOrderItemList().iterator();
		while(i.hasNext()){
			orderItem = i.next();

			
			if(orderItem.getProduct() != null){

				// Clear commission
				orderItem.setCommission(null);
	
				// Calculate Sum
				orderItem.getCommission().setPrice(orderItem.getUnitPrice().multiply(orderItem.getQuantity()));
				
				// Calculate Profit
				orderItem.getCommission().setCommission(orderItem.getUnitPrice().subtract(orderItem.getProduct().getPurchasePrice() ).multiply(orderItem.getQuantity()));
				
				// Calculate Points
				BigDecimal userPoints = calculateUserPointsPerCommission(order.getUser(), orderItem.getCommission().getCommission());
				if(userPoints != null && userPoints.compareTo(new BigDecimal(0)) > 0){
					// Special User Points
					orderItem.getCommission().setPoints(userPoints);
				}else{
					orderItem.getCommission().setPoints(this.calculatePointsPerProfit( orderItem.getCommission().getCommission()));
				}
				
				// Add Product Points "PUSH"
				orderItem.getCommission().setPoints(orderItem.getCommission().getPoints().add(orderItem.getProduct().getCommission().getPoints()));

				// Tax
				orderItem.getCommission().setTax(orderItem.getProduct().getTaxRate().getTax());
				
			}

			order.setCommission(order.getCommission().addCommissionable(orderItem.getCommission()));

			// VAT
			if(orderItem.getCommission().getTax() != null){
				order.getCommission().setVat( order.getCommission().getVat().add( TaxHelper.getVatFromNetPrice(orderItem.getCommission().getPrice(), orderItem.getCommission().getTax()) ) );
			}
			else{
				order.getCommission().setVat( order.getCommission().getVat().add(orderItem.getCommission().getVat()) );
			}
		}

		
		// Special User Points
		BigDecimal userPoints = calculateUserPointsPerCommission(order.getUser(), order.getCommission().getCommission());
		if(userPoints != null && userPoints.compareTo(new BigDecimal(0)) > 0){
			order.getCommission().setPoints(userPoints);
		}
	}



	@Override
	public void calculateCommission(Invoice invoice){
		
		
		
		if(invoice.isCancelation()){
//			invoice.setCommission(invoice.getCommission().addCommissionable(invoice.getCanceledinvoice().getCommission()));
//			invoice.getCommission().invert();
			return;
		}

		// CLEAR Commission
		invoice.setCommission(null);

		// PRODUCT OPTIONS
		Iterator<? extends InvoiceItem> i = null;
		InvoiceItem invoiceItem;

		i =  invoice.getInvoiceItemList().iterator();
		while(i.hasNext()){
			invoiceItem = i.next();
			if(invoiceItem.getProduct() != null){

				// Clear commission
				invoiceItem.setCommission(null);
	
				// Calculate Sum
				invoiceItem.getCommission().setPrice(invoiceItem.getUnitPrice().multiply(invoiceItem.getQuantity()));
				
				// Calculate Profit
				invoiceItem.getCommission().setCommission(invoiceItem.getUnitPrice().subtract(invoiceItem.getProduct().getAvergePurchasePrice() ).multiply(invoiceItem.getQuantity()));
				
				// Calculate Points
				BigDecimal userPoints = calculateUserPointsPerCommission(invoice.getUser(), invoiceItem.getCommission().getCommission());
				if(userPoints != null && userPoints.compareTo(new BigDecimal(0)) > 0){
					// Special User Points
					invoiceItem.getCommission().setPoints(userPoints);
				}else{
					invoiceItem.getCommission().setPoints(this.calculatePointsPerProfit( invoiceItem.getCommission().getCommission()));
				}
				
				// Add Product Points "PUSH"
				invoiceItem.getCommission().setPoints(invoiceItem.getCommission().getPoints().add(invoiceItem.getProduct().getCommission().getPoints()));

				// Tax
				invoiceItem.getCommission().setTax(invoiceItem.getProduct().getTaxRate().getTax());
				
				// VAT
				invoiceItem.getCommission().setVat( TaxHelper.getVatFromNetPrice(invoiceItem.getCommission().getPrice(), invoiceItem.getCommission().getTax()) );

				
			}
			
			if(invoiceItem.isManualItem()){
				// VAT
				invoiceItem.getCommission().setVat( TaxHelper.getVatFromNetPrice(invoiceItem.getCommission().getPrice(), invoiceItem.getCommission().getTax()) );

			}

			invoice.setCommission(invoice.getCommission().addCommissionable(invoiceItem.getCommission()));


//			// VAT
//			if(invoiceItem.getCommission().getTax() != null){
//				invoice.getCommission().setVat( invoice.getCommission().getVat().add( TaxHelper.getVatFromNetPrice(invoiceItem.getCommission().getPrice(), invoiceItem.getCommission().getTax()) ) );
//			}
//			else{
//				invoice.getCommission().setVat( invoice.getCommission().getVat().add(invoiceItem.getCommission().getVat()) );
//			}
		}
		
		// Special User Points
		BigDecimal userPoints = calculateUserPointsPerCommission(invoice.getUser(), invoice.getCommission().getCommission());
		if(userPoints != null && userPoints.compareTo(new BigDecimal(0)) > 0){
			invoice.getCommission().setPoints(userPoints);
		}
	}


	@Override
	public ProductCommissionVOType removeProductFromProductCommissionVOType(
			ProductCommissionVOType voType, BaseProduct product) {
		
		if(product == null || voType == null)
			return voType;
		
		ProductCommissionVOTypeEntity voTypeEntity = (ProductCommissionVOTypeEntity) voType;
		
		//check if product already in List
		Iterator<? extends ProductCommissionVOTypeProductRelation> i = voTypeEntity.getProductCommissionList().iterator();
		ProductCommissionVOTypeProductRelation relation;
		while(i.hasNext()){
			relation = i.next();
			if(relation.getProduct().equals(product)){
				i.remove();
			}
					
		}
		
		voTypeEntity = (ProductCommissionVOTypeEntity) voEao.saveVOType(voTypeEntity);
		
		
		return voTypeEntity;
	}

	@Override
	public Commissionable factoryNewCommission() {
		return new CommissionableEmbeddable();
	}

	@Override
	public ProductCommissionVOTypeProductRelation factoryNewProductCommissionVOTypeProductRelation() {
		return new ProductCommissionVOTypeProductRelationEntity();
	}
	

	
	private ProductCommissionVOTypeProductRelation getProductCommissionVOTypeProductRelation(
			ProductCommissionVOType voType, ContractItem cachedTariff,
			List<? extends ContractItem> cachedTariffOptionList,
			boolean exactMatch) {
		
		Iterator<? extends ContractItem> i = cachedTariffOptionList.iterator();
		List<ProductOption> optionList = new ArrayList<ProductOption>();
		while(i.hasNext()){
			optionList.add((ProductOption) i.next().getBaseProduct());
		}
		
		return getProductCommissionVOTypeProductRelation(voType, cachedTariff.getBaseProduct(), optionList, exactMatch);
	}
	
	@Override
	public Commissionable getProductTypeCommission(ProductCommissionVOType voType, BaseProduct product, List<? extends ProductOption> productOptionList, boolean exactMatch){

		ProductCommissionVOTypeProductRelation relation = getProductCommissionVOTypeProductRelation( (ProductCommissionVOType) voType, product, null, true);
		
		if(relation != null)
			return relation.getCommission();
		else 
			return null;
	}
	
	@Override
	public ProductCommissionVOTypeProductRelation getProductCommissionVOTypeProductRelation(ProductCommissionVOType voType, BaseProduct product, List<? extends ProductOption> productOptionList, boolean exactMatch){
		
		/*
		 * 
		 * Treffer nach genauigkeit sortieren und nur das beste returnen.
		 * 
		 */
		ProductCommissionVOTypeProductRelation bestRelation = null;
		int relationAccuracy = 0;
		
		
		Iterator<? extends ProductCommissionVOTypeProductRelation> i = voType.getProductCommissionList().iterator();
		ProductCommissionVOTypeProductRelation rel;
		int accuracy = 0;
		
		mainloop:
		while(i.hasNext()){
			rel = i.next();
			if(!rel.getProduct().equals(product)){

				continue;
			}
			
			// empty productOptionList fast forward
			if(productOptionList == null || productOptionList.size() == 0){
				
				// relation do have options and parameter dont
				if(rel.getProductOptionList() != null && rel.getProductOptionList().size() > 0){
					continue;
				}
				
				// both relation and parameter do not have any options
				// BEST match
				return rel;
			}
			

			Iterator<? extends ProductOption> i2 = rel.getProductOptionList().iterator();
			ProductOption option;
			while(i2.hasNext()){
				option = i2.next();
				if(!productOptionList.contains(option)){
					continue mainloop;
				}
			}

			accuracy = productOptionList.size() - rel.getProductOptionList().size();
			
			// found matching
			if(bestRelation == null){
				bestRelation = rel;
				relationAccuracy = accuracy;
			}
			else if(relationAccuracy > accuracy){
				bestRelation = rel;
				relationAccuracy = accuracy;
			}
			
		}
		
		if(exactMatch){
			if(relationAccuracy == 0)
				return bestRelation;
			else
				return null;
		}

		
		return bestRelation;
	}
	
	@Override
	public ProductCommissionVOTypeProductRelation saveProductCommissionVOTypeProductRelation(ProductCommissionVOTypeProductRelation relation){
		return voEao.saveProductCommissionVOTypeProductRelation(relation);
	}

	
	@Override
	public BigDecimal calculateUserPointsPerCommission(User user, BigDecimal commission){
		BigDecimal points = null;
		
		if(user == null)
			return null;

		if(user.getPointsPerCommission() != null && user.getPointsPerCommission().compareTo(new BigDecimal(0)) > 0){
			
			points = commission.multiply(user.getPointsPerCommission()).setScale(4, RoundingMode.HALF_UP);
		}
		
		return points;
	}
	
	public static List<? extends CommissionCache> mergeCommissionCacheList(List<? extends CommissionCache> a, List<? extends CommissionCache> b){
		List<CommissionCacheEntity> list = new ArrayList<CommissionCacheEntity>();
		CommissionCacheEntity cc1,cc2;
		Iterator<CommissionCacheEntity> i,i2;
		boolean found;
		
		i = (Iterator<CommissionCacheEntity>) a.iterator();
		while(i.hasNext()){
			cc2 = i.next();
			
			cc1 = new CommissionCacheEntity();
			cc1.setYear(cc2.getYear());
			cc1.setMonth(cc2.getMonth());
			cc1.setPointGoal(cc2.getPointGoal());
			cc1.setCommission(cc2.getCommission());
			
			list.add(cc1);
		}

		i = (Iterator<CommissionCacheEntity>) b.iterator();
		while(i.hasNext()){
			cc2 = i.next();
			
			i2 = list.iterator();
			found = false;
			while(i2.hasNext()){
				cc1 = i2.next();
				
				if(cc1.getYear() != cc2.getYear() || cc1.getMonth() != cc2.getMonth())
					continue;

				found = true;
				cc1.setPointGoal(cc1.getPointGoal().add( cc2.getPointGoal()));
				cc1.setCommission(cc1.getCommission().addCommissionable( cc2.getCommission()));
				break;
			}
			if(!found){
				
				cc1 = new CommissionCacheEntity();
				cc1.setYear(cc2.getYear());
				cc1.setMonth(cc2.getMonth());
				cc1.setPointGoal(cc2.getPointGoal());
				cc1.setCommission(cc2.getCommission());
				
				list.add(cc1);
			}
		}
		
		return list;
	}
}
