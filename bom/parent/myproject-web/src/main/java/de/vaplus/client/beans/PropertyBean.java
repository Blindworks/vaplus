package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.entity.Customer;


@ManagedBean(name="propertyBean")
@SessionScoped
public class PropertyBean implements Serializable{
	
	private static final long serialVersionUID = -1836400320897162195L;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private CustomerControllerInterface customerController;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	@Inject
	private FacesContext facesContext;
	
	private boolean systemPropertiesEditable;

	private Map<String,BigDecimal> decimalPropertyCache;
	private Map<String,Long> longPropertyCache;
	private Map<String,String> stringPropertyCache;

	private Map<String,String> stringUserPropertyCache;
	private Map<String,Boolean> booleanUserPropertyCache;
	private Map<String,Long> longUserPropertyCache;
	private Map<String,BigDecimal> decimalUserPropertyCache;
	
	
	private String licenseName;

	public PropertyBean() {

		decimalPropertyCache = new HashMap<String,BigDecimal>();
		longPropertyCache = new HashMap<String,Long>();
		stringPropertyCache = new HashMap<String,String>();
		
		stringUserPropertyCache = new HashMap<String,String>();
		booleanUserPropertyCache = new HashMap<String,Boolean>();
		longUserPropertyCache = new HashMap<String,Long>();
		decimalUserPropertyCache = new HashMap<String,BigDecimal>();
		
	}
	
	private Long getLongUserProperty(String key, Long defaultValue){
		
		if(! longUserPropertyCache.containsKey(key))
			longUserPropertyCache.put(key, propertyController.getLongUserProperty(userBean.getActiveUser(), key, defaultValue));
		
		return longUserPropertyCache.get(key);
	}
	
	private void updateLongUserProperty(String key, Long value){
		
		propertyController.updateLongUserProperty(userBean.getActiveUser(), key, value);
		longUserPropertyCache.put(key, value);
	}
	
	private boolean getBooleanUserProperty(String key, Boolean defaultValue){
		
		if(! booleanUserPropertyCache.containsKey(key))
			booleanUserPropertyCache.put(key, propertyController.getBooleanUserProperty(userBean.getActiveUser(), key, defaultValue));
		
		return booleanUserPropertyCache.get(key);
	}
	
	private void updateBooleanUserProperty(String key, Boolean value){
		
		propertyController.updateBooleanUserProperty(userBean.getActiveUser(), key, value);
		booleanUserPropertyCache.put(key, value);
	}
	
	private String getStringUserProperty(String key, String defaultValue){
		
		if(! stringUserPropertyCache.containsKey(key))
			stringUserPropertyCache.put(key, propertyController.getStringUserProperty(userBean.getActiveUser(), key, defaultValue));
		
		return stringUserPropertyCache.get(key);
	}
	
	private void updateStringUserProperty(String key, String value){
		
		propertyController.updateStringUserProperty(userBean.getActiveUser(), key, value);
		stringUserPropertyCache.put(key, value);
	}
	
	private BigDecimal getDecimalUserProperty(String key, BigDecimal defaultValue){
		
		if(! decimalUserPropertyCache.containsKey(key))
			decimalUserPropertyCache.put(key, propertyController.getDecimalUserProperty(userBean.getActiveUser(), key, defaultValue));
		
		return decimalUserPropertyCache.get(key);
	}
	
	private void updateDecimalUserProperty(String key, BigDecimal value){
		
		propertyController.updateDecimalUserProperty(userBean.getActiveUser(), key, value);
		decimalUserPropertyCache.put(key, value);
	}
	

	private BigDecimal getDecimalProperty(String key, BigDecimal defaultValue){
		
		if(! decimalPropertyCache.containsKey(key))
			decimalPropertyCache.put(key, propertyController.getDecimalProperty(key, defaultValue));
		
		return decimalPropertyCache.get(key);
	}
	
