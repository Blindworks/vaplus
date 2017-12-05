package de.vaplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.ImportLine;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.controller.ImportControllerInterface;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.pojo.CsvFileReadResult;
import de.vaplus.api.pojo.ImportResult;
import de.vaplus.pojo.ImportLineImpl;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

@Singleton
public class ImportController implements ImportControllerInterface{

	private static final long serialVersionUID = 5442395235882261434L;

	@EJB
	private ImportWorker importWorker;
	
	@EJB
	private UserControllerInterface userController;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private VOControllerInterface voController;
	
	private boolean debug = false;
	
	private Future<ImportResult> importWorkerAsyncResult = new AsyncResult<ImportResult>(new ImportResult());
	
	public ImportController(){
		 System.out.println("### INIT ImportController ###");
	}

	@Override
	public CsvFileReadResult readPSWCube24Row(InputStream is) throws Exception{
		CsvFileReadResult result = new CsvFileReadResult();
		result.importLines = new LinkedList<ImportLineImpl>();
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );

		int skipFirstLines = 10;
		
		int line = 0;
		
		try {
		     
			org.apache.poi.ss.usermodel.Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(is);
		 
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
		    
			org.apache.poi.ss.usermodel.Cell cell;
			org.apache.poi.ss.usermodel.Row row;
		    
		    ImportLine importLine;
		    String[] productNames;
		    
		    result.csvLines = 0;
		     
		    Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
		    while(rowIterator.hasNext()) {
		        row = rowIterator.next();
		        line++;
		        
		        result.csvLines++;
		        
		        if(debug){
		        	
		        	System.out.println("PSW Import Line "+line);
		        	
		        	cell = row.getCell(0);
			        if(cell != null){
			        	System.out.println("Cell 0: "+cell.getStringCellValue());
			        }

			        // Firma		
			        cell = row.getCell(1);
			        if(cell != null){
			        	System.out.println("Cell 1: "+cell.getStringCellValue());
			        }
		        	
		        	
		        	if(line > 20)
		        		break;
		        	
		        	continue;
		        }
		        
		        if(line < skipFirstLines)
		        	continue;
		        
		        importLine = new ImportLineImpl();
		        productNames = new String[5];
		        
		        
		        // Datum		
		        cell = row.getCell(0);
		        if(cell != null){
		        	if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING)
		        		importLine.setCreationDate(formatter.parse(cell.getStringCellValue() ));
		        	else if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC)
		        		importLine.setCreationDate(cell.getDateCellValue());
		        }

		        // Firma		
		        cell = row.getCell(1);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setCompanyName(cell.getStringCellValue());
		        }

		        // Nachname		
		        cell = row.getCell(2);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setLastname(cell.getStringCellValue());
		        }

		        // Vorname		
		        cell = row.getCell(3);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setFirstname(cell.getStringCellValue());
		        }

		        // Anschrift		
		        cell = row.getCell(4);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setAddressLine1(cell.getStringCellValue());
		        }

		        // PLZ		
		        cell = row.getCell(5);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setZip(cell.getStringCellValue());
		        }

		        // Ort		
		        cell = row.getCell(6);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setCity(cell.getStringCellValue());
		        }

		        // E-Mail		
		        cell = row.getCell(7);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setEmail(cell.getStringCellValue());
		        }

		        // Geb.-Datum		
		        cell = row.getCell(8);
		        if(cell != null){
		        	if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING)
		        		importLine.setDayOfBirth(formatter.parse(cell.getStringCellValue() ));
		        	else if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC)
		        		importLine.setDayOfBirth(cell.getDateCellValue());
		        }
		        

		        // Kennwort		
		        cell = row.getCell(9);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setPassword(cell.getStringCellValue());
		        }

		        // SoHo		
		        cell = row.getCell(10);
		        if(cell != null){ 
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	
		        	if(cell.getStringCellValue().equalsIgnoreCase("ja"))
		        		importLine.setCompany(true);
		        	else
			        	importLine.setCompany(false);
		        }
		        else
		        	importLine.setCompany(false);

		        // Rufnummer		
		        cell = row.getCell(11);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setDdi(cell.getStringCellValue());
		        }

		        // IMEI-Nummer		
//		        cell = row.getCell(12);

		        // Sub-/Aktivierungs-ID		
