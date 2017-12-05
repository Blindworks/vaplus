package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="UserProperty")
@NamedQueries({
    @NamedQuery(
            name = "UserProperty.findByName",
            query = "SELECT up FROM UserPropertyEntity up Where up.user = :user AND up.name = :name",
            hints = {
                    @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase)
            }
        )
    })
public class UserPropertyEntity extends BaseEntity implements Property {

	private static final long serialVersionUID = 750575232230747141L;

	@Column(name="name")
	private String name;
	
	@Column(name="stringValue")
	private String stringValue;

	@Column(name="longValue")
	private long longValue;

	@Column(name="doubleValue")
	private double doubleValue;

	@Column(name="decimalValue", precision = 10, scale = 4)
	private BigDecimal decimalValue;

	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	public UserPropertyEntity() {
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
