package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.File;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="File")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FileEntity extends BaseEntity implements File {

	private static final long serialVersionUID = -1515940056105948217L;

	@Column(name="mimeType", nullable = false)
	private String mimeType;

	@Column(name="code", nullable = false)
	private String code;

	@Column(name="title", nullable = true)
	private String title;

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Transient
	@Override
	public String getURI(){
		return "/File/"+this.getCode();
	}


}