//		        cell = row.getCell(13);

		        // Kategorie		
		        cell = row.getCell(14);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setCategoryName(cell.getStringCellValue());
		        }

		        // Artikel		
		        cell = row.getCell(15);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[0] = cell.getStringCellValue();
		        }

		        // Mitarbeiter		
		        cell = row.getCell(16);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setUserName(cell.getStringCellValue());
		        }

		        // Shop		
		        cell = row.getCell(17);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setShopName(cell.getStringCellValue());
		        }

		        // VO-Nummer		
		        cell = row.getCell(18);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setVoNumber(cell.getStringCellValue());
		        }

		        // Infos		
		        cell = row.getCell(19);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setInfo(cell.getStringCellValue());
		        }

		        // Weiterer Artikel [1]		
		        cell = row.getCell(20);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[1] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [2]		
		        cell = row.getCell(21);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[2] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [3]		
		        cell = row.getCell(22);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[3] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [4]		
		        cell = row.getCell(23);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[4] = cell.getStringCellValue();
		        }
		        
		        importLine.setProductNames(productNames);

		        // CUBIES	
//		        cell = row.getCell(24);

		        
		        ((List<ImportLine>)result.importLines).add(importLine);
		    }
		    is.close();
//		    FileOutputStream out = 
//		        new FileOutputStream(new File("C:\\test.xls"));
//		    workbook.write(out);
//		    out.close();
		     
		} catch (Exception e) {
//			e.printStackTrace();
			
			throw new Exception(e.getLocalizedMessage());
			// TODO Auto-generated catch block
		}
		
		
		
		return result;
	}
	


	@Override
	public CsvFileReadResult readPSWCube45Row(InputStream is) throws Exception{
		CsvFileReadResult result = new CsvFileReadResult();
		result.importLines = new LinkedList<ImportLineImpl>();
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );

		int skipFirstLines = 11;
		
		int line = 0;
		
		try {
		     
			org.apache.poi.ss.usermodel.Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(is);
		 
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
		    
			org.apache.poi.ss.usermodel.Cell cell;
			org.apache.poi.ss.usermodel.Row row;
		    
		    ImportLine importLine;
		    String[] productNames;
		    
		    result.csvLines = 0;
		     
		    Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
		    while(rowIterator.hasNext()) {
		        row = rowIterator.next();
		        line++;
		        
		        result.csvLines++;
		        
		        if(debug){
		        	
		        	System.out.println("PSW Import Line "+line);
		        	
		        	cell = row.getCell(0);
			        if(cell != null){
			        	System.out.println("Cell 0: "+cell.getStringCellValue());
			        }

			        // Firma		
			        cell = row.getCell(1);
			        if(cell != null){
			        	System.out.println("Cell 1: "+cell.getStringCellValue());
			        }
		        	
		        	
		        	if(line > 20)
		        		break;
		        	
		        	continue;
		        }
		        
		        if(line < skipFirstLines)
		        	continue;
		        
		        importLine = new ImportLineImpl();
		        productNames = new String[5];

		        
		        // STORNO?
		        cell = row.getCell(24);
		        if(cell != null){

		        	if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN){
		        		if(cell.getBooleanCellValue()){
			        		result.cancelationLines++;
			        		continue;
		        		}
		        			
		        	}
		        }
		        
		        // Datum		
		        cell = row.getCell(0);
		        if(cell != null){
		        	if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING)
		        		importLine.setCreationDate(formatter.parse(cell.getStringCellValue() ));
		        	else if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC)
		        		importLine.setCreationDate(cell.getDateCellValue());
		        }

		        // Firma		
		        cell = row.getCell(1);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setCompanyName(cell.getStringCellValue());
		        }

		        // Nachname		
		        cell = row.getCell(2);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setLastname(cell.getStringCellValue());
		        }

		        // Vorname		
		        cell = row.getCell(3);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setFirstname(cell.getStringCellValue());
		        }

		        // Anschrift		
		        cell = row.getCell(4);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setAddressLine1(cell.getStringCellValue());
		        }

		        // PLZ		
		        cell = row.getCell(5);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setZip(cell.getStringCellValue());
		        }

		        // Ort		
		        cell = row.getCell(6);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setCity(cell.getStringCellValue());
		        }

		        // E-Mail		
		        cell = row.getCell(7);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setEmail(cell.getStringCellValue());
		        }

		        // Geb.-Datum		
		        cell = row.getCell(8);
		        if(cell != null){
		        	if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING)
		        		importLine.setDayOfBirth(formatter.parse(cell.getStringCellValue() ));
		        	else if(cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC)
		        		importLine.setDayOfBirth(cell.getDateCellValue());
		        }
		        

		        // Kennwort		
		        cell = row.getCell(9);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setPassword(cell.getStringCellValue());
		        }

		        // SoHo		
		        cell = row.getCell(10);
		        if(cell != null){ 
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	
		        	if(cell.getStringCellValue().equalsIgnoreCase("ja"))
		        		importLine.setCompany(true);
		        	else
			        	importLine.setCompany(false);
		        }
		        else
		        	importLine.setCompany(false);

		        // Rufnummer		
		        cell = row.getCell(12);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setDdi(cell.getStringCellValue());
		        }

		        // IMEI-Nummer		
