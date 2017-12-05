package de.vaplus.client.entity.commission;

import javax.persistence.Entity;

import de.vaplus.api.entity.AquiredRevenueLevel;

/**
 * Entity implementation class for Entity: AquiredRevenueStepEntity
 *
 */
@Entity
public class AquiredRevenueLevelEntity extends RevenueLevelEntity implements AquiredRevenueLevel {

	private static final long serialVersionUID = 7923723537647746612L;

	public AquiredRevenueLevelEntity() {
		super();
	}
   
}
