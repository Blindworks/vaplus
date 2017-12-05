package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import de.vaplus.api.entity.SecureLink;

/**
 * Entity implementation class for Entity: SecureLink
 *
 */
@Entity
@Table(name="SecureLink")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SecureLinkEntity extends StatusBaseEntity implements SecureLink {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2942703658909509125L;

	// COLLATE utf8_bin
	@Column(name="code", nullable = false)
	private String code;

	@Column(name="operation", nullable = false)
	private String operation;
	
	public SecureLinkEntity() {
		super();
	}
	
	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

   
}
