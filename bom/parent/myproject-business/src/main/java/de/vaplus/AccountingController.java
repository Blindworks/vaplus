package de.vaplus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ManualCommission;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOCommission;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingFile;
import de.vaplus.api.entity.VendorCommissionAccountingItem;
import de.vaplus.api.entity.VodafoneCommissionAccountingItem;
import de.vaplus.client.eao.AccountingEao;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.ManualCommissionEntity;
import de.vaplus.client.entity.VendorEntity;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentLineEntity;
import de.vaplus.client.entity.commission.vendor.CommissionTypeAssociationEntity;


@Stateless
public class AccountingController implements AccountingControllerInterface {

	private static final long serialVersionUID = -8376341270023119257L;

	@EJB
	private UserControllerInterface userController;

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private CommissionControllerInterface commissionController;

	@EJB
	private VOControllerInterface voController;

	@Inject
    private AccountingEao accountingEao;

	@Inject
    private ContractEao contractEao;

	
	public AccountingController(){
		// System.out.println("### INIT AccountingController ###");
	}

	@Override
	public List<? extends VendorCommissionAccountingFile> getCommissionAccountingFileList(
			int year, int month) {

		return accountingEao.getCommissionAccountingFileList(year, month);
	}

	@Override
	public CommissionTypeAssociation getCommissionTypeAssociationByText(
			String commissiontText, String commissiontSubText) {
		return accountingEao.getCommissionTypeAssociationByText(commissiontText, commissiontSubText);
	}

	@Override
	public CommissionTypeAssociation factoryNewCommissionTypeAssociation(String commissionText, String commissionSubText) {
		CommissionTypeAssociationEntity cta = new CommissionTypeAssociationEntity();
		cta.setCommissionText(commissionText);
		cta.setCommissionSubText(commissionSubText);
		
		return cta;
	}
	
	@Override 
	public CommissionTypeAssociation getCommissionTypeAssociationByCommissionAccountingItem(VendorCommissionAccountingItem accountingItem){
		CommissionTypeAssociation cta = null;
		
		if(accountingItem instanceof VodafoneCommissionAccountingItem){
			VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
			
			
			cta = getCommissionTypeAssociationByText(vItem.getProvisionsart(), vItem.getTarifbeschreibung());
			if(cta == null){
				cta = factoryNewCommissionTypeAssociation(vItem.getProvisionsart(), vItem.getTarifbeschreibung());
			}
		}
		
		return cta;
	}

	@Override
	public CommissionTypeAssociation saveCommissionTypeAssociation(
			CommissionTypeAssociation commissionTypeAssociation) {
		return accountingEao.saveUnknownCommissionTypeAssociation(commissionTypeAssociation);
	}
	
//	@Override
//	public AccountingAssignment factoryAccountingAssignment(String key){
//		return new AccountingAssignmentEntity(key);
//	}
//	
//	@Override
//	public AccountingAssignment factoryAccountingAssignment(String key, PhoneContract pContract){
//		return new AccountingAssignmentEntity(key, pContract);
//	}
	
	@Override
	public void checkAccountingAssignment(AccountingAssignment accountingAssignment){
		AccountingAssignmentEntity ass = (AccountingAssignmentEntity) accountingAssignment;

		checkAccountingAssignmentCustomer(ass);
		checkAccountingAssignmentDate(ass);
		checkAccountingAssignmentUser(ass);
		checkAccountingAssignmentCommission(ass);
		checkAccountingAssignmentElements(ass);
		checkAccountingAssignmentVO(ass);
	}
	
	private void addCommissionTypeAssociationToLine(VendorCommissionAccountingItem accountingItem, AccountingAssignmentLineEntity line){
		
		line.getCommissionAccountingList().add(accountingItem);
//		
//		accountingItem.get
//		
//		if(accountingItem instanceof VodafoneCommissionAccountingItem){
//			VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
//
//			line.addRightLine(vItem.getProvisionsart()+" | "+vItem.getProvisionsmodell());
//			line.setRightWorth(vItem.getNettobetrag());
//		}
	}

