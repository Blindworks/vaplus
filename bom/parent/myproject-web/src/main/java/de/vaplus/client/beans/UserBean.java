package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import org.picketlink.Identity;
import org.picketlink.idm.model.basic.Group;

import de.vaplus.api.ActivityControllerInterface;
import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.SecureLinkControllerInterface;
import de.vaplus.api.TestControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.JobTitle;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserStats;
import de.vaplus.client.applicationbeans.ResourceBean;
import de.vaplus.client.mail.Mailer;
import de.vaplus.client.mail.Theming;
import de.vaplus.client.picketlink.LoginController;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1612030540118217886L;

	@EJB
	private TestControllerInterface testBean;

	@EJB
	private UserControllerInterface userController;

    @EJB
    private SecureLinkControllerInterface secureLinkController;

    @EJB
    private CommissionControllerInterface commissionController;

    @EJB
    private ActivityControllerInterface activityController;

	@EJB
	private EventControllerInterface eventController;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private HolidayControllerInterface holidayController;
    
	@EJB
	private ResourceBean resourceBean;


	@Inject
	private FacesContext facesContext;

    @Inject
    private SecureLinkBean secureLinkBean;

    @EJB
    private PicketLinkBean picketLinkBean;

    @ManagedProperty(value="#{helperBean}")
    private HelperBean helperBean;

    @ManagedProperty(value="#{mailBean}")
    private MailBean mailBean;

    @Inject
    private Identity credentials;

    @Inject
    private LoginController loginController;


	private User activeUser;

	private User selectedUser;
	private String selectedUserRole;

	private boolean userListEditable;


	private String oldPassword;
	private String password;
	private String password2;

	private JobTitle selectedJobTitle;
	private EmploymentForm selectedEmploymentForm;


	public UserBean() {
//		 System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}


//    @Produces
//    @Named
//    @RequestScoped
    public User getActiveUser() {
    	
    	org.picketlink.idm.model.basic.User loggedInUser = (org.picketlink.idm.model.basic.User) credentials.getAccount();
    	
    	if(loggedInUser == null){
    		activeUser = null;
    		try {
    			if(!facesContext.getExternalContext().isResponseCommitted())
    				facesContext.getExternalContext().redirect("/login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
    	}
    	
    	
    	if(activeUser != null){
    		if(! activeUser.getAccountTypeId().equalsIgnoreCase(loggedInUser.getId()))
        		activeUser = null;
    	}
    	
    	if(activeUser == null){
    		activeUser = userController.getUserByAccountTypeId(loggedInUser.getId());
    	}
    	


//    	Date d2 = new Date();
//
//    	long diff = d2.getTime() - d.getTime();
//
//    	System.out.println("getActiveUser: duration: "+diff+"ms");

        return activeUser;

    }

    public void checkSupervisorAccount(){
    	if(picketLinkBean.checkUserPassword("supervisor", "passw0rd"))
    		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte Supervisor Passwort erneuern.", null));
    }

    public User getUserById(long id){
    	 return userController.getUserById(id);
    }

    public User getUserByAlias(String alias){
    	 return userController.getUserByAlias(alias);
    }

	public List<? extends User> getUserList(){
		return userController.getUserList();
	}

	public List<? extends User> getUserList(boolean showDisabled){
		return userController.getUserList(showDisabled);
	}


	public String proceedSecureChangePasswordLink(){

//		System.out.println("secureLinkBean "+secureLinkBean.getSecureLink());

//		secureLinkController.expireSecureLink(secureUserLink);

		return "/sl/close";
	}

	public void prepareUser(){
//		System.out.println("UserBean prepareUser");

		if(selectedUser.getId() > 0)
			return;

		userController.createAccountTypeEntity(selectedUser);

	}

	public String saveUser(){
//		System.out.println("UserBean saveUser");

		boolean newUser = false;

		if(selectedUser.getId() == 0)
			newUser = true;
		

		if(selectedUser.getJobTitle() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte MA Position angeben.", null));
			return "";
		}
		
		if(selectedUser.getFormOfEmployment() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte MA Beschäftigung Art angeben.", null));
			return "";
		}


		selectedUser = userController.saveUser(selectedUser);
		if(selectedUserRole != null){
			if(selectedUserRole.equalsIgnoreCase("Supervisor")){
				picketLinkBean.revokeRole(selectedUser, "Employee");
				picketLinkBean.grantRole(selectedUser, "Supervisor");
			}else if(selectedUserRole.equalsIgnoreCase("Employee")){
				picketLinkBean.revokeRole(selectedUser, "Supervisor");
				picketLinkBean.grantRole(selectedUser, "Employee");
			}
		}

		if(newUser)
			sendWelcomeMessage();

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Mitarbeiter erfolgreich gespeichert.", null));

		userController.flushCache();

		resourceBean.refreshLists();

		return "userList";
	}

	public void loadSecureUser(){

	}

	public void resetUserLogon(){

	}

	public User getSelectedUser() {
		if(selectedUser == null)
			selectedUser = userController.factoryNewUser();
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;

		if(picketLinkBean.hasRole(selectedUser, "Supervisor")){
			selectedUserRole = "Supervisor";
		}else if(picketLinkBean.hasRole(selectedUser, "Employee")){
			selectedUserRole = "Employee";
		}


	}

	public HelperBean getHelperBean() {
		return helperBean;
	}

	public void setHelperBean(HelperBean helperBean) {
		this.helperBean = helperBean;
	}


	public MailBean getMailBean() {
		return mailBean;
	}


	public void setMailBean(MailBean mailBean) {
		this.mailBean = mailBean;
	}


	public void checkSecureLink(){
//		System.out.println("check secureLinkBean "+secureLinkBean.getSecureCode());
	}

	public void sendChangePwdLink(){
		mailBean.sendChangePwdLink(selectedUser);
	}


	public void sendWelcomeMessage(){
		mailBean.sendWelcomeMessage(selectedUser);
	}



	public String getSelectedUserRole() {
		return selectedUserRole;
	}


	public void setSelectedUserRole(String selectedUserRole) {
		this.selectedUserRole = selectedUserRole;
	}


	public boolean isUserListEditable() {
		return userListEditable;
	}


	public void toggleUserListEditable() {
		userListEditable = userListEditable ? false : true;
	}

	public List<Part> getNewUserImage(){
		return null;
	}

	public void setNewUserImage(List<Part> files) throws IOException {

		if(files != null && files.size() > 0){
			Part file = files.get(0);

			if(!file.getContentType().equalsIgnoreCase("image/png") && !file.getContentType().equalsIgnoreCase("image/jpeg")){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nur png oder jpeg Bilder erlaubt!", null));
				return;
			}

			if(file.getSize() > 100*1024){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Max 100kb pro Bild erlaubt!", null));
				return;
			}
			selectedUser = userController.updateUserImage(selectedUser, file.getInputStream(), file.getContentType(), file.getSize());
		}
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword2() {
		return password2;
	}


	public void setPassword2(String password2) {
		this.password2 = password2;
	}


	public void changePwd(){


		if(! userController.checkOldPassword(getActiveUser(), oldPassword)){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Altes Passwort nicht korrekt!", null));
			return;
		}

		if(password.length() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte Passwort angeben!", null));
			return;
		}

		if(password.compareTo(password2) != 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Passwort Wiederholdung falsch!", null));
			return;
		}

		if(password.compareTo(picketLinkBean.getUserLoginName(getActiveUser())) == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Passwort darf nicht dem Benutzernamen entsprechen!", null));
			return;
		}

		if(password.length() < 8 || userController.checkPasswordStrength(password) < 3){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Passwort entspricht nicht den Mindestanforderungen!", null));
			return;
		}

		picketLinkBean.changeUserPassword(getActiveUser(), password);

		password = null;
		password2 = null;


		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Neues Kennwort gespeichert.", null));
	}


	public String getOldPassword() {
		return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public List<? extends Activity> getTimeline(){
		return activityController.getUserTimeline(this.getActiveUser(), 10, 0);
	}

	public List<Group> getUserGroupList(){
		return picketLinkBean.getGroupList();
	}

	public List<String> getUserGroups(){
		List<String> selectedGroups = new ArrayList<String>();

		Iterator<Group> i = getUserGroupList().iterator();
		Group group;

		while(i.hasNext()){
			group = i.next();

			if(picketLinkBean.hasGroup(selectedUser, group))
				selectedGroups.add(group.getId());
		}

		return selectedGroups;
	}

	public void setUserGroups(List<String> groups){

		Iterator<Group> i = getUserGroupList().iterator();
		Group group;

		while(i.hasNext()){
			group = i.next();

			if(groups.contains(group.getId())){
				picketLinkBean.addToGroup(selectedUser, group);
			}else{
				picketLinkBean.removeFromGroup(selectedUser, group);
			}
		}

	}

	public long calculateFreeWorkingTime(User user, Date start, Date end){

		Calendar c = Calendar.getInstance();
		c.setTime(end);
		c.add(Calendar.MILLISECOND, -1);

		return userController.calculateFreeWorkingTime(user, start, c.getTime());
	}


	public float calculateVacationDuration(Event event){
		return userController.calculateVacationDuration(event);
	}

	public boolean isWorkingDay(Date date, User user){
		return userController.isWorkingDay(date, user);
	}

	public float calculateVacation(User user, int year){
		return userController.calculateVacation(user, year);
	}

	public float calculateRemainingVacation(User user, int year){

		float remainingVacation = user.getVacationDays();

		return remainingVacation - calculateVacation(user, year);
	}

	public void clearFailedLogins(){
		selectedUser = userController.clearFailedLogins(selectedUser);
	}

	public int getOvertime(User user) {

		Iterator<? extends UserStats> i = user.getStatsList().iterator();
		UserStats stats;
		int overtime = 0;
		while(i.hasNext()){
			stats = i.next();

			overtime += stats.getOvertime();
		}
		return overtime;
	}

	public UserStats getFirstUserStats(){

			Calendar c = Calendar.getInstance();

			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.MONTH, selectedUser.getPlaningStartMonth());
			c.set(Calendar.YEAR, selectedUser.getPlaningStartYear());

			c.add(Calendar.MONTH, -1);

			return selectedUser.getStats(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
	}

	public void updateAllUserAllStats(){
		userController.updateAllUserAllStats();
	}

	public List<Integer> getPlaningYearList(){

		Event firstEvent = eventController.getFirstEvent(selectedUser);
		int firstYear;
		Calendar c = Calendar.getInstance();

		if(firstEvent != null){
			c.setTime(firstEvent.getEffectiveDate());
		}

		firstYear = c.get(Calendar.YEAR);

	    c.setTime(new Date());
	    int actYear = c.get(Calendar.YEAR);

	    List<Integer> yearList = new LinkedList<Integer>();

		for(int i = firstYear; i <= actYear; i++){
			yearList.add(i);
		}

		return yearList;
	}


	public String deleteUser(){

		selectedUser.setDeleted();
		selectedUser = userController.saveUser(selectedUser);

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Mitarbeiter gelöscht.", null));

		return "/settings/userList";
	}

	public List<? extends EmploymentForm> getEmploymentFormList(){
		return userController.getEmploymentFormList();
	}

	public List<? extends JobTitle> getJobTitleList(){
		return userController.getJobTitleList();
	}

	public void setLastSeenCustomer(Customer customer){
		User user = this.getActiveUser();

		Iterator<? extends UserCustomerHistory> i = user.getCustomerHistoryList().iterator();
		UserCustomerHistory history = null;
		while(i.hasNext()){
			history = i.next();
			if(history.getCustomer().getId() == customer.getId())
				break;

			history = null;
		}

		if(history != null){
			history.refreshLastOpened();
		}else{
			history = userController.factoryNewUserCustomerHistory(user, customer);
			((List<UserCustomerHistory>)user.getCustomerHistoryList()).add(history);
		}

		Collections.sort(user.getCustomerHistoryList(), new Comparator<UserCustomerHistory>() {
			  public int compare(UserCustomerHistory h1, UserCustomerHistory h2) {
			      return h2.getLastOpened().compareTo(h1.getLastOpened());
			  }
		});

		while(user.getCustomerHistoryList().size() > 15){
			user.getCustomerHistoryList().remove(user.getCustomerHistoryList().size() -1);
		}

		userController.saveUser(user);
	}


	public JobTitle getSelectedJobTitle() {
		if(selectedJobTitle == null)
			selectedJobTitle = userController.factoryJobTitle();
		return selectedJobTitle;
	}


	public void setSelectedJobTitle(JobTitle selectedJobTitle) {
		this.selectedJobTitle = selectedJobTitle;
	}

	
	public String saveJobTitle(){
		selectedJobTitle = userController.saveJobTitle(selectedJobTitle);
		return "lists";
	}


	public EmploymentForm getSelectedEmploymentForm() {
		if(selectedEmploymentForm == null)
			selectedEmploymentForm = userController.factoryEmploymentForm();
		return selectedEmploymentForm;
	}


	public void setSelectedEmploymentForm(EmploymentForm selectedEmploymentForm) {
		this.selectedEmploymentForm = selectedEmploymentForm;
	}

	
	public String saveEmploymentForm(){
		selectedEmploymentForm = userController.saveEmploymentForm(selectedEmploymentForm);
		return "lists";
	}


	public List<? extends UserAlias> getUserAliasList(){
		return userController.getUserAliasList();
	}

	
	public void deleteUserAlias(UserAlias userAlias){
		userController.deleteUserAlias(userAlias);
	}

}
