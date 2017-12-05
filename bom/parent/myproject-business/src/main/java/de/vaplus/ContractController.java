package de.vaplus;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang.SerializationUtils;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
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
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.entity.BaseContractCancellationEntity;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ContractRetailItemEntity;
import de.vaplus.client.entity.ContractStatusChangeEntity;
import de.vaplus.client.entity.ContractStatusEntity;
import de.vaplus.client.entity.PhoneContractEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;


@Stateless
public class ContractController implements ContractControllerInterface {

	private static final long serialVersionUID = 7410728476404635370L;

	@EJB
	private CommissionControllerInterface commissionController;

	@EJB
	private PropertyControllerInterface propertyController;

	@Inject
    private ContractEao contractEao;

	@Override
	public BaseContract factoryCellPhoneContract(){
		return new PhoneContractEntity();
	}

	@Override
	public BaseContract saveContract(BaseContract contract) {
		contract = contractEao.saveContract(contract);

		commissionController.updateUserComissionHistory(contract.getUser());
		commissionController.updateShopComissionHistory(contract.getShop());

		return contract;
	}

	@Override
	public void addRetailItemToContract(BaseContract contract, Product product, String serial){
		((List<ContractRetailItem>)contract.getContractRetailItemList()).add( new ContractRetailItemEntity(contract, product, serial));


	}

	@Override
	public void removeRetailItemFromContract(BaseContract contract,
			ContractRetailItem contractRetailItem) {

		contract.getContractRetailItemList().remove(contractRetailItem);
	}

	@Override
	public List<? extends BaseContract> getContractList(Shop shop, VO vo,
			User user, Date from, Date to) {

		return contractEao.getContractList(shop, vo, user, null, from, to, false);
	}

	@Override
	public List<? extends CommissionActivity> getCommissionActivityList(
			Shop shop, User user, Date from, Date to) {

		return contractEao.getCommissionActivityList(shop, user, null, from, to, false);
	}

	@Override
	public List<? extends BaseContract> getContractList(Customer customer, Date from, Date to) {

		return contractEao.getContractList(null, null, null, customer, from, to, false);
	}

	@Override
	public List<? extends BaseContract> getRenewableContractList(Date expiryDateFrom, Date expiryDateTo) {

		List<? extends BaseContract> list = contractEao.getContractListByExpiryDate(getExpiryDateFrom(), expiryDateTo, true);
		BaseContract contract;
//		while(i.hasNext()){
//			contract = i.next();
//		}

		return list;
	}
	
	@Override
	public List<? extends BaseContract> getRenewableContractList(Shop shop, User user, ContractStatus status, BaseProduct tariff) {
		
		List<? extends BaseContract> list = contractEao.getContractListByExpiryDate(getExpiryDateFrom(), shop, user, status, tariff, 0, 0, true, null, null);
//		while(i.hasNext()){
//			contract = i.next();
//		}

		return list;
	}
	
	private Date getExpiryDateFrom(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);


		c.add(Calendar.DAY_OF_YEAR, (int) propertyController.getLongProperty("extensionOfTermLimit", 90L));
		Date expiryDateFrom = c.getTime();
		
