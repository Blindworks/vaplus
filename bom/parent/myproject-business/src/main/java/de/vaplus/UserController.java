package de.vaplus;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.SecureLinkControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.JobTitle;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.api.entity.UserStats;
import de.vaplus.api.entity.VacationRequest;
import de.vaplus.client.eao.UserEao;
import de.vaplus.client.entity.EmploymentFormEntity;
import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.JobTitleEntity;
import de.vaplus.client.entity.UserAliasEntity;
import de.vaplus.client.entity.UserCustomerHistoryEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.UserGroupEntity;
import de.vaplus.client.mail.Mailer;
import de.vaplus.client.util.picketlink.PicketlinkEao;
import de.vaplus.client.util.picketlink.SHA512Generator;
import de.vaplus.client.util.picketlink.SecureRandomGenerator;


@Stateless
public class UserController implements UserControllerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3122845987859644921L;


	@EJB
	private FileControllerInterface fileController;
	
	@EJB
	private SecureLinkControllerInterface secureLinkController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private EventControllerInterface eventController;
	
	@EJB
	private HolidayControllerInterface holidayController;
	
	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private ExceptionControllerInterface exceptionController;
	
	@Inject
	private Mailer mailer;

    @Inject
    private UserEao userEao;

    @Inject
    private PicketlinkEao picketLinkEao;
	
	public UserController(){
		// System.out.println("### INIT UserController ###");
	}
	
    @Override
    public void setAccountTypeEntity(User user, String accountTypeEntityid){
    	((UserEntity)user).setAccountTypeEntity(picketLinkEao.getAccountTypeEntityById(accountTypeEntityid));
    }
    
    @Override
    public User saveUser(User user){
    	
    	if(! (user instanceof UserEntity))
    		return null;

    	UserEntity u = (UserEntity) user;

    	if(u.getJobTitle() == null)
    		u.setJobTitle(userEao.getJobTitleByName("Mitarbeiter", true));

    	if(u.getAccountTypeEntity() == null){
    		createAccountTypeEntity(user);
    	}

    	user = userEao.saveUser(u);

		commissionController.updateUserComissionHistory(user);

    	return user;
    }
    
    @Override
    public UserGroup saveUserGroup(UserGroup userGroup){
    	
    	if(! (userGroup instanceof UserGroupEntity))
    		return null;

    	UserGroupEntity ug = (UserGroupEntity) userGroup;

    	userGroup = userEao.saveUserGroup(ug);

    	return userGroup;
    }
    
    @Override
    public User factoryNewUser(){
    	UserEntity user = new UserEntity();
    	user.setUuid(UUID.randomUUID().toString());
    	user.getAddress().setCountry("D");
    	user.setEnabled(true);
    	return user;
    }
    
    @Override
    public UserGroup factoryNewUserGroup(){
    	UserGroupEntity userGroup = new UserGroupEntity();
    	userGroup.setEnabled(true);
    	return userGroup;
    }
    
    @Override
    public UserAlias factoryNewUserAlias(String alias){
    	UserAliasEntity userAlias = new UserAliasEntity();
    	userAlias.setAlias(alias);
    	return userAlias;
    }
    
    @Override
    public UserCustomerHistory factoryNewUserCustomerHistory(User user, Customer customer){
    	UserCustomerHistoryEntity history = new UserCustomerHistoryEntity(user, customer);
    	return history; 
    }
    