	private void checkAccountingAssignmentElements(AccountingAssignmentEntity accountingAssignment){
		
		boolean result = true;
		boolean foundItem;

		List<VendorCommissionAccountingItem> unasignedAccountingItemList = new LinkedList<VendorCommissionAccountingItem>(); 
		unasignedAccountingItemList.addAll(accountingAssignment.getAccountingItemList()); 
		List<VendorCommissionAccountingItem> asignedAccountingItemList = new LinkedList<VendorCommissionAccountingItem>(); 
		Iterator<? extends VendorCommissionAccountingItem> icai;
		VendorCommissionAccountingItem accountingItem;

		Iterator<? extends ProductOption> ipo;
		ProductOption productOption;
		
		CommissionTypeAssociation cta;
		
//		Iterator<PhoneContract> ic = accountingAssignment.getpContractList().iterator();
//		PhoneContract contract;
		AccountingAssignmentLineEntity line;
//		while(ic.hasNext()){	
//			contract = ic.next();
		
		if(accountingAssignment.getContract() != null){
			
			line = new AccountingAssignmentLineEntity();
			
			line.addLeftLine(accountingAssignment.getContract().getCachedTariff().getProductName());
			line.setLeftWorth(accountingAssignment.getContract().getFinalCommission().getCommission());
			
			
			
			// Tariff

			line = new AccountingAssignmentLineEntity();
			
			line.addLeftLine(accountingAssignment.getContract().getCachedTariff().getProductName());
			line.setLeftWorth(accountingAssignment.getContract().getFinalCommission().getCommission());
			icai = unasignedAccountingItemList.iterator();
			foundItem = false;
			while(icai.hasNext()){	
				accountingItem = icai.next();
				cta = getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
				
				if(cta.hasProduct(accountingAssignment.getContract().getTariff())){
					foundItem = true;
					asignedAccountingItemList.add(accountingItem);
					addCommissionTypeAssociationToLine(accountingItem, line);

				}

			}
			((List<AccountingAssignmentLineEntity>)accountingAssignment.getElementLines()).add(line);
			if(!foundItem){
				accountingAssignment.addInfoLine("Tarif ("+accountingAssignment.getContract().getCachedTariff().getProductName()+") in Provisionen nicht gefunden!");
				result = false;
			}
			
			// VVL
			if(accountingAssignment.getContract().isExtensionOfTerm()){

				line = new AccountingAssignmentLineEntity();
				
				line.addLeftLine("Vertrags VL");
				
				icai = unasignedAccountingItemList.iterator();
				foundItem = false;
				while(icai.hasNext()){	
					accountingItem = icai.next();
					cta = getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
					
					if(cta.isExtensionOfTheTerm()){
						foundItem = true;
						asignedAccountingItemList.add(accountingItem);
						addCommissionTypeAssociationToLine(accountingItem, line);

					}

				}
				((List<AccountingAssignmentLineEntity>)accountingAssignment.getElementLines()).add(line);
				if(!foundItem){
					accountingAssignment.addInfoLine("Vertrags VL in Provisionen nicht gefunden!");
					result = false;
				}
			}
			
			
			// Product Options
			ipo = accountingAssignment.getContract().getTariffOptionList().iterator();
			while(ipo.hasNext()){
				productOption = ipo.next();
				
				line = new AccountingAssignmentLineEntity();
				
				line.addLeftLine(productOption.getName());
				icai = unasignedAccountingItemList.iterator();
				foundItem = false;
				while(icai.hasNext()){	
					accountingItem = icai.next();
					cta = getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
					
					if(cta.hasProductOption(productOption)){
						foundItem = true;
						asignedAccountingItemList.add(accountingItem);
						addCommissionTypeAssociationToLine(accountingItem, line);

					}

				}
				if(!foundItem){
					accountingAssignment.addInfoLine("Produkt Option ("+productOption.getName()+") in Provisionen nicht gefunden!");
					result = false;
				}
				((List<AccountingAssignmentLineEntity>)accountingAssignment.getElementLines()).add(line);
				
			}
			
		}
//		}
		
		unasignedAccountingItemList.removeAll(asignedAccountingItemList);
		
		
		// add not assigned Elements
		icai = unasignedAccountingItemList.iterator();
		while(icai.hasNext()){	
			accountingItem = icai.next();
			
			line = new AccountingAssignmentLineEntity();
			
			addCommissionTypeAssociationToLine(accountingItem, line);
				
			((List<AccountingAssignmentLineEntity>)accountingAssignment.getElementLines()).add(line);

		}

		
		
		// backwards check
		icai = accountingAssignment.getAccountingItemList().iterator();
		while(icai.hasNext()){
			accountingItem = icai.next();
			cta = getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
			if(cta == null){
				accountingAssignment.addInfoLine("Keine Provisionszuordnung gefunden!");
				result = false;
			}
			
			
			// Extension of the term
			if(cta.isExtensionOfTheTerm()){
				foundItem = false;
				
//				ic = accountingAssignment.getpContractList().iterator();
//				while(ic.hasNext()){	
//					contract = ic.next();
					if(accountingAssignment.getContract() != null && accountingAssignment.getContract().isExtensionOfTerm())
						foundItem = true;
//				}
				if(!foundItem){
					accountingAssignment.addInfoLine("Vertrags VL in Vertrag nicht gesetzt!");
					result = false;
				}
				
			}
			
			// Tariff 
			if(cta.getProduct() != null){
				foundItem = false;
				
//				ic = accountingAssignment.getpContractList().iterator();
//				while(ic.hasNext()){	
//					contract = ic.next();
					if(accountingAssignment.getContract() != null && accountingAssignment.getContract().getTariff().equals(cta.getProduct()))
						foundItem = true;
//				}
				if(!foundItem){
					accountingAssignment.addInfoLine("Tarif ("+cta.getProduct()+") in Vertrag nicht gefunden!");
					result = false;
				}
			}
			
			// Tariff Options 
			if(cta.getProductOptionList() != null && cta.getProductOptionList().size() > 0){

				ipo = cta.getProductOptionList().iterator();
				while(ipo.hasNext()){
					productOption = ipo.next();
				
					foundItem = false;
					
//					ic = accountingAssignment.getpContractList().iterator();
//					while(ic.hasNext()){	
//						contract = ic.next();
						if(accountingAssignment.getContract() != null && accountingAssignment.getContract().getTariffOptionList().contains(productOption))
							foundItem = true;
//					}
					
					if(!foundItem){
						accountingAssignment.addInfoLine("Tarif Option ("+productOption.getName()+") in Vertrag nicht gefunden!");
						result = false;
					}
					
				}
			}
			
		}
		
		accountingAssignment.setCorrectElements(result);
		
	}

