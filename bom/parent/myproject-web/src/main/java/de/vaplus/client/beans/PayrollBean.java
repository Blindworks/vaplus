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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.CommissionAccountingControllerInterface;
import de.vaplus.api.ContractControllerInterface;
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
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.PhoneContract;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserPayroll;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingItem;

@ManagedBean(name = "payrollBean")
@SessionScoped
public class PayrollBean implements Serializable {


	@EJB
	private PayrollControllerInterface payrollController;
	
	@EJB
	private ContractControllerInterface contractController;
	
	@EJB
	private OrderControllerInterface orderController;

    @ManagedProperty(value="#{achievementBean}")
    private AchievementBean achievementBean;

	@Inject
	private FacesContext facesContext;

	private int month;
	private int year;
	
	private UserPayroll selectedUserPayroll;
	
	private List<? extends UserPayroll> userPayrollList;

	private List<? extends BaseContract> contractList;
	private List<? extends CommissionActivity> commissionActivityList;
	private List<? extends Order> orderList;
	private List<? extends Achievement> achievementList;
	
	public PayrollBean(){
		if(month == 0 || year == 0){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -1);
			
			this.setYear(c.get(Calendar.YEAR));
			this.setMonth(c.get(Calendar.MONTH));
		}
	}
	
	public AchievementBean getAchievementBean() {
		return achievementBean;
	}

	public void setAchievementBean(AchievementBean achievementBean) {
		this.achievementBean = achievementBean;
	}

	public void saveSelectedPayroll(){
		if(selectedUserPayroll == null)
			return;
		
		selectedUserPayroll = payrollController.saveUserPayroll(selectedUserPayroll);
	}

	private void clear(){
		userPayrollList = null;
		contractList = null;
		commissionActivityList = null;
		orderList = null;
	}
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		this.clear();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		this.clear();
	}
	
	public List<? extends UserPayroll> getUserPayrollList() {
		if(userPayrollList == null)
			userPayrollList = payrollController.getUserPayrollList(month, year);
		
		return userPayrollList;
	}
	
	public void generateUserPayrolls(){
		payrollController.generateUserPayrolls(month, year);
		this.clear();
	}
	
	public void setSelectedUserPayroll(UserPayroll userPayroll){
		this.selectedUserPayroll = userPayroll;
		clear();
	}
	
	public UserPayroll getSelectedUserPayroll(){
		return this.selectedUserPayroll;
	}

	private Date getFrom(){
		
		if(selectedUserPayroll == null)
			return new Date();

		// All Month workaround
		int m = selectedUserPayroll.getMonth();
		if(m == -1)
			m = 0;

	    Calendar cal = Calendar.getInstance();
	    cal.set(selectedUserPayroll.getYear(), m, 1, 0, 0, 0);
	    return cal.getTime();
	}
	
	private Date getTo(){

		// All Month workaround
		int m = selectedUserPayroll.getMonth();
		if(m == -1)
			m = 11;

	    Calendar cal = Calendar.getInstance();
	    cal.set(selectedUserPayroll.getYear(), m, 1, 0, 0, 0);
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.MILLISECOND, -1);
	    
	    return cal.getTime();
	}
	
	public List<? extends BaseContract> getContractList() {
		
		if(selectedUserPayroll == null)
			return new ArrayList<BaseContract>();
		
		if(contractList == null)
			contractList = contractController.getContractList(null, null, selectedUserPayroll.getUser(), getFrom(), getTo());
		
		return contractList;
	}

	
	public List<? extends Order> getOrderList() {
		
		if(selectedUserPayroll == null)
			return new ArrayList<Order>();
		
		if(orderList == null)
			orderList = orderController.getOrderList(null, selectedUserPayroll.getUser(), getFrom(), getTo());
		
		return orderList;
	}
	
	public List<? extends CommissionActivity> getCommissionActivityList() {
		
		if(selectedUserPayroll == null)
			return new ArrayList<CommissionActivity>();
		
		if(commissionActivityList == null)
			commissionActivityList = contractController.getCommissionActivityList(null, selectedUserPayroll.getUser(), getFrom(), getTo());
		
		return commissionActivityList;
	}
	
	public List<? extends Achievement> getAchievementList(){

		if(selectedUserPayroll == null)
			return new ArrayList<Achievement>();
		
		if(achievementList == null)
			achievementList = achievementBean.getUserAchievementList(selectedUserPayroll.getUser(), selectedUserPayroll.getMonth(), selectedUserPayroll.getYear());
		
		return achievementList;
	}
}
