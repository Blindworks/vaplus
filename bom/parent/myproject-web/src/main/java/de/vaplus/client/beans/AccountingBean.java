package de.vaplus.client.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.CommissionAccountingControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.ManualCommission;
import de.vaplus.api.entity.PhoneContract;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingItem;

@ManagedBean(name = "accountingBean")
@SessionScoped
public class AccountingBean implements Serializable {

	private static final long serialVersionUID = -4994017198578278590L;

	@EJB
	private CommissionAccountingControllerInterface commissionAccountingController;
	
	@EJB
	private ProductControllerInterface productController;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private AccountingControllerInterface accountingController;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private UserControllerInterface userController;

	@EJB
	private VOControllerInterface voController;

	@Inject
	private FacesContext facesContext;

    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;
	
	private List<Part> importFiles;
	
	private String importLog;
	
	private String importError;

	private Vendor selectedVendor;

	private int selectedMonth;
	
	private int selectedYear;
	
	private UserAlias unknownUserAlias;
	private CommissionTypeAssociation unknownCommissionTypeAssociation;

	private List<CommissionTypeAssociation> unknownCommissionTypeAssociationList;
	private List<? extends CommissionTypeAssociation> commissionTypeAssociationList;
	private List<String> unknownUserAliasList;
	private List<? extends BaseContract> contractList;
//	private Map<String, AccountingAssignment> accountingAssignmentMap;
	private List<? extends AccountingAssignment> accountingAssignmentList;
	private List<? extends AccountingAssignment> currentAccountingAssignmentList;
	private List<? extends AccountingAssignment> previousAccountingAssignmentList;
	private Map<Shop, BigDecimal> airtimeSum;
	private Map<Shop, BigDecimal> retairSum;
	
	private CommissionTypeAssociation selectedCommissionTypeAssociation;
	
	private ManualCommission selectedManualCommission;

	BigDecimal currentAccountingAssignmentSum;
	BigDecimal currentAccountingAssignmentPositiveSum;
	BigDecimal currentAccountingAssignmentNegativeSum;
	
	@PostConstruct
	public void init(){

		selectedVendor = productController.getDefaultVendor();

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.add(Calendar.MONTH, -1);
	    selectedYear = cal.get(Calendar.YEAR);
	    selectedMonth = cal.get(Calendar.MONTH);
	}
	