	private void checkAccountingAssignmentCommission(AccountingAssignmentEntity accountingAssignment){
		accountingAssignment.setCorrectCommission(
				accountingAssignment.getSum().compareTo(new BigDecimal(0)) == 0
		);
			
	}
	
	private void checkAccountingAssignmentUser(AccountingAssignmentEntity accountingAssignment){

		User user = null, otherUser = null;
		
	
		
		if(accountingAssignment.getContract() == null && accountingAssignment.getAccountingItemList().size() > 0){
			accountingAssignment.addInfoLine("Kein Vertrag zugeordnet");
		}
		
		if(accountingAssignment.getContract() != null && accountingAssignment.getAccountingItemList().size() > 0){
			
//			Iterator<PhoneContract> i1 = accountingAssignment.getpContract().iterator();
//			PhoneContract contract;
//			while(i1.hasNext()){	
//				contract = i1.next();
				if(user == null)
					user = accountingAssignment.getContract().getUser();
				else{
					if(! user.equals(accountingAssignment.getContract().getUser())){
						accountingAssignment.addInfoLine("Mitarbeiter in Vertrag abweichend ("+user+" != "+accountingAssignment.getContract().getUser()+")");
						accountingAssignment.setCorrectUser(false);
						return;
					}
				}
//			}
			
			if(user == null){
				accountingAssignment.addInfoLine("Kein Mitarbeiter im Vertrag gefunden.");
				accountingAssignment.setCorrectUser(false);
				return;
			}
			
			Iterator<? extends VendorCommissionAccountingItem> i2 = accountingAssignment.getAccountingItemList().iterator();
			VendorCommissionAccountingItem accountingItem;
			while(i2.hasNext()){	
				accountingItem = i2.next();

				if(accountingAssignment.getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
					VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
					
					if(vItem.getVerkaeufer().trim().length() == 0)
						continue;
					
					otherUser = userController.getUserByAlias(vItem.getVerkaeufer().trim());
					if(otherUser == null){
						accountingAssignment.addInfoLine("Mitarbeiter "+vItem.getVerkaeufer().trim()+" nicht zugeordnet.");
						accountingAssignment.setCorrectUser(false);
						return;
					}
					
					if(! otherUser.equals(user)){
						accountingAssignment.addInfoLine("Mitarbeiter "+user+" != "+otherUser);
						accountingAssignment.setCorrectUser(false);
						return;
					}
					
				}else{
					accountingAssignment.addInfoLine("Kein Provisionseintrag zugeordnet");
					accountingAssignment.setCorrectUser(false);
					return;
				}

			}
			
			
		}else{
			accountingAssignment.setCorrectUser(false);
			return;
		}	
	
		accountingAssignment.setCorrectUser(true);
		return;
	}


