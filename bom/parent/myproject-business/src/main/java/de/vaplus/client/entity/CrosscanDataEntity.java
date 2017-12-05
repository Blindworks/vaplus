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
import javax.persistence.Index;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.CrosscanData;
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
@Table(name="CrosscanData",
	indexes = {@Index(name = "time",  columnList="shop_id", unique = true)
})
@NamedQueries({
	@NamedQuery(
	        name = "CrosscanData.listAll",
	        query = "SELECT c FROM CrosscanDataEntity c WHERE c.deleted = false",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),
	@NamedQuery(
	        name = "CrosscanData.getLatestDataTime",
	        query = "SELECT c.time FROM CrosscanDataEntity c WHERE c.shop = :shop Order By c.time DESC"
    ),
	@NamedQuery(
	        name = "CrosscanData.checkIfDataExists",
	        query = "SELECT COUNT(c) FROM CrosscanDataEntity c WHERE c.shop = :shop and c.time = :time"
    ),
	@NamedQuery(
	        name = "CrosscanData.amountSum",
	        query = "SELECT SUM(c.amount) FROM CrosscanDataEntity c WHERE c.shop = :shop and c.time >= :startTime and c.time <= :endTime"
    )
})
public class CrosscanDataEntity extends BaseEntity implements CrosscanData {

	private static final long serialVersionUID = 2311374773822834152L;

	@Column(name="cssid", nullable = false)
	private String cssid;

	@Column(name="companyid", nullable = true)
	private String companyid;

	@Column(name="storeno", nullable = false)
	private int storeno;

	@Column(name="company", nullable = false)
	private String company;

	@Column(name="time", nullable = false)
	private int time;
	
	@Column(name="eventtype", nullable = false)
	private int eventtype;

	@Column(name="eventvalue", nullable = false)
	private int eventvalue;

	@Column(name="valuename", nullable = false)
	private String valuename;

	@Column(name="amount", nullable = false)
	private int amount;

	@Column(name="storetitle", nullable = false)
	private String storetitle;

	@Column(name="storetown", nullable = false)
	private String storetown;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable = true)
    private ShopEntity shop;

	public CrosscanDataEntity() {
		super();
	}

	public String getCssid() {
		return cssid;
	}

	public void setCssid(String cssid) {
		this.cssid = cssid;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public int getStoreno() {
		return storeno;
	}

	public void setStoreno(int storeno) {
		this.storeno = storeno;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getEventtype() {
		return eventtype;
	}

	public void setEventtype(int eventtype) {
		this.eventtype = eventtype;
	}

	public int getEventvalue() {
		return eventvalue;
	}

	public void setEventvalue(int eventvalue) {
		this.eventvalue = eventvalue;
	}

	public String getValuename() {
		return valuename;
	}

	public void setValuename(String valuename) {
		this.valuename = valuename;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getStoretitle() {
		return storetitle;
	}

	public void setStoretitle(String storetitle) {
		this.storetitle = storetitle;
	}

	public String getStoretown() {
		return storetown;
	}

	public void setStoretown(String storetown) {
		this.storetown = storetown;
	}

	public ShopEntity getShop() {
		return shop;
	}

	public void setShop(ShopEntity shop) {
		this.shop = shop;
	}

}
