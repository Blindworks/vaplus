package de.vaplus.api.entity;

public interface FileSystemFile extends File{

	String getFilename();

	String getRelativeFilePath();

	long getSize();

	DBFile getThumbnail();

}
