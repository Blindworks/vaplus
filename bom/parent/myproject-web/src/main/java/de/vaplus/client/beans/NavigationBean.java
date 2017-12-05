package de.vaplus.client.beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.ModulControllerInterface;
import de.vaplus.api.entity.Customer;
import de.vaplus.client.pojo.Icon;
import de.vaplus.client.pojo.Page;


@SessionScoped
@ManagedBean(name="navigation")
public class NavigationBean implements Serializable{
	
	@EJB
	private ModulControllerInterface modulController;
	
	@Inject
	private FacesContext facesContext;

	@ManagedProperty(value="#{customerBean}")
	private CustomerBean customerBean;
	

	private Customer customer;
	
	private static final long serialVersionUID = 8297151885977240211L;

//	private List<Page> subPages;
	private List<Page> mainPages;
	
	public NavigationBean() {
	}
	
	public CustomerBean getCustomerBean() {
		return customerBean;
	}

	public void setCustomerBean(CustomerBean customerBean) {
		this.customerBean = customerBean;
	}
	
	private void generateMainNavigation(){
		customer = customerBean.getSelectedCustomer();

		resetMainPages();

		if(customer != null && customer.getId() > 0){
			Page customerPage = new Page(customer.getName(),"/customer/",Icon.MALE);
			customerPage.addSubPage(new Page("Profil","/customer/profile",Icon.INFO));
			customerPage.addSubPage(new Page("Aktivitäten","/customer/activities",Icon.EXCHANGE));
			customerPage.addSubPage(new Page("Vertrag","/redirect/setNewContract",Icon.PLUS, "/customer/contract"));
			customerPage.addSubPage(new Page("Verkauf","/redirect/setNewOrder",Icon.BANK, "/customer/order"));
			customerPage.addSubPage(new Page("Kundendaten","/customer/customer",Icon.LIST));
			mainPages.add(customerPage);
		}

		
		mainPages.add(new Page("Dashboard","/dashboard",Icon.DASHBOARD));
		
//		Page customerHistory = new Page("Kunden Verlauf","/pseudo",Icon.CLOCK_O);
//		customerHistory.addSubPage(new Page("Max Mustermann","/pseudo",Icon.MALE));
//		customerHistory.addSubPage(new Page("John Doe","/pseudo",Icon.MALE));
//		customerHistory.addSubPage(new Page("Lara Schmidt","/pseudo",Icon.MALE));
//		customerHistory.addSubPage(new Page("Susanne Müller","/pseudo",Icon.MALE));
//		mainPages.add(customerHistory);
		
		

		mainPages.add(new Page("Neukunde","/redirect/setNewCustomer",Icon.PLUS, "/newCustomer"));

		Page stats = new Page("Controlling","/stats/",Icon.CHART_BAR_O);
		stats.addSubPage(new Page("Übersicht","/stats/overview",Icon.CHART_BAR_O));
		stats.addSubPage(new Page("Filial Vergleich","/stats/shopList",Icon.CHART_BAR_O));
		stats.addSubPage(new Page("MA Vergleich","/stats/shopList",Icon.CHART_BAR_O));
		stats.addSubPage(new Page("VO Vergleich","/stats/shopList",Icon.CHART_BAR_O));
		// stats.addSubPage(new Page("Filialen","/stats/shopList",Icon.BUILDING_O));
		stats.addSubPage(new Page("Verträge","/stats/contractList",Icon.QUESTION));
		mainPages.add(stats);

		Page cal = new Page("Kalender","/calendar/",Icon.CALENDAR);
		cal.addSubPage(new Page("Mitarbeiter Kalender","/calendar/user",null));
		cal.addSubPage(new Page("Filial Kalender","/calendar/shop",null));
		cal.addSubPage(new Page("Filial Übersicht","/calendar/shopList",null));
		mainPages.add(cal);
		
		
		

		if(modulController.isDevelopmentView()){
			

//			 mainPages.add(new Page("Chat","/chat/chat",Icon.COMMENTS));
//			 mainPages.add(new Page("Wiedervorlagen","/followUp/followUp",Icon.REPEAT));
			
			Page management = new Page("Verwaltung","/management/",Icon.GEARS);
			management.addSubPage(new Page("Kunden Liste","/management/customer/list",Icon.GROUP));
			mainPages.add(management);
			
		}

		Page settings = new Page("Einstellungen","/settings/",Icon.GEARS);
		settings.addSubPage(new Page("System","/settings/system",Icon.TOGGLE_ON));
		settings.addSubPage(new Page("Mitarbeiter","/settings/userList",Icon.GROUP, "/settings/user"));
		settings.addSubPage(new Page("Mitarbeiter Gruppen","/settings/userGroupList",Icon.GROUP, "/settings/userGroup"));
		settings.addSubPage(new Page("Filialen","/settings/shopList",Icon.BUILDING_O, "/settings/shop"));
		settings.addSubPage(new Page("VO Management","/settings/voManagement",Icon.CODEPEN, "/settings/vo"));
		settings.addSubPage(new Page("Anbieter","/settings/vendorList",Icon.CODEPEN, "/settings/vendor"));
		settings.addSubPage(new Page("Produkt Kategorien","/settings/productCategoryList",Icon.CODEPEN, "/settings/productCategory"));
		settings.addSubPage(new Page("Produkte","/settings/productList",Icon.CODEPEN, "/settings/product"));
		settings.addSubPage(new Page("AU/GU Matrix","/settings/revenueLevelMatrix",Icon.CODEPEN, "/settings/revenueLevel"));
		settings.addSubPage(new Page("Sonder Kond.","/settings/productCommissionList",Icon.CODEPEN));
		settings.addSubPage(new Page("MwSt. Sätze","/settings/taxRateList",Icon.QUESTION));
		
		mainPages.add(settings);
		

		Page support = new Page("Support","/support/",Icon.LIFE_RING);
		support.addSubPage(new Page("FAQ","/support/faq",Icon.QUESTION));
		support.addSubPage(new Page("Funktionsanfragen","/support/featureRequests",Icon.LIGHTBULB_O));
		support.addSubPage(new Page("BUG-Tracker","/support/bugTracker",Icon.BUG));
		support.addSubPage(new Page("Kontakt","/support/contact",Icon.ENVELOPE));
		support.addSubPage(new Page("Versionshinweise","/support/releaseNotes",Icon.FILE_TEXT_O));
		
		Page documentation = new Page("Dokumentation","/support/docs",Icon.BOOK);
		documentation.addSubPage(new Page("Kapitel 1","/support/docs/kapitel1",Icon.BOOKMARK));
		documentation.addSubPage(new Page("Kapitel 2","/support/docs/kapitel2",Icon.BOOKMARK));
		documentation.addSubPage(new Page("Kapitel 3","/support/docs/kapitel3",Icon.BOOKMARK));
		support.addSubPage(documentation);

		mainPages.add(support);
		
		
	}
	
