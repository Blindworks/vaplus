package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.ImportLine;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.controller.ImportControllerInterface;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.pojo.CsvFileReadResult;
import de.vaplus.api.pojo.ImportResult;

@ManagedBean(name = "importBean")
@SessionScoped
public class ImportBean implements Serializable {

	private static final long serialVersionUID = -4994017198578278590L;

	public static final int IMPORT_TYPE_PWSCUBE_24_ROW = 1;
	public static final int IMPORT_TYPE_SORTICS_2_5_4 = 2;
	public static final int IMPORT_TYPE_EPOS_ACTIVATION = 3;
	public static final int IMPORT_TYPE_EPOS_PORTING = 4;
	public static final int IMPORT_TYPE_EPOS_DC_CHANGE = 5;
	public static final int IMPORT_TYPE_PWSCUBE_45_ROW = 6;
	
	@EJB
	private ImportControllerInterface importController;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private UserControllerInterface userController;

	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private VOControllerInterface voController;
	
	private List<Part> importFiles;
	
	private String importLog;
	
	private String importError;
	
	private int importingType;
	
	private List<? extends ImportLine> importLines;


	private UserAlias unknownUserAlias;
	private List<String> unknownUserAliasList;

	private ShopAlias unknownShopAlias;
	private List<String> unknownShopAliasList;
	
	public ImportBean(){
		clear();
	}
	
	public void clear(){
		importLines = new LinkedList<ImportLine>();
		importLog = "";
		importError = "";
		unknownUserAliasList = null;
		unknownUserAlias = null;
	}
	