	private void checkAccountingAssignmentCustomer(AccountingAssignmentEntity accountingAssignment){
		
		Customer c = null;
		
		if(accountingAssignment.getContract() != null && accountingAssignment.getAccountingItemList().size() > 0){
			
			c = accountingAssignment.getContract().getCustomer();

			if(c == null){
				accountingAssignment.addInfoLine("Keinen Kunde in Vertrag gefunden");
				accountingAssignment.setCorrectCustomer(false);
				return;
			}

			if(c.getFirstname() == null && c.getLastname() == null && c.getCompanyName() == null){
				accountingAssignment.addInfoLine("Kundenname fehlerhaft: Kunde "+c.getId()+" enthält keinen Namen!");
				accountingAssignment.setCorrectCustomer(false);
				return;
			}
			
			if(! c.equals(accountingAssignment.getContract().getCustomer())){
				accountingAssignment.addInfoLine("Kundenname fehlerhaft: "+c.getName()+" != "+accountingAssignment.getContract().getCustomer().getLastname());
				accountingAssignment.setCorrectCustomer(false);
				return;
			}
			
			
			
			Iterator<? extends VendorCommissionAccountingItem> i2 = accountingAssignment.getAccountingItemList().iterator();
			VendorCommissionAccountingItem accountingItem;
			while(i2.hasNext()){	
				accountingItem = i2.next();
				
				


				if(accountingAssignment.getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
					VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
					
					if(vItem.getKundenname() == null){
						accountingAssignment.addInfoLine("Kundenname fehlerhaft: Kundenname in Abrechnungselement ist NULL");
						accountingAssignment.setCorrectCustomer(false);
						return;
					}
					
					
					if(!c.getName().contains(vItem.getKundenname().trim())){
						accountingAssignment.addInfoLine("Kundenname fehlerhaft: "+c.getName()+" != "+vItem.getKundenname());
						accountingAssignment.setCorrectCustomer(false);
						return;
					}
					
				}else{
					accountingAssignment.setCorrectCustomer(false);
					return;
				}

			}
			
			
		}else{
			accountingAssignment.setCorrectCustomer(false);
			return;
		}	
		
		accountingAssignment.setCorrectCustomer(true);
		return;
	}

	private void checkAccountingAssignmentVO(AccountingAssignmentEntity accountingAssignment){
		
		
		if(accountingAssignment.getContract() != null && accountingAssignment.getAccountingItemList().size() > 0){
			
			Iterator<? extends VendorCommissionAccountingItem> i2 = accountingAssignment.getAccountingItemList().iterator();
			VendorCommissionAccountingItem accountingItem;
			while(i2.hasNext()){	
				accountingItem = i2.next();

				if(accountingAssignment.getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
					VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
					
					if(! accountingAssignment.getVo().equalsIgnoreCase(vItem.getVoIDAbrechnung())){
						accountingAssignment.addInfoLine("VO fehlerhaft: "+accountingAssignment.getVo()+" != "+vItem.getVoIDAbrechnung().trim());
						accountingAssignment.setCorrectVO(false);
						return;
					}
					
				}else{
					accountingAssignment.setCorrectVO(false);
					return;
				}

			}
			
			
		}else{
			accountingAssignment.setCorrectVO(false);
			return;
		}	
		
		accountingAssignment.setCorrectVO(true);
		return;
	}
	
