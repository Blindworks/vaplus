package de.vaplus.client.entity.commission;

import javax.persistence.Entity;

import de.vaplus.api.entity.ContractedRevenueLevel;

/**
 * Entity implementation class for Entity: ContractedRevenueStepEntity
 *
 */
@Entity
public class ContractedRevenueLevelEntity extends RevenueLevelEntity implements ContractedRevenueLevel {

	private static final long serialVersionUID = 3095566166788724918L;

	public ContractedRevenueLevelEntity() {
		super();
	}
   
}
