package de.vaplus.api.entity;

public interface FileActivity extends Activity{

	File getFile();

	boolean isInvisible();

	void setInvisible(boolean invisible);

	User getCreator();

}
