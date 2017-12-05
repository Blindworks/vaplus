package de.vaplus.api.controller.stub;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Transient;

import de.vaplus.api.entity.FeatureRequest;


public class FeatureRequestStub extends StatusBaseStub{

	private int status;

	private String title;

	private String description;

	private String tags;

	private String owner;

	private String editor;

	private String releaseVersion;
	
	public FeatureRequestStub(){}
	
	public FeatureRequestStub(FeatureRequest obj){
		super(obj);
		
		status = obj.getStatus();
		title = obj.getTitle();
		description = obj.getDescription();
		tags = obj.getTags();
		owner = obj.getOwner();
		editor = obj.getEditor();
		releaseVersion = obj.getReleaseVersion();
	}

//	@Override
	public int getStatus() {
		return status;
	}

//	@Override
	public String getTitle() {
		return title;
	}

//	@Override
	public void setTitle(String title) {
		this.title = title;
	}

//	@Override
	public String getDescription() {
		return description;
	}

//	@Override
	public void setDescription(String description) {
		this.description = description;
	}

//	@Override
	public String getTags() {
		return tags;
	}

//	@Override
	public void setTags(String tags) {
		this.tags = tags.toUpperCase();
	}

//	@Override
	public String getOwner() {
		return owner;
	}

//	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

//	@Override
	public String getEditor() {
		return editor;
	}

//	@Override
	@Transient
	public String[] getTagList() {
		String[] tagList = new String[0];
		
		if(tags != null)
			tagList = tags.split(",");
		
		return tagList;
	}
//	@Override
	@Transient
	public long getDatesSinceCreation(){
		if(getCreationDate() == null)
			return 0;
		
		TimeUnit timeUnit = TimeUnit.DAYS;
		long diffInMillies = (new Date()).getTime() - getCreationDate().getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

//	@Override
	public String getReleaseVersion() {
		return releaseVersion;
	}
	
}