		return expiryDateFrom;
	}
	
	@Override
	public long countRenewableContractList(Shop shop, User user, ContractStatus status, BaseProduct tariffFilter) {
	
		if(user != null && user.getId() == 1){
			// no Filter for supervisors
			user = null;
		}
		
		return contractEao.countContractListByExpiryDate(getExpiryDateFrom(), shop, user, status, tariffFilter, true);
	}
	
	
	
	@Override
	public List<? extends BaseContract> getRenewableContractList(Shop shop, User user, ContractStatus status, BaseProduct tariffFilter, int limit, int offset, String sortField, String sortOrder) {
		
		if(user != null && user.getId() == 1){
			// no Filter for supervisors
			user = null;
		}
		
		List<? extends BaseContract> list = contractEao.getContractListByExpiryDate(getExpiryDateFrom(), shop, user, status, tariffFilter, limit, offset, true, sortField, sortOrder);

		return list;
	}

	@Override
	public long getRenewableContractCount(Date expiryDateFrom, Date expiryDateTo) {
		return contractEao.getContractCountByExpiryDate(expiryDateFrom, expiryDateTo);
	}

	@Override
	public int getExtensionOfTheTermContractSum(Shop shop, VO vo, User user, Date from, Date to) {

		int sum = 0;

		Iterator<? extends BaseContract> i = contractEao.getContractList(shop, vo, user, null, from, to, false).iterator();
		BaseContract contract;
		while(i.hasNext()){
			contract = i.next();

			if(contract.isExtensionOfTerm())
				sum++;
		}

		return sum;
	}

	@Override
	public int getNewContractSum(Shop shop, VO vo, User user, Date from, Date to) {

		int sum = 0;

		Iterator<? extends BaseContract> i = contractEao.getContractList(shop, vo, user, null, from, to, false).iterator();
		BaseContract contract;
		while(i.hasNext()){
			contract = i.next();

			if(! contract.isExtensionOfTerm())
				sum++;
		}

		return sum;
	}

	@Override
	public boolean isContractAbleToExtensionOfTheTerm(BaseContract contract){
		
		if(!contract.isExtensionOfTheTermPermission())
			return false;
		
		long extensionOfTermLimit =  propertyController.getLongProperty("extensionOfTermLimit", 90);

		Date today = new Date();
		Date valueDate = contract.getExpiryDate();

		long diffInMillies = valueDate.getTime() - today.getTime();


		long contractRemainingTerm = TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);

		if(contractRemainingTerm <= extensionOfTermLimit)
			return true;

		return false;
	}

	@Override
	public int getFirstContractYear(){
		return contractEao.getFirstContractYear();
	}

	@Override
	public BaseContractCancellation saveBaseContractCancellation(BaseContractCancellation baseContractCancellation){
		return contractEao.saveBaseContractCancellation((BaseContractCancellationEntity) baseContractCancellation);
	}

	@Override
	public void cancelContract(BaseContract contract){

		if(contract.isCanceled())
			return;

		contract = contractEao.refresh(contract);

		BaseContractCancellationEntity cancellation = new BaseContractCancellationEntity();

		CommissionableEmbeddable commission =  (CommissionableEmbeddable) SerializationUtils.clone( contract.getFinalCommission());
		commission.invert();

		cancellation.setContract(contract);
		cancellation.setCommission(commission);
		cancellation.setEffectiveDate(new Date());
		cancellation.setExpiryDate(new Date());

		cancellation.setEnabled(true);
		cancellation.setCustomer(contract.getCustomer());
		cancellation.setShop(contract.getShop());
		cancellation.setUser(contract.getUser());

		contract.setContractCancellation(cancellation);
		contract = this.saveContract(contract);

	}

	@Override
	public BaseContract cloneContract(BaseContract contract) {

		BaseContractEntity clone = (BaseContractEntity) contract;
		contractEao.detachBaseContract(clone);
		clone.setId(0);

		return clone;
	}

	@Override
	public List<? extends ContractStatus> getContractStatusList() {
		return contractEao.getContractStatusList();
	}

	@Override
	public ContractStatus getContractStatusById(long id) {
		return contractEao.getContractStatusById(id);
	}

	@Override
	public ContractStatus saveContractStatus(ContractStatus contractStatus) {
		return contractEao.saveContractStatus(contractStatus);
	}

	@Override
	public ContractStatus factoryNewContractStatus() {
		ContractStatus status = new ContractStatusEntity();
		status.setEditable(true);
		return status;
	}

	@Override
	public ContractStatusChange factoryNewContractStatusChange() {
		ContractStatusChange contractStatusChange = new ContractStatusChangeEntity();
		
		contractStatusChange.setEffectiveDate(new Date());
		contractStatusChange.setEnabled(true);
		return contractStatusChange;
	}

	@Override
	public ContractStatusChange saveContractStatusChange(ContractStatusChange contractStatusChange) {
		return contractEao.saveContractStatusChange(contractStatusChange);
	}

	@Override
	public ContractStatus getContractStatus_ACTIVE() {
		return this.getContractStatusById(1);
	}

}
