package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopDocNumberRanges;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.VO;
import de.vaplus.client.applicationbeans.ResourceBean;

@ManagedBean(name="shopBean")
@SessionScoped
public class ShopBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;

	@EJB
	private ShopControllerInterface shopController;
	
    @EJB
    private CommissionControllerInterface commissionController;
    
	@EJB
	private ResourceBean resourceBean;

	@Inject
	private FacesContext facesContext;
    
	private Shop activeShop;
    
	private Shop selectedShop;
	
	private ShopDocNumberRanges selectedShopDocNumberRanges;
    
	private ShopGroup selectedShopGroup;
    
	private VO selectedVO;
	
	private boolean shopListEditable;
	
	private boolean shopGroupListEditable;
	
	private boolean shopVOPermissionListEditable;
	
	
    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;
    
	
	

	public ShopBean() {
		// TODO Auto-generated constructor stub
	}

	public List<? extends Shop> getShopList(){
		if(userBean.getActiveUser() == null)
			return new LinkedList<Shop>();
		
		if(userBean.getActiveUser().isSupervisor())
			return resourceBean.getShopList();
		else{
			return userBean.getActiveUser().getAllowedShops();
		}
	}
	
	public List<? extends Shop> getShopList(boolean showDisabled){
		return resourceBean.getShopList(showDisabled);
	}

	public List<? extends ShopGroup> getShopGroupList(){
		return shopController.getShopGroupList();
	}
	
	public List<? extends ShopGroup> getShopGroupList(boolean showDisabled){
		return shopController.getShopGroupList(showDisabled);
	}


	

	public String saveShop(){
		
		selectedShop = shopController.saveShop(selectedShop);
		
		updateShopDocNumberRanges();
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Filiale erfolgreich gespeichert.", null));
		
		resourceBean.refreshLists();
		
		return "shopList";
	}
	public String saveShopGroup(){
		
		selectedShopGroup = shopController.saveShopGroup(selectedShopGroup);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Filial-Gruppe erfolgreich gespeichert.", null));

		resourceBean.refreshLists();
		
		return "shopGroupList";
	}
	
	public Shop getSelectedShop() {
		if(selectedShop == null)
			selectedShop = shopController.factoryNewShop();
		return selectedShop;
	}

	public void setSelectedShop(Shop selectedShop) {
		if(selectedShop != null)
			this.selectedShop = shopController.refreshShop(selectedShop);
		
		setSelectedShopDocNumberRanges(null);
	}
	
	public ShopGroup getSelectedShopGroup() {
		if(selectedShopGroup == null)
			selectedShopGroup = shopController.factoryNewShopGroup();
		return selectedShopGroup;
	}

	public void setSelectedShopGroup(ShopGroup selectedShopGroup) {
		this.selectedShopGroup = selectedShopGroup;
	}

	public boolean isShowShopSelector(){
		return getShopList().size() > 1 ? true : false;
	}

	public void checkActiveShop() {
		
		if(activeShop == null){
			
			if(shopController.getShopCount() == 1){
				activeShop = shopController.getShopList().get(0);
			}
			else{
	    		try {
	    			//facesContext.getExternalContext().responseReset();
//	    			facesContext.responseComplete();
					facesContext.getExternalContext().redirect("/shopSelection.xhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public Shop getActiveShop() {
		
		if(activeShop == null){
			
			if(shopController.getShopCount() == 1){
				activeShop = shopController.getShopList().get(0);
			}
			else{
	    		try {
	    			//facesContext.getExternalContext().responseReset();
	    			if(!facesContext.getExternalContext().isResponseCommitted()){
						facesContext.getExternalContext().redirect("/shopSelection.xhtml");
						facesContext.responseComplete();
	    			}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		return null;
			}
		}
		
		return activeShop;
	}

	public void setActiveShop(Shop activeShop) {
		this.activeShop = activeShop;
	}


	public List<Part> getNewShopImage(){
		return null;
	}

	public void setNewShopImage(List<Part> files) throws IOException {

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
			
			selectedShop = shopController.updateShopImage(selectedShop, file.getInputStream(), file.getContentType(), file.getSize());
		}
	}
	

	public boolean isShopListEditable() {
		return shopListEditable;
	}

	public void toggleShopListEditable() {
		shopListEditable = shopListEditable ? false : true;
	}
	
	public boolean isShopGroupListEditable() {
		return shopGroupListEditable;
	}

	public void toggleShopGroupListEditable() {
		shopGroupListEditable = shopGroupListEditable ? false : true;
	}

	public List<? extends VO> getAvailableShopPermissionVOList(){
		return shopController.getAvailableShopPermissionVOList(this.getSelectedShop());
	}

	public VO getSelectedVO() {
		return selectedVO;
	}

	public void setSelectedVO(VO selectedVO) {
		this.selectedVO = selectedVO;
	}
	
	public void addVOToShopPermission(){
		
		if(this.getSelectedShop() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Filiale angegeben!", null));
			return;
		}
		
		if(this.getSelectedVO() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine VO angegeben!", null));
			return;
		}
		
		
		shopController.addVOToShopPermission(this.getSelectedShop(), this.getSelectedVO());
	}
	
	public void removeVOFromShopPermission(VO vo){
		
		if(this.getSelectedShop() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Filiale angegeben!", null));
			return;
		}
		
		if(vo == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine VO angegeben!", null));
			return;
		}
		
		
		shopController.removeVOFromShopPermission(this.getSelectedShop(), vo);
	}

	public boolean isShopVOPermissionListEditable() {
		return shopVOPermissionListEditable;
	}

	public void toggleShopVOPermissionListEditable() {
		shopVOPermissionListEditable = shopVOPermissionListEditable ? false : true;
	}

	
	public String deleteShop(){
		
		selectedShop.setDeleted();
		selectedShop = shopController.saveShop(selectedShop);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Filiale gelöscht.", null));
		
		return "/settings/shopList";
	}
	
	public String deleteShopGroup(){
		
		selectedShopGroup.setDeleted();
		selectedShopGroup = shopController.saveShopGroup(selectedShopGroup);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Filial-Gruppe gelöscht.", null));
		
		return "shopGroupList";
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}


	public List<? extends ShopAlias> getShopAliasList(){
		return shopController.getShopAliasList();
	}
	
	public void deleteShopAlias(ShopAlias shopAlias){
		shopController.deleteShopAlias(shopAlias);
	}

	public ShopDocNumberRanges getSelectedShopDocNumberRanges() {
		if(selectedShopDocNumberRanges == null)
			selectedShopDocNumberRanges = shopController.getShopDocNumberRanges(this.getSelectedShop());
		return selectedShopDocNumberRanges;
	}

	public void setSelectedShopDocNumberRanges(ShopDocNumberRanges selectedShopDocNumberRanges) {
		this.selectedShopDocNumberRanges = selectedShopDocNumberRanges;
	}
	
	private void updateShopDocNumberRanges(){
		if(selectedShopDocNumberRanges != null)
			selectedShopDocNumberRanges = shopController.updateShopDocNumberRanges(selectedShopDocNumberRanges);
	}

}
