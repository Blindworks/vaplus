package de.vaplus.client.entity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.FileSystemFile;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="FileSystemFile")
public class FileSystemFileEntity extends FileEntity implements FileSystemFile {


	private static final long serialVersionUID = 9057106785698229158L;

	@Column(name="filename", nullable = false)
	private String filename;

	@Column(name="relativeFilePath", nullable = false)
	private String relativeFilePath;

	@Column(name="size", nullable = false)
	private long size;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="thumbnail_id")
    private DBFileEntity thumbnail;

	@Override
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String getRelativeFilePath() {
		return relativeFilePath;
	}

	public void setRelativeFilePath(String relativeFilePath) {
		this.relativeFilePath = relativeFilePath;
	}

	@Override
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public DBFile getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(DBFileEntity thumbnail) {
		this.thumbnail = thumbnail;
	}
}
