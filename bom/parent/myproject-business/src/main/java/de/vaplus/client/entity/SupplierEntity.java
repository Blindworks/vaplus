package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Supplier")
@NamedQueries({
	@NamedQuery(
	        name = "Supplier.listAll",
	        query = "SELECT s FROM SupplierEntity s WHERE s.deleted = false",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),
	@NamedQuery(
	        name = "Supplier.listEnabled",
	        query = "SELECT s FROM SupplierEntity s WHERE s.deleted = false AND s.enabled = true",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    )
})
public class SupplierEntity extends StatusBaseEntity implements Supplier {

	private static final long serialVersionUID = -3875097793549198311L;
	
	@Column(name="name", nullable = false)
	private String name;

	@Embedded
	private AddressEmbeddable address;

	public SupplierEntity() {
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
	public Address getAddress() {
		if(address == null)
			address = new AddressEmbeddable();
		return address;
	}

	@Override
	public void setAddress(Address address) {
		this.address = (AddressEmbeddable) address;
	}

   
}
