package de.vaplus.client.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.AchievementControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

@ManagedBean(name = "achievementBean")
@SessionScoped
public class AchievementBean implements Serializable {

	private static final long serialVersionUID = 4827530947440565225L;

	@EJB
	private AchievementControllerInterface achievementController;

	@Inject
	private FacesContext facesContext;
	
    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;
	
    @ManagedProperty(value="#{shopBean}")
    private ShopBean shopBean;
	
    @ManagedProperty(value="#{permissionBean}")
    private PermissionBean permissionBean;
	
	private boolean achievementListEditable;
	
	private Achievement selectedAchievement;

	private List<Achievement> ownActiveAchievementList;
	
	private Shop ownActiveAchievementListCachedShop;
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}

	public PermissionBean getPermissionBean() {
		return permissionBean;
	}

	public void setPermissionBean(PermissionBean permissionBean) {
		this.permissionBean = permissionBean;
	}

	public List<? extends Achievement> getAchievementList(){
		return achievementController.getAchievementList();
	}
	
	public List<? extends Achievement> getActiveAchievementList(){
		return achievementController.getActiveAchievementList();
	}
	
	public List<? extends Achievement> getActiveAchievementListForCompanyDashboard(){
		List<Achievement> list = new LinkedList<Achievement>();
		
		Iterator<Achievement> i = (Iterator<Achievement>) achievementController.getActiveAchievementListForCompanyDashboard().iterator();
		Achievement a;
		while(i.hasNext()){
			a = i.next();
				
			if(a.isInvisible() && !permissionBean.hasPermission("achievement", "readInvisible")  && !userBean.getActiveUser().isSupervisor())
				continue;
			
			list.add(a);
		}
		
		
//	}
	return list;
		
		
	}
	
	public List<? extends Achievement> getOpenAchievementList(){
		return achievementController.getOpenAchievementList();
	}
	
	public List<? extends Achievement> getClosedAchievementList(){
		return achievementController.getClosedAchievementList();
	}
	
	public List<? extends Achievement> getOwnActiveAchievementList(){
		if(shopBean.getActiveShop() == null)
			return new LinkedList<Achievement>();
		
//		if(ownActiveAchievementList == null || ! ownActiveAchievementListCachedShop.equals(shopBean.getActiveShop() ) ){
			ownActiveAchievementList = new LinkedList<Achievement>();
			ownActiveAchievementListCachedShop = shopBean.getActiveShop();
			Iterator<Achievement> i = (Iterator<Achievement>) achievementController.getActiveAchievementList().iterator();
			Achievement a;
			while(i.hasNext()){
				a = i.next();
				if(achievementController.isAchivementSource(a, userBean.getActiveUser(), shopBean.getActiveShop(), null, shopBean.getActiveShop().getShopVOList())){
					
					if(a.isInvisible() && !permissionBean.hasPermission("achievement", "readInvisible") && !userBean.getActiveUser().isSupervisor())
						continue;
					
					ownActiveAchievementList.add(a);
				}
			}
			
			
//		}
		return ownActiveAchievementList;
	}
	
	public List<? extends Achievement> getUserAchievementList(User user,int month, int year){
		List<Achievement> list = new LinkedList<Achievement>();
		
		if(user == null)
			return list;
		
			Iterator<Achievement> i = (Iterator<Achievement>) achievementController.getAchievementList(month, year).iterator();
			Achievement a;
			while(i.hasNext()){
				a = i.next();
				if(achievementController.isAchivementSource(a, user, null, null, null)){
					list.add(a);
				}
			}
			
			
		return list;
	}

	public boolean isAchievementListEditable() {
		return achievementListEditable;
	}

	public void toggleAchievementListEditable() {
		achievementListEditable = achievementListEditable ? false : true;
	}

	public Achievement getSelectedAchievement() {
		if(selectedAchievement == null)
			selectedAchievement = achievementController.factoryNewAchievement();
		return selectedAchievement;
	}

	public void setSelectedAchievement(Achievement selectedAchievement) {
		this.selectedAchievement = selectedAchievement;
	}

	public String saveAchievement() {
		
//		if(! selectedAchievement.isDebidCreditChange() && ! selectedAchievement.isExtensionOfTheTerm() && ! selectedAchievement.isNewContract()){
//			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte Produktfilter anpassen: Neuverträge, VertragsVL oder DC Wechsel wählen.", null));
//			return null;
//		}
		
		selectedAchievement = achievementController.saveAchievement(getSelectedAchievement());
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Leistungsziel gespeichert.", null));

		achievementController.calculateAchievementGoalAttainment(getSelectedAchievement());
		
		return "achievementList";
	}

	public String deleteAchievement() {
		getSelectedAchievement().setDeleted();
		saveAchievement();
		return "achievementList";
	}

	public void setCloneAchievement(Achievement achievement) {
		this.selectedAchievement = achievementController.cloneAchievement(achievement);
	}
	
	public void cloneAchievement(){
		
	}
	
	public void calculateAchievementGoalAttainment(Achievement achievement){
		achievementController.calculateAchievementGoalAttainment(achievement);
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Leistungsziel "+achievement.getName()+" aktualisiert.", null));
	}
	
	

	
	public void setTotalPieceTargetToSelectedAchievement(){
		getSelectedAchievement().setTotalPieceTarget(achievementController.factoryNewAchievementTarget());
	}
	
	public void setTotalSumTargetToSelectedAchievement(){
		getSelectedAchievement().setTotalSumTarget(achievementController.factoryNewAchievementTarget());
	}
	
	public void setTotalAquiredRevenueTargetToSelectedAchievement(){
		getSelectedAchievement().setTotalAquiredRevenueTarget(achievementController.factoryNewAchievementTarget());
	}
	
	public void setTotalContractedRevenueTargetToSelectedAchievement(){
		getSelectedAchievement().setTotalContractedRevenueTarget(achievementController.factoryNewAchievementTarget());
	}
	
	
	public void clearTotalPieceTargetOnSelectedAchievement(){
		getSelectedAchievement().setTotalPieceTarget(null);
	}
	
	public void clearTotalSumTargetOnSelectedAchievement(){
		getSelectedAchievement().setTotalSumTarget(null);
	}
	
	public void clearTotalAquiredRevenueTargetOnSelectedAchievement(){
		getSelectedAchievement().setTotalAquiredRevenueTarget(null);
	}
	
	public void clearTotalContractedRevenueTargetOnSelectedAchievement(){
		getSelectedAchievement().setTotalContractedRevenueTarget(null);
	}
	
	
	public void addPieceTargetToSelectedAchievement(){
		((List<AchievementTarget>) getSelectedAchievement().getPieceTargetList()).add(achievementController.factoryNewAchievementTarget());
	}
	
	public void addSumTargetToSelectedAchievement(){
		((List<AchievementTarget>) getSelectedAchievement().getSumTargetList()).add(achievementController.factoryNewAchievementTarget());
	}
	
	public void addAquiredRevenueTargetToSelectedAchievement(){
		((List<AchievementTarget>) getSelectedAchievement().getAquiredRevenueTargetList()).add(achievementController.factoryNewAchievementTarget());
	}
	
	public void addContractedRevenueTargetToSelectedAchievement(){
		((List<AchievementTarget>) getSelectedAchievement().getContractedRevenueTargetList()).add(achievementController.factoryNewAchievementTarget());
	}
	

	public void removePieceTargetFromSelectedAchievement(AchievementTarget achievementTarget){
		((List<AchievementTarget>) getSelectedAchievement().getPieceTargetList()).remove(achievementTarget);
	}
	
	public void removeSumTargetFromSelectedAchievement(AchievementTarget achievementTarget){
		((List<AchievementTarget>) getSelectedAchievement().getSumTargetList()).remove(achievementTarget);
	}
	
	public void removeAquiredRevenueTargetFromSelectedAchievement(AchievementTarget achievementTarget){
		((List<AchievementTarget>) getSelectedAchievement().getAquiredRevenueTargetList()).remove(achievementTarget);
	}
	
	public void removeContractedRevenueTargetFromSelectedAchievement(AchievementTarget achievementTarget){
		((List<AchievementTarget>) getSelectedAchievement().getContractedRevenueTargetList()).remove(achievementTarget);
	}
		

	public BigDecimal getUserPieceGoalAttainment(Achievement achievement, User user){
		return achievement.getUserGoalAttainment(user).getPieceGoalAttainment();
	}
	
	public BigDecimal getUserSumGoalAttainment(Achievement achievement, User user){
		return achievement.getUserGoalAttainment(user).getSumGoalAttainment();
	}
	
	public BigDecimal getUserAquiredRevenueGoalAttainment(Achievement achievement, User user){
		return achievement.getUserGoalAttainment(user).getAquiredRevenueGoalAttainment();
	}
	
	public BigDecimal getUserContractedRevenueGoalAttainment(Achievement achievement, User user){
		return achievement.getUserGoalAttainment(user).getContractedRevenueGoalAttainment();
	}

}
