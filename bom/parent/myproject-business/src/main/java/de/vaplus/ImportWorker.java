package de.vaplus;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import org.jboss.ejb3.annotation.TransactionTimeout;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.ImportLine;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.PhoneContract;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.pojo.ImportResult;
import de.vaplus.client.entity.ContractItemEntity;
import de.vaplus.client.entity.ExternalCustomerEntity;
import de.vaplus.client.entity.PhoneContractEntity;
import de.vaplus.client.entity.VendorEntity;

@Singleton
public class ImportWorker {


	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private VOControllerInterface voController;
	
	@EJB
	private UserControllerInterface userController;
	
	@Asynchronous
	@TransactionTimeout(108000)
	public Future<ImportResult> runAsync(List<? extends ImportLine> importLines){

		ImportResult result = new ImportResult();
		result.start();
		
		
		Iterator<? extends ImportLine> i = importLines.iterator();
		ImportLine importLine;
		
		Date from, to;
		Calendar cal = Calendar.getInstance();
		List<? extends BaseContract> contractList;
		Iterator<? extends BaseContract> ci;
		
		BaseContract contract; 
		boolean contractExists;
		VO vo;
		User user;
		Shop shop;
		
		ContractItemEntity cachedTariff;
		
		System.out.println("Import DATA LINES "+importLines.size());
		int k = 0;
		
		while(i.hasNext()){
			importLine = i.next();
			contractExists = false;

			result.setProcessedDataSets(result.getProcessedDataSets() +1);
			
			k++;
			if(k%100 == 0){
				System.out.println("Import Data Line: "+k);
			}
			
			if(importLine.getFirstname() == null && importLine.getLastname() == null && importLine.getCompanyName() == null)
				continue;
			
			// insert new Customer ?
			Customer c = customerController.getCustomerOrCreate(importLine.getFirstname(), importLine.getLastname(), importLine.getCompanyName(), importLine.isCompany(), importLine.getStreet(), importLine.getStreetNumber(), importLine.getAddressLine2(), importLine.getZip(), importLine.getCity(), importLine.getCreationDate());
			
			if(c.getId() == 0)
				result.setImportetCustomer(result.getImportetCustomer() +1);
			
			if(importLine.getDayOfBirth() != null && c.getBday() == null)
					c.setBday(importLine.getDayOfBirth());
			
			if(importLine.getEmail() != null && c.getEmail() == null)
					c.setEmail(importLine.getEmail());		
			
			if(importLine.getDdi() != null && c.getTel() == null)
				c.setTel(importLine.getDdi());	
			
			if(importLine.getShopName() != null && c.getShop() == null)
				c.setShop( shopController.getShopByAlias(importLine.getShopName()) );
			
			

			
			
			c = customerController.saveCustomer(c);
			
			
			
			
			// check if Contract exists
			
			cal.setTime( importLine.getCreationDate() );
			
			cal.add(Calendar.DAY_OF_MONTH, +1);
			to = cal.getTime();
					
			cal.add(Calendar.DAY_OF_MONTH, -2);
			from = cal.getTime();
			
			contractList = contractController.getContractList(c, from, to);
			
			
			ci = contractList.iterator();
			while(ci.hasNext()){
				contract = ci.next();
				

				
				
//				UserTransaction ut;
//				try {
//					ut = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
//				     ut.setTransactionTimeout(20);                     
//				     ut.begin();
//				} catch (NamingException | SystemException | NotSupportedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}   
//				System.out.println("check old contract: "+contract.getId());
				
				if(contract.getEffectiveDate().compareTo(importLine.getCreationDate()) != 0){
//						System.out.println("wrong EffectiveDate skip");
					continue;
				}
				
				
				if(importLine.getProductNames().length > 0 && ! contract.getCachedTariff().getProductName().equalsIgnoreCase(importLine.getProductNames()[0])){
//						System.out.println("wrong ProductName skip: "+importLine.getProductNames()[0]+" != "+contract.getCachedTariff().getProductName());
					continue;
				}
				
				
				if(contract instanceof PhoneContract){
					if(importLine.getDdi().length() > 0 && ! ((PhoneContract) contract).getCallingNumber().equalsIgnoreCase(importLine.getDdi())){
//							System.out.println("wrong DDI skip: "+importLine.getDdi()+" != "+((PhoneContract) contract).getCallingNumber());
						continue;
					}
				}else{
//						System.out.println("continue anyway");
					continue;
				}

				contractExists = true;
			}
			
			if(contractExists){
//				System.out.println("contractExists");
				continue;
			}
			
			
			vo = voController.getVOByNumber(importLine.getVoNumber());
			if(vo == null){
				System.out.println("no VO found: "+importLine.getVoNumber());
				continue;
			}
			
			user = userController.getUserByAlias(importLine.getUserName());
			if(user == null){
				System.out.println("no User found: "+importLine.getUserName());
				continue;
			}
			
			shop = shopController.getShopByAlias(importLine.getShopName());
			if(shop == null){
				System.out.println("no Shop found: "+importLine.getShopName());
				continue;
			}

			
			result.setImportetContracts(result.getImportetContracts() +1);
			
			PhoneContractEntity newContract = new PhoneContractEntity();
			
			newContract.setVo(vo);
			newContract.setUser(user);
			newContract.setShop(shop);
			newContract.setCustomer(c);
			newContract.setStatus(contractController.getContractStatusById(1));
			
			newContract.newExternalCustomer();
			newContract.getExternalCustomer().setCustomerId("TMP_"+importLine.getDdi()+"_"+Math.round((Math.random() * 100000)));
			newContract.getExternalCustomer().setPassword(importLine.getPassword());
			newContract.getExternalCustomer().setCustomer(c);
			
			
			
			
			newContract.setNote(importLine.getInfo());
			
			newContract.setEnabled(true);
			
			newContract.setEffectiveDate(importLine.getCreationDate());
			
			cal.setTime(importLine.getCreationDate());
			cal.add(Calendar.MONTH, 24);
			newContract.setExpiryDate(cal.getTime());
			
			newContract.setCreationDate(importLine.getCreationDate());
			
			newContract.setCallingNumber(importLine.getDdi());
			
			newContract.getGeneratedRevenue();
			newContract.getRevenueStepCommission();
			newContract.getFinalCommission();
			
			if(importLine.getProductNames().length > 0){

			
				cachedTariff = new ContractItemEntity();
				cachedTariff.setProductName(importLine.getProductNames()[0].trim());
				cachedTariff.setVendor((VendorEntity) vo.getVendor());

				// init Commissions
				cachedTariff.getProductCommission();
				cachedTariff.getVoCommission();
				
				newContract.setCachedTariff(cachedTariff);
			}
			
			for(int j = 1; j< importLine.getProductNames().length; j++){
				
				if(importLine.getProductNames()[j] == null || importLine.getProductNames()[j].length() == 0)
					continue;

				
				cachedTariff = new ContractItemEntity();
				cachedTariff.setProductName(importLine.getProductNames()[j].trim());
				cachedTariff.setVendor((VendorEntity) vo.getVendor());

				// init Commissions
				cachedTariff.getProductCommission();
				cachedTariff.getVoCommission();
				
				((List<ContractItemEntity>) newContract.getCachedTariffOptionList()).add(cachedTariff);
			}
			
			newContract = (PhoneContractEntity) contractController.saveContract(newContract);

		}

		result.end();
		
		return new AsyncResult<ImportResult>(result);
	}
}