//		        cell = row.getCell(13);

		        // Sub-/Aktivierungs-ID		
//		        cell = row.getCell(14);

		        // Kategorie		
		        cell = row.getCell(15);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setCategoryName(cell.getStringCellValue());
		        }

		        // Artikel		
		        cell = row.getCell(16);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[0] = cell.getStringCellValue();
		        }

		        // Mitarbeiter		
		        cell = row.getCell(18);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setUserName(cell.getStringCellValue());
		        }

		        // Shop		
		        cell = row.getCell(19);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setShopName(cell.getStringCellValue());
		        }

		        // VO-Nummer		
		        cell = row.getCell(20);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setVoNumber(cell.getStringCellValue());
		        }

		        // Infos		
		        cell = row.getCell(22);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	importLine.setInfo(cell.getStringCellValue());
		        }

		        // Weiterer Artikel [1]		
		        cell = row.getCell(28);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[1] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [2]		
		        cell = row.getCell(31);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[2] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [3]		
		        cell = row.getCell(34);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[3] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [4]		
		        cell = row.getCell(37);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[4] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [5]		
		        cell = row.getCell(40);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[4] = cell.getStringCellValue();
		        }

		        // Weiterer Artikel [6]		
		        cell = row.getCell(43);
		        if(cell != null){
		        	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
		        	productNames[4] = cell.getStringCellValue();
		        }
		        
		        importLine.setProductNames(productNames);

		        
		        ((List<ImportLine>)result.importLines).add(importLine);
		    }
		    is.close();
