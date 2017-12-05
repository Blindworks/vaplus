package de.vaplus.client.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.Note;
import de.vaplus.api.entity.User;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="FileActivity")
public class FileActivityEntity extends ActivityEntity implements FileActivity {

	private static final long serialVersionUID = -8542258161065189198L;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="file_id", nullable = false)
	private FileEntity file;

	@Column(name="invisible", nullable = false)
	private boolean invisible;

	@ManyToOne
    @JoinColumn(name="creator_id", nullable = false)
    private UserEntity creator;

	@Column(name="relationClass", nullable = false)
	private String relationClass;

	@Column(name="relation", nullable = false)
	private long relation;

	public FileActivityEntity() {
		super();
	}

	@Override
	public File getFile() {
		return file;
	}

	public void setFile(FileEntity file) {
		this.file = file;
	}

	@Override
	public boolean isInvisible() {
		return invisible;
	}

	@Override
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	@Override
	public User getCreator() {
		return creator;
	}

	public void setCreator(UserEntity creator) {
		this.creator = creator;
	}

	public long getRelation() {
		return relation;
	}

	public void setRelation(long relation) {
		this.relation = relation;
	}

	public String getRelationClass() {
		return relationClass;
	}

	public void setRelationClass(String relationClass) {
		this.relationClass = relationClass;
	}

}