	private void checkAccountingAssignmentDate(AccountingAssignmentEntity accountingAssignment){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Date d = null;
		Date otherDate;
		
		if(accountingAssignment.getContract() != null && accountingAssignment.getAccountingItemList().size() > 0){
			
//			Iterator<PhoneContract> i1 = accountingAssignment.getpContractList().iterator();
//			PhoneContract contract;
//			while(i1.hasNext()){	
//				contract = i1.next();
				if(d == null)
					d = accountingAssignment.getContract().getEffectiveDate();
				else{
					if(d.compareTo(accountingAssignment.getContract().getEffectiveDate()) != 0){
						accountingAssignment.addInfoLine("Datum fehlerhaft. Ein Vertrag weicht ab: "+formatter.format(accountingAssignment.getContract().getEffectiveDate()));
						accountingAssignment.setCorrectDate(false);
						return;
					}
				}
//			}
			
			if(d == null){
				accountingAssignment.setCorrectDate(false);
				return;
			}
			
			Iterator<? extends VendorCommissionAccountingItem> i2 = accountingAssignment.getAccountingItemList().iterator();
			VendorCommissionAccountingItem accountingItem;
			while(i2.hasNext()){	
				accountingItem = i2.next();

				if(accountingAssignment.getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
					otherDate = accountingItem.getDate();
					
					if(otherDate == null){
						accountingAssignment.setCorrectDate(false);
						return;						
					}

					if(d.compareTo(otherDate) != 0){
						accountingAssignment.addInfoLine("Datum fehlerhaft. Provision weicht ab: "+formatter.format(otherDate));
						accountingAssignment.setCorrectDate(false);
						return;
					}
					
				}else{
					accountingAssignment.setCorrectDate(false);
					return;
				}

			}
			
			
		}else{
			accountingAssignment.setCorrectDate(false);
			return;
		}	
		
		accountingAssignment.setCorrectDate(true);
		return;
	}
	


	@Override
	public AccountingAssignment factoryNewAccountingAssignment(BaseContract contract) {
		
		
		AccountingAssignmentEntity aae = new AccountingAssignmentEntity();
		
		Calendar c = Calendar.getInstance();
		c.setTime(contract.getEffectiveDate());
		
//		aae.setCommissionMonth(c.get(Calendar.MONTH));
//		aae.setCommissionYear(c.get(Calendar.YEAR));
//		aae.setDate(contract.getEffectiveDate());
//		aae.setVendor((VendorEntity) contract.getCachedTariff().getVendor());
		
		aae.setBaseContract(contract);
		
		aae = accountingEao.saveAccountingAssignment(aae);
		
		
//		System.out.println("NEW AccountingAssignment: "+aae.getId()+" contract:"+contract.getId());
		
//		contract.setAccountingAssignment(aae);
//		contract = contractEao.saveContract(contract);
//
		return aae;
	}
	
	private AccountingAssignment factoryNewAccountingAssignment( VendorCommissionAccountingItem accountingItem, Vendor vendor, int commissionYear, int commissionMonth) {

		AccountingAssignmentEntity aae = new AccountingAssignmentEntity(vendor, commissionYear, commissionMonth);
		aae.addAccountingItem(accountingItem);
		
		aae = accountingEao.saveAccountingAssignment(aae);

//		System.out.println("NEW AccountingAssignment: "+aae+" ID:"+aae.getId());
		return aae;
	}

