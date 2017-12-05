package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.applicationbeans.ResourceBean;
import de.vaplus.client.beans.PropertyBean;
import de.vaplus.client.beans.UserBean;
import de.vaplus.client.pojo.SideBarProduct;


@ManagedBean(name = "sideBarBean")
@SessionScoped
public class SideBarBean implements Serializable {

	private static final long serialVersionUID = -2600432075279854154L;

	@EJB
	private ResourceBean resourceBean;
	
    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

	@ManagedProperty(value="#{propertyBean}")
	private PropertyBean propertyBean;
	
	public SideBarBean(){
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public PropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public List<? extends User> getSalesUserList(){
		return resourceBean.getSalesUserList();
	}
	
	public List<? extends Shop> getSalesShopList(){
		
		if(userBean.getActiveUser() == null)
			return new LinkedList<Shop>();
		
		if(userBean.getActiveUser().isSupervisor())
			return resourceBean.getSalesShopList();
		else{
			List<Shop> l = new LinkedList<Shop>();
			Iterator<? extends Shop> i = resourceBean.getSalesShopList().iterator();
			Shop s;
			while(i.hasNext()){
				s = i.next();
				if(userBean.getActiveUser().getAllowedShops().contains(s))
					l.add(s);
			}
			return l;
		}
	}


	public List<SideBarProduct> getSideBarProductList(){
		if(propertyBean.isShowGroupedProductsOnSideBar())
			return resourceBean.getGroupedSideBarProductList();
		else
			return resourceBean.getSideBarProductList();
	}

}
