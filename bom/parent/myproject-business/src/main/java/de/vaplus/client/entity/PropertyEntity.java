package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Property;


/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="Property")
@NamedQueries({
    @NamedQuery(
        name = "Property.findByName",
        query = "SELECT p FROM PropertyEntity p Where p.name = :name",
        hints = {
                @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
            }
    )
})
public class PropertyEntity extends BaseEntity implements Property {

	private static final long serialVersionUID = -4467361688547487056L;
	
	@Column(name="name", unique = true)
	private String name;
	
	@Column(name="stringValue")
	private String stringValue;

	@Column(name="longValue")
	private long longValue;

	@Column(name="doubleValue")
	private double doubleValue;

	@Column(name="decimalValue", precision = 10, scale = 4)
	private BigDecimal decimalValue;

	public PropertyEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public BigDecimal getDecimalValue() {
		return decimalValue;
	}

	public void setDecimalValue(BigDecimal decimalValue) {
		this.decimalValue = decimalValue;
	}

}
