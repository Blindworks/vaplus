package de.vaplus.api.entity;

public interface Bug extends StatusBase{

	int getStatus();

	String getTitle();

	void setTitle(String title);

	String getDescription();

	void setDescription(String description);

	String getTags();

	void setTags(String tags);

	String getOwner();

	void setOwner(String owner);

	String getEditor();

	String[] getTagList();

	long getDatesSinceCreation();

	String getReleaseVersion();


}