	public Map<Integer, String> getImportingTypes() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(IMPORT_TYPE_PWSCUBE_24_ROW, "PSW Cube (24 Spaltig)");
		map.put(IMPORT_TYPE_PWSCUBE_45_ROW, "PSW Cube (45 Spaltig)");
		map.put(IMPORT_TYPE_SORTICS_2_5_4, "Sortics 2.5.4");
		map.put(IMPORT_TYPE_EPOS_ACTIVATION, "EPOS Aktivierung");
		map.put(IMPORT_TYPE_EPOS_PORTING, "EPOS Portierung");
		map.put(IMPORT_TYPE_EPOS_DC_CHANGE, "EPOS DC Wechsel");
		return map;
	}

	public int getImportingType() {
		return importingType;
	}

	public void setImportingType(int importingType) {
		this.importingType = importingType;
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
	

	public List<? extends ImportLine> getImportLines() {
		return importLines;
	}

	public void setImportLines(List<? extends ImportLine> importLines) {
		this.importLines = importLines;
	}

	public void proceedImport(){
//		System.out.println("proceedImport");
		

		importLog = "";
		importError = "";
		
		if( this.getImportingType() == 0){
			importError += "Bitte Format wählen und erneut versuchen.\n";
			System.out.println("Import Fehler: Kein Format gewählt!");
			return;
		}	
		
		Iterator<Part> i = importFiles.iterator();
		Part p;
		CsvFileReadResult result = new CsvFileReadResult();
		while(i.hasNext()){
			p = i.next();
			
//			System.out.println("Next File: "+p.getContentType()+" "+p.getSize()+" "+p.getName()+" "+p.getSubmittedFileName());
			
			if(p.getSize() == 0)
				continue;
			
			switch(importingType){
			case IMPORT_TYPE_PWSCUBE_24_ROW:
			case IMPORT_TYPE_PWSCUBE_45_ROW:
//				System.out.println("proceed PSW Cube Import");
				
				if(! p.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") && 
						! p.getContentType().equalsIgnoreCase("application/vnd.ms-excel")){
					importError += "Import Fehler: Falscher Dateityp!\n";
					System.out.println("Import Fehler: Falscher Dateityp!");
					continue;
				}	
				
				try {
					
					if(importLog.length() > 0)
						importLog += "\n";
					
					

					if(importingType == IMPORT_TYPE_PWSCUBE_24_ROW)
						result = importController.readPSWCube24Row(p.getInputStream());
					if(importingType == IMPORT_TYPE_PWSCUBE_45_ROW)
						result = importController.readPSWCube45Row(p.getInputStream());
					
					((List<ImportLine>)importLines).addAll( result.importLines );

					importLog += result.csvLines+" Zeilen verarbeitet."+"\n";
					importLog += result.importLines.size()+" Datenzeilen zum import vorgemerkt."+"\n";
					importLog += p.getSubmittedFileName()+" erfolgreich eingelesen."+"\n";
					if(result.cancelationLines > 0)
						importLog += result.cancelationLines+" Storno Zeilen übersprungen."+"\n";
					
				} catch (Exception e) {
					importError += "Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage()+"\n";
					System.out.println("Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage());
					
					e.printStackTrace();
				}
				
				break;
				
			case IMPORT_TYPE_SORTICS_2_5_4:
//				System.out.println("proceed Sortics Import");
				
				if(! p.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") && 
						! p.getContentType().equalsIgnoreCase("application/vnd.ms-excel")){
					importError += "Import Fehler: Falscher Dateityp!\n";
					System.out.println("Import Fehler: Falscher Dateityp!");
					continue;
				}	
				
				try {
					
					if(importLog.length() > 0)
						importLog += "\n";
					
					result = importController.readSortics_2_5_4(p.getInputStream());

					((List<ImportLine>)importLines).addAll( result.importLines );

					importLog += result.csvLines+" Zeilen verarbeitet."+"\n";
					importLog += result.importLines.size()+" Datenzeilen zum import vorgemerkt."+"\n";
					importLog += p.getSubmittedFileName()+" erfolgreich eingelesen."+"\n";
					if(result.cancelationLines > 0)
						importLog += result.cancelationLines+" Storno Zeilen übersprungen."+"\n";
					
				} catch (Exception e) {
					importError += "Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage()+"\n";
					System.out.println("Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage());
				}
				
				break;
				
			case IMPORT_TYPE_EPOS_ACTIVATION:
//				System.out.println("proceed EPOS Activation Import: "+p.getContentType());
				
				if(! p.getContentType().equalsIgnoreCase("text/csv")){
					importError += "Import Fehler: Falscher Dateityp!\n";
					System.out.println("Import Fehler: Falscher Dateityp!");
					continue;
				}	
				
				try {
					
					if(importLog.length() > 0)
						importLog += "\n";
					
					result = importController.readEposActivation(p.getInputStream());

					((List<ImportLine>)importLines).addAll( result.importLines );

					importLog += result.csvLines+" Zeilen verarbeitet."+"\n";
					importLog += result.importLines.size()+" Datenzeilen zum import vorgemerkt."+"\n";
					importLog += p.getSubmittedFileName()+" erfolgreich eingelesen."+"\n";
					if(result.cancelationLines > 0)
						importLog += result.cancelationLines+" Storno Zeilen übersprungen."+"\n";
					
				} catch (Exception e) {
					importError += "Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage()+"\n";
					System.out.println("Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage());
				}
				
				break;

			case IMPORT_TYPE_EPOS_PORTING:
			case IMPORT_TYPE_EPOS_DC_CHANGE:
//				System.out.println("proceed EPOS Other Import: "+p.getContentType());
				
				if(! p.getContentType().equalsIgnoreCase("text/csv")){
					importError += "Import Fehler: Falscher Dateityp!\n";
					System.out.println("Import Fehler: Falscher Dateityp!");
					continue;
				}	
				
				try {
					
					if(importLog.length() > 0)
						importLog += "\n";
					
					result = importController.readEposOther(p.getInputStream()) ;

					((List<ImportLine>)importLines).addAll( result.importLines );

					importLog += result.csvLines+" Zeilen verarbeitet."+"\n";
					importLog += result.importLines.size()+" Datenzeilen zum import vorgemerkt."+"\n";
					importLog += p.getSubmittedFileName()+" erfolgreich eingelesen."+"\n";
					if(result.cancelationLines > 0)
						importLog += result.cancelationLines+" Storno Zeilen übersprungen."+"\n";
					
				} catch (Exception e) {
					importError += "Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage()+"\n";
					System.out.println("Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage());
				}
				
				break;
			}
			
		}
		
		List<String> unknownVOList = getUnknownVOList();
		if(unknownVOList != null){
			Iterator<String> voI = unknownVOList.iterator();
			while(voI.hasNext()){
				importError += "VO nicht eingerichtet: "+voI.next()+"\n";
			}
		}
		
		
		
	}
	
	public void importData(){
		
		if(!importController.isImportWorkerRunning()){
			importController.importData(importLines);
			clear();
		}
		else{
			System.out.println("Ein Import Task läuft bereits");
		}
		
//		clear();
//
//		importLog += importResult.importetCustomer +" Kunden importiert."+"\n";
//		importLog += importResult.importetContracts +" Verträge importiert."+"\n";
		
	}
	
	public ImportResult getImportResult(){
		return importController.getImportResult();
	}
	
	public boolean isImportRunning(){
		return importController.isImportWorkerRunning();
	}
	
	public List<String> getUnknownVOList() {
		List<String> unknownVOList = importController.getUnknownVOList(importLines);
		
		return unknownVOList;
	}
	
	public List<String> getUnknownUserAliasList() {
		if(unknownUserAliasList == null){
			unknownUserAliasList = importController.getUnknownUserAliasList(importLines);
		}
		
		return unknownUserAliasList;
	}

	public void setUnknownUserAliasList(List<String> unknownUserAliasList) {
		this.unknownUserAliasList = unknownUserAliasList;
	}
	
	public UserAlias getUnknownUserAlias(){
		
		if(unknownUserAlias != null)
			return unknownUserAlias;
		
		List<String> list = getUnknownUserAliasList();
		if(list == null)
			return null;

		Iterator<String> i = list.iterator();
		User u;
		String alias;
		while(i.hasNext()){
			alias = i.next();
			
			u = userController.getUserByAlias(alias);
			if(u == null){
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

	
	public List<String> getUnknownShopAliasList() {
		if(unknownShopAliasList == null){
			unknownShopAliasList = importController.getUnknownShopAliasList(importLines);
		}
		
		return unknownShopAliasList;
	}

	public void setUnknownShopAliasList(List<String> unknownShopAliasList) {
		this.unknownShopAliasList = unknownShopAliasList;
	}
	
	public ShopAlias getUnknownShopAlias(){
		
		if(unknownShopAlias != null)
			return unknownShopAlias;
		
		List<String> list = getUnknownShopAliasList();
		if(list == null)
			return null;

		Iterator<String> i = list.iterator();
		Shop s;
		String alias;
		while(i.hasNext()){
			alias = i.next();
			
			s = shopController.getShopByAlias(alias);
			if(s == null){
				unknownShopAlias = shopController.factoryNewShopAlias(alias);
				break;
			}
		}
		
		return unknownShopAlias;
	}
	
	public void saveUnknownShopAlias(){
		if(unknownShopAlias != null && unknownShopAlias.getShop() != null){
			shopController.saveShopAlias(unknownShopAlias);
			unknownShopAlias = null;
		}
	}

}
