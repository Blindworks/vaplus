package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.AchievementController;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.AchievementUserGoalAttainment;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;

@Entity
@Table(name="Achievement")
@NamedQueries({
    @NamedQuery(
            name = "Achievement.findActive",
            query = "SELECT a FROM AchievementEntity a WHERE a.deleted = false and a.enabled = true and a.effectiveDate < :effectiveDate and a.expiryDate > :expiryDate ORDER BY a.weight, a.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        	),
    @NamedQuery(
            name = "Achievement.findOpen",
            query = "SELECT a FROM AchievementEntity a WHERE a.deleted = false and ( a.expiryDate > :date or a.expiryDate IS NULL ) ORDER BY a.weight, a.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        	),
    @NamedQuery(
            name = "Achievement.findClosed",
            query = "SELECT a FROM AchievementEntity a WHERE a.deleted = false and a.expiryDate < :date ORDER BY a.weight, a.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        	),
    @NamedQuery(
            name = "Achievement.findFinishedBetween",
            query = "SELECT a FROM AchievementEntity a WHERE a.deleted = false and a.expiryDate >= :from and a.expiryDate <= :to ORDER BY a.weight, a.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        	),
    @NamedQuery(
            name = "Achievement.getAll",
            query = "SELECT a FROM AchievementEntity a WHERE a.deleted = false ORDER BY a.weight, a.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        	)
})
public class AchievementEntity extends StatusBaseEntity implements Achievement{

	private static final long serialVersionUID = 4752082286337809068L;
	
	@Column(name="name")
	private String name;
	
	@Column(name="invisible")
	private boolean invisible;
	
	@Column(name="showOnCompanyDashboard")
	private boolean showOnCompanyDashboard;

	@Column(name="weight", nullable = false)
	private int weight;

	@Column(name="newContract", nullable = false)
	private boolean newContract;

	@Column(name="extensionOfTheTerm", nullable = false)
	private boolean extensionOfTheTerm;

	@Column(name="debidCreditChange", nullable = false)
	private boolean debidCreditChange;


	@Column(name="pieceGoalAttainment", nullable = false)
	private BigDecimal pieceGoalAttainment;
	
	@Column(name="sumGoalAttainment", nullable = false)
	private BigDecimal sumGoalAttainment;
	
	@Column(name="aquiredRevenueGoalAttainment", nullable = false)
	private BigDecimal aquiredRevenueGoalAttainment;
	
	@Column(name="contractedRevenueGoalAttainment", nullable = false)
	private BigDecimal contractedRevenueGoalAttainment;
	
	@OneToMany(mappedBy="achievement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AchievementUserGoalAttainmentEntity> userGoalAttainmentList;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedProductCategory",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductCategoryID", referencedColumnName="id")})
	private List<ProductCategoryEntity> selectedProductCategoryList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedProduct",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductID", referencedColumnName="id")})
	private List<BaseProductEntity> selectedProductList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedProductCombination",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductCombinationID", referencedColumnName="id")})
	private List<BaseProductCombinationEntity> selectedProductCombinationList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedProductFilter",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductID", referencedColumnName="id")})
	private List<BaseProductEntity> selectedProductFilterList;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedProductCombinationFilter",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedProductCombinationFilterID", referencedColumnName="id")})
	private List<BaseProductCombinationEntity> selectedProductCombinationFilterList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedShop",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedShopID", referencedColumnName="id")})
	private List<ShopEntity> sourceShopList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedUser",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedUserID", referencedColumnName="id")})
	private List<UserEntity> sourceUserList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedVO",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedVOID", referencedColumnName="id")})
	private List<VOEntity> sourceVOList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSelectedShopGroup",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="selectedShopGroupID", referencedColumnName="id")})
	private List<ShopGroupEntity> sourceShopGroupList;
	
	public AchievementEntity(){
//		System.out.println("AchievementEntity CONSTRUCTOR");
	}
	
	@Column(name="payoutToSource")
	private boolean payoutToSource;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementPayoutUser",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="payoutUserID", referencedColumnName="id")})
	private List<UserEntity> payoutUserList;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="totalPieceTarget_id", nullable = false)
	private AchievementTargetEntity totalPieceTarget;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="totalSumTarget_id", nullable = false)
	private AchievementTargetEntity totalSumTarget;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="totalAquiredRevenueTarget_id", nullable = false)
	private AchievementTargetEntity totalAquiredRevenueTarget;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="totalContractedRevenueTarget_id", nullable = false)
	private AchievementTargetEntity totalContractedRevenueTarget;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementPieceTarget",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="pieceTargetID", referencedColumnName="id")})
	private List<AchievementTargetEntity> pieceTargetList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementSumTarget",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="sumTargetID", referencedColumnName="id")})
	private List<AchievementTargetEntity> sumTargetList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementAquiredRevenueTarget",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="aquiredRevenueTargetID", referencedColumnName="id")})
	private List<AchievementTargetEntity> aquiredRevenueTargetList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="AchievementContractedRevenueTarget",
	joinColumns={@JoinColumn(name="achievementID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="contractedRevenueTargetID", referencedColumnName="id")})
	private List<AchievementTargetEntity> contractedRevenueTargetList;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public void setEffectiveDate(Date date){
		
		if(date == null){
			super.setEffectiveDate(date);
			return;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		super.setEffectiveDate(c.getTime());
	}
	
	@Override
	public void setExpiryDate(Date date){
		
		if(date == null){
			super.setEffectiveDate(date);
			return;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		super.setExpiryDate(c.getTime());
	}

	@Override
	public boolean isInvisible() {
		return invisible;
	}

	@Override
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	@Override
	public List<? extends ProductCategory> getSelectedProductCategoryList() {
		if(selectedProductCategoryList == null)
			selectedProductCategoryList = new LinkedList<ProductCategoryEntity>();
		return selectedProductCategoryList;
	}

	@Override
	public void setSelectedProductCategoryList(
			List<? extends ProductCategory> selectedProductCategoryList) {
		this.selectedProductCategoryList = (List<ProductCategoryEntity>) selectedProductCategoryList;
	}

	@Override
	public List<? extends BaseProduct> getSelectedProductList() {
		if(selectedProductList == null)
			selectedProductList = new LinkedList<BaseProductEntity>();
		return selectedProductList;
	}

	@Override
	public void setSelectedProductList(List<? extends BaseProduct> selectedProductList) {
		this.selectedProductList = (List<BaseProductEntity>) selectedProductList;
	}

	@Override
	public List<? extends BaseProductCombination> getSelectedProductCombinationList() {
		if(selectedProductCombinationList == null)
			selectedProductCombinationList = new LinkedList<BaseProductCombinationEntity>();
		return selectedProductCombinationList;
	}

	@Override
	public void setSelectedProductCombinationList(List<? extends BaseProductCombination> selectedProductCombinationList) {
		this.selectedProductCombinationList = (List<BaseProductCombinationEntity>) selectedProductCombinationList;
	}

	@Override
	public List<? extends BaseProductCombination> getSelectedProductCombinationFilterList() {
		if(selectedProductCombinationFilterList == null)
			selectedProductCombinationFilterList = new LinkedList<BaseProductCombinationEntity>();
		return selectedProductCombinationFilterList;
	}

	@Override
	public void setSelectedProductCombinationFilterList(List<? extends BaseProductCombination> selectedProductCombinationFilterList) {
		this.selectedProductCombinationFilterList = (List<BaseProductCombinationEntity>) selectedProductCombinationFilterList;
	}

	@Override
	public List<? extends BaseProduct> getSelectedProductFilterList() {
		if(selectedProductFilterList == null)
			selectedProductFilterList = new LinkedList<BaseProductEntity>();
		return selectedProductFilterList;
	}

	@Override
	public void setSelectedProductFilterList(List<? extends BaseProduct> selectedProductFilterList) {
		this.selectedProductFilterList = (List<BaseProductEntity>) selectedProductFilterList;
	}

	@Override
	public List<? extends Shop> getSourceShopList() {
		if(sourceShopList == null)
			sourceShopList = new LinkedList<ShopEntity>();
		return sourceShopList;
	}

	@Override
	public void setSourceShopList(List<? extends Shop> sourceShopList) {
		this.sourceShopList = (List<ShopEntity>) sourceShopList;
	}

	@Override
	public List<? extends User> getSourceUserList() {
		if(sourceUserList == null)
			sourceUserList = new LinkedList<UserEntity>();
		return sourceUserList;
	}

	@Override
	public void setSourceUserList(List<? extends User> sourceUserList) {
		this.sourceUserList = (List<UserEntity>) sourceUserList;
	}

	@Override
	public List<? extends VO> getSourceVOList() {
		if(sourceVOList == null)
			sourceVOList = new LinkedList<VOEntity>();
		return sourceVOList;
	}

	@Override
	public void setSourceVOList(List<? extends VO> sourceVOList) {
		this.sourceVOList = (List<VOEntity>) sourceVOList;
	}

	@Override
	public List<? extends ShopGroup> getSourceShopGroupList() {
		if(sourceShopGroupList == null)
			sourceShopGroupList = new LinkedList<ShopGroupEntity>();
		return sourceShopGroupList;
	}

	@Override
	public void setSourceShopGroupList(List<? extends ShopGroup> sourceShopGroupList) {
		this.sourceShopGroupList = (List<ShopGroupEntity>) sourceShopGroupList;
	}

	@Override
	public boolean isPayoutToSource() {
		return payoutToSource;
	}

	@Override
	public void setPayoutToSource(boolean payoutToSource) {
		this.payoutToSource = payoutToSource;
	}

	@Override
	public List<? extends User> getPayoutUserList() {
		if(payoutUserList == null)
			payoutUserList = new LinkedList<UserEntity>();
		return payoutUserList;
	}

	@Override
	public void setPayoutUserList(List<? extends User> payoutUserList) {
		this.payoutUserList = (List<UserEntity>) payoutUserList;
	}

	@Override
	public AchievementTarget getTotalPieceTarget() {
//		if(totalPieceTarget == null)
//			totalPieceTarget = new AchievementTargetEntity();
		return totalPieceTarget;
	}

	@Override
	public void setTotalPieceTarget(AchievementTarget totalPieceTarget) {
		this.totalPieceTarget = (AchievementTargetEntity) totalPieceTarget;
	}

	@Override
	public AchievementTarget getTotalSumTarget() {
//		if(totalSumTarget == null)
//			totalSumTarget = new AchievementTargetEntity();
		return totalSumTarget;
	}

	@Override
	public void setTotalSumTarget(AchievementTarget totalSumTarget) {
		this.totalSumTarget = (AchievementTargetEntity) totalSumTarget;
	}

	@Override
	public AchievementTarget getTotalAquiredRevenueTarget() {
//		if(totalAquiredRevenueTarget == null)
//			totalAquiredRevenueTarget = new AchievementTargetEntity();
		return totalAquiredRevenueTarget;
	}

	@Override
	public void setTotalAquiredRevenueTarget(
			AchievementTarget totalAquiredRevenueTarget) {
		this.totalAquiredRevenueTarget = (AchievementTargetEntity) totalAquiredRevenueTarget;
	}

	@Override
	public AchievementTarget getTotalContractedRevenueTarget() {
//		if(totalContractedRevenueTarget == null)
//			totalContractedRevenueTarget = new AchievementTargetEntity();
		return totalContractedRevenueTarget;
	}

	@Override
	public void setTotalContractedRevenueTarget(
			AchievementTarget totalContractedRevenueTarget) {
		this.totalContractedRevenueTarget = (AchievementTargetEntity) totalContractedRevenueTarget;
	}

	@Override
	public List<? extends AchievementTarget> getPieceTargetList() {
		if(pieceTargetList == null)
			pieceTargetList = new LinkedList<AchievementTargetEntity>();
		return pieceTargetList;
	}

	@Override
	public void setPieceTargetList(List<? extends AchievementTarget> pieceTargetList) {
		this.pieceTargetList = (List<AchievementTargetEntity>) pieceTargetList;
	}

	@Override
	public List<? extends AchievementTarget> getSumTargetList() {
		if(sumTargetList == null)
			sumTargetList = new LinkedList<AchievementTargetEntity>();
		return sumTargetList;
	}

	@Override
	public void setSumTargetList(List<? extends AchievementTarget> sumTargetList) {
		this.sumTargetList = (List<AchievementTargetEntity>) sumTargetList;
	}

	@Override
	public List<? extends AchievementTarget> getAquiredRevenueTargetList() {
		if(aquiredRevenueTargetList == null)
			aquiredRevenueTargetList = new LinkedList<AchievementTargetEntity>();
		return aquiredRevenueTargetList;
	}

	@Override
	public void setAquiredRevenueTargetList(
			List<? extends AchievementTarget> aquiredRevenueTargetList) {
		this.aquiredRevenueTargetList = (List<AchievementTargetEntity>) aquiredRevenueTargetList;
	}

	@Override
	public List<? extends AchievementTarget> getContractedRevenueTargetList() {
		if(contractedRevenueTargetList == null)
			contractedRevenueTargetList = new LinkedList<AchievementTargetEntity>();
		return contractedRevenueTargetList;
	}

	@Override
	public void setContractedRevenueTargetList(
			List<? extends AchievementTarget> contractedRevenueTargetList) {
		this.contractedRevenueTargetList = (List<AchievementTargetEntity>) contractedRevenueTargetList;
	}

	@Override
	public boolean isNewContract() {
		return newContract;
	}

	@Override
	public void setNewContract(boolean newContract) {
		this.newContract = newContract;
	}

	@Override
	public boolean isExtensionOfTheTerm() {
		return extensionOfTheTerm;
	}

	@Override
	public void setExtensionOfTheTerm(boolean extensionOfTheTerm) {
		this.extensionOfTheTerm = extensionOfTheTerm;
	}

	@Override
	public boolean isDebidCreditChange() {
		return debidCreditChange;
	}

	@Override
	public void setDebidCreditChange(boolean debidCreditChange) {
		this.debidCreditChange = debidCreditChange;
	}
	
	@Override
	public BigDecimal getPieceGoalAttainment() {
		if(pieceGoalAttainment == null)
			pieceGoalAttainment = new BigDecimal(0);
		return pieceGoalAttainment;
	}

	@Override
	public void setPieceGoalAttainment(BigDecimal pieceGoalAttainment) {
		this.pieceGoalAttainment = pieceGoalAttainment;
	}

	@Override
	public BigDecimal getSumGoalAttainment() {
		if(sumGoalAttainment == null)
			sumGoalAttainment = new BigDecimal(0);
		return sumGoalAttainment;
	}

	@Override
	public void setSumGoalAttainment(BigDecimal sumGoalAttainment) {
		this.sumGoalAttainment = sumGoalAttainment;
	}

	@Override
	public BigDecimal getAquiredRevenueGoalAttainment() {
		if(aquiredRevenueGoalAttainment == null)
			aquiredRevenueGoalAttainment = new BigDecimal(0);
		return aquiredRevenueGoalAttainment;
	}

	@Override
	public void setAquiredRevenueGoalAttainment(
			BigDecimal aquiredRevenueGoalAttainment) {
		this.aquiredRevenueGoalAttainment = aquiredRevenueGoalAttainment;
	}

	@Override
	public BigDecimal getContractedRevenueGoalAttainment() {
		if(contractedRevenueGoalAttainment == null)
			contractedRevenueGoalAttainment = new BigDecimal(0);
		return contractedRevenueGoalAttainment;
	}

	@Override
	public void setContractedRevenueGoalAttainment(
			BigDecimal contractedRevenueGoalAttainment) {
		this.contractedRevenueGoalAttainment = contractedRevenueGoalAttainment;
	}
	
	@Override
	public List<? extends AchievementUserGoalAttainment> getUserGoalAttainmentList() {
		if(userGoalAttainmentList == null)
			userGoalAttainmentList = new LinkedList<AchievementUserGoalAttainmentEntity>();
		return userGoalAttainmentList;
	}
	
	@Override
	public AchievementUserGoalAttainment getUserGoalAttainment(User user) {
		
		Iterator<? extends AchievementUserGoalAttainment> i = getUserGoalAttainmentList().iterator();
		AchievementUserGoalAttainment userGoalAttainment;
		while(i.hasNext()){
			userGoalAttainment = i.next();
			if(userGoalAttainment.getUser().equals(user))
					return userGoalAttainment;
		}
		
		userGoalAttainment = new AchievementUserGoalAttainmentEntity(this,(UserEntity) user);
		((List<AchievementUserGoalAttainment>)getUserGoalAttainmentList()).add(userGoalAttainment);
		
		return userGoalAttainment;
	}

	@Override
	public void setUserGoalAttainmentList(List<? extends AchievementUserGoalAttainment> userGoalAttainmentList) {
		this.userGoalAttainmentList = (List<AchievementUserGoalAttainmentEntity>) userGoalAttainmentList;
	}

	@Override
	public void clearGoalAttainment(){
		setPieceGoalAttainment(new BigDecimal(0));
		setSumGoalAttainment(new BigDecimal(0));
		setAquiredRevenueGoalAttainment(new BigDecimal(0));
		setContractedRevenueGoalAttainment(new BigDecimal(0));
		
		//TODO
		
		//Clear User Goal Attainment
		Iterator<? extends AchievementUserGoalAttainment> i = getUserGoalAttainmentList().iterator();
		AchievementUserGoalAttainment userGoalAttainment;
		while(i.hasNext()){
			userGoalAttainment = i.next();
			userGoalAttainment.clearGoalAttainment();	
		}
	}
	
	@Override
	public void addPieceGoalAttainment(User user, BigDecimal pieces){
		setPieceGoalAttainment( getPieceGoalAttainment().add(pieces));
		
		getUserGoalAttainment(user).addPieceGoalAttainment(pieces);
	}
	
	@Override
	public void addSumGoalAttainment(User user, BigDecimal sum){
		setSumGoalAttainment( getSumGoalAttainment().add(sum));
		
		getUserGoalAttainment(user).addSumGoalAttainment(sum);
	}
	
	@Override
	public void addAquiredRevenueGoalAttainment(User user, BigDecimal revenue){
		setAquiredRevenueGoalAttainment( getAquiredRevenueGoalAttainment().add(revenue));
		
		getUserGoalAttainment(user).addAquiredRevenueGoalAttainment(revenue);
	}
	
	@Override
	public void addContractedRevenueGoalAttainment(User user, BigDecimal revenue){
		setContractedRevenueGoalAttainment( getContractedRevenueGoalAttainment().add(revenue));
		
		getUserGoalAttainment(user).addContractedRevenueGoalAttainment(revenue);
	}

	@Override
	public BigDecimal getMaxPieceTarget(){
		return getMaxTarget(getPieceTargetList());
	}
	
	@Override
	public BigDecimal getMaxSumTarget(){
		return getMaxTarget(getSumTargetList());
	}
	
	@Override
	public BigDecimal getMaxAquiredRevenueTarget(){
		return getMaxTarget(getAquiredRevenueTargetList());
	}
	
	@Override
	public BigDecimal getMaxContractedRevenueTarget(){
		return getMaxTarget(getContractedRevenueTargetList());
	}
	


	
	private BigDecimal getMaxTarget(List<? extends AchievementTarget> list){
		BigDecimal target = new BigDecimal(0);
		Iterator<? extends AchievementTarget> i = list.iterator();
		AchievementTarget achievementTarget;
		while(i.hasNext()){
			achievementTarget = i.next();
			if(target.compareTo(achievementTarget.getTarget()) < 0)
				target = achievementTarget.getTarget();
		}
		return target;
	}

	
	@Override
	public boolean isTotalPieceTargetReached(){
		if(getTotalPieceTarget() == null)
			return true;
		
		return this.getTotalPieceTarget().isTargetReached(this.getPieceGoalAttainment());
	}
	
	@Override
	public boolean isTotalSumTargetReached(){
		if(getTotalSumTarget() == null)
			return true;
		
		return this.getTotalSumTarget().isTargetReached(this.getSumGoalAttainment());
	}
	
	@Override
	public boolean isTotalAquiredRevenueTargetReached(){
		if(getTotalAquiredRevenueTarget() == null)
			return true;
		
		return this.getTotalAquiredRevenueTarget().isTargetReached(this.getAquiredRevenueGoalAttainment());
	}
	
	@Override
	public boolean isTotalContractedRevenueTargetReached(){
		if(getTotalContractedRevenueTarget() == null)
			return true;
		
		return this.getTotalContractedRevenueTarget().isTargetReached(this.getContractedRevenueGoalAttainment());
	}
	
	@Override
	public boolean isPieceTargetReached(){
		
		if(getPieceTargetList().size() == 0)
			return true;
		
		if(getReachedPieceTarget() != null)
			return true;
		
		return false;
	}
	
	@Override
	public boolean isSumTargetReached(){
		
		if(getSumTargetList().size() == 0)
			return true;
		
		if(getReachedSumTarget() != null)
			return true;
		
		return false;
	}
	
	@Override
	public boolean isAquiredRevenueTargetReached(){
		
		if(getAquiredRevenueTargetList().size() == 0)
			return true;
		
		if(getReachedAquiredRevenueTarget() != null)
			return true;
		
		return false;
	}
	
	@Override
	public boolean isContractedRevenueTargetReached(){
		
		if(getContractedRevenueTargetList().size() == 0)
			return true;
		
		if(getReachedContractedRevenueTarget() != null)
			return true;
		
		return false;
	}
	
	@Override 
	public AchievementTarget getReachedPieceTarget(){
		Iterator<? extends AchievementTarget> i = this.getPieceTargetList().iterator();
		
		AchievementTarget target, highestReachedTarget = null;
		BigDecimal biggestTarget = new BigDecimal(0);
		
		while(i.hasNext()){
			target = i.next();
			
			if(target.isTargetReached(this.getPieceGoalAttainment())){
				if(target.getTarget().compareTo(biggestTarget) > 0){
					biggestTarget = target.getTarget();
					highestReachedTarget = target;
				}
			}
			
		}
		
		return highestReachedTarget;
	}
	
	@Override 
	public AchievementTarget getReachedSumTarget(){
		Iterator<? extends AchievementTarget> i = this.getSumTargetList().iterator();
		
		AchievementTarget target, highestReachedTarget = null;
		BigDecimal biggestTarget = new BigDecimal(0);
		
		while(i.hasNext()){
			target = i.next();
			
			if(target.isTargetReached(this.getSumGoalAttainment())){
				if(target.getTarget().compareTo(biggestTarget) > 0){
					biggestTarget = target.getTarget();
					highestReachedTarget = target;
				}
			}
			
		}
		
		return highestReachedTarget;
	}
	
	@Override 
	public AchievementTarget getReachedAquiredRevenueTarget(){
		Iterator<? extends AchievementTarget> i = this.getAquiredRevenueTargetList().iterator();
		
		AchievementTarget target, highestReachedTarget = null;
		BigDecimal biggestTarget = new BigDecimal(0);
		
		while(i.hasNext()){
			target = i.next();
			
			if(target.isTargetReached(this.getAquiredRevenueGoalAttainment())){
				if(target.getTarget().compareTo(biggestTarget) > 0){
					biggestTarget = target.getTarget();
					highestReachedTarget = target;
				}
			}
			
		}
		
		return highestReachedTarget;
	}
	
	@Override 
	public AchievementTarget getReachedContractedRevenueTarget(){
		Iterator<? extends AchievementTarget> i = this.getContractedRevenueTargetList().iterator();
		
		AchievementTarget target, highestReachedTarget = null;
		BigDecimal biggestTarget = new BigDecimal(0);
		
		while(i.hasNext()){
			target = i.next();
			
			if(target.isTargetReached(this.getContractedRevenueGoalAttainment())){
				if(target.getTarget().compareTo(biggestTarget) > 0){
					biggestTarget = target.getTarget();
					highestReachedTarget = target;
				}
			}
			
		}
		
		return highestReachedTarget;
	}
	
	@Override
	public BigDecimal getAchievedCommission(){
		BigDecimal commission = new BigDecimal(0);
		
		if(! isTargetReached())
			return commission;
		
		if(getTotalPieceTarget() != null){
			commission = commission.add(this.getTotalPieceTarget().getCommission());
		}
		
		if(getTotalSumTarget() != null){
			commission = commission.add(this.getTotalSumTarget().getCommission());
		}
		
		if(getTotalAquiredRevenueTarget() != null){
			commission = commission.add(this.getTotalAquiredRevenueTarget().getCommission());
		}
		
		if(getTotalContractedRevenueTarget() != null){
			commission = commission.add(this.getTotalContractedRevenueTarget().getCommission());
		}
		
		if(getReachedPieceTarget() != null){
			commission = commission.add(
					getReachedPieceTarget().getCommission().multiply(getPieceGoalAttainment())
			);
		}
		
		if(getReachedSumTarget() != null){
			commission = commission.add(
					getReachedSumTarget().getCommission().multiply(getSumGoalAttainment())
			);
			
		}
		
		if(getReachedAquiredRevenueTarget() != null){
			commission = commission.add(
					getReachedAquiredRevenueTarget().getCommission().multiply(getAquiredRevenueGoalAttainment())
			);
			
		}
		
		if(getReachedContractedRevenueTarget() != null){
			commission = commission.add(
					getReachedContractedRevenueTarget().getCommission().multiply(getContractedRevenueGoalAttainment())
			);
			
		}
		
		return commission;
	}
	
	@Override
	public BigDecimal getPayout(User user){
		
		AchievementUserGoalAttainment attainment = this.getUserGoalAttainment(user);
		
		BigDecimal payout = new BigDecimal(0);
		
		if(! isTargetReached())
			return payout;
		
		if(getTotalPieceTarget() != null){
			payout = payout.add(this.getTotalPieceTarget().getPayout());
		}
		
		if(getTotalSumTarget() != null){
			payout = payout.add(this.getTotalSumTarget().getPayout());
		}
		
		if(getTotalAquiredRevenueTarget() != null){
			payout = payout.add(this.getTotalAquiredRevenueTarget().getPayout());
		}
		
		if(getTotalContractedRevenueTarget() != null){
			payout = payout.add(this.getTotalContractedRevenueTarget().getPayout());
		}
		
		if(getReachedPieceTarget() != null){
			payout = payout.add(
					getReachedPieceTarget().getPayout().multiply(attainment.getPieceGoalAttainment())
			);
		}
		
		if(getReachedSumTarget() != null){
			payout = payout.add(
					getReachedSumTarget().getPayout().multiply(attainment.getSumGoalAttainment())
			);
			
		}
		
		if(getReachedAquiredRevenueTarget() != null){
			payout = payout.add(
					getReachedAquiredRevenueTarget().getPayout().multiply(attainment.getAquiredRevenueGoalAttainment())
			);
			
		}
		
		if(getReachedContractedRevenueTarget() != null){
			payout = payout.add(
					getReachedContractedRevenueTarget().getPayout().multiply(attainment.getAquiredRevenueGoalAttainment())
			);
			
		}
		
		return payout;
		
	}
	
	@Override
	public String getPayoutText(User user){
		
		AchievementUserGoalAttainment attainment = this.getUserGoalAttainment(user);
		
		String payout = "";
		
		if(! isTargetReached())
			return payout;
		
		if(getTotalPieceTarget() != null && getTotalPieceTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getTotalPieceTarget().getPayoutText();
		}
		
		if(getTotalSumTarget() != null && getTotalSumTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getTotalSumTarget().getPayoutText();
		}
		
		if(getTotalAquiredRevenueTarget() != null && getTotalAquiredRevenueTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getTotalAquiredRevenueTarget().getPayoutText();
		}
		
		if(getTotalContractedRevenueTarget() != null && getTotalContractedRevenueTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getTotalContractedRevenueTarget().getPayoutText();
		}
		
		if(getReachedPieceTarget() != null && getReachedPieceTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getReachedPieceTarget().getPayoutText();
		}
		
		if(getReachedSumTarget() != null && getReachedSumTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getReachedSumTarget().getPayoutText();
		}
		
		if(getReachedAquiredRevenueTarget() != null && getReachedAquiredRevenueTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getReachedAquiredRevenueTarget().getPayoutText();
		}
		
		if(getReachedContractedRevenueTarget() != null && getReachedContractedRevenueTarget().getPayoutText() != null){
			if(payout.length() > 0)
				payout += "<br/>";
			payout += this.getReachedContractedRevenueTarget().getPayoutText();
		}
		
		return payout;
	}
	
	@Override
	public boolean isExpired(){
		if(getExpiryDate() == null)
			return false;
		if(getExpiryDate().getTime() < (new Date()).getTime())
			return true;
		
		return false;
	}
	
	@Override
	public boolean isTargetReached(){
		return
				isEnabled() &&
				isTotalPieceTargetReached()	&&
				isTotalSumTargetReached() &&
				isTotalAquiredRevenueTargetReached() &&
				isTotalContractedRevenueTargetReached() &&
				isPieceTargetReached() &&
				isSumTargetReached() &&
				isAquiredRevenueTargetReached() &&
				isContractedRevenueTargetReached();
	}
	
	@Override
	public List<User> getAcquisitionSourceUserList(){
		Iterator<? extends AchievementUserGoalAttainment> i = getUserGoalAttainmentList().iterator();
		AchievementUserGoalAttainment attainment;
		List<User> userList = new LinkedList<User>();
		while(i.hasNext()){
			attainment = i.next();
			
			
			if(! userList.contains(attainment.getUser()))
				userList.add(attainment.getUser());
		}
		
		return userList;
	}

	@Override
	public boolean isShowOnCompanyDashboard() {
		return showOnCompanyDashboard;
	}

	@Override
	public void setShowOnCompanyDashboard(boolean showOnCompanyDashboard) {
		this.showOnCompanyDashboard = showOnCompanyDashboard;
	}
}
