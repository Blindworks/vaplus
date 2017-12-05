package de.vaplus.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.FileSystemFile;

public interface FileControllerInterface extends Serializable{

	File getFile(String code);

	File factoryDBNewFile();

	DBFile updateDBFile(DBFile dbFile, InputStream is, String contentType,
			long size);

	DBFile saveDBFile(InputStream is, String contentType, long size);

	String getFilePath(FileSystemFile file);

	FileSystemFile saveFileSystemFile(InputStream is, String fileName, String mimeType, long size) ;

	List<? extends FileActivity> getOwnerFileList(ActivityOwner owner, Base fileRelation, boolean onlyVisible);

	long getFileConsumption();

	void deleteFileActivity(FileActivity fileActivity);

	long getStorageLimit();

	DBFile getDBFileEntity(long id);

	void flushEao();

	java.io.File getTempFile(String suffix) throws IOException, Exception;

	FileSystemFile saveInvoice(java.io.File tmpFile, String invoiceNumber) throws IOException;

}