//		    FileOutputStream out = 
//		        new FileOutputStream(new File("C:\\test.xls"));
//		    workbook.write(out);
//		    out.close();
		     
		} catch (Exception e) {
//			e.printStackTrace();
			
			throw new Exception(e.getLocalizedMessage());
			// TODO Auto-generated catch block
		}
		
		
		
		return result;
	}

	@Override
	public CsvFileReadResult readSortics_2_5_4(InputStream is) throws Exception{
		CsvFileReadResult result = new CsvFileReadResult();
		result.importLines = new LinkedList<ImportLineImpl>();
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );
		
		int skipFirstLines = 4;
		
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			
			Workbook workbook = Workbook.getWorkbook(is,ws);

			Sheet sheet = workbook.getSheet(0);
		    
			Cell cell;
		    
		    ImportLine importLine;
		    String[] productNames;

		    result.csvLines = 0;
		    result.cancelationLines = 0;
		    
		    for(int line=0; line < sheet.getRows(); line++){
		    	Cell[] row = sheet.getRow(line);
		    	
		    	result.csvLines++;

		        if(debug){

		        	
		        	System.out.println("SORTICS Import Line "+line);
		        	
		        	if(row.length > 0){
			        	System.out.println("Cell 0: "+getJxlCellValue(row[0]));
			        }

			        // Firma		
		        	if(row.length > 1){
			        	System.out.println("Cell 1: "+getJxlCellValue(row[1]));
			        }
		        	
		        	if(line > 20)
		        		break;
		        	
		        	continue;
		        }
		        
		        
		        if(line < skipFirstLines)
		        	continue;
		        

		        importLine = new ImportLineImpl();
		        productNames = new String[5];
		        
		        // STORNO?
		        if(row.length > 22){
		        	cell = row[22];
		        	
		        	if(getJxlCellValue(cell).equalsIgnoreCase("ja")){
		        		result.cancelationLines++;
		        		continue;
		        	}
		        }
		        
		        
		        // Datum		
		        if(row.length > 10){
		        	cell = row[10];
		        	if(getJxlCellValue(cell).length() > 0){
		        		importLine.setCreationDate(formatter.parse(getJxlCellValue(cell) ));
		        	}else{
		        		// Fallback take Date from Col 0
		        		  if(row.length > 0){
		  		        	cell = row[0];
		  		        	if(getJxlCellValue(cell).length() > 0){
		  		        		importLine.setCreationDate(formatter.parse(getJxlCellValue(cell) ));
		  		        	}
		  		        }
		        	}
		        }

		        // Firma		
		        if(row.length > 1){
		        	cell = row[1];
		        	importLine.setCompanyName(getJxlCellValue(cell));
		        }

		        // Nachname		
		        if(row.length > 2){
		        	cell = row[2];
		        	importLine.setLastname(getJxlCellValue(cell));
		        }

		        // Vorname		
		        if(row.length > 3){
		        	cell = row[3];
		        	importLine.setFirstname(getJxlCellValue(cell));
		        }

		        // Anschrift		
		        if(row.length >= 4){
		        	cell = row[4];
		        	importLine.setAddressLine1(getJxlCellValue(cell));
		        }

		        // PLZ		
		        if(row.length > 5){
		        	cell = row[5];
		        	importLine.setZip(getJxlCellValue(cell));
		        }

		        // Ort		
		        if(row.length > 6){
		        	cell = row[6];
		        	importLine.setCity(getJxlCellValue(cell));
		        }

		        // E-Mail	
		        if(row.length > 9){
		        	cell = row[9];	
		        	importLine.setEmail(getJxlCellValue(cell));
		        }

		        // Geb.-Datum	
		        if(row.length > 7){
		        	cell = row[7];	
		        	if(getJxlCellValue(cell).length() > 0)
		        		importLine.setDayOfBirth(formatter.parse(getJxlCellValue(cell) ));
		        }
		        

		        // Kennwort		
		        if(row.length > 8){
		        	cell = row[8];
		        	importLine.setPassword(getJxlCellValue(cell));
		        }

		        // SoHo		
		        if(importLine.getCompanyName() != null && importLine.getCompanyName().length() > 0) 
		        	importLine.setCompany(true);
		        else
		        	importLine.setCompany(false);

		        // Rufnummer	
		        if(row.length > 12){
		        	cell = row[12];	
		        	importLine.setDdi(getJxlCellValue(cell));
		        }

		        // IMEI-Nummer		
//		        cell = row.getCell(13);

		        // Sub-/Aktivierungs-ID		
//		        cell = row.getCell();

		        // Kategorie		
		        if(row.length > 14){
		        	cell = row[14];
		        	importLine.setCategoryName(getJxlCellValue(cell));
		        }

		        // Artikel	
		        if(row.length > 15){
		        	cell = row[15];	
		        	productNames[0] = getJxlCellValue(cell);
		        }

		        // Mitarbeiter		
		        if(row.length > 17){
		        	cell = row[17];
		        	importLine.setUserName(getJxlCellValue(cell));
		        }

		        // Shop		
		        if(row.length >= 18){
		        	cell = row[18];
		        	importLine.setShopName(getJxlCellValue(cell));
		        }

		        // VO-Nummer	
		        if(row.length > 19){
		        	cell = row[19];	
		        	importLine.setVoNumber(getJxlCellValue(cell));
		        }

		        // Infos		
		        if(row.length > 20){
		        	cell = row[20];
		        	importLine.setInfo(getJxlCellValue(cell));
		        }



		        // Option 1	
		        if(row.length > 26){
		        	cell = row[26];	
		        	if(getJxlCellValue(cell).length() > 0)
		        		productNames[1] = getJxlCellValue(cell);
		        }

		        // Option 2	
		        if(row.length > 29){
		        	cell = row[29];	
		        	if(getJxlCellValue(cell).length() > 0)
		        		productNames[2] = getJxlCellValue(cell);
		        }

		        // Option 3	
		        if(row.length > 32){
		        	cell = row[32];	
		        	if(getJxlCellValue(cell).length() > 0)
		        		productNames[3] = getJxlCellValue(cell);
		        }
		        
		        importLine.setProductNames(productNames);

//
//		        System.out.println("### IMPORT LINE ###");
//		        System.out.println("Datum: "+importLine.getCreationDate());
//		        System.out.println("Company: "+importLine.getCompanyName());
//		        System.out.println("Firstname: "+importLine.getFirstname());
//		        System.out.println("Lastname: "+importLine.getLastname());
//		        System.out.println("Address 1: "+importLine.getAddressLine1());
//		        System.out.println("Address 2: "+importLine.getAddressLine2());
//		        System.out.println("PLZ: "+importLine.getZip());
//		        System.out.println("Ort: "+importLine.getCity());
//		        System.out.println("EMail: "+importLine.getEmail());
//		        System.out.println("Geb.Date: "+importLine.getDayOfBirth());
//		        System.out.println("Password: "+importLine.getPassword());
//		        System.out.println("SoHo: "+importLine.isCompany());
//		        System.out.println("DDI: "+importLine.getDdi());
//		        System.out.println("Category: "+importLine.getCategoryName());
//		        System.out.println("Product: "+importLine.getProductNames()[0]);
//		        System.out.println("User: "+importLine.getUserName());
//		        System.out.println("Shop: "+importLine.getShopName());
//		        System.out.println("VO: "+importLine.getVoNumber());
//		        System.out.println("Info: "+importLine.getInfo());
		        
		        
		        ((List<ImportLine>)result.importLines).add(importLine);
		    }
		    is.close();
