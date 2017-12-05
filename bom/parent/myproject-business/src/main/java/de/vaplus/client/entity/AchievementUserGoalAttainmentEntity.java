package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.AchievementUserGoalAttainment;
import de.vaplus.api.entity.User;

@Entity
@IdClass(AchievementUserGoalAttainmentEntityPK.class)
@Table(name="AchievementUserGoalAttainment")
public class AchievementUserGoalAttainmentEntity implements AchievementUserGoalAttainment{

	@Id
	@ManyToOne
    @JoinColumn(name="achievement_id", nullable = false)
	private AchievementEntity achievement;

	@Id
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
	private UserEntity user;

	@Column(name="pieceGoalAttainment", nullable = false)
	private BigDecimal pieceGoalAttainment;
	
	@Column(name="sumGoalAttainment", nullable = false)
	private BigDecimal sumGoalAttainment;
	
	@Column(name="aquiredRevenueGoalAttainment", nullable = false)
	private BigDecimal aquiredRevenueGoalAttainment;
	
	@Column(name="contractedRevenueGoalAttainment", nullable = false)
	private BigDecimal contractedRevenueGoalAttainment;

	public AchievementUserGoalAttainmentEntity(){
		
	}

	public AchievementUserGoalAttainmentEntity(AchievementEntity achievement, UserEntity user){
		setAchievement(achievement);
		setUser(user);
	}
	
	@Override
	public Achievement getAchievement() {
		return achievement;
	}

	@Override
	public void setAchievement(Achievement achievement) {
		this.achievement = (AchievementEntity) achievement;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
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
	public void clearGoalAttainment(){
		setPieceGoalAttainment(new BigDecimal(0));
		setSumGoalAttainment(new BigDecimal(0));
		setAquiredRevenueGoalAttainment(new BigDecimal(0));
		setContractedRevenueGoalAttainment(new BigDecimal(0));
	}
	
	@Override
	public void addPieceGoalAttainment(BigDecimal quantity){
		setPieceGoalAttainment( getPieceGoalAttainment().add(quantity));
	}
	
	@Override
	public void addSumGoalAttainment(BigDecimal quantity){
		setSumGoalAttainment( getSumGoalAttainment().add(quantity));
	}
	
	@Override
	public void addAquiredRevenueGoalAttainment(BigDecimal quantity){
		setAquiredRevenueGoalAttainment( getAquiredRevenueGoalAttainment().add(quantity));
	}
	
	@Override
	public void addContractedRevenueGoalAttainment(BigDecimal quantity){
		setContractedRevenueGoalAttainment( getContractedRevenueGoalAttainment().add(quantity));
	}

}
