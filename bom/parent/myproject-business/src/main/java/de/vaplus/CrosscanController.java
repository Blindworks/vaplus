package de.vaplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.ejb3.annotation.TransactionTimeout;

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
public class CrosscanController implements CrosscanControllerInterface {
	
	private static final long serialVersionUID = 3766489258073727142L;
	
	@EJB
	private ShopControllerInterface shopController;
	
	@EJB
	private CrosscanImporter importer;

    @Inject
    private CrosscanDataEao eao;
    
    private final DateFormat startDateFormatter = new SimpleDateFormat( "yyyyMMdd" );
	
	public CrosscanController(){
		
	}

	private int getLatestCrosscanData(Shop shop){
		return eao.getLatestDataTime(shop);
	}

	
	@Override
	@Asynchronous
//	@Schedule( minute="10", hour="*", persistent=false)
	@TransactionTimeout(108000)
	public void updateCrosscanData(){
//		System.out.println("@Schedule START updateCrosscanData");

		List<? extends Shop> shopList = shopController.getShopList();
		Iterator<? extends Shop> i = shopList.iterator();
		Shop s;
		
		while(i.hasNext()){
			s = i.next();
			importCrosscanData(s);
		}
		
//		System.out.println("@Schedule END updateCrosscanData");
	}
	
	public void importCrosscanData(Shop shop){
		
		if(shop.getCrosscanData_authID() == null)
			return;
		
		if(shop.getCrosscanData_storeID() == null)
			return;
		
		int time = getLatestCrosscanData(shop);

		String startdate = ""; // &startdate=20161001
		String enddate = ""; // &enddate=20161026
		
		String month, day;
		
		
		URL crosscanDataUrl = null;
		
		try{

			if(time > 0){
					
					String timeBaseString =   String.valueOf(time).substring(0, 8);
					startdate = "&startdate="+timeBaseString;
					
					importer.importCrosscanData(shop, startdate, enddate);
				
			}else{
				
				Calendar c = Calendar.getInstance();
				
				long now = c.getTimeInMillis();
				
				c.set(Calendar.YEAR, 2000);
				c.set(Calendar.DAY_OF_YEAR, 1);
				
				while(now > c.getTimeInMillis()){
					
					month = String.valueOf(c.get(Calendar.MONTH));
					if(month.length() == 1)
						month = "0"+month;
					
					day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
					if(day.length() == 1)
						day = "0"+day;
					
					startdate = "&startdate="+c.get(Calendar.YEAR)+month+day;

					c.add(Calendar.MONTH, 1);
					
					month = String.valueOf(c.get(Calendar.MONTH));
					if(month.length() == 1)
						month = "0"+month;
					
					day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
					if(day.length() == 1)
						day = "0"+day;
					
					enddate = "&enddate="+c.get(Calendar.YEAR)+month+day;
					
					importer.importCrosscanData(shop, startdate, enddate);
					
				}
				
				
			}
			
			
		}catch (IOException e) {
			System.out.println("Error in Crosscan URL "+crosscanDataUrl);
			System.out.println("Error:"+e.getMessage());
		}
	}

	@Override
	public long getCrosscanDataForMonth(Shop shop, int year, int month) {
		
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		Date d = c.getTime();
		
		int timeFrom = Integer.valueOf(startDateFormatter.format(d) +"00" );
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		
		d = c.getTime();
		
		int timeTo = Integer.valueOf(startDateFormatter.format(d) +"23");

		return eao.getAmountSum(shop, timeFrom, timeTo);
	}
	
}