//		    FileOutputStream out = 
//		        new FileOutputStream(new File("C:\\test.xls"));
//		    workbook.write(out);
//		    out.close();
		    
		    workbook.close();
		     
		} catch (Exception e) {
//			e.printStackTrace();
			
			throw new Exception(e.getLocalizedMessage());
		}
		
		
		
		return result;
	}




	@Override
	public CsvFileReadResult readEposActivation(InputStream is) throws Exception{
		CsvFileReadResult result = new CsvFileReadResult();
		result.importLines = new LinkedList<ImportLineImpl>();
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );
		
		Charset inputCharset = Charset.forName("ISO-8859-1");
		
		BufferedReader br = null;
		int lineNum = 0;
		String line = "";
		String cvsSplitBy = ";(?=([^\"]|\"[^\"]*\")*$)";
		String[] row;
		String cell = null;


	    ImportLine importLine;
	    String[] productNames;
	    
		try {
			br = new BufferedReader(new InputStreamReader(is, inputCharset));
			
			result.csvLines = 0;
			
			while ((line = br.readLine()) != null) {
				result.csvLines++;

				if(lineNum++ == 0)
					continue;
				
				row = line.split(cvsSplitBy);
				
//				System.out.println(Arrays.toString(row));
				
				
		        
		        importLine = new ImportLineImpl();
		        productNames = new String[5];
		        
		        // Datum		
		        if(row.length >= 0){
		        	cell = row[0];
		        	if(getCSVCellValue(cell).length() > 0)
		        		importLine.setCreationDate(formatter.parse(getCSVCellValue(cell) ));
		        }

		        // Nachname		
		        if(row.length >= 1){
		        	cell = row[1];
		        	importLine.setLastname(getCSVCellValue(cell));
		        }

		        // Vorname		
		        if(row.length >= 2){
		        	cell = row[2];
		        	importLine.setFirstname(getCSVCellValue(cell));
		        }

		        // Strasse		
		        if(row.length >= 3){
		        	cell = row[3];
		        	importLine.setStreet(getCSVCellValue(cell));
		        }

		        // Strassen Nummer		
		        if(row.length >= 4){
		        	cell = row[4];
		        	importLine.setStreetNumber(getCSVCellValue(cell));
		        }

		        // PLZ		
		        if(row.length >= 5){
		        	cell = row[5];
		        	importLine.setZip(getCSVCellValue(cell));
		        }

		        // Ort		
		        if(row.length >= 6){
		        	cell = row[6];
		        	importLine.setCity(getCSVCellValue(cell));
		        }

		        // Geb.-Datum	
		        if(row.length >= 7){
		        	cell = row[7];	
		        	if(getCSVCellValue(cell).length() > 0)
		        		importLine.setDayOfBirth(formatter.parse(getCSVCellValue(cell) ));
		        }
		        
		        // Rufnummer	
		        if(row.length >= 8){
		        	cell = row[8];	
		        	importLine.setDdi(getCSVCellValue(cell).replaceAll("/", ""));
		        }

		        // Artikel	
		        if(row.length >= 11){
		        	cell = row[11];	
		        	productNames[0] = getCSVCellValue(cell);
		        }

		        // Datendienste	
		        if(row.length >= 13){
		        	cell = row[13];	
		        	productNames[1] = getCSVCellValue(cell);
		        }

		        // Zusatzdienste	
		        if(row.length >= 14){
		        	cell = row[14];	
		        	String[] dienste = getCSVCellValue(cell).split(";");
		        	
		        	if(dienste.length > 0)
		        		productNames[1] = dienste[0];
		        	
		        	if(dienste.length > 1)
		        		productNames[1] = dienste[1];
		        	
		        	if(dienste.length > 2)
		        		productNames[1] = dienste[2];
		        	
		        	if(dienste.length > 3)
		        		productNames[1] = dienste[3];
		        	
		        }

		        // Mitarbeiter		
	        	importLine.setUserName("Import Mitarbeiter");

		        // VO-Nummer	
		        if(row.length >= 15){
		        	cell = row[15];	
		        	importLine.setVoNumber(getCSVCellValue(cell));
		        }
		        
		        // Shop
		        VO vo = voController.getVOByNumber(importLine.getVoNumber());
		        if(vo != null){
		        	importLine.setShopName(vo.getShop().getName());
		        }else{
		        	importLine.setShopName("Unbekannter Shop");
		        }
		        
		        
		        importLine.setProductNames(productNames);


//		        System.out.println("### IMPORT LINE ###");
//		        System.out.println("Datum: "+importLine.getCreationDate());
//		        System.out.println("Company: "+importLine.getCompanyName());
//		        System.out.println("Firstname: "+importLine.getFirstname());
//		        System.out.println("Lastname: "+importLine.getLastname());
//		        System.out.println("Address 1: "+importLine.getAddressLine1());
//		        System.out.println("Strasse: "+importLine.getStreet());
//		        System.out.println("Nr: "+importLine.getStreetNumber());
//		        System.out.println("Address 2: "+importLine.getAddressLine2());
//		        System.out.println("PLZ: "+importLine.getZip());
//		        System.out.println("Ort: "+importLine.getCity());
//		        System.out.println("EMail: "+importLine.getEmail());
//		        System.out.println("Geb.Date: "+importLine.getDayOfBirth());
//		        System.out.println("Password: "+importLine.getPassword());
//		        System.out.println("SoHo: "+importLine.isCompany());
//		        System.out.println("DDI: "+importLine.getDdi());
//		        System.out.println("Category: "+importLine.getCategoryName());
//		        System.out.println("Product: "+importLine.getProductNames()[0]);
//		        System.out.println("User: "+importLine.getUserName());
//		        System.out.println("Shop: "+importLine.getShopName());
//		        System.out.println("VO: "+importLine.getVoNumber());
//		        System.out.println("Info: "+importLine.getInfo());
		        
		        
		        ((List<ImportLine>)result.importLines).add(importLine);
		    }
		    is.close();
		     
		} catch (Exception e) {
//			e.printStackTrace();
			
			throw new Exception(e.getLocalizedMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		return result;
	}


	@Override
	public CsvFileReadResult readEposOther(InputStream is) throws Exception{
		CsvFileReadResult result = new CsvFileReadResult();
		result.importLines = new LinkedList<ImportLineImpl>();
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );
		
		Charset inputCharset = Charset.forName("ISO-8859-1");
		
		BufferedReader br = null;
		int lineNum = 0;
		String line = "";
		String cvsSplitBy = ";(?=([^\"]|\"[^\"]*\")*$)";
		String[] row;
		String cell = null;


	    ImportLine importLine;
	    String[] productNames;
	    
		try {
			br = new BufferedReader(new InputStreamReader(is, inputCharset));
			
			result.csvLines = 0;
			
			while ((line = br.readLine()) != null) {
				result.csvLines++;

				if(lineNum++ == 0)
					continue;
				
				row = line.split(cvsSplitBy);
				
		        
		        importLine = new ImportLineImpl();
		        productNames = new String[5];
		        
		        // Datum		
		        if(row.length >= 0){
		        	cell = row[0];
		        	if(getCSVCellValue(cell).length() > 0)
		        		importLine.setCreationDate(formatter.parse(getCSVCellValue(cell) ));
		        }

		        // Nachname		
		        if(row.length >= 1){
		        	cell = row[1];
		        	importLine.setLastname(getCSVCellValue(cell));
		        }

		        // Vorname		
		        if(row.length >= 2){
		        	cell = row[2];
		        	importLine.setFirstname(getCSVCellValue(cell));
		        }

		        // Strasse		
		        if(row.length >= 3){
		        	cell = row[3];
		        	importLine.setStreet(getCSVCellValue(cell));
		        }

		        // Strassen Nummer		
		        if(row.length >= 4){
		        	cell = row[4];
		        	importLine.setStreetNumber(getCSVCellValue(cell));
		        }

		        // PLZ		
		        if(row.length >= 5){
		        	cell = row[5];
		        	importLine.setZip(getCSVCellValue(cell));
		        }

		        // Ort		
		        if(row.length >= 6){
		        	cell = row[6];
		        	importLine.setCity(getCSVCellValue(cell));
		        }

		        // Geb.-Datum	
		        if(row.length >= 7){
		        	cell = row[7];	
		        	if(getCSVCellValue(cell).length() > 0)
		        		importLine.setDayOfBirth(formatter.parse(getCSVCellValue(cell) ));
		        }
		        
		        // Rufnummer	
		        if(row.length >= 8){
		        	cell = row[8];	
		        	importLine.setDdi(getCSVCellValue(cell));
		        }

		        // Artikel	
		        if(row.length >= 10){
		        	cell = row[10];	
		        	productNames[0] = getCSVCellValue(cell);
		        }

		        // Datendienste	
		        if(row.length >= 12){
		        	cell = row[12];	
		        	productNames[1] = getCSVCellValue(cell);
		        }

		        // Zusatzdienste	
		        if(row.length >= 13){
		        	cell = row[13];	
		        	String[] dienste = getCSVCellValue(cell).split(";");
		        	
		        	if(dienste.length > 0)
		        		productNames[1] = dienste[0];
		        	
		        	if(dienste.length > 1)
		        		productNames[1] = dienste[1];
		        	
		        	if(dienste.length > 2)
		        		productNames[1] = dienste[2];
		        	
		        	if(dienste.length > 3)
		        		productNames[1] = dienste[3];
		        	
		        }

		        // Mitarbeiter		
	        	importLine.setUserName("Import Mitarbeiter");

		        // VO-Nummer	
		        if(row.length >= 14){
		        	cell = row[14];	
		        	importLine.setVoNumber(getCSVCellValue(cell));
		        }
		        
		        // Shop
		        VO vo = voController.getVOByNumber(importLine.getVoNumber());
		        if(vo != null){
		        	importLine.setShopName(vo.getShop().getName());
		        }else{
		        	importLine.setShopName("Unbekannter Shop");
		        }
		        
		        
		        importLine.setProductNames(productNames);


//		        System.out.println("### IMPORT LINE ###");
//		        System.out.println("Datum: "+importLine.getCreationDate());
//		        System.out.println("Company: "+importLine.getCompanyName());
//		        System.out.println("Firstname: "+importLine.getFirstname());
//		        System.out.println("Lastname: "+importLine.getLastname());
//		        System.out.println("Address 1: "+importLine.getAddressLine1());
//		        System.out.println("Strasse: "+importLine.getStreet());
//		        System.out.println("Nr: "+importLine.getStreetNumber());
//		        System.out.println("Address 2: "+importLine.getAddressLine2());
//		        System.out.println("PLZ: "+importLine.getZip());
//		        System.out.println("Ort: "+importLine.getCity());
//		        System.out.println("EMail: "+importLine.getEmail());
//		        System.out.println("Geb.Date: "+importLine.getDayOfBirth());
//		        System.out.println("Password: "+importLine.getPassword());
//		        System.out.println("SoHo: "+importLine.isCompany());
//		        System.out.println("DDI: "+importLine.getDdi());
//		        System.out.println("Category: "+importLine.getCategoryName());
//		        System.out.println("Product: "+importLine.getProductNames()[0]);
//		        System.out.println("User: "+importLine.getUserName());
//		        System.out.println("Shop: "+importLine.getShopName());
//		        System.out.println("VO: "+importLine.getVoNumber());
//		        System.out.println("Info: "+importLine.getInfo());
		        
		        
		        ((List<ImportLine>)result.importLines).add(importLine);
		    }
		    is.close();
		     
		} catch (Exception e) {
//			e.printStackTrace();
			
			throw new Exception(e.getLocalizedMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		return result;
	}
	
	private String getCSVCellValue(String csvCell){
		return csvCell.replace("\"", "");
	}

	
	
	private String getJxlCellValue(Cell cell){
		DateFormat formatter = new SimpleDateFormat( "dd.MM.yyyy" );
		
		if (cell.getType() == CellType.LABEL) 
		{ 
			LabelCell lc = (LabelCell) cell; 
			return lc.getString(); 
		} 

		if (cell.getType() == CellType.NUMBER) 
		{ 
			
			NumberCell nc = (NumberCell) cell; 
			return String.valueOf( nc.getContents() ); 
		} 

		if (cell.getType() == CellType.DATE) 
		{ 
			DateCell dc = (DateCell) cell; 
			return formatter.format(dc.getDate());
		} 
		
		return "";
	}

	@Override
	public List<String> getUnknownUserAliasList(
			List<? extends ImportLine> importLines) {
		
		if(importLines == null || importLines.size() == 0)
			return null;

		List<String> unknownUserAliasList = new LinkedList<String>();
		ImportLine importLine;

		Iterator<? extends ImportLine> i = importLines.iterator();
		User user;
			
		while(i.hasNext()){
			importLine = i.next();
			
			user = userController.getUserByAlias(importLine.getUserName());
			if(user == null && importLine.getUserName().trim().length() > 0){
				if(! unknownUserAliasList.contains(importLine.getUserName().trim())){
					unknownUserAliasList.add(importLine.getUserName().trim());
				}
			}
		}
		
		return unknownUserAliasList;
	}


	@Override
	public List<String> getUnknownShopAliasList(
			List<? extends ImportLine> importLines) {
		
		if(importLines == null || importLines.size() == 0)
			return null;

		List<String> unknownShopAliasList = new LinkedList<String>();
		ImportLine importLine;

		Iterator<? extends ImportLine> i = importLines.iterator();
		Shop shop;
			
		while(i.hasNext()){
			importLine = i.next();
			
			shop = shopController.getShopByAlias(importLine.getShopName());
			if(shop == null && importLine.getShopName().trim().length() > 0){
				if(! unknownShopAliasList.contains(importLine.getShopName().trim())){
					unknownShopAliasList.add(importLine.getShopName().trim());
				}
			}
		}
		
		return unknownShopAliasList;
	}

	@Override
	public List<String> getUnknownVOList(
			List<? extends ImportLine> importLines) {
		
		if(importLines == null || importLines.size() == 0)
			return null;

		List<String> unknownVOList = new LinkedList<String>();
		ImportLine importLine;

		Iterator<? extends ImportLine> i = importLines.iterator();
		VO vo;
			
		while(i.hasNext()){
			importLine = i.next();
			
			vo = voController .getVOByNumber(importLine.getVoNumber());
			if(vo == null && importLine.getVoNumber() != null && importLine.getVoNumber().trim().length() > 0){
				if(! unknownVOList.contains(importLine.getVoNumber().trim())){
					unknownVOList.add(importLine.getVoNumber().trim());
				}
			}
		}
		
		return unknownVOList;
	}
	

	

	@Override
	public void importData(List<? extends ImportLine> importLines) {

		
		if(! importWorkerAsyncResult.isDone())
			return;
		
		importWorkerAsyncResult = importWorker.runAsync(importLines);
		
	}

	@Override
	public boolean isImportWorkerRunning() {
		return !importWorkerAsyncResult.isDone();
	}

	@Override
	public ImportResult getImportResult() {
		if(isImportWorkerRunning())
			return new ImportResult();
		
		try {
			return importWorkerAsyncResult.get();
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ImportResult();
	}
}
