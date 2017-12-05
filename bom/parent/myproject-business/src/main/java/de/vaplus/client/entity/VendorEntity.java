package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Vendor")
public class VendorEntity extends StatusBaseEntity implements Vendor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8919814991792928255L;


	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="defaultSelection", nullable = false)
	private boolean defaultSelection;

	public VendorEntity() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString(){
		return this.getName();
	}
	
	@Override
	public boolean isDefaultSelection() {
		return defaultSelection;
	}

	@Override
	public void setDefaultSelection(boolean defaultSelection) {
		this.defaultSelection = defaultSelection;
	}
   
}
