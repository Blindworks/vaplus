package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="VOType")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(
        name = "VOType.findByVendor",
        query = "SELECT v FROM VOTypeEntity v WHERE v.deleted = false AND v.vendor = :vendor ORDER BY v.name ASC"
        ,
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    ),
    @NamedQuery(
            name = "VOType.getProductCommissionVOTypeEntity",
            query = "SELECT v FROM ProductCommissionVOTypeEntity v"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "VOType.getRevenueLevelVOTypeEntity",
            query = "SELECT v FROM RevenueLevelVOTypeEntity v WHERE v.deleted = false AND v.vendor = :vendor ORDER BY v.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        )
})
public abstract class VOTypeEntity extends StatusBaseEntity implements VOType {
	
	private static final long serialVersionUID = 8757835739388836757L;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name="shortName", nullable = false)
	private String shortName;

	@Column(name="pointsPerCommission", nullable = false)
	private BigDecimal pointsPerCommission;

	@ManyToOne
    @JoinColumn(name="vendor_id", nullable = false)
    private VendorEntity vendor;
	
	public VOTypeEntity() {
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
	public Vendor getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(Vendor vendor) {
		this.vendor = (VendorEntity) vendor;
	}

	@Override
	public String getShortName() {
		return shortName;
	}

	@Override
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public BigDecimal getPointsPerCommission() {
		if(pointsPerCommission == null) 
			pointsPerCommission = new BigDecimal(0);
		return pointsPerCommission;
	}

	@Override
	public void setPointsPerCommission(BigDecimal pointsPerCommission) {
		this.pointsPerCommission = pointsPerCommission;
	}

	@Override
	public String toString(){
		return this.getName();
	}
}
