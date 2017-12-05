package de.vaplus.client.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.VOTypeCommissionRevenueLevel;

@ManagedBean(name="commissionBean")
@SessionScoped
public class CommissionBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;

	@EJB
	private CommissionControllerInterface commissionController;

	@EJB
	private UserControllerInterface userController;

	@EJB
	private ShopControllerInterface shopController;
	
	public CommissionBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void updateCommissionHistory(){
		
		Iterator<? extends User> ui = userController.getUserList().iterator();
		User user;
		while(ui.hasNext()){
			user = ui.next();
			commissionController.updateUserComissionHistory(user);
		}
		
		Iterator<? extends Shop> si = shopController.getShopList().iterator();
		Shop shop;
		while(si.hasNext()){
			shop = si.next();
			commissionController.updateShopComissionHistory(shop);
		}
	}

	public BigDecimal calculatePoints(BigDecimal commission, VOType votype){
		return commissionController.calculatePoints(commission, votype);
	}

	public BigDecimal calculateRevenuePoints(BigDecimal commission, VOTypeCommissionRevenueLevel revenueLevel){
		return commissionController.calculateRevenuePoints(commission, revenueLevel);
	}

}
