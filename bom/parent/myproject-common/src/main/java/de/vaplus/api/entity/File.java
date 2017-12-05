package de.vaplus.api.entity;


public interface File extends Base{

	String getMimeType();

	void setMimeType(String mimeType);

	String getCode();

	void setCode(String code);

	String getURI();

	String getTitle();

	void setTitle(String title);


}
