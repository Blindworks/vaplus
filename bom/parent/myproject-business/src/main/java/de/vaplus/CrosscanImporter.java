package de.vaplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.inject.Inject;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CrosscanControllerInterface;
import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.Holiday;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.State;
import de.vaplus.client.eao.CrosscanDataEao;
import de.vaplus.client.eao.HolidayEao;
import de.vaplus.client.eao.StateEao;
import de.vaplus.client.eao.UserEao;
import de.vaplus.client.entity.CrosscanDataEntity;
import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.HolidayEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.StateEntity;

@Stateful
public class CrosscanImporter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7431372819219038174L;

	@EJB
	private ShopControllerInterface shopController;

    @Inject
    private CrosscanDataEao eao;
    
    private final DateFormat srcFormatter = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" );
    private final DateFormat dstFormatter = new SimpleDateFormat( "yyyyMMddHH" );
	
	public CrosscanImporter(){
		
	}
	
	private CrosscanDataEntity factoryNewCrosscanDataEntity(Shop shop){
		CrosscanDataEntity crosscanData = new CrosscanDataEntity();
		crosscanData.setShop((ShopEntity) shop);
		return crosscanData;
	}

	public void importCrosscanData(Shop shop, String startdate, String enddate) throws IOException{
		

		String authID = shop.getCrosscanData_authID();
		String storeID = shop.getCrosscanData_storeID();
		
		URL crosscanDataUrl = new URL("https://vf.connect.crosscan.com/"+authID+"/csv_generator/16/get_csv.csv?stores="+storeID+startdate+enddate+"&aggregation=hourly&excelcompat=true&locale=de_DE&directionSelection=0&consider_openingtimes=true&showColumns%5Bobject_group%5D=false&show_all_stores=false&sort=descending&lineending=unix&display_store_info=true");
		
		InputStream is = crosscanDataUrl.openStream();
		
        String line = "";
        String delimiter = ";";
        String field;
        
        CrosscanDataEntity crosscanData = null;
        
        int row = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
        	

            while ((line = br.readLine()) != null) {

	        	if(row++ == 0)
	        		continue;
	        	
	        	crosscanData = factoryNewCrosscanDataEntity(shop);
	        	
                // use comma as separator
                String[] fields = line.split(delimiter);

                for(int i = 0;i<fields.length;i++){
                	
                	field = fields[i];
                	
                	if(field.startsWith("\"") && field.endsWith("\"")){
	                	
	                	if(field.length() > 2)
	                		field = field.substring(1, field.length() - 1);
	                	else
	                		field = "";

                	}
                	
                	switch(i){
                		case 0:
                			crosscanData.setCssid(field);
                			break;
                		case 1:
                			crosscanData.setCompanyid(field);
                			break;
                		case 2:
                			crosscanData.setStoreno(Integer.valueOf(field));
                			break;
                		case 3:
                			crosscanData.setCompany(field);
                			break;
                		case 4:
                			Date d = srcFormatter.parse(field);
    	                	crosscanData.setTime(Integer.valueOf(dstFormatter.format(d)));
                			break;
                		case 5:
                			crosscanData.setEventtype(Integer.valueOf(field));
                			break;
                		case 6:
                			crosscanData.setEventvalue(Integer.valueOf(field));
                			break;
                		case 7:
                			crosscanData.setValuename(field);
                			break;
                		case 8:
                			crosscanData.setAmount(Integer.valueOf(field));
                			break;
                		case 9:
                			crosscanData.setStoretitle(field);
                			break;
                		case 10:
                			crosscanData.setStoretown(field);
                			break;
                	}

                }
                
                if(! eao.checkIfDataExists(shop, crosscanData.getTime()))
                	crosscanData = eao.saveCrosscanData(crosscanData);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
