package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import de.vaplus.api.entity.Note;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Note")
public class NoteEntity extends ActivityEntity implements Note {

	private static final long serialVersionUID = -8542258161065189198L;
	
	@Lob 
	@Column(name="content")
	private String content;

	public NoteEntity() {
		super();
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

   
}