	@Override
	public void updateAccountingAssignment(Vendor vendor, int year, int month) {
		
//		System.out.println("updateAccountingAssignment "+vendor.getName()+" "+month+" "+year);
		
//		boolean debugItem;
		
		Iterator<? extends AccountingAssignment> i2;
		
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Date from = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		
		Date to = c.getTime();		
		
		boolean onlyActive = true;
		
		List<? extends BaseContract> contractsList = contractEao.getContractList(vendor, from, to, onlyActive);
		Iterator<? extends BaseContract> i = contractsList.iterator();
		BaseContract contract;
		while(i.hasNext()){
			contract = i.next();
			
			if(contract.getAccountingAssignment() == null){
				factoryNewAccountingAssignment(contract);
			}
		}
		
		accountingEao.flush();


		List<? extends AccountingAssignment> list = getAccountingAssignmentList( vendor, year, month);
		
		
		AccountingAssignment aa;
		if(list == null){
			list = new LinkedList<AccountingAssignmentEntity>();
		}
		
			
		i2 = list.iterator();
		while(i2.hasNext()){
			aa = i2.next();
			aa.clearInfo();
			aa.setAccountingItemList(null);
		}
		
		
		List<? extends VendorCommissionAccountingFile> commissionAccountingFileList = getCommissionAccountingFileList(year, month);
		if(commissionAccountingFileList != null){
			Iterator<? extends VendorCommissionAccountingFile> i3 = commissionAccountingFileList.iterator();
			Iterator<? extends VendorCommissionAccountingItem> i4;
			
			
			VendorCommissionAccountingFile accountingFile;
			VendorCommissionAccountingItem accountingItem;
			Shop shop;
			User vUser;
			
			CommissionTypeAssociation cta;
			VO vo,voAktivierung;
			List<String> calculatedVOList = new ArrayList<String>();
			boolean foundAccountingItem;
			while(i3.hasNext()){
				accountingFile = i3.next();
				i4 = accountingFile.getItemList().iterator();
				while(i4.hasNext()){
					accountingItem = i4.next();
					
//					debugItem = false;
					
					if(accountingItem instanceof VodafoneCommissionAccountingItem){
						VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
						vo = voController.getVOByNumber(vItem.getVoIDAbrechnung());
						voAktivierung = voController.getVOByNumber(vItem.getVoIDAktivierung());
						if(voAktivierung != null){
							// clear VOCommission
							if(! calculatedVOList.contains(voAktivierung.getNumber())){
								VOCommission voCommission = voAktivierung.getVOCommission(year, month);
								voCommission.clear();
								voController.saveVO(voAktivierung);
								calculatedVOList.add(voAktivierung.getNumber());
							}
						}
						shop = vo == null ? null : vo.getShop();
						
						if(vItem.getProvisionsmodell().equalsIgnoreCase("AirTime Provision")){
							
							if(voAktivierung == null)
								continue;

							VOCommission voCommission = voAktivierung.getVOCommission(year, month);
						
							if(vItem.getProvisionsart().equalsIgnoreCase("Gesamtkundenbestand (Bonus AirTime)")){
								voCommission.setBonusAirtime(new BigDecimal(vItem.getNettobetragEinzelEUR().replace(",", ".").trim()));
							}
						
							if(vItem.getProvisionsart().equalsIgnoreCase("Gesamtkundenbestand (Basis AirTime)")){
								voCommission.setBaseAirtime(new BigDecimal(vItem.getNettobetragEinzelEUR().replace(",", ".").trim()));
							}
							
							voController.saveVO(voAktivierung);
							
							continue;
						}
						
						if(vItem.getProvisionsmodell().equalsIgnoreCase("Reparaturen")){

							if(voAktivierung == null)
								continue;

							VOCommission voCommission = voAktivierung.getVOCommission(year, month);
						
							voCommission.setRepairs( voCommission.getRepairs().add( new BigDecimal(vItem.getNettobetragEinzelEUR().replace(",", ".").trim())) );
							
							voController.saveVO(voAktivierung);
							
							continue;
						}
						
						if(vItem.getProvisionsmodell().equalsIgnoreCase("Sonderprovision") || vItem.getRufnummerBarcodeWebOrderID() == null || vItem.getRufnummerBarcodeWebOrderID().trim().length() == 0){
							

//							System.out.println("Sonderprovision for: "+vItem.getRufnummerBarcodeWebOrderID()+" "+vItem.getNettobetragEinzelEUR());
//							debugItem = true;
							

							if(voAktivierung == null)
								continue;

							VOCommission voCommission = voAktivierung.getVOCommission(year, month);
						
							if(vItem.getProvisionsart().equalsIgnoreCase("Service-Pakete")){
								voCommission.setServicePackages( voCommission.getServicePackages().add( new BigDecimal(vItem.getNettobetragEinzelEUR().replace(",", ".").trim())) );
							}
							
							voController.saveVO(voAktivierung);
							
							continue;
						}
						
						if(vItem.isCancelation()){

							
//							System.out.println("found cancelation for "+vItem.getAuftragsVO()+" "+vItem.getDatumAktivierung()+" "+vItem.getRufnummerBarcodeWebOrderID()+" "+vItem.getKundenname());
							

							aa = factoryNewAccountingAssignment(accountingItem, vo.getVendor(), year, month);
							
//							System.out.println("SEARCH FOR CONTRACT");
							
							Calendar c2 = Calendar.getInstance();
							
							c.setTime(vItem.getDate());
							c.add(Calendar.MONTH, -1);
							Date contractSearchFrom = c.getTime();
							
							c.setTime(vItem.getDate());
							c.add(Calendar.MONTH, 1);
							Date contractSearchTo = c.getTime();
							
							
							
							List<? extends BaseContractEntity> cancelationContracts = contractEao.findContract(aa.getCustomerId(), aa.getCallingNumber(), contractSearchFrom, contractSearchTo);
							// find contract to cancel
							
							if(cancelationContracts.size() == 1){
								// exact one contract found  -> good
								aa.setCancelationBaseContract(cancelationContracts.get(0));
//								System.out.println("Contract "+cancelationContracts.get(0).getId()+" found");
							}else if(cancelationContracts.size() > 1){
								// to many contracts found!!
//								System.out.println("too many contracts found");
							}
							
							
							
							aa = accountingEao.saveAccountingAssignment((AccountingAssignmentEntity) aa);

							((List<AccountingAssignment>)list).add(aa);
							
							continue;
						}
						
						
						
//						cta = getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
//						if(cta.getId() == 0)
//							continue;
						
						i2 = list.iterator();
						foundAccountingItem = false;
						while(i2.hasNext()){
							aa = i2.next();
							
							
							if(aa.getContract() == null && aa.getAccountingItemList().size() == 0){
								i2.remove();
								continue;
							}

							
							//check VO
							if(vItem.getVoIDAktivierung() == null)
								continue;
							

							
							if(aa.getVo() == null){
								continue;
							}

							
							// NO VO CHECK !
//							if(! aa.getVo().equalsIgnoreCase(vItem.getVoIDAbrechnung()))
//								continue;
							
							//check KdNr.
							if(! aa.getCustomerId().equalsIgnoreCase(vItem.getKundennummer()))
								continue;
							
							//check RufnummerBarcodeWebOrderID
							if(! aa.getCallingNumber().equalsIgnoreCase(vItem.getRufnummerBarcodeWebOrderID()) && ! aa.getCallingNumber().equalsIgnoreCase("0"+vItem.getRufnummerBarcodeWebOrderID()) )
								continue;
							
//							if(debugItem)
//								System.out.println("AccountingItem match Contract!");
							
							aa.addAccountingItem(accountingItem);
							foundAccountingItem = true;

							break;
						}
						
						if(! foundAccountingItem){
							
							if(vo != null){
								aa = factoryNewAccountingAssignment(accountingItem, vo.getVendor(), year, month);
								((List<AccountingAssignment>)list).add(aa);
							}
							
						}
						

						
						
						
					}
				}
			}
			
			
//			#####
			
			
		}
		
		
		i2 = list.iterator();
		while(i2.hasNext()){
			
			aa = i2.next();
			
			checkAccountingAssignment((AccountingAssignmentEntity) aa);

			aa = accountingEao.saveAccountingAssignment((AccountingAssignmentEntity) aa);
		}
			
		
			
	}

	

