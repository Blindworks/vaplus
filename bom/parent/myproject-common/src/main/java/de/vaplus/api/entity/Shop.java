package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.api.interfaces.RankableByCommission;

public interface Shop extends StatusBase, RankableByCommission, ActivityOwner{

	String getName();

	void setName(String name);

	Address getAddress();

	void setAddress(Address address);

	BigDecimal getPointGoal();

	void setPointGoal(BigDecimal pointGoal);

	List<? extends CommissionCache> getCommissionCacheList();

	String getUuid();

	DBFile getShopImage();

	void setShopImage(DBFile shopImage);

	List<? extends VO> getShopVOList();

	String getContactPerson();

	void setContactPerson(String contactPerson);

	String getTel();

	void setTel(String tel);

	String getFax();

	void setFax(String fax);

	String getEmail();

	void setEmail(String email);

	List<? extends VO> getShopVOPermissionList();

	CommissionCache getLiveCommissionCache();

	CommissionCache getCurrentCommissionCache();

	CommissionCache[] getCommissionCacheList(int year);

	CommissionCache[] getCurrentRangeCommissionCacheList();

	CommissionCache getLastCommissionCache();

	CommissionCache getNextCommissionCache();

	CommissionCache getCommissionCache(int year, int month);

	String getColor();

	void setColor(String color);

	
	String getBusinessHours_start_0();

	void setBusinessHours_start_0(String businessHours_start_0);

	String getBusinessHours_start_1();

	void setBusinessHours_start_1(String businessHours_start_1);

	String getBusinessHours_start_2();

	void setBusinessHours_start_2(String businessHours_start_2);

	String getBusinessHours_start_3();

	void setBusinessHours_start_3(String businessHours_start_3);

	String getBusinessHours_start_4();

	void setBusinessHours_start_4(String businessHours_start_4);

	String getBusinessHours_start_5();

	void setBusinessHours_start_5(String businessHours_start_5);

	String getBusinessHours_start_6();

	void setBusinessHours_start_6(String businessHours_start_6);


	
	String getBusinessHours_end_0();

	void setBusinessHours_end_0(String businessHours_end_0);

	String getBusinessHours_end_1();

	void setBusinessHours_end_1(String businessHours_end_1);

	String getBusinessHours_end_2();

	void setBusinessHours_end_2(String businessHours_end_2);

	String getBusinessHours_end_3();

	void setBusinessHours_end_3(String businessHours_end_3);

	String getBusinessHours_end_4();

	void setBusinessHours_end_4(String businessHours_end_4);

	String getBusinessHours_end_5();

	void setBusinessHours_end_5(String businessHours_end_5);

	String getBusinessHours_end_6();

	void setBusinessHours_end_6(String businessHours_end_6);

	ProductCategorySalesCache getProductCategorySalesCache(
			ProductCategory category, int year, int month);

	List<? extends ProductCategorySalesCache> getProductCategorySalesCacheList();

	void setProductCategorySalesCacheList(
			List<? extends ProductCategorySalesCache> productCategorySalesCacheList);

	List<? extends ShopAlias> getAliasList();

	void setAliasList(List<? extends ShopAlias> aliasList);

	List<? extends ProductCategorySalesCache> getCurrentRangeProductCategorySalesCacheList(
			ProductCategory category);

	State getState();

	void setState(State state);

	String getCalendarNote();

	void setCalendarNote(String calendarNote);

	Stock getStock();

	void setStock(Stock stock);

	String getCrosscanData_authID();

	void setCrosscanData_authID(String crosscanData_authID);

	String getCrosscanData_storeID();

	void setCrosscanData_storeID(String crosscanData_storeID);

	String getDrafterLine();

	void setDrafterLine(String drafterLine);

	String getDocFooterLine1();

	void setDocFooterLine1(String docFooterLine1);

	String getDocFooterLine2();

	void setDocFooterLine2(String docFooterLine2);

	String getDocFooterLine3();

	void setDocFooterLine3(String docFooterLine3);

//	long getInvoiceNumber();
//
//	void setInvoiceNumber(long invoiceNumber);
//
//	long getOrderNumber();
//
//	void setOrderNumber(long orderNumber);
//
//	long getPickslipNumber();
//
//	void setPickslipNumber(long pickslipNumber);

	

}