	private void updateDecimalProperty(String key, BigDecimal value){

		propertyController.updateDecimalProperty(key, value);
		decimalPropertyCache.put(key, value);
	}

	
	private Long getLongProperty(String key, Long defaultValue){
		
		if(! longPropertyCache.containsKey(key))
			longPropertyCache.put(key, propertyController.getLongProperty(key, defaultValue));
		
		return longPropertyCache.get(key);
	}
	
	private void updateLongProperty(String key, Long value){
		
		propertyController.updateLongProperty(key, value);
		longPropertyCache.put(key, value);
	}
	
	private String getStringProperty(String key, String defaultValue){
		
		if(! stringPropertyCache.containsKey(key))
			stringPropertyCache.put(key, propertyController.getStringProperty(key, defaultValue));
		
		return stringPropertyCache.get(key);
	}
	
	private void updateStringProperty(String key, String value){
		
		propertyController.updateStringProperty(key, value);
		stringPropertyCache.put(key, value);
	}
	
	@PostConstruct
	private void init(){
	}
	
	public String getLicenseName(){
		if(licenseName == null)
			licenseName = propertyController.getLicenseName();
		return licenseName;
	}

	public void setPointsPerProfit(BigDecimal value){
		updateDecimalProperty("pointsPerProfit", value);
	}

	public BigDecimal getPointsPerProfit(){
		return getDecimalProperty("pointsPerProfit", new BigDecimal(0));
	}

	public void setExtensionOfTermLimit(long value){
		updateLongProperty("extensionOfTermLimit", value);
	}

	public long getExtensionOfTermLimit(){
		return getLongProperty("extensionOfTermLimit", 90L);
	}
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public boolean isSystemPropertiesEditable() {
		return systemPropertiesEditable;
	}

	public void toggleSystemPropertiesEditable() {
		systemPropertiesEditable = systemPropertiesEditable ? false : true;
	}
	
	public boolean isShowGroupedProductsOnSideBar(){
		if(userBean.getActiveUser() == null)
			return true;
		
		return getBooleanUserProperty("ShowGroupedProductsOnSideBar", true);
	}
	
	public void setShowGroupedProductsOnSideBar(boolean value){
		updateBooleanUserProperty("ShowGroupedProductsOnSideBar", value);
	}
	
	public void toggleShowGroupedProductsOnSideBar(){
		if(isShowGroupedProductsOnSideBar())
			setShowGroupedProductsOnSideBar(false);
		else
			setShowGroupedProductsOnSideBar(true);
	}
	
	public void setDefaultSideBarTab(String value){
		updateStringUserProperty("defaultSideBarTab", "1");
	}
	
	public String getDefaultSideBarTab(){
		if(userBean.getActiveUser() == null)
			return "1";

		return getStringUserProperty("defaultSideBarTab", "1");
	}
	
	public void doNothing(){
		
	}
	
	public String getAppVerson(){
		return propertyController.getAppVerson();
	}
	
	public String getDBVerson(){
		return propertyController.getDBVersion();
	}
	
	public void setMinimumPricePremium(BigDecimal minimumPricePremium){
		updateDecimalProperty("minimumPricePremium", minimumPricePremium);
	}
	
	public BigDecimal getMinimumPricePremium(){
		return getDecimalProperty("minimumPricePremium", new BigDecimal(0));
	}

	public void setSubsidyTitle(String value){
		updateStringProperty("subsidyTitle", value);
	}

	public String getSubsidyTitle(){
		return getStringProperty("subsidyTitle", "Vertrags-Subvention");
	}


	public void setDefaultCustomer(Customer customer){
		if(customer == null)
			updateLongProperty("defautCustomerId", 0L);
		else
			updateLongProperty("defautCustomerId", customer.getId());
	}
	public void setDefaultCustomerId(long id){
		if(id == 0)
			updateLongProperty("defautCustomerId", 0L);
		else{
			Customer customer = customerController.getCustomer(id);
			if(customer != null)
				updateLongProperty("defautCustomerId", customer.getId());
		}
	}