//    @Override
//    public UserCustomerHistory saveUserCustomerHistory(UserCustomerHistory history){
//    	return userEao.saveUserCustomerHistory(history);
//    }
    
    @Override
	public void createSupervisorUser(){
    	
    	
    	
    	
//		System.out.println("userController createSupervisorUser");
		
		UserEntity u = new UserEntity();
		u.setLastname("Supervisor");
		u.setTitle("Herr");
		u.setFirstname("");
		u.setEmail("");
		u.setPointGoal(new BigDecimal(0));
		u.setUuid(UUID.randomUUID().toString());
		
		JobTitle jobTitle = getJobTitleByName("Supervisor", true);
		u.setJobTitle(jobTitle);
		
		
		PartitionTypeEntity pte = picketLinkEao.getPartitionTypeEntity("default", true);
		
		AccountTypeEntity ate = picketLinkEao.getAccountTypeEntity("supervisor");
		
		if(ate != null){
//			System.err.println("Supervisor already created!");
			return;
		}
		
		
		ate = new AccountTypeEntity();
		
		ate.setId(UUID.randomUUID().toString());
		
		ate.setLoginName("supervisor");
		ate.setTypeName("org.picketlink.idm.model.basic.User");
		ate.setEnabled(true);
		ate.setCreatedDate(Calendar.getInstance().getTime());
		ate.setPartition(pte);
		
		u.setAccountTypeEntity(ate);
		
		u = userEao.saveUser(u);
		
		grantRole(u, "Supervisor");

		PasswordCredentialTypeEntity pcte = new PasswordCredentialTypeEntity();

		String password = "passw0rd";
		String salt = SecureRandomGenerator.generateSalt();
		
		pcte.setTypeName("org.picketlink.idm.credential.storage.EncodedPasswordStorage");
		pcte.setExpiryDate(null);
		pcte.setPasswordSalt(salt);
		pcte.setPasswordEncodedHash(SHA512Generator.generateHash(password, salt));
		pcte.setEffectiveDate(Calendar.getInstance().getTime());
		
		
		pcte.setOwner(ate);
		pcte = (PasswordCredentialTypeEntity) picketLinkEao.saveAbstractCredentialTypeEntity(pcte);
		
	}
    
    @Override 
    public void grantRole(User u, String role){
    	
		PartitionTypeEntity pte = picketLinkEao.getPartitionTypeEntity("default");
		
		RoleTypeEntity rte = picketLinkEao.getRoleTypeEntity(role, true, pte);
		
		AccountTypeEntity ate = ((UserEntity)u).getAccountTypeEntity();
		
		picketLinkEao.grantRole(ate, rte);
		
    }
	
	@Override
	public SecureUserLink getPasswordResetLink(User user){
		
		if(user.getEmail() == null)
			return null;

		String operation = SecureLinkControllerInterface.OPERATION_CHANGE_PASSWORD;
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, SecureLinkControllerInterface.DEFAULT_SECURELINK_LIFETIME);
		
		return secureLinkController.factoryNewSecureUserLink(user, operation, c.getTime());
	}

	@Override
	public User getUserByUUID(String uuid){
		return userEao.getUserByUUID(uuid);
	}
	
	@Override
	public List<? extends User> getUserList(){
		return userEao.getUserList();
	}
	
	@Override
	public List<? extends User> getUserList(boolean showDisabled){
		return userEao.getUserList(showDisabled);
	}

	@Override
	public List<? extends UserAlias> getUserAliasList() {
		return userEao.getUserAliasList();
	}


	@Override
	public UserGroup getUserGroupById(long id) {
		return userEao.getUserGroupById(id);
	}
	
	@Override
	public List<? extends UserGroup> getUserGroupList(){
		return userEao.getUserGroupList();
	}

	@Override
	public List<? extends User> getSalesUserList() {
		return userEao.getSalesUserList();
	}
	
	@Override
	public Long getUserCount(){
		return userEao.getUserCount();
	}
	
	@Override 
	public boolean isLoginNameSet(User u){
		AccountTypeEntity ate = ((UserEntity)u).getAccountTypeEntity();
		if(ate == null)
			return false;

		if(ate.getLoginName() == null || ate.getLoginName().length() == 0)
			return false;
		
		return true;
	}
	
	@Override
	public boolean isLoginNameFree(String loginName){
		AccountTypeEntity ate = picketLinkEao.getAccountTypeEntity(loginName);
		if(ate == null)
			return true;
		return false;
	}
	
	@Override
	public void setLoginName(User user, String loginName){
		
		if(((UserEntity)user).getAccountTypeEntity() == null)
			createAccountTypeEntity(user);

		AccountTypeEntity ate = ((UserEntity)user).getAccountTypeEntity();
		ate.setLoginName(loginName);
		ate = (AccountTypeEntity) picketLinkEao.saveAttributedTypeEntity(ate);
		
	}
	
	@Override
	public void createAccountTypeEntity(User user){

		AccountTypeEntity ate = new AccountTypeEntity();

		PartitionTypeEntity pte = picketLinkEao.getPartitionTypeEntity("default", true);
		
		ate.setId(UUID.randomUUID().toString());
		
		ate.setTypeName("org.picketlink.idm.model.basic.User");
		ate.setEnabled(true);
		ate.setCreatedDate(Calendar.getInstance().getTime());
		ate.setPartition(pte);
		
		((UserEntity)user).setAccountTypeEntity(ate);
		
	}
	
	@Override
	public void setLoginPassword(User user, String password){

		AccountTypeEntity ate = ((UserEntity)user).getAccountTypeEntity();
		
		PasswordCredentialTypeEntity pcte = picketLinkEao.getPasswordCredentialTypeEntity(ate);
		
		if(pcte == null){
			pcte = new PasswordCredentialTypeEntity();
			pcte.setOwner(ate);
		}
		
		
		String salt = SecureRandomGenerator.generateSalt();
		
		pcte.setTypeName("org.picketlink.idm.credential.storage.EncodedPasswordStorage");
		pcte.setExpiryDate(null);
		pcte.setPasswordSalt(salt);
		pcte.setPasswordEncodedHash(SHA512Generator.generateHash(password, salt));
		pcte.setEffectiveDate(Calendar.getInstance().getTime());
		
		pcte = (PasswordCredentialTypeEntity) picketLinkEao.saveAbstractCredentialTypeEntity(pcte);
		
	}

	@Override 
	public int checkPasswordStrength(String password) {
        	int strengthPercentage=0;
			String[] partialRegexChecks = { 
					".*[a-z]+.*", // lower
			        ".*[A-Z]+.*", // upper
			        ".*[\\d]+.*", // digits
			        ".*[^a-zA-Z0-9]+.*" // symbols
			};
		
		
		    if (password.matches(partialRegexChecks[0])) {
		            strengthPercentage+=1;
		    }
		    if (password.matches(partialRegexChecks[1])) {
		            strengthPercentage+=1;
		    }
		    if (password.matches(partialRegexChecks[2])) {
		            strengthPercentage+=1;
		    }
		    if (password.matches(partialRegexChecks[3])) {
		            strengthPercentage+=1;
		    }
		
		return strengthPercentage;
	}

	@Override
	public User getUserByAccountTypeId(String attributedTypeId) {
		return userEao.getUserByAccountTypeId(attributedTypeId);
	}

	@Override
	public JobTitle getJobTitleByName(String name) {
		return getJobTitleByName(name, false);
	}

	@Override
	public JobTitle getJobTitleByName(String name, boolean createIfNotExisting) {
		return userEao.getJobTitleByName(name, createIfNotExisting);
	}

	@Override
	public JobTitle saveJobTitle(JobTitle jobTitle) {
		return userEao.saveJobTitle((JobTitleEntity) jobTitle);
	}

	@Override
	public User getUserById(long id) {
		return userEao.getUserById(id);
	}

	@Override
	public BigDecimal getUserPoints(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getUserFuturePoints(User user) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public User updateUserImage(User user, InputStream is, String contentType, long size) {
		
		DBFile userImage = user.getUserImage();
		
		userImage = fileController.updateDBFile(userImage, is, contentType, size);
		
		user.setUserImage(userImage);
		
		return userEao.saveUser((UserEntity) user);
	}

	@Override
	public boolean checkOldPassword(User user, String password) {
		
		AccountTypeEntity ate = ((UserEntity)user).getAccountTypeEntity();
		
		PasswordCredentialTypeEntity pcte = picketLinkEao.getPasswordCredentialTypeEntity(ate);
		
		if(pcte == null){
			return false;
		}
		
		
		String salt = pcte.getPasswordSalt();
		
		String newHash = SHA512Generator.generateHash(password, salt);
		
		String oldHash = pcte.getPasswordEncodedHash();
		
		
		return oldHash.compareTo(newHash) == 0;
	}
	
	@Override
	public void flushCache(){
		userEao.flushUserCache();
		userEao.flushUserGroupCache();
	}

	@Override
	public User getUserByLoginName(String loginName) {
		AccountTypeEntity ate = picketLinkEao.getAccountTypeEntity(loginName);
		if(ate == null)
			return null;
		
		return this.getUserByAccountTypeId(ate.getId());
	}

	@Override
	public void loginFailed(User user) {
		if(user == null)
			return;
		
		user.setFailedLogins(user.getFailedLogins() + 1);
		userEao.saveUser((UserEntity) user);
	}

	@Override
	public User clearFailedLogins(User user) {
		if(user == null)
			return user;
		
		user.setFailedLogins(0);
		return userEao.saveUser((UserEntity) user);
	}
	
	@Schedule( minute="0", hour="1", persistent=false)
	@Override
    public void updateAllUserStats() {
//		System.out.println("@Schedule START updateAllUserStats");
		
		try{
		
			Iterator<UserEntity> ui = userEao.getUserList().iterator();
			while(ui.hasNext()){
				updateUserStats(ui.next());
			}
			
		}catch(Exception e){
			exceptionController.logException(e, null);
		}
//		System.out.println("@Schedule END updateAllUserStats");
    }
	
	@Override
    public void updateAllUserAllStats() {
//		System.out.println("updateAllUserStats START");
		
		Iterator<UserEntity> ui = userEao.getUserList().iterator();
		UserEntity u;
		int planingStartMonth;
		int planingStartYear;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);

		Calendar cNow = Calendar.getInstance();
		
		while(ui.hasNext()){
			u = ui.next();
			
			planingStartMonth = u.getPlaningStartMonth();
			planingStartYear = u.getPlaningStartYear();

			
			if(planingStartYear == 0){
				u.setStatsList(null);
				u = (UserEntity) saveUser(u);
			}else{

				c.set(Calendar.YEAR, planingStartYear);
				c.set(Calendar.MONTH, planingStartMonth);
				c.add(Calendar.MONTH, -1);
				
				// clear all Stats before planing start
				Iterator<? extends UserStats> i2 = u.getStatsList().iterator();
				UserStats us;
				while(i2.hasNext()){
					us = i2.next();
					
					if(us.getYear() < c.get(Calendar.YEAR) || (us.getYear() == c.get(Calendar.YEAR)  && us.getMonth() < c.get(Calendar.MONTH) )){
						// Stats are before planing start date -> delete
						i2.remove();
					}
				}

				u = (UserEntity) saveUser(u);

				c.set(Calendar.YEAR, planingStartYear);
				c.set(Calendar.MONTH, planingStartMonth);
				
				while(c.get(Calendar.YEAR) < cNow.get(Calendar.YEAR) || (c.get(Calendar.YEAR) == cNow.get(Calendar.YEAR) && c.get(Calendar.MONTH) <= cNow.get(Calendar.MONTH))){
					updateUserStats(u, c.get(Calendar.YEAR), c.get(Calendar.MONTH));
					c.add(Calendar.MONTH, 1);
				}
				
				
				
			}
		}
    }
	
	@Override
	public void updateUserStats(User user){

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		
		updateUserStats(user, year, month);
	}
	
	@Override
	public void updateUserStats(User user, int year, int month){
//		System.out.println("updateUserStats for "+user+" year:"+year+" month:"+month);
		
		if(user.getPlaningStartYear() == 0){
//			System.out.println("not updating UserStats -> NO PLANING START SET");
			return;
		}
		
		if(year < user.getPlaningStartYear()){
//			System.out.println("not updating UserStats -> PLANING START later than given month");
			return;
		}
		
		if(year == user.getPlaningStartYear() && month < user.getPlaningStartMonth()){
//			System.out.println("not updating UserStats -> PLANING START later than given month");
			return;
		}

//		System.out.println("start updateUserStats for "+user+" year:"+year+" month:"+month);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int currentMonth = c.get(Calendar.MONTH);
		int currentYear = c.get(Calendar.YEAR);
		int currentDay = c.get(Calendar.DAY_OF_MONTH);

		Date calculationFrom;
		Date calculationTo;

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		calculationFrom = c.getTime();
		
		UserStats stats = user.getStats(year, month);
		
		if(year > currentYear || (month > currentMonth && currentYear == year)){
			// future Month
			stats.setOvertime(0);
			user = saveUser(user);
			return;
		}else if(currentMonth == month && currentYear == year){
			// current Month
			c.set(Calendar.DAY_OF_MONTH, currentDay);
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);
			calculationTo = c.getTime();
		}else{
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);
			calculationTo = c.getTime();
		}
		
		
		int contractedWorkingTime = calculateContractedWorkingTime(user, calculationFrom, calculationTo);
		
		int workingTime = calculateWorkingTime(user, calculationFrom, calculationTo);
		
		int overtime = workingTime - contractedWorkingTime;
		
		
