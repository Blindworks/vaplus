package de.vaplus.client.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import de.vaplus.api.entity.DBFile;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="DBFile")
public class DBFileEntity extends FileEntity implements DBFile {
	
	private static final long serialVersionUID = -9026748741167200858L;
	
	@Lob
	@Column(name="imageFile", nullable = false)
	@Basic(fetch=FetchType.LAZY) // this gets ignored anyway, but it is recommended for blobs
	private byte[] imageFile;

	@Override
	public byte[] getImageFile() {
		return imageFile;
	}

	@Override
	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}
	
}
