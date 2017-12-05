package de.vaplus.api.controller.stub;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Transient;

import de.vaplus.api.entity.Bug;

public class BugStub extends StatusBaseStub {

	private int status = 0;
	
	private String title;

	private String description;

	private String tags;

	private String owner;

	private String editor;

	private String releaseVersion;
	
	public BugStub(){}
	
	public BugStub(Bug bug){
		super(bug);
		status = bug.getStatus();
		title = bug.getTitle();
		description = bug.getDescription();
		tags = bug.getTags();
		owner = bug.getOwner();
		editor = bug.getEditor();
		releaseVersion = bug.getReleaseVersion();
	}
	

	public int getStatus() {
		return status;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags.toUpperCase();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getEditor() {
		return editor;
	}
	
	@Transient
	public String[] getTagList() {
		String[] tagList = new String[0];
		
		if(tags != null)
			tagList = tags.split(",");
		
		return tagList;
	}
	
	@Transient
	public long getDatesSinceCreation(){
		if(getCreationDate() == null)
			return 0;
		
		TimeUnit timeUnit = TimeUnit.DAYS;
		long diffInMillies = (new Date()).getTime() - getCreationDate().getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

}
