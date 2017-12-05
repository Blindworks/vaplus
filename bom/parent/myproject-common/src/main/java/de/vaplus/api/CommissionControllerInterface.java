package de.vaplus.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.AquiredRevenueLevel;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ContractedRevenueLevel;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.VOTypeCommissionRevenueLevel;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;

public interface CommissionControllerInterface extends Serializable {

	void updateUserComissionHistory(User user);

	void updateShopComissionHistory(Shop shop);

	List<? extends RevenueLevel> getRevenueLevelList();

	RevenueLevel factoryNewAquiredRevenueLevel();

	RevenueLevel factoryNewContractedRevenueLevel();

	RevenueLevel saveRevenueLevel(RevenueLevel selectedRevenueLevel);

	List<? extends ContractedRevenueLevel> getContractedRevenueLevelList(Vendor vendor);

	void deleteRevenueLevel(RevenueLevel selectedRevenueLevel);

	ContractedRevenueLevel getContractedRevenueLevel(BigDecimal commission, BigDecimal tax, Vendor vendor);

	AquiredRevenueLevel getAquiredRevenueLevel(BigDecimal commission, BigDecimal tax, Vendor vendor);


//	BigDecimal calculateAquiredCommissionRevenuePoints(BigDecimal commission,
//			RevenueLevelVOType voType);
//
//	BigDecimal calculateContractedCommissionRevenuePoints(
//			BigDecimal commission, RevenueLevelVOType voType);

//	ProductCommissionVOType addProductToProductCommissionVOType(ProductCommissionVOType voType,
//			BaseProduct product);

	ProductCommissionVOType removeProductFromProductCommissionVOType(
			ProductCommissionVOType voType, BaseProduct product);

	List<? extends AquiredRevenueLevel> getAquiredRevenueLevelList(
			Vendor selectedVendor);

	Commissionable factoryNewCommission();

	BigDecimal calculatePoints(BigDecimal commission, VOType votype);

	BigDecimal calculateRevenuePoints(BigDecimal commission,
			VOTypeCommissionRevenueLevel revenueLevel);

	Commissionable calculateCommission(BaseProduct product, VOType voType);

	ProductCommissionVOTypeProductRelation factoryNewProductCommissionVOTypeProductRelation();

	ProductCommissionVOTypeProductRelation getProductCommissionVOTypeProductRelation(
			ProductCommissionVOType voType, BaseProduct product,
			List<? extends ProductOption> productOptionList, boolean exactMatch);

	ProductCommissionVOTypeProductRelation saveProductCommissionVOTypeProductRelation(
			ProductCommissionVOTypeProductRelation relation);

	BigDecimal calculateUserPointsPerCommission(User user, BigDecimal commission);

	Commissionable getProductTypeCommission(ProductCommissionVOType voType,
			BaseProduct product,
			List<? extends ProductOption> productOptionList, boolean exactMatch);

	void calculateCommission(BaseContract contract);

	void calculateCommission(Order selectedOrder);

	BigDecimal calculatePointsPerProfit(BigDecimal profit);

	void updateUserCommissionCache(User user, int month, int year);

	void updateVOComissionHistory(VO vo);

	void updateAllComissionHistory();

	void calculateCommission(Invoice invoice);


}
