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
import javax.ws.rs.QueryParam;

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

@ManagedBean(name = "debugBean")
@SessionScoped
public class DebugBean implements Serializable {

	private static final long serialVersionUID = -8928139540320447900L;


	boolean hideRightSideBar = false;
	boolean hideFooter = false;
	
	public boolean isHideRightSideBar(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
		
		if(requestMap.containsKey("hideRightSideBar")){
			if(requestMap.get("hideRightSideBar").equalsIgnoreCase("true"))
				hideRightSideBar = true;
			if(requestMap.get("hideRightSideBar").equalsIgnoreCase("false"))
				hideRightSideBar = false;
		}
		
		return hideRightSideBar;
	}
	
	public boolean isHideFooter(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
		
		if(requestMap.containsKey("hideFooter")){
			if(requestMap.get("hideFooter").equalsIgnoreCase("true"))
				hideFooter = true;
			if(requestMap.get("hideFooter").equalsIgnoreCase("false"))
				hideFooter = false;
		}
		
		return hideFooter;
	}

}
