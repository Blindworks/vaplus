package de.vaplus.client.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.CommissionAccountingControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CrosscanControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.PayrollControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.CommissionActivity;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.PhoneContract;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserPayroll;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingItem;
import de.vaplus.api.interfaces.RankableByCommission;
import de.vaplus.client.applicationbeans.ResourceBean;
import de.vaplus.client.entity.CommissionCacheEntity;

@ManagedBean(name = "crosscanBean")
@ApplicationScoped
public class CrosscanBean implements Serializable {

	private static final long serialVersionUID = -6008596107053317581L;
	
	@EJB
	private CrosscanControllerInterface crosscanController;

	@EJB
	private ResourceBean resourceBean;

	private Map<RankableByCommission,long[]> currentMonthRangeData;
	private Date currentMonthRangeDataGenerated;
	
	public CrosscanBean(){
	}
	
	private Map<RankableByCommission,long[]> getCurrentMonthRangeData(){
		
		if(currentMonthRangeDataGenerated == null){
			currentMonthRangeData = null;
		}else{
			
			long diff = currentMonthRangeDataGenerated.getTime() - (new Date()).getTime();
			
			if(diff > 15 * 60 * 1000){ // 15 Minuten
				currentMonthRangeData = null;
			}
		}
		
		
		if(currentMonthRangeData == null){
			currentMonthRangeData = new HashMap<RankableByCommission,long[]>();
			currentMonthRangeDataGenerated = new Date();
		}
			
		return currentMonthRangeData;
	}

	public boolean isCrosscanDataSet(Object crosscanOwner) {
		Shop shop;
		
		if(crosscanOwner instanceof Shop){
			shop = (Shop) crosscanOwner;
			
			if(shop.getCrosscanData_storeID() != null && shop.getCrosscanData_storeID().length() > 0){
				return true;
			}

		}if(crosscanOwner instanceof ShopGroup){
			
			ShopGroup shopGroup = (ShopGroup) crosscanOwner;
			
			Iterator<? extends Shop> i = shopGroup.getShopList().iterator();
			
			while(i.hasNext()){
				shop = i.next();
				if(shop.isDeleted())
					continue;
				if(! shop.isEnabled())
					continue;
				
				if(shop.getCrosscanData_storeID() != null && shop.getCrosscanData_storeID().length() > 0){
					return true;
				}
			}
		}

		return false;
	}
	
	public long[] getCrosscanDataCurrentMonthRange(Object crosscanOwner) {
		
		long[] data = {0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L};
		long[] data2;
		Shop shop;
		
		if(crosscanOwner instanceof Shop){
			shop = (Shop) crosscanOwner;
			

			if(getCurrentMonthRangeData().containsKey(shop))
				return getCurrentMonthRangeData().get(shop);
			
			data = calculateCrosscanDataCurrentMonthRange(shop);
			
			getCurrentMonthRangeData().put(shop, data);
		}if(crosscanOwner instanceof ShopGroup){
			
			ShopGroup shopGroup = (ShopGroup) crosscanOwner;
			

			if(getCurrentMonthRangeData().containsKey(shopGroup))
				return getCurrentMonthRangeData().get(shopGroup);
			
			Iterator<? extends Shop> i = shopGroup.getShopList().iterator();
			
			while(i.hasNext()){
				shop = i.next();
				if(shop.isDeleted())
					continue;
				if(! shop.isEnabled())
					continue;
				
				data2 = calculateCrosscanDataCurrentMonthRange(shop);
				
				for(int j=0;j<12;j++){
					data[j] = data[j] + data2[j];
				}
			}
			

			getCurrentMonthRangeData().put(shopGroup, data);
		}
		
		return data;
	}
	
	private long[] calculateCrosscanDataCurrentMonthRange(Shop shop){
		long[] data = {0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L};
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -9);

		for(int i = 0; i < data.length; i++) {
			
			data[i] = getCrosscanDataForMonth(shop, c.get(Calendar.YEAR), c.get(Calendar.MONTH));
			c.add(Calendar.MONTH, 1);
			
		}
		
		return data;
	}
	
	public long getCrosscanDataForMonth(Shop shop, int year, int month){
		
		return crosscanController.getCrosscanDataForMonth(shop, year, month);
		
	}
}