	@Override
	public List<? extends AccountingAssignment> getAccountingAssignmentList( Vendor vendor, int commissionYear, int commissionMonth) {
		
		List<? extends AccountingAssignment> list = accountingEao.getAccountingAssignmentList( vendor, commissionYear, commissionMonth);
//		System.out.println("getAccountingAssignmentList list size: "+list.size());
		
		Iterator<? extends AccountingAssignment> i = list.iterator();
		AccountingAssignment aa;
		while(i.hasNext()){
			aa = i.next();
			
//			System.out.println("getAccountingAssignmentList aa: "+aa.getId());
			
			accountingEao.refresh(aa);
			
			if(aa.getBaseContract() == null && aa.getCancelationBaseContract() == null && aa.getAccountingItemList().size() == 0){

				accountingEao.remove(aa);
				i.remove();
			}
		}
//		System.out.println("getAccountingAssignmentList list size: "+list.size());
		return list;
	}

	@Override
	public List<String> getUnknownUserAliasList(Vendor vendor, int year, int month) {

		List<String> unknownUserAliasList = new LinkedList<String>();
		Iterator<? extends VendorCommissionAccountingFile> i1;
		Iterator<? extends VendorCommissionAccountingItem> i2;
		List<? extends VendorCommissionAccountingFile> commissionAccountingFileList;
		VendorCommissionAccountingFile accountingFile;
		VendorCommissionAccountingItem accountingItem;
		User vUser;
		VodafoneCommissionAccountingItem vItem;
		
		commissionAccountingFileList = getCommissionAccountingFileList(year, month);
		if(commissionAccountingFileList != null){
			i1 = commissionAccountingFileList.iterator();
			
			while(i1.hasNext()){
				accountingFile = i1.next();
				i2 = accountingFile.getItemList().iterator();
				while(i2.hasNext()){
					accountingItem = i2.next();
					
					if(accountingItem instanceof VodafoneCommissionAccountingItem){
						vItem = (VodafoneCommissionAccountingItem) accountingItem;
						
						vUser = userController.getUserByAlias(vItem.getVerkaeufer());
						if(vUser == null && vItem.getVerkaeufer().trim().length() > 0){
							if(! unknownUserAliasList.contains(vItem.getVerkaeufer().trim())){
								unknownUserAliasList.add(vItem.getVerkaeufer().trim());
							}
						}
					}
				}
			}
		}
		
		return unknownUserAliasList;
	}