//		System.out.println("calculated overtime: "+overtime);
		
		stats.setOvertime(overtime);
		user = saveUser(user);
	}
	
	public int calculateWorkingTime(User user, Date from, Date to){
		List<? extends Event> list = eventController.getEvents(from, to, user, null);
		Iterator<? extends Event> i = list.iterator();
		Event e;
		int workingTime = 0;
		while(i.hasNext()){
			e = i.next();
			
			workingTime += calculateWorkingTime(e, from, to);
			
		}
		
		return workingTime;
	}
	
	@Override
	public int calculateWorkingTime(Event e, Date from, Date to){
		
		
		if(! e.isWorkingTime())
			return 0;
		
		if(from == null)
			from = e.getEffectiveDate();
		if(to == null)
			to = e.getExpiryDate();

//		if(e.getShop() != null && (e.getShop().isDeleted() || ! e.getShop().isEnabled()))
//		if(e.getShop() == null)
//			return 0;
		
		if(e.isSingleDay()){
			
			if(e.isAllDay()){
				return e.getUser().getFullDayWorkingTime(); 
			}else{
				return (int) e.getWorkingDuration();
			}
			
			
		}
		
//		System.out.println("calculateWorkingTime for multi Day");
		
		// multi Day Event
		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar ce = Calendar.getInstance();
		c.setTime(e.getEffectiveDate());
		c2.setTime(e.getExpiryDate());

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		
		int duration = 0;
		
		
		// go through days
		while(c.getTime().compareTo(c2.getTime()) <= 0){
			
//			System.out.println("check: "+c.getTime());

			if(c.getTime().compareTo(from) < 0){
//				System.out.println("lower");
				c.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			
			if(c.getTime().compareTo(to) > 0){
//				System.out.println("upper");
				c.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			
			if(! isWorkingDay(c.getTime(), e.getUser())){
//				System.out.println("no working day");
				c.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			

//			System.out.println("working day !");
			
			duration +=  e.getUser().getFullDayWorkingTime(); 

			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		
		// check for substraction on half first or last day
		if(! e.isAllDay()){
			ce.setTime(e.getEffectiveDate());
			
			if(isWorkingDay(ce.getTime(), e.getUser()) && ce.get(Calendar.HOUR_OF_DAY) >= Integer.valueOf( propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] )){
//				System.out.println("substract half day from duration cause of EffectiveDate");
				duration -= e.getUser().getFullDayWorkingTime() / 2;
			}

			ce.setTime(e.getExpiryDate());
			if(isWorkingDay(ce.getTime(), e.getUser()) && ce.get(Calendar.HOUR_OF_DAY) <= Integer.valueOf( propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] )){
//				System.out.println("substract half day from duration cause of ExpiryDate");
				duration -= e.getUser().getFullDayWorkingTime() / 2;
			}
			
		}
		
		return duration;
	}
	
	
	public int calculateContractedWorkingTime(User user, Date from, Date to){
		
//		System.out.println("calculateContractedWorkingTime for "+user+" rage: "+from.toString()+" - "+to.toString());
		
		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar ce = Calendar.getInstance();
		c.setTime(from);
		c2.setTime(to);
//
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//
//		c2.set(Calendar.HOUR_OF_DAY, 0);
//		c2.set(Calendar.MINUTE, 0);
//		c2.set(Calendar.SECOND, 0);
//		c2.set(Calendar.MILLISECOND, 0);
		
		int workingHours = 0;
		
		while(c.getTime().compareTo(c2.getTime()) <= 0){

//			System.out.println("check "+c.getTime().toString());
			
			if(! isWorkingDay(c.getTime(), user)){
//				System.out.println("no Working Day! -> SKIP");
				c.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			
			workingHours += user.getFullDayWorkingTime();
			
			c.add(Calendar.DAY_OF_YEAR, 1);
		}

//		System.out.println("result workingHours: "+workingHours);
		
		return workingHours;
	}
	
	@Override
	public long calculateFreeWorkingTime(User user, Date start, Date end){
//		System.out.println("calculateFreeWorkingTime for "+user.getName()+" range: "+start.toString()+" - "+end.toString());
		
		List<? extends Event> eventList = eventController.getEvents(start, end, user, null);
		Iterator<? extends Event> i = eventList.iterator();
		Event e;
		long duration, planedWorkingMinutes = 0;
		
		while(i.hasNext()){
			e = i.next();
			duration = 0;

//			System.out.println("Calculate Event : "+e.getId()+" "+e.getEffectiveDate().toString()+" - "+e.getExpiryDate().toString());
			
			if(! e.isWorkingTime()){
//				System.out.println("Event is not working time -> skip");
				continue;
			}
			
//			System.out.println("Calculate Event : "+e.getId()+" "+e.getEffectiveDate().toString()+" - "+e.getExpiryDate().toString());
			
			
			if(!e.isSingleDay()){
				// calculate working time within multiple days
//				System.out.println("calculate working time within multiple days");

				Calendar ce = Calendar.getInstance();
				
				Calendar c1 = Calendar.getInstance();
				c1.setTime(start);
				c1.set(Calendar.HOUR_OF_DAY, 0);
				c1.set(Calendar.MINUTE, 0);
				c1.set(Calendar.SECOND, 0);
				c1.set(Calendar.MILLISECOND, 0);
				
				Calendar c2 = Calendar.getInstance();
				c2.setTime(start);
				c2.set(Calendar.HOUR_OF_DAY, 23);
				c2.set(Calendar.MINUTE, 59);
				c2.set(Calendar.SECOND, 59);
				c2.set(Calendar.MILLISECOND, 999);
				

				while(c2.getTime().compareTo(end) < 0){

//					System.out.println("check c1: "+c1.getTime().toString());
//					System.out.println("check c2: "+c2.getTime().toString());
					
					if(c2.getTime().compareTo(e.getEffectiveDate()) >= 0 && c1.getTime().compareTo(e.getExpiryDate()) <= 0){
//						System.out.println("Event is on this date");
						
						if(this.isWorkingDay(c1.getTime(), user)){
//							System.out.println("WorkingDay");
							
							duration += user.getFullDayWorkingTime();
							
						}
					}

					c1.add(Calendar.DAY_OF_YEAR, 1);
					c2.add(Calendar.DAY_OF_YEAR, 1);
				}
				
				
				// running throuh days complete
				// check for substraction on half first or last day
				if(! e.isAllDay()){
					ce.setTime(e.getEffectiveDate());
					if(isWorkingDay(ce.getTime(), e.getUser()) && ce.get(Calendar.HOUR_OF_DAY) >= Integer.valueOf( propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] )){
//						System.out.println("substract half day from duration cause of EffectiveDate");
						duration -= user.getFullDayWorkingTime() / 2;
					}

					ce.setTime(e.getExpiryDate());
					if(isWorkingDay(ce.getTime(), e.getUser()) && ce.get(Calendar.HOUR_OF_DAY) <= Integer.valueOf( propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] )){
//						System.out.println("substract half day from duration cause of ExpiryDate");
						duration -= user.getFullDayWorkingTime() / 2;
					}
				}
				
				
			}
			else if(e.isAllDay()){
				duration = user.getFullDayWorkingTime();

//				System.out.println("All Day: "+duration);
			}else{
				duration = e.getWorkingDuration();
				
//				System.out.println("normal: "+duration);
			}
			

//			System.out.println("event duration: "+duration);

			planedWorkingMinutes += duration;
		}

//		System.out.println("planedWorkingMinutes: "+planedWorkingMinutes);
//		System.out.println("#####");
		
		int contractedWorkingTime = calculateContractedWorkingTime(user, start, end);
		
		return planedWorkingMinutes - contractedWorkingTime; //(user.getWeeklyWorkingTime() * 60);
	}

	@Override
	public boolean isWorkingDay(Date date, User user){
//		System.out.println("isWorkingDay: "+date.toString());
//		long debug = (new Date()).getTime();
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// skip sundays
		if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
//			System.out.println("SUNDAY -> NO WORKINGDAY "+((new Date()).getTime() - debug)+"ms");
			return false;
		}

//		System.out.println("a "+((new Date()).getTime() - debug)+"ms");
		
		// skip saturdays if user is in 5-day-week
		if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && user.getWeeklyWorkingDays() == 5){
//			System.out.println("5-day-week -> NO WORKINGDAY "+((new Date()).getTime() - debug)+"ms");
			return false;
		}
		