	public void resetMainPages(){
		setMainPages(new ArrayList<Page>());
	}

	private void setMainPages(List<Page> mainPages) {
		this.mainPages = mainPages;
	}

	public List<Page> getMainPages() {
		
		if(mainPages == null || !customer.equals(customerBean.getSelectedCustomer()))
			generateMainNavigation();
		return mainPages;
	}
	
//	public List<Page> getBreadcrumps(){
//		List<Page> crumps = new ArrayList<Page>();
//		if(mainPages == null)
//			return crumps;
//		
//		
//		String viewId = facesContext.getViewRoot().getViewId();
//		
//
//		searchForBreadcrumps(viewId, mainPages, crumps);
//		
//		return crumps;
//	}
	
	private void searchForBreadcrumps(String viewId, List<Page> pageList, List<Page> crumps){
		Iterator<Page> i = pageList.iterator();
		Page p;
		while(i.hasNext()){
			p = i.next();
			
			if( (p.getViewId() + ".xhtml").equalsIgnoreCase(viewId)){
				crumps.add(p);
				return;
			}
			
			if( viewId.startsWith(p.getViewId())){
				crumps.add(p);
				
				if(p.getSubPages() != null)
					searchForBreadcrumps(viewId, p.getSubPages(), crumps);
				
				return;
			}
			
		}
	}

}
