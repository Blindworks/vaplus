package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.embeddable.Commissionable;


public interface BaseContract extends Activity{

	String getInfo();

	void setInfo(String info);

	Customer getCustomer();

	void setCustomer(Customer customer);

	Tariff getTariff();

	List<? extends ProductOption> getTariffOptionList();

	void setTariffOptionList(List<? extends ProductOption> tariffOptionList);

	User getUser();

	void setUser(User user);

	Shop getShop();

	void setShop(Shop shop);

	VO getVo();

	void setVo(VO vo);

	ExternalCustomer getExternalCustomer();

	void setExternalCustomer(ExternalCustomer externalCustomer);

	void setTariff(Tariff tariff);

	ContractItem getCachedTariff();

	void setCachedTariff(ContractItem cachedTariff);

	List<? extends ContractItem> getCachedTariffOptionList();

	void setCachedTariffOptionList(
			List<? extends ContractItem> cachedTariffOptionList);

	Commissionable getRevenueStepCommission();

	void setRevenueStepCommission(Commissionable revenueStepCommission);

	Commissionable getFinalCommission();

	void setFinalCommission(Commissionable finalCommission);

	boolean isExtensionOfTheTermPermission();

	void setExtensionOfTheTermPermission(boolean extensionOfTheTermPermission);

	int getLeadTime();

	void setLeadTime(int leadTime);

	List<? extends ContractRetailItem> getContractRetailItemList();

	void setContractRetailItemList(
			List<? extends ContractRetailItem> contractRetailItemList);

	BaseContract getExtendedContract();

	void setExtendedContract(BaseContract extendedContract);

	boolean isExtensionOfTerm();

	void setExtensionOfTerm(boolean extensionOfTerm);

	BigDecimal getGeneratedRevenue();

	void setGeneratedRevenue(BigDecimal generatedRevenue);

	String getOrderNumber();

	void setOrderNumber(String orderNumber);

	void newExternalCustomer();

	BigDecimal getSubsidyBudgetary();

	void setSubsidyBudgetary(BigDecimal subsidyBudgetary);

	Order getSubsidyOrder();

	void setSubsidyOrder(Order subsidyOrder);

	AccountingAssignment getAccountingAssignment();

	void setAccountingAssignment(AccountingAssignment accountingAssignment);

	boolean isDebidCreditChange();

	void setDebidCreditChange(boolean debidCreditChange);

	BaseContractCancellation getContractCancellation();

	void setContractCancellation(BaseContractCancellation contractCancellation);

	boolean isCanceled();

	AccountingAssignment getCancelationAccountingAssignment();

	void setCancelationAccountingAssignment(
			AccountingAssignment cancelationAccountingAssignment);

	ContractStatus getStatus();

	void setStatus(ContractStatus status);

	Invoice getSubsidyInvoice();

	void setSubsidyInvoice(Invoice subsidyInvoice);

}