//		System.out.println("b "+((new Date()).getTime() - debug)+"ms");
		
		// skip holidays
		if(holidayController.isHoliday(c.getTime(), user.getState())){
//			System.out.println("holiday -> NO WORKINGDAY "+((new Date()).getTime() - debug)+"ms");
			return false;
		}

//		System.out.println("WORKINGDAY "+((new Date()).getTime()  - debug) +"ms");
		return true;
	}

	@Override
	public float calculateVacation(User user, int year){
		if(user == null)
			return 0;
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		Date from = c.getTime();
		
		c.add(Calendar.YEAR, 1);
		Date to = c.getTime();
		
		List<? extends Event> eventList;
		Iterator<? extends Event> i;
		Event e;
		float vacationsum = 0;

		
		eventList = eventController.getEventsByType(from, to, user, eventController.getEventTypeByShortName("U"));
		i = eventList.iterator();
		while(i.hasNext()){
			e = i.next();
			vacationsum += calculateVacationDuration(e, year);
		}
		
		eventList = eventController.getEventsByType(from, to, user, eventController.getEventTypeByShortName("EZ"));
		i = eventList.iterator();
		while(i.hasNext()){
			e = i.next();
			vacationsum += calculateVacationDuration(e, year);
		}
		
		return vacationsum;
	}


	// return days
	@Override
	public float calculateVacationDuration(Event event){
		return calculateVacationDuration(event, 0);
	}

	// return days
	@Override
	public float calculateVacationDuration(Event event, int year){
		
//		System.out.println("calculateVacationDuration "+event.getId()+" "+event.getEffectiveDate()+" - "+event.getExpiryDate());
		long debug = ((new Date()).getTime());

		if(event.getEventType() == null)
			return 0;
		
		if(event.getEventType().getShortName().equalsIgnoreCase("EZ")){
			// Parent Time
			
			if(event.getUser().getVacationDays() == 0)
				return 0;
			
			
			float duration = event.getExpiryDate().getTime() - event.getEffectiveDate().getTime();
			float days = TimeUnit.MILLISECONDS.toDays((long) duration);
			int parentTimeVacationMultiplier = (int) (days / 30);
			float base = ((float)event.getUser().getVacationDays()) / ((float)12);
			
			float vacationDays = base * parentTimeVacationMultiplier;

			
			return vacationDays;
		}
		
		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar ce = Calendar.getInstance();
		c.setTime(event.getEffectiveDate());
		c2.setTime(event.getExpiryDate());

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		
		float duration = 0;
		
		while(c.getTime().compareTo(c2.getTime()) <= 0){
			
			if(year > 0 && c.get(Calendar.YEAR) != year){
				c.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			
			if(! isWorkingDay(c.getTime(), event.getUser())){
//				System.out.println("No Working Day: "+c.getTime());
				c.add(Calendar.DAY_OF_YEAR, 1);
				continue;
			}
			
//			System.out.println("Add Day: "+c.getTime());
			duration ++;
			
			c.add(Calendar.DAY_OF_YEAR, 1);
		}

		
		// check for substraction on half first or last day
		if(! event.isAllDay()){
			ce.setTime(event.getEffectiveDate());

			
			if((ce.get(Calendar.YEAR) == year || year == 0) && isWorkingDay(ce.getTime(), event.getUser()) && ce.get(Calendar.HOUR_OF_DAY) >= Integer.valueOf( propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] )){
//				System.out.println("substract half day from duration cause of EffectiveDate");
			
				
				duration -= 0.5;
			}

			ce.setTime(event.getExpiryDate());
			if((ce.get(Calendar.YEAR) == year || year == 0) && isWorkingDay(ce.getTime(), event.getUser()) && ce.get(Calendar.HOUR_OF_DAY) <= Integer.valueOf( propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] )){
//				System.out.println("substract half day from duration cause of ExpiryDate");
				duration -= 0.5;
			}
			
		}
		
		long debugDiff = ((new Date()).getTime()) - debug;

//		System.out.println("calculateVacationDuration END "+debugDiff+"ms");
		
		
		return duration;
	}

	private User getUserByName(String firstname, String lastname) {
		return userEao.getUserByName(firstname, lastname);
	}

	@Override
	public User getUserByAlias(String alias) {
		if(alias == null || alias.trim().length() == 0){
			return null;
		}
		
		User u  =  userEao.getUserByAlias(alias.trim());
		
		if(u == null){
		
			String[] name = alias.split(" ");
			if(name.length >= 2){
				u = getUserByName(name[0], name[1]);
				if(u != null){
					UserAlias userAlias = factoryNewUserAlias(alias);
					userAlias.setUser(u);
					userAlias = saveUserAlias(userAlias);
				}
			}
		}

		
		return u;
	}

	@Override
	public UserAlias saveUserAlias(UserAlias userAlias) {
		return userEao.saveUserAlias(userAlias);
	}

	@Override
	public Object getEmploymentFormById(long id) {
		return userEao.getEmploymentFormById(id);
	}

	@Override
	public List<? extends EmploymentForm> getEmploymentFormList() {
		return userEao.getEmploymentFormList();
	}

	@Override
	public Object getJobTitleById(long id) {
		return userEao.getJobTitleById(id);
	}

	@Override
	public List<? extends JobTitle> getJobTitleList() {
		return userEao.getJobTitleList();
	}

	@Override
	public User getUserByEmail(String email) {
		if(email == null || email.trim().length() == 0)
			return null;
		
		return userEao.getUserByEmail(email.trim());
	}

	@Override
	public JobTitle factoryJobTitle() {
		return new JobTitleEntity();
	}

	@Override
	public EmploymentForm factoryEmploymentForm() {
		return new EmploymentFormEntity();
	}

	@Override
	public EmploymentForm saveEmploymentForm(EmploymentForm employmentForm) {
		return userEao.saveEmploymentForm(employmentForm);
	}
	
	
//	@Override
//	public BigDecimal getUserPoints(User user) {
//		
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//		
//		Date from = c.getTime();
//		Date to = new Date();
//		return userEao.getUserPoints(user, from, to);
//	}
//
//	@Override
//	public BigDecimal getUserFuturePoints(User user) {
//
//		
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//
//		Date from = c.getTime();
//		
//		c.add(Calendar.MONTH, 1);
//		c.add(Calendar.MILLISECOND, -1);
//		Date to = c.getTime();
//		
//		return userEao.getUserPoints(user, from, to);
//	}
//	


	@Override
	public void deleteUserAlias(UserAlias userAlias) {
		userEao.deleteUserAlias(userAlias);
	}
}
