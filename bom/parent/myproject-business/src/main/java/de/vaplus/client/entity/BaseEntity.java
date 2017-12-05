package de.vaplus.client.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.internal.sessions.DirectToFieldChangeRecord;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;

import de.vaplus.api.entity.Base;
import de.vaplus.client.changetracking.TrackChangesListener;
import de.vaplus.client.eao.BaseEao;

/**
 * Entity implementation class for Entity: User
 *
 */

@EntityListeners({TrackChangesListener.class})
@MappedSuperclass
@Cache(
	  type=CacheType.SOFT, // Cache everything until the JVM decides memory is low.
	  size=64000,  // Use 64,000 as the initial cache size.
	  expiry=600000,  // 6 minutes
	  coordinationType=CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS  // if cache coordination is used, only send invalidation messages.
)
public abstract class BaseEntity implements Base {

	private static final long serialVersionUID = 4288931898165919537L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creationDate", nullable = false)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updateDate", nullable = false)
	private Date updateDate;

	@Column(name="deleted", nullable = false)
	private boolean deleted;
	
	@Transient
	private List<DirectToFieldChangeRecord> changeRecordList;
	
	@Transient
	private BaseEao eao;
	
//	@Id
//	@Column(name="id")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;


	public BaseEntity() {
		super();
	}

	@PrePersist
	@PreUpdate
	protected void onCreate() {
		if(creationDate == null)
			creationDate = new Date();

		updateDate = new Date();
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted() {
		this.deleted = true;
	}


	@Override
	public boolean equals(Object other) {
	    return (other instanceof BaseEntity) && (id != 0)
	        ? id == ((BaseEntity) other).id
	        : (other == this);
	}

	@Override
	public int hashCode() {
	    return (int) ((id != 0)
	        ? (this.getClass().hashCode() + id)
	        : super.hashCode());
	}

	public List<DirectToFieldChangeRecord> getChangeRecordList() {
		if(changeRecordList == null)
			changeRecordList = new ArrayList<DirectToFieldChangeRecord>();
		return changeRecordList;
	}

	public void setChangeRecordList(List<DirectToFieldChangeRecord> changeSet) {
		this.changeRecordList = changeSet;
	}

	public BaseEao getEao() {
		return eao;
	}

	public void setEao(BaseEao eao) {
		this.eao = eao;
	}

}
