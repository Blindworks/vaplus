package de.vaplus.api;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.JobTitle;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserGroup;


public interface UserControllerInterface extends Serializable {

	public abstract List<? extends User> getUserList();

	public User saveUser(User user);

	public boolean isLoginNameSet(User u);

	boolean isLoginNameFree(String loginName);

	int checkPasswordStrength(String password);

	void setLoginName(User user, String loginName);

	void setLoginPassword(User user, String password);

	User factoryNewUser();

	User getUserByAccountTypeId(String attributedTypeId);

	Long getUserCount();

	void createSupervisorUser();

	void grantRole(User u, String role);

	JobTitle getJobTitleByName(String name);

	JobTitle saveJobTitle(JobTitle jobTitle);

	JobTitle getJobTitleByName(String name, boolean createIfNotExisting);

	User getUserById(long id);

	BigDecimal getUserPoints(User user);

	BigDecimal getUserFuturePoints(User user);

	User getUserByUUID(String uuid);

	SecureUserLink getPasswordResetLink(User user);

	User updateUserImage(User user,
			InputStream inputStream, String contentType, long size);

	boolean checkOldPassword(User user, String password);

	public abstract List<? extends User> getSalesUserList();

	void setAccountTypeEntity(User user, String accountTypeEntityid);

	void createAccountTypeEntity(User user);

	UserGroup getUserGroupById(long id);

	List<? extends UserGroup> getUserGroupList();

	UserGroup factoryNewUserGroup();

	UserGroup saveUserGroup(UserGroup userGroup);

	void flushCache();

	User clearFailedLogins(User u);

	void loginFailed(User user);

	User getUserByLoginName(String loginName);

	List<? extends User> getUserList(boolean showDisabled);

	void updateUserStats(User user);
	
	void updateAllUserStats();

	long calculateFreeWorkingTime(User user, Date start, Date end);

	boolean isWorkingDay(Date date, User user);

	float calculateVacation(User user, int year);

	float calculateVacationDuration(Event event, int year);

	void updateUserStats(User user, int year, int month);

	void updateAllUserAllStats();

	public abstract User getUserByAlias(String alias);

	public abstract User getUserByEmail(String email);

	UserAlias factoryNewUserAlias(String alias);

	public abstract UserAlias saveUserAlias(UserAlias userAlias);

	float calculateVacationDuration(Event event);

	int calculateWorkingTime(Event e, Date from, Date to);

	public abstract Object getEmploymentFormById(long id);

	public abstract List<? extends EmploymentForm> getEmploymentFormList();

	public abstract Object getJobTitleById(long id);

	public abstract List<? extends JobTitle> getJobTitleList();

	UserCustomerHistory factoryNewUserCustomerHistory(User user,
			Customer customer);

	public abstract JobTitle factoryJobTitle();

	public abstract EmploymentForm factoryEmploymentForm();

	public abstract EmploymentForm saveEmploymentForm(EmploymentForm selectedEmploymentForm);

	List<? extends UserAlias> getUserAliasList();

	public abstract void deleteUserAlias(UserAlias userAlias);


}