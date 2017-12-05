package de.vaplus.client.entity.commission;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.VOTypeCommissionRevenueLevel;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.entity.BaseEntity;
import de.vaplus.client.entity.VendorEntity;

/**
 * Entity implementation class for Entity: RevenueStep
 *
 */
@Entity
@Table(name="RevenueLevel")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
	@NamedQuery(
	        name = "RevenueLevel.listAquiredRevenueLevelEntityInRange",
	        query = "SELECT rl FROM AquiredRevenueLevelEntity rl WHERE rl.scaleFrom <= :gteScaleFrom AND rl.scaleTo >= :lteScaleTo AND rl.vendor.id = :vendorId",
	        hints = {
	                @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
	            }
    )
})
public abstract class RevenueLevelEntity extends BaseEntity implements RevenueLevel {

	private static final long serialVersionUID = -4962676987574468939L;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name="scaleFrom", precision = 10, scale = 4, nullable = false)
	private BigDecimal scaleFrom;

	@Column(name="scaleTo", precision = 10, scale = 4, nullable = false)
	private BigDecimal scaleTo;

	@ManyToOne
    @JoinColumn(name="vendor_id", nullable = false)
	private VendorEntity vendor;
	
//	@ManyToOne
//    @JoinColumn(name="revenueStepVOType_id")
//	private RevenueLevelVOTypeEntity revenueStepVOType;
	
	@OneToMany(mappedBy="revenueLevel", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VOTypeCommissionRevenueLevelEntity> voTypeCommissionRevenueLevelList;
	
	public RevenueLevelEntity() {
		super();
	}

	@Override
	public List<? extends VOTypeCommissionRevenueLevel> getVoTypeCommissionRevenueLevelList() {
		return voTypeCommissionRevenueLevelList;
	}

	public void setVoTypeCommissionRevenueLevelList(
			List<VOTypeCommissionRevenueLevelEntity> voTypeCommissionRevenueLevelList) {
		this.voTypeCommissionRevenueLevelList = voTypeCommissionRevenueLevelList;
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
	public BigDecimal getScaleFrom() {
		return scaleFrom;
	}

	@Override
	public void setScaleFrom(BigDecimal scaleFrom) {
		this.scaleFrom = scaleFrom;
	}

	@Override
	public BigDecimal getScaleTo() {
		return scaleTo;
	}

	@Override
	public void setScaleTo(BigDecimal scaleTo) {
		this.scaleTo = scaleTo;
	}

	@Override
	public Vendor getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(Vendor vendor) {
		this.vendor = (VendorEntity) vendor;
	}

//	@Override
//	public RevenueLevelVOType getRevenueStepVOType() {
//		return revenueStepVOType;
//	}
//
//	@Override
//	public void setRevenueStepVOType(RevenueLevelVOType revenueStepVOType) {
//		this.revenueStepVOType = (RevenueLevelVOTypeEntity) revenueStepVOType;
//	}
   
}
