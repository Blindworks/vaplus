package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.client.beans.PermissionBean;


@ManagedBean(name = "userGroupBean")
@SessionScoped
public class UserGroupBean implements Serializable {

	private static final long serialVersionUID = 4159212482579463739L;
	
	
	
	@EJB
	private UserControllerInterface userController;

	@Inject
	private FacesContext facesContext;
	
    @ManagedProperty(value="#{permissionBean}")
    private PermissionBean permissionBean;

	private UserGroup userGroup;
	
	private boolean userGroupListEditable;

	public List<? extends UserGroup> getUserGroupList(){
		
		return userController.getUserGroupList();
	}
	
	public String deleteUserGroup(){
		
		if(userGroup == null)
			return "";
		
		userGroup.setDeleted();

		userGroup = userController.saveUserGroup(userGroup);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Mitarbeiter Gruppe gelöscht.", null));
		
		return "userGroupList";
	}



	public boolean isUserGroupListEditable() {
		return userGroupListEditable;
	}

	public void toggleUserGroupListEditable() {
		userGroupListEditable = userGroupListEditable ? false : true;
	}

	public UserGroup getUserGroup() {
		if(userGroup == null)
			userGroup = userController.factoryNewUserGroup();
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public PermissionBean getPermissionBean() {
		return permissionBean;
	}

	public void setPermissionBean(PermissionBean permissionBean) {
		this.permissionBean = permissionBean;
	}

	public String saveUserGroup(){
		boolean newGroup = userGroup.getId() == 0;
		
		userGroup = userController.saveUserGroup(userGroup);

		if(userGroup != null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Mitarbeiter-Gruppe erfolgreich gespeichert!", null));
		}
		
		if(newGroup == true){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte Rechte der neuen Mitarbeiter-Gruppe anpassen!", null));
		}
		

		if(newGroup == true){
			return "";
		}else{
			return "userGroupList";
		}
	}
	
	private void setPermission(String resource, String operation, boolean b){
		if(b)
			permissionBean.grantPermission(userGroup, resource, operation);
		else
			permissionBean.revokePermission(userGroup, resource, operation);

		userGroup = userController.saveUserGroup(userGroup);
	}
	
	private boolean getPermission(String resource, String operation){
		return permissionBean.hasOwnPermission(userGroup, resource, operation);
	}
	
	// Customer
	
	public void setPermissionCustomerCreate(boolean b){
		setPermission("customer", "create", b);
	}
	public boolean getPermissionCustomerCreate(){
		return getPermission("customer", "create");
	}
	
	public void setPermissionCustomerRead(boolean b){
		setPermission("customer", "read", b);
	}
	public boolean getPermissionCustomerRead(){
		return getPermission("customer", "read");
	}
	
	public void setPermissionCustomerEdit(boolean b){
		setPermission("customer", "edit", b);
	}
	public boolean getPermissionCustomerEdit(){
		return getPermission("customer", "edit");
	}
	
	public void setPermissionCustomerDelete(boolean b){
		setPermission("customer", "delete", b);
	}
	public boolean getPermissionCustomerDelete(){
		return getPermission("customer", "delete");
	}


	
	// Contract
	
	public void setPermissionContractCreate(boolean b){
		setPermission("contract", "create", b);
	}
	public boolean getPermissionContractCreate(){
		return getPermission("contract", "create");
	}
	
	public void setPermissionContractRead(boolean b){
		setPermission("contract", "read", b);
	}
	public boolean getPermissionContractRead(){
		return getPermission("contract", "read");
	}
	
	public void setPermissionContractListRead(boolean b){
		setPermission("contract_list", "read", b);
	}
	public boolean getPermissionContractListRead(){
		return getPermission("contract_list", "read");
	}
	
	public void setPermissionContractListReadOwn(boolean b){
		setPermission("contract_list", "read_own", b);
	}
	public boolean getPermissionContractListReadOwn(){
		return getPermission("contract_list", "read_own");
	}
	
	public void setPermissionContractEdit(boolean b){
		setPermission("contract", "edit", b);
	}
	public boolean getPermissionContractEdit(){
		return getPermission("contract", "edit");
	}
	
	public void setPermissionContractEditOwn(boolean b){
		setPermission("contract", "edit_own", b);
	}
	public boolean getPermissionContractEditOwn(){
		return getPermission("contract", "edit_own");
	}
	
	public void setPermissionContractDelete(boolean b){
		setPermission("contract", "delete", b);
	}
	public boolean getPermissionContractDelete(){
		return getPermission("contract", "delete");
	}

	
	// Order
	
	public void setPermissionOrderCreate(boolean b){
		setPermission("order", "create", b);
	}
	public boolean getPermissionOrderCreate(){
		return getPermission("order", "create");
	}
	
	public void setPermissionOrderRead(boolean b){
		setPermission("order", "read", b);
	}
	public boolean getPermissionOrderRead(){
		return getPermission("order", "read");
	}
	
	public void setPermissionOrderEdit(boolean b){
		setPermission("order", "edit", b);
	}
	public boolean getPermissionOrderEdit(){
		return getPermission("order", "edit");
	}
	
	public void setPermissionOrderEditOwn(boolean b){
		setPermission("order", "edit_own", b);
	}
	public boolean getPermissionOrderEditOwn(){
		return getPermission("order", "edit_own");
	}
	
	public void setPermissionOrderDelete(boolean b){
		setPermission("order", "delete", b);
	}
	public boolean getPermissionOrderDelete(){
		return getPermission("order", "delete");
	}

	
	// Sale
	
	public void setPermissionSaleCreate(boolean b){
		setPermission("sale", "create", b);
	}
	public boolean getPermissionSaleCreate(){
		return getPermission("sale", "create");
	}
	
	public void setPermissionSaleRead(boolean b){
		setPermission("sale", "read", b);
	}
	public boolean getPermissionSaleRead(){
		return getPermission("sale", "read");
	}
	
	public void setPermissionSaleCancel(boolean b){
		setPermission("sale", "cancel", b);
	}
	public boolean getPermissionSaleCancel(){
		return getPermission("sale", "cancel");
	}

	
	// StockManagement
	
	public void setPermissionStockManagementRead(boolean b){
		setPermission("stockManagement", "read", b);
	}
	public boolean getPermissionStockManagementRead(){
		return getPermission("stockManagement", "read");
	}
	
	public void setPermissionStockManagementWrite(boolean b){
		setPermission("stockManagement", "edit", b);
	}
	public boolean getPermissionStockManagementWrite(){
		return getPermission("stockManagement", "edit");
	}
	
	
	// Kalender
	
	public void setPermissionCalendarRead(boolean b){
		setPermission("calendar", "read", b);
	}
	public boolean getPermissionCalendarRead(){
		return getPermission("calendar", "read");
	}
	
	public void setPermissionCalendarWrite(boolean b){
		setPermission("calendar", "edit", b);
	}
	public boolean getPermissionCalendarWrite(){
		return getPermission("calendar", "edit");
	}

	
	// Controlling
	
	public void setPermissionControllingRead(boolean b){
		setPermission("controlling", "read", b);
	}
	public boolean getPermissionControllingRead(){
		return getPermission("controlling", "read");
	}

	
	// Management
	
	public void setPermissionManagementRead(boolean b){
		setPermission("management", "read", b);
	}
	public boolean getPermissionManagementRead(){
		return getPermission("management", "read");
	}

	
	// Settings
	
	public void setPermissionSettingsRead(boolean b){
		setPermission("settings", "read", b);
	}
	public boolean getPermissionSettingsRead(){
		return getPermission("settings", "read");
	}
	
	public void setPermissionSettingsEdit(boolean b){
		setPermission("settings", "edit", b);
	}
	public boolean getPermissionSettingsEdit(){
		return getPermission("settings", "edit");
	}

	
	// Import
	
	public void setPermissionImportEdit(boolean b){
		setPermission("import", "edit", b);
	}
	public boolean getPermissionImportEdit(){
		return getPermission("import", "edit");
	}
	
	// Support
	
	public void setPermissionSupportRead(boolean b){
		setPermission("support", "read", b);
	}
	public boolean getPermissionSupportRead(){
		return getPermission("support", "read");
	}
	
	// Files
	
	public void setPermissionReadFiles(boolean b){
		setPermission("file", "read", b);
	}
	public boolean getPermissionReadFiles(){
		return getPermission("file", "read");
	}
	
	public void setPermissionReadAllFiles(boolean b){
		setPermission("file", "read_all", b);
	}
	public boolean getPermissionReadAllFiles(){
		return getPermission("file", "read_all");
	}
	
	// Achievement
	
	public void setPermissionAchievementReadInvisible(boolean b){
		setPermission("achievement", "readInvisible", b);
	}
	public boolean getPermissionAchievementReadInvisible(){
		return getPermission("achievement", "readInvisible");
	}
	
	// Cash Register
	
	public void setPermissionCashRegisterRead(boolean b){
		setPermission("cashRegister", "read", b);
	}
	public boolean getPermissionCashRegisterRead(){
		return getPermission("cashRegister", "read");
	}
	
	public void setPermissionCashRegisterOpen(boolean b){
		setPermission("cashRegister", "open", b);
	}
	public boolean getPermissionCashRegisterOpen(){
		return getPermission("cashRegister", "open");
	}
	
	public void setPermissionCashRegisterClose(boolean b){
		setPermission("cashRegister", "close", b);
	}
	public boolean getPermissionCashRegisterClose(){
		return getPermission("cashRegister", "close");
	}
	
	
	
}
