package de.vaplus.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseContractCancellation;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.CommissionActivity;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.ContractStatusChange;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;

public interface ContractControllerInterface extends Serializable{

	BaseContract factoryCellPhoneContract();

	BaseContract saveContract(BaseContract contract);

	void addRetailItemToContract(BaseContract contract, Product product, String serial);

	void removeRetailItemFromContract(BaseContract contract,
			ContractRetailItem contractRetailItem);

	List<? extends BaseContract> getContractList(Shop shop, VO vo, User user,
			Date from, Date to);

	boolean isContractAbleToExtensionOfTheTerm(BaseContract contract);

	int getFirstContractYear();

	int getExtensionOfTheTermContractSum(Shop shop, VO vo, User user,
			Date from, Date to);

	int getNewContractSum(Shop shop, VO vo, User user, Date from, Date to);

	List<? extends BaseContract> getContractList(Customer customer, Date from,
			Date to);

	List<? extends BaseContract> getRenewableContractList(Date expiryDateFrom,
			Date expiryDateTo);

	BaseContractCancellation saveBaseContractCancellation(
			BaseContractCancellation baseContractCancellation);

	void cancelContract(BaseContract contract);

	List<? extends CommissionActivity> getCommissionActivityList(Shop shop, User user, Date from, Date to);

	BaseContract cloneContract(BaseContract contract);

	long getRenewableContractCount(Date expiryDateFrom, Date expiryDateTo);

	List<? extends ContractStatus> getContractStatusList();

	ContractStatus getContractStatusById(long id);

	ContractStatus saveContractStatus(ContractStatus selectedContractStatus);

	ContractStatus factoryNewContractStatus();

	ContractStatusChange factoryNewContractStatusChange();

	ContractStatusChange saveContractStatusChange(ContractStatusChange contractStatusChange);

	List<? extends BaseContract> getRenewableContractList(Shop renewableContractList_Filter_Shop,
			User renewableContractList_Filter_User, ContractStatus renewableContractList_Filter_Status, BaseProduct tariff);

	List<? extends BaseContract> getRenewableContractList(Shop renewableContractList_Filter_Shop,
			User renewableContractList_Filter_User, ContractStatus renewableContractList_Filter_Status, BaseProduct tariffFilter, int limit, int offset, String sortField, String sortOrder);

	ContractStatus getContractStatus_ACTIVE();

	long countRenewableContractList(Shop shop, User user, ContractStatus status, BaseProduct tariffFilter);


}
