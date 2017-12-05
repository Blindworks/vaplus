package de.vaplus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.CommissionAccountingControllerInterface;
import de.vaplus.client.eao.AccountingEao;
import de.vaplus.client.eao.CommissionEao;
import de.vaplus.client.entity.commission.vendor.VendorCommissionAccountingFileEntity;
import de.vaplus.client.entity.commission.vendor.VendorCommissionAccountingItemEntity;
import de.vaplus.client.entity.commission.vendor.VodafoneCommissionAccountingItemEntity;


@Stateless
public class CommissionAccountingController implements CommissionAccountingControllerInterface {

	private static final long serialVersionUID = -8728717373840260625L;
	
	@Inject
    private CommissionEao commissionEao;
	
	@Inject
    private AccountingEao accountingEao;
	
	@Override
	public void importVendorAccountingCsv(InputStream is) throws Exception{
		
		BufferedReader br = null;
		String line = "";
		int i=0;
		
		VodafoneCommissionAccountingItemEntity commissionAccountingItem;
		VendorCommissionAccountingFileEntity commissionAccountingFile;
		List<VendorCommissionAccountingItemEntity> commissionAccountingItemList = new LinkedList<VendorCommissionAccountingItemEntity>();
		
		Map<String,Integer> dateVoterMap = new HashMap<String,Integer>();
		String dateVoterKey;
		
		int dateRangeVotes = 0;
		String dateRange = null;
		String accountingVO = null;
		
		Calendar c = Calendar.getInstance();
		try {
			br = new BufferedReader( new InputStreamReader( is, "8859_1"));
			i=0;
			while ((line = br.readLine()) != null) {
				if(i++ < 1)
					continue;
				Date d = VodafoneCommissionAccountingItemEntity.getTimeRange(line);
				accountingVO = VodafoneCommissionAccountingItemEntity.getAccountingVO(line);
				
				if(d != null){
					c.setTime(d);
					dateVoterKey = String.valueOf(c.get(Calendar.YEAR)) + "_" + String.valueOf(c.get(Calendar.MONTH));
					if(! dateVoterMap.containsKey(dateVoterKey)){
						dateVoterMap.put(dateVoterKey, 0);
					}
					
					dateVoterMap.put(dateVoterKey, dateVoterMap.get(dateVoterKey) + 1);
				}
				commissionAccountingItem = new VodafoneCommissionAccountingItemEntity(line);
				commissionAccountingItemList.add(commissionAccountingItem);

			}
			
			
			Iterator<String> it = dateVoterMap.keySet().iterator();
			while(it.hasNext()){
				dateVoterKey = it.next();
				
				if(dateVoterMap.get(dateVoterKey) > dateRangeVotes){
					dateRange = dateVoterKey;
					dateRangeVotes = dateVoterMap.get(dateVoterKey);
				}
					
			}

//			System.out.println( "Best dateRange: "+dateRange ); 
//			System.out.println( "accountingVO: "+accountingVO ); 
			
			if(dateRange == null || dateRange.length() == 0){
				throw new Exception("Kein Zeitraum in Datei gefunden");
			}
			
			if(accountingVO == null || accountingVO.length() == 0){
				throw new Exception("Keine Abrechnungs VO in Datei gefunden");
			}
			
			
			// check if file already imported
			commissionAccountingFile = accountingEao.getCommissionAccountingFile(accountingVO, Integer.valueOf(dateRange.split("_")[0]), Integer.valueOf(dateRange.split("_")[1]));
			
			if(commissionAccountingFile != null)
				throw new Exception("Daten für VO:"+accountingVO+" im Zeitraum "+ (Integer.valueOf(dateRange.split("_")[1])+1)+" "+ dateRange.split("_")[0] +" bereits importiert!");
			
			commissionAccountingFile = new VendorCommissionAccountingFileEntity(accountingVO, Integer.valueOf(dateRange.split("_")[0]), Integer.valueOf(dateRange.split("_")[1]));
			commissionAccountingFile.setItemList(commissionAccountingItemList);

			commissionAccountingFile = accountingEao.saveVendorCommissionAccountingFile(commissionAccountingFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("Datei nicht gefunden: "+e.getMessage());
		} catch (IOException e) {
			throw new Exception("Datei fehler: "+e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
}