	public Customer getDefaultCustomer(){
		long id = getLongProperty("defautCustomerId", 0L);
		return customerController.getCustomer(id);
	}

	public long getDefaultCustomerId(){
		return getLongProperty("defautCustomerId", 0L);
	}

	
	public String getCalendarChangingTime(){
		return getStringProperty("calendarChangingTime", "14:00");
	}

	public void setCalendarChangingTime(String value){
		updateStringProperty("calendarChangingTime", value);
	}
	
	
	public String getExternalCalendarId(){
		return getStringProperty("externalCalendarId", "0");
	}

	public void setExternalCalendarId(String value){
		updateStringProperty("externalCalendarId", value);
	}
	
	
	public String getYearViewCalendarId(){
		return getStringProperty("yearViewCalendarId", "0");
	}

	public void setYearViewCalendarId(String value){
		updateStringProperty("yearViewCalendarId", value);
	}
	
	
	public long getPauseLength6h(){
		return getLongProperty("pauseLength6h", 30L);
	}

	public void setPauseLength6h(long value){
		if(value < 30){
			value = 30;
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Minimale Pausenlänge bei 6h Arbeitszeit 30 Min.", null));
		}
		updateLongProperty("pauseLength6h", value);
	}
	
	
	public long getPauseLength9h(){
		return getLongProperty("pauseLength9h", 45L);
	}

	public void setPauseLength9h(long value){
		if(value < 45){
			value = 45;
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Minimale Pausenlänge bei 9h Arbeitszeit 45 Min.", null));
		}
		updateLongProperty("pauseLength9h", value);
	}

	
	public boolean isShowShopGroupDashboard(){
		if(userBean.getActiveUser() == null)
			return true;

		return getBooleanUserProperty("ShowShopGroupDashboard", false);
	}
	
	public void setShowShopGroupDashboard(boolean value){
		updateBooleanUserProperty("ShowShopGroupDashboard", value);

		try {
		
			if(value){
				if(facesContext.getViewRoot().getViewId().equalsIgnoreCase("/dashboard.xhtml")){
					facesContext.getExternalContext().redirect("/dashboardShopGroup.xhtml");
				    FacesContext.getCurrentInstance().responseComplete();
				}
			}else{
	
				if(facesContext.getViewRoot().getViewId().equalsIgnoreCase("/dashboardShopGroup.xhtml")){
				    facesContext.getExternalContext().redirect("/dashboard.xhtml");
				    FacesContext.getCurrentInstance().responseComplete();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getDashboardId(){
		if(isShowShopGroupDashboard())
			return "dashboardShopGroup";
		else
			return "dashboard";
	}

	public String getInvoiceIntroduction(){
		return getStringProperty("invoiceIntroduction", "");
	}

	public void setInvoiceIntroduction(String value){
		updateStringProperty("invoiceIntroduction", value);
	}

	public String getInvoiceClosing(){
		return getStringProperty("invoiceClosing", "");
	}

	public void setInvoiceClosing(String value){
		updateStringProperty("invoiceClosing", value);
	}

	public String getDocFooterLine1(){
		return getStringProperty("docFooterLine1", "");
	}

	public void setDocFooterLine1(String value){
		updateStringProperty("docFooterLine1", value);
	}

	public String getDocFooterLine2(){
		return getStringProperty("docFooterLine2", "");
	}

	public void setDocFooterLine2(String value){
		updateStringProperty("docFooterLine2", value);
	}

	public String getDocFooterLine3(){
		return getStringProperty("docFooterLine3", "");
	}

	public void setDocFooterLine3(String value){
		updateStringProperty("docFooterLine3", value);
	}

	public String getDrafterLine(){
		return getStringProperty("drafterLine", "");
	}

	public void setDrafterLine(String value){
		updateStringProperty("drafterLine", value);
	}
	
}