	public void clear(){
		contractList = null;
		accountingAssignmentList = null;
		currentAccountingAssignmentList = null;
		previousAccountingAssignmentList = null;
		airtimeSum = null;
		retairSum = null;
		unknownCommissionTypeAssociation = null;
		unknownCommissionTypeAssociationList = null;
		unknownUserAlias = null;
		unknownUserAliasList = null;
		commissionTypeAssociationList = null;
		currentAccountingAssignmentSum = null;
		currentAccountingAssignmentPositiveSum = null;
		currentAccountingAssignmentNegativeSum = null;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public Vendor getSelectedVendor() {
		return selectedVendor;
	}

	public void setSelectedVendor(Vendor selectedVendor) {
		this.selectedVendor = selectedVendor;
		clear();
	}

	public List<? extends AccountingAssignment> getAccountingAssignmentList() {
		if(accountingAssignmentList == null || accountingAssignmentList.size() == 0)
			this.generateAccountingAssignmentList();
		return accountingAssignmentList;
	}


	public int getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(int selectedMonth) {
		this.selectedMonth = selectedMonth;
		clear();
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
		clear();
	}	
	
	private Date getFrom(){

	    Calendar cal = Calendar.getInstance();
	    cal.set(selectedYear, selectedMonth, 1, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
	private Date getTo(){

	    Calendar cal = Calendar.getInstance();
	    cal.set(selectedYear, selectedMonth +1, 1, 0, 0, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    cal.add(Calendar.SECOND, -1);
	    return cal.getTime();
	}

	public List<Part> getImportFiles() {
		return importFiles;
	}

	public void setImportFiles(List<Part> importFiles) {
		this.importFiles = importFiles;
	}

	public String getImportLog() {
		if(importLog != null)
			return importLog.replaceAll("\\n", "<br/>");
		return null;
	}

	public String getImportError() {
		if(importError != null)
			return importError.replaceAll("\\n", "<br/>");
		return null;
	}
	
	public List<Integer> getYearList(){
		int firstYear = contractController.getFirstContractYear();
		
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    int actYear = cal.get(Calendar.YEAR);
	    
	    List<Integer> yearList = new LinkedList<Integer>();
		
		for(int i = firstYear; i <= actYear; i++){
			yearList.add(i);
		}
		
		return yearList;
	}

	public void proceedImport(){
		System.out.println("proceedImport");
		

		importLog = "";
		importError = "";
		
		Iterator<Part> i = importFiles.iterator();
		Part p;
		while(i.hasNext()){
			p = i.next();

			System.out.println("Next File: "+p.getContentType()+" "+p.getSize());
			
			if(p.getSize() == 0)
				continue;
			
			if(! p.getContentType().equalsIgnoreCase("text/csv") 
					&& ! p.getContentType().equalsIgnoreCase("text/plain") 
					&& ! p.getContentType().equalsIgnoreCase("text/comma-separated-values")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.ms-excel")){
				importError += "Import Fehler: Falscher Dateityp!\n";
				System.out.println("Import Fehler: Falscher Dateityp!");
				continue;
			}	
			
			try {
				
				if(importLog.length() > 0)
					importLog += "\n";
				
				commissionAccountingController.importVendorAccountingCsv(p.getInputStream());
				importLog += p.getSubmittedFileName()+" erfolgreich importiert";
				
			} catch (Exception e) {
				importError += "Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage()+"\n";
				System.out.println("Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage());
			}
			
			
		}
		
	}

	
//	public List<? extends BaseContract> getContractList() {
//		if(contractList == null)
//			contractList = contractController.getContractList(null, null, null, getFrom(), getTo());
//		
//		return contractList;
//	}
	
	private void generateAccountingAssignmentList(){
		
		accountingAssignmentList = accountingController.getAccountingAssignmentList(selectedVendor, selectedYear, selectedMonth);
		
		
//		accountingAssignmentMap = new HashMap<String, AccountingAssignment>();
//		Iterator<? extends BaseContract> i = getContractList().iterator();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatterImport = new SimpleDateFormat("dd.MM.yyyy");
		airtimeSum = new HashMap<Shop, BigDecimal>();
		retairSum = new HashMap<Shop, BigDecimal>();
		BaseContract contract;
		PhoneContract pContract;
		String key;
		Date d = null;
		
		
		
		
		
		// Contracts
		
//		while(i.hasNext()){
//			contract = i.next();
//			
//			if(contract instanceof PhoneContract){
//				pContract = (PhoneContract) contract;
//			
//				if(pContract.getTariff() instanceof LandlineTariff){
//					
////					key = formatter.format(contract.getEffectiveDate())+"-"+pContract.getExternalCustomer().getCustomerId();
//					key = pContract.getVo().getNumber()+"-"+pContract.getExternalCustomer().getCustomerId();
//				}else{
////					key = formatter.format(contract.getEffectiveDate())+"-"+pContract.getCallingNumber();
//					key = pContract.getVo().getNumber()+"-"+pContract.getCallingNumber();
//				}
//				
////				System.out.println("contract key: "+key);
//				
////				if(!getAccountingAssignmentMap().containsKey(key)){
////					getAccountingAssignmentMap().put(key, accountingController.factoryAccountingAssignment(key, pContract) );
////				}
//				
////				else{
////					getAccountingAssignmentMap().get(key).getpContractList().add(pContract);
////				}
//			}
//		}
			
		/*
		
		// AccountingItems
		List<? extends VendorCommissionAccountingFile> commissionAccountingFileList = accountingController.getCommissionAccountingFileList(selectedYear, selectedMonth);
		Iterator<? extends VendorCommissionAccountingFile> i2 = commissionAccountingFileList.iterator();
		Iterator<? extends VendorCommissionAccountingItem> i3;
		VendorCommissionAccountingFile accountingFile;
		VendorCommissionAccountingItem accountingItem;
		Shop shop;
		User vUser;
		CommissionTypeAssociation cta;
		VO vo;
		while(i2.hasNext()){
			accountingFile = i2.next();
			i3 = accountingFile.getItemList().iterator();
			while(i3.hasNext()){
				accountingItem = i3.next();
				
				if(accountingItem instanceof VodafoneCommissionAccountingItem){
					VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
					vo = voController.getVOByNumber(vItem.getVoIDAbrechnung());
					shop = vo == null ? null : vo.getShop();
					
					vUser = userController.getUserByAlias(vItem.getVerkaeufer());
					if(vUser == null && vItem.getVerkaeufer().trim().length() > 0){
						if(! getUnknownUserAliasList().contains(vItem.getVerkaeufer().trim())){
							getUnknownUserAliasList().add(vItem.getVerkaeufer().trim());
						}
					}
					
					if(vItem.getProvisionsmodell().equalsIgnoreCase("AirTime Provision")){

						if(shop == null)
							continue;
						
						if(! airtimeSum.containsKey(shop))
							airtimeSum.put(shop, new BigDecimal(0));
						
						
						airtimeSum.put(
								shop, 
									airtimeSum.get(shop).add( new BigDecimal(vItem.getNettobetragEinzelEUR().replace(",", ".").trim()))
								);
						
						continue;
					}
					
					if(vItem.getProvisionsmodell().equalsIgnoreCase("Reparaturen")){

						if(shop == null)
							continue;
						
						if(! retairSum.containsKey(shop))
							retairSum.put(shop, new BigDecimal(0));
						
						
						retairSum.put(
								shop, 
								retairSum.get(shop).add( new BigDecimal(vItem.getNettobetragEinzelEUR().replace(",", ".").trim()))
								);
						
						continue;
					}
					
					

					cta = accountingController.getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
					if(! getUnknownCommissionTypeAssociationList().contains(cta)){
						
						cta.setVendor(productController.getDefaultVendor());
						
						getUnknownCommissionTypeAssociationList().add(cta);
					}
					
//					try {
						if(vItem.getDatumAktivierung().trim().length() > 0){
//							d = formatterImport.parse(vItem.getDatumAktivierung());
//							key = formatter.format(d)+"-0"+vItem.getRufnummerBarcodeWebOrderID();
							key = vItem.getVoIDAktivierung()+"-0"+vItem.getRufnummerBarcodeWebOrderID();
						}else{
							key = "-";
						}

//						System.out.println("accounting key: "+key);
						
//						if(!getAccountingAssignmentMap().containsKey(key)){
//							getAccountingAssignmentMap().put(key, accountingController.factoryAccountingAssignment(key) );
//						}
//						
//						((List<VendorCommissionAccountingItem>)getAccountingAssignmentMap().get(key).getAccountingItemList()).add(vItem);
						
		
//						System.out.println("ITEM: "+vItem.getProvisionsmodell()+" | "+vItem.getProvisionsart()+" | "+vItem.getTarifbeschreibung()+" | "+vItem.getTarifcode()+" | "+vItem.getKommentar());
						
						
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
					
					
					
				}
			}
		}
		
		
		*/
		
		// check getAccountingAssignment Items
//		Iterator<String> ik = getAccountingAssignmentMap().keySet().iterator();
//		while(ik.hasNext()){
//			key = ik.next();
//			accountingController.checkAccountingAssignment(getAccountingAssignmentMap().get(key));
//		}
	}
	
//	public void checkCommissionTypeAssociations(){
//		List<? extends VendorCommissionAccountingItem> list = accountingController.getVendorCommissionAccountingItemEntity(getFrom(), getTo());
//		Iterator<? extends VendorCommissionAccountingItem> i = list.iterator();
//		VendorCommissionAccountingItem item;
//		while(i.hasNext()){
//			item = i.next();
//			
//			if(item instanceof VodafoneCommissionAccountingItem){
//				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) item;
//
//				System.out.println("ITEM: "+vItem.getProvisionsmodell()+" | "+vItem.getProvisionsart()+" | "+vItem.getTarifbeschreibung()+" | "+vItem.getTarifcode()+" | "+vItem.getKommentar());
//				
//				
//			}
//		}
//	}

//	public Map<String, AccountingAssignment> getAccountingAssignmentMap() {
//		if(accountingAssignmentMap == null){
//			generateAccountingAssignmentList();
//		}
//		
//		return accountingAssignmentMap;
//	}
//
//	public void setAccountingAssignmentMap(Map<String, AccountingAssignment> accountingAssignmentMap) {
//		this.accountingAssignmentMap = accountingAssignmentMap;
//	}
	
	public List<? extends AccountingAssignment> getCurrentAccountingAssignmentList(){
		
		if(currentAccountingAssignmentList == null || currentAccountingAssignmentList.size() == 0){
			currentAccountingAssignmentList = getAccountingAssignmentList();
			

//			Date from = this.getFrom();
//			
//			Iterator<? extends AccountingAssignment> i = currentAccountingAssignmentList.iterator();
//			AccountingAssignment a;
//			while(i.hasNext()){
//				a = i.next();
//				if(a.getSortingTime() < from.getTime()){
//					i.remove();
//				}
//				
////				if(a.getpContractList().size() > 1){
////					System.out.println("AccountingAssignment: "+a.getpContractList().size()+" Contracts:");
////					System.out.println("Contract: "+a.getpContractList().get(0).getCallingNumber());
////				}
//				
////				if(a.getAccountingItemList().size() > 1){
////					System.out.println("AccountingAssignment: "+a.getAccountingItemList().size()+" AccountingItems:");
////					System.out.println("AccountingItem: "+((VodafoneCommissionAccountingItem)a.getAccountingItemList().get(0)).getRufnummerBarcodeWebOrderID());
////				}
//				
//			}
//			
//			Collections.sort(currentAccountingAssignmentList);
		}
		
		return currentAccountingAssignmentList;
	}
	
	public void generteCurrentAccountingAssignmentSum(){
		currentAccountingAssignmentSum = new BigDecimal(0);
		currentAccountingAssignmentPositiveSum = new BigDecimal(0);
		currentAccountingAssignmentNegativeSum = new BigDecimal(0);
		
		Iterator<? extends AccountingAssignment> i = getCurrentAccountingAssignmentList().iterator();
		AccountingAssignment a;
		while(i.hasNext()){
			a = i.next();
			
			currentAccountingAssignmentSum = currentAccountingAssignmentSum.add(a.getSum());
//			System.out.println(a.getSum());
			if(a.getSum().compareTo(new BigDecimal(0)) > 0){
//				System.out.println("p");
				currentAccountingAssignmentPositiveSum = currentAccountingAssignmentPositiveSum.add(a.getSum());
			}else{
				currentAccountingAssignmentNegativeSum = currentAccountingAssignmentNegativeSum.add(a.getSum());
			}
		}
	}
	
	public BigDecimal getCurrentAccountingAssignmentSum(){
		
		if(currentAccountingAssignmentSum == null){
			generteCurrentAccountingAssignmentSum();
		}
		
		return currentAccountingAssignmentSum;
	}
	
	public BigDecimal getCurrentAccountingAssignmentPositiveSum(){
		
		if(currentAccountingAssignmentPositiveSum == null){
			generteCurrentAccountingAssignmentSum();
		}
		
		return currentAccountingAssignmentPositiveSum;
	}
	
	public BigDecimal getCurrentAccountingAssignmentNegativeSum(){
		
		if(currentAccountingAssignmentNegativeSum == null){
			generteCurrentAccountingAssignmentSum();
		}
		
		return currentAccountingAssignmentNegativeSum;
	}
	
	public List<? extends AccountingAssignment> getPreviousAccountingAssignmentList(){
		
		
		previousAccountingAssignmentList = new LinkedList<AccountingAssignment>();
		
//		if(previousAccountingAssignmentList == null){
//			previousAccountingAssignmentList = getAccountingAssignmentList();

//			Date from = this.getFrom();
//			
//			Iterator<? extends AccountingAssignment> i = previousAccountingAssignmentList.iterator();
//			AccountingAssignment a;
//			while(i.hasNext()){
//				a = i.next();
//				if(a.getSortingTime() >= from.getTime())
//					i.remove();
//			}
//			
//			Collections.sort(previousAccountingAssignmentList);
//		}
		
		return previousAccountingAssignmentList;
	}
	
	public BigDecimal getPreviousAccountingAssignmentSum(){
		BigDecimal sum = new BigDecimal(0);
		Iterator<? extends AccountingAssignment> i = getPreviousAccountingAssignmentList().iterator();
		AccountingAssignment a;
		while(i.hasNext()){
			a = i.next();
			sum.add(a.getSum());
		}
		
		return sum;
	}

	public Map<Shop, BigDecimal> getAirtimeSum() {
		if(airtimeSum == null)
			generateAccountingAssignmentList();
		return airtimeSum;
	}

	public void setAirtimeSum(Map<Shop, BigDecimal> airtimeSum) {
		this.airtimeSum = airtimeSum;
	}

	public Map<Shop, BigDecimal> getRetairSum() {
		if(retairSum == null)
			generateAccountingAssignmentList();
		return retairSum;
	}

	public void setRetairSum(Map<Shop, BigDecimal> retairSum) {
		this.retairSum = retairSum;
	}

	public List<String> getUnknownUserAliasList() {
		if(unknownUserAliasList == null){
			unknownUserAliasList = accountingController.getUnknownUserAliasList(selectedVendor, selectedYear, selectedMonth);
		}
		
		return unknownUserAliasList;
	}

	public void setUnknownUserAliasList(List<String> unknownUserAliasList) {
		this.unknownUserAliasList = unknownUserAliasList;
	}
	
	public UserAlias getUnknownUserAlias(){
		
		if(unknownUserAlias != null)
			return unknownUserAlias;

		Iterator<String> i = getUnknownUserAliasList().iterator();
		User u;
		String alias;
		while(i.hasNext()){
			alias = i.next();
			
			u = userController.getUserByAlias(alias);
			if(u == null){
				System.out.println("return new Alias for "+alias);
				unknownUserAlias = userController.factoryNewUserAlias(alias);
				break;
			}
		}
		
		return unknownUserAlias;
	}
	
	public void saveUnknownUserAlias(){
		if(unknownUserAlias != null && unknownUserAlias.getUser() != null){
			userController.saveUserAlias(unknownUserAlias);
			unknownUserAlias = null;
		}
	}


	public List<CommissionTypeAssociation> getUnknownCommissionTypeAssociationList() {
		
		if(unknownCommissionTypeAssociationList == null){
			unknownCommissionTypeAssociationList = accountingController.getUnknownCommissionTypeAssociationList(selectedVendor, selectedYear, selectedMonth);
		}
		return unknownCommissionTypeAssociationList;
	}

	public List<? extends CommissionTypeAssociation> getCommissionTypeAssociationList() {
		
		if(commissionTypeAssociationList == null){
			commissionTypeAssociationList = accountingController.getCommissionTypeAssociationList(selectedVendor);
		}
		return commissionTypeAssociationList;
	}

	public void setUnknownCommissionTypeAssociationList(List<CommissionTypeAssociation> unknownCommissionTypeAssociationList) {
		this.unknownCommissionTypeAssociationList = unknownCommissionTypeAssociationList;
	}
	
	public CommissionTypeAssociation getUnknownCommissionTypeAssociation(){
		
		if(unknownCommissionTypeAssociation != null)
			return unknownCommissionTypeAssociation;

		Iterator<CommissionTypeAssociation> i = getUnknownCommissionTypeAssociationList().iterator();
		CommissionTypeAssociation cta,cta2;
		while(i.hasNext()){
			cta = i.next();

			cta2 = accountingController.getCommissionTypeAssociationByText(cta.getCommissionText(), cta.getCommissionSubText());
			if(cta2 == null){
				unknownCommissionTypeAssociation = cta;
				break;
			}
		}
		
		return unknownCommissionTypeAssociation;
	}

	public void saveUnknownCommissionTypeAssociation(){
		if(unknownCommissionTypeAssociation != null){
			accountingController.saveCommissionTypeAssociation(unknownCommissionTypeAssociation);
			unknownCommissionTypeAssociation = null;
		}
	}

	public void saveSelectedCommissionTypeAssociation(){
		if(selectedCommissionTypeAssociation != null){
			accountingController.saveCommissionTypeAssociation(selectedCommissionTypeAssociation);
			selectedCommissionTypeAssociation = null;
			clear();
		}
	}
//	
//	public void setUnknownUserAlias(UserAlias userAlias){
//		unknownUserAlias = 
//	}

	public CommissionTypeAssociation getSelectedCommissionTypeAssociation() {
		return selectedCommissionTypeAssociation;
	}
	
	public void setSelectedCommissionTypeAssociation(CommissionTypeAssociation selectedCommissionTypeAssociation) {
		this.selectedCommissionTypeAssociation = selectedCommissionTypeAssociation;
	}

	public void setSelectedCommissionTypeAssociationByCommissionAccountingItem(VendorCommissionAccountingItem accountingItem) {
		this.selectedCommissionTypeAssociation = accountingController.getCommissionTypeAssociationByCommissionAccountingItem(accountingItem);
	}
	
	public void updateAccountingAssignment(){
		accountingController.updateAccountingAssignment(selectedVendor, selectedYear, selectedMonth);
		clear();
	}
	
	public String deleteSelectedCommissionTypeAssociation(){
		accountingController.deleteCommissionTypeAssociation(selectedCommissionTypeAssociation);
		return "commissionTypeAssociationList";
	}
	
	public String saveCommissionTypeAssociation(CommissionTypeAssociation commissionTypeAssociation){
		commissionTypeAssociation = accountingController.saveCommissionTypeAssociation(commissionTypeAssociation);
		return "commissionTypeAssociationList";
	}

	public ManualCommission getSelectedManualCommission() {
		if(selectedManualCommission == null)
			selectedManualCommission = accountingController.factoryNewManualCommission(getUserBean().getActiveUser());
		return selectedManualCommission;
	}

	public void setSelectedManualCommission(ManualCommission selectedManualCommission) {
		this.selectedManualCommission = selectedManualCommission;
	}
	
	public void saveManualCommission(){
		accountingController.saveManualCommission(selectedManualCommission);
		selectedManualCommission = null;
	}
	
	public void clearManualCommission(){
		selectedManualCommission = null;
	}
	
	public void deleteManualCommission(){
		selectedManualCommission.setDeleted();
		saveManualCommission();
	}
	
	public List<? extends ManualCommission> getManualCommissionList(){
		return accountingController.getManualCommissionList();
	}
}
