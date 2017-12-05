package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Stock")
@NamedQueries({
	@NamedQuery(
	        name = "Stock.listAll",
	        query = "SELECT w FROM StockEntity w WHERE w.deleted = false",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),@NamedQuery(
            name = "Stock.listEnabled",
            query = "SELECT w FROM StockEntity w WHERE w.enabled = :enabled AND w.deleted = false",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	)
})
public class StockEntity extends StatusBaseEntity implements Stock {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5907286963659976536L;

	@Column(name="name", nullable = false)
	private String name;

	@Embedded
	private AddressEmbeddable address;

	public StockEntity() {
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