	@Override
	public List<CommissionTypeAssociation> getUnknownCommissionTypeAssociationList(Vendor vendor, int year, int month) {

		List<CommissionTypeAssociation> unknownCommissionTypeAssociationList = new LinkedList<CommissionTypeAssociation>();
		
		Iterator<? extends VendorCommissionAccountingFile> i1;
		Iterator<? extends VendorCommissionAccountingItem> i2;
		List<? extends VendorCommissionAccountingFile> commissionAccountingFileList;
		VendorCommissionAccountingFile accountingFile;
		VendorCommissionAccountingItem accountingItem;
		VodafoneCommissionAccountingItem vItem;
		CommissionTypeAssociation cta;
		
		commissionAccountingFileList = getCommissionAccountingFileList(year, month);
		if(commissionAccountingFileList != null){
			i1 = commissionAccountingFileList.iterator();
			
			while(i1.hasNext()){
				accountingFile = i1.next();
				i2 = accountingFile.getItemList().iterator();
				while(i2.hasNext()){
					accountingItem = i2.next();
					
					if(accountingItem instanceof VodafoneCommissionAccountingItem){
						vItem = (VodafoneCommissionAccountingItem) accountingItem;

						if(vItem.getProvisionsmodell().equalsIgnoreCase("AirTime Provision")){
							continue;
						}
						
						if(vItem.getProvisionsmodell().equalsIgnoreCase("Reparaturen")){
							continue;
						}
						
						cta = getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
						if(cta.getId() == 0 && ! unknownCommissionTypeAssociationList.contains(cta)){
							
							cta.setVendor(productController.getDefaultVendor());
							unknownCommissionTypeAssociationList.add(cta);
						}
						
						
					}
				}
			}
		}
		
		return unknownCommissionTypeAssociationList;
	}

	@Override
	public List<? extends CommissionTypeAssociation> getCommissionTypeAssociationList(
			Vendor vendor) {
		return accountingEao.getCommissionTypeAssociationList(vendor);
	}

	@Override
	public void deleteCommissionTypeAssociation(
			CommissionTypeAssociation commissionTypeAssociation) {
		accountingEao.deleteCommissionTypeAssociation(commissionTypeAssociation);
	}

	@Override
	public ManualCommission factoryNewManualCommission(User creator) {
		ManualCommission manualCommission = new ManualCommissionEntity();
		manualCommission.setCreator(creator);
		manualCommission.setEnabled(true);
		
		return manualCommission;
	}

	@Override
	public ManualCommission saveManualCommission(ManualCommission manualCommission) {
		manualCommission = accountingEao.saveManualCommission(manualCommission);
		
		commissionController.updateUserComissionHistory(manualCommission.getUser());
		commissionController.updateShopComissionHistory(manualCommission.getShop());
		
		return manualCommission;
	}

	@Override
	public List<? extends ManualCommission> getManualCommissionList() {
		return accountingEao.getManualCommissionList();
	}
}

