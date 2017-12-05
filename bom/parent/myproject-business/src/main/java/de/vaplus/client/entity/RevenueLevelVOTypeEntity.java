package de.vaplus.client.entity;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.VOTypeCommissionRevenueLevel;
import de.vaplus.client.entity.commission.VOTypeCommissionRevenueLevelEntity;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="RevenueLevelVOType")
public class RevenueLevelVOTypeEntity extends VOTypeEntity implements RevenueLevelVOType {
	
	private static final long serialVersionUID = 583032368119602062L;
	

	@OneToMany(mappedBy="revenueLevelVOType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VOTypeCommissionRevenueLevelEntity> voTypeCommissionRevenueLevelList;
	
	public RevenueLevelVOTypeEntity() {
		super();
	}
	
	@Override
	public List<VOTypeCommissionRevenueLevelEntity> getVoTypeCommissionRevenueLevelList() {
		return voTypeCommissionRevenueLevelList;
	}

	public void setVoTypeCommissionRevenueLevelList(
			List<VOTypeCommissionRevenueLevelEntity> voTypeCommissionRevenueLevelList) {
		this.voTypeCommissionRevenueLevelList = voTypeCommissionRevenueLevelList;
	}

	@Override
	public VOTypeCommissionRevenueLevel getVoTypeCommissionRevenueLevel(RevenueLevel revenueLevel){
		VOTypeCommissionRevenueLevelEntity voTypeCommissionRevenueLevelEntity;
		
//		System.out.println("getVoTypeCommissionRevenueLevel");
		
		Iterator<VOTypeCommissionRevenueLevelEntity> it = getVoTypeCommissionRevenueLevelList().iterator();
		while(it.hasNext()){
			voTypeCommissionRevenueLevelEntity = it.next();
//			System.out.println("check voTypeCommissionRevenueLevelEntity"+voTypeCommissionRevenueLevelEntity.getId());
			if(voTypeCommissionRevenueLevelEntity.getRevenueLevel() != null && voTypeCommissionRevenueLevelEntity.getRevenueLevel().equals(revenueLevel))
				return voTypeCommissionRevenueLevelEntity;
		}
		
		// No VOTypeCommissionRevenueLevelEntity found!
		voTypeCommissionRevenueLevelEntity = new VOTypeCommissionRevenueLevelEntity();
		voTypeCommissionRevenueLevelEntity.setRevenueLevel(revenueLevel);
		voTypeCommissionRevenueLevelEntity.setRevenueLevelVOType(this);
		getVoTypeCommissionRevenueLevelList().add(voTypeCommissionRevenueLevelEntity);
		return voTypeCommissionRevenueLevelEntity;
		
	}
}
