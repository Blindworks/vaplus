package de.vaplus;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import de.vaplus.api.ControllerClientInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.FileSystemFile;
import de.vaplus.client.eao.FileEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.DBFileEntity;
import de.vaplus.client.entity.FileActivityEntity;
import de.vaplus.client.entity.FileEntity;
import de.vaplus.client.entity.FileSystemFileEntity;
import de.vaplus.helper.FileHelper;


@Stateless
public class FileController implements FileControllerInterface {

	private static final long serialVersionUID = 6149126285737887003L;

	@Inject
    private FileEao eao;
	
	@EJB
	private PropertyControllerInterface propertyController;

	@Override
	public File factoryDBNewFile(){
		return new DBFileEntity();
	}

	private String getFreeCode(){
		return UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();
	}

	private File initFile(File file){
		file.setCode(getFreeCode());

		return file;
	}

	@Override
	public File getFile(String code) {

		return eao.getFile(code);
	}

	@Override
	public DBFile saveDBFile(InputStream is, String contentType, long size) {
		return updateDBFile(null, is, contentType, size);
	}


	@Override
	public DBFile updateDBFile(DBFile dbFile, InputStream is, String contentType, long size) {
		if(dbFile == null){
			dbFile = new DBFileEntity();
			dbFile = (DBFile) initFile(dbFile);
		}

		dbFile.setMimeType(contentType);

		try {
			dbFile.setImageFile(FileHelper.readFromFileInputStream(is, size));
		} catch (IOException e) {
			return null;
		}

		return (DBFile) eao.saveFile((FileEntity) dbFile);
	}

	@Override
	public void flushEao(){
		eao.flush();
	}
	
	@Override
	public String getFilePath(FileSystemFile file){
		return getAbsoluteFilePath(file.getRelativeFilePath());
	}

	public void generateThumbnail(FileSystemFileEntity file){
		BufferedImage originalImage;
		BufferedImage thumbnail = null;
		String fileType = "";
		String mimeType = file.getMimeType();
		try{

			if(file.getThumbnail() == null){
				file.setThumbnail((DBFileEntity) factoryDBNewFile());
				initFile(file.getThumbnail());
			}

			if(file.getMimeType().equalsIgnoreCase("application/pdf")){
				

				PDDocument document = null;
				try{

			        document = PDDocument.load(new java.io.File(getFilePath(file)));
			        PDFRenderer pdfRenderer = new PDFRenderer(document);
			        PDPage page = document.getDocumentCatalog().getPages().get(0);
					BufferedImage pdfImage = null;
					
			        if( page != null )
			        {
			        	pdfImage = pdfRenderer.renderImage(0);

			        	int type = pdfImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : pdfImage.getType();
			        	thumbnail = createThumbnailFromImage(pdfImage, type);
			        	mimeType = "image/jpeg";
			        	fileType = "jpg";
//			            File outputfile = new File("/Users/dennisknopp/ownCloud/Projects/VAPlus/testpdf.jpg");
//			            ImageIO.write(bi, "jpg", outputfile);
			        }
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(document != null)
						document.close();
				}



			}else if(file.getMimeType().equalsIgnoreCase("image/jpeg")){
				originalImage = ImageIO.read(new java.io.File(getFilePath(file)));
				int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

				thumbnail = createThumbnailFromImage(originalImage, type);
				fileType = "jpg";

//				ByteArrayOutputStream os = new ByteArrayOutputStream();
//				ImageIO.write(resizeImage, "jpg", os);
//				InputStream is = new ByteArrayInputStream(os.toByteArray());
//				updateDBFile(file.getThumbnail(), is , file.getMimeType(), os.size());

			}else if(file.getMimeType().equalsIgnoreCase("image/png")){
				originalImage = ImageIO.read(new java.io.File(getFilePath(file)));
				int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

				thumbnail = createThumbnailFromImage(originalImage, type);
				fileType = "png";

//				ByteArrayOutputStream os = new ByteArrayOutputStream();
//				ImageIO.write(resizeImage, "jpg", os);
//				InputStream is = new ByteArrayInputStream(os.toByteArray());
//				updateDBFile(file.getThumbnail(), is , file.getMimeType(), os.size());

			}

			if(thumbnail != null){

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(thumbnail, fileType, os);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				file.setThumbnail((DBFileEntity) updateDBFile(file.getThumbnail(), is , mimeType, os.size()));
			}



		}catch(IOException e){
			System.out.println(e.getMessage());
		}

	}

	private static BufferedImage createThumbnailFromImage(BufferedImage originalImage, int type){

		float scale = ((float)originalImage.getWidth()) / ((float)originalImage.getHeight());

		int newWidth = (int) (100 * scale);
		int newHeight = 100;

		if(newWidth > 400){
			newWidth = 400;
			newHeight = (int) (400. / (100 * scale) * 100.);
		}


		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.dispose();

		return resizedImage;
	}
	
	@Override	
	public java.io.File getTempFile(String suffix) throws Exception{
		
		String basePath = propertyController.getBaseFilePath();
		
		if(basePath == null)
			throw new Exception("Keine Erlaubniss für den Zugriff auf Dateisystem.");
		
        java.io.File f = java.io.File.createTempFile("tmp", suffix, new java.io.File(basePath));
        
        return f;
	}

	private String getAbsoluteFilePath(String relativeFilePath){

		String basePath = propertyController.getBaseFilePath();
		
		if(basePath == null)
			return null;
		
		if(! basePath.endsWith("/"))
			basePath += "/";

		return basePath + relativeFilePath;
	}

	private String getFreeRelativeFilePath(String extension){

		// generate BaseFolder
		String baseFolder = UUID.randomUUID().toString().substring(0, 5);
		String relativeFilePath;
		java.io.File targetFile, baseFolderFile;

		baseFolderFile = new java.io.File(getAbsoluteFilePath(baseFolder));
		baseFolderFile.mkdirs();

		while(true){

			relativeFilePath = baseFolder + java.io.File.separator + UUID.randomUUID().toString() +"."+ extension;
			targetFile = new java.io.File(getAbsoluteFilePath(relativeFilePath));

			if(!targetFile.exists())
				break;

		}

		return relativeFilePath;
	}

	private String getExtensionByFilename(String fileName){
		String[] splits = fileName.split("\\.");

		if(splits.length < 2)
			return "file";

		return splits[splits.length -1];
	}

	private String getTitleByFilename(String fileName){

		return fileName.replace("."+getExtensionByFilename(fileName), "");
	}

	@Override
	public FileSystemFile saveFileSystemFile(InputStream is, String filename, String mimeType, long size) {
		String relativeFilePath = getFreeRelativeFilePath(getExtensionByFilename(filename));
		return saveFileSystemFile(is, filename, relativeFilePath, mimeType, size);
	}

	@Override
	public FileSystemFile saveInvoice(java.io.File tmpFile, String invoiceNumber) throws IOException {
		
		String filename = invoiceNumber +".pdf";
		
		String mimeType = "application/pdf";
				
		// generate BaseFolder
		String baseFolder = "invoice";
		String relativeFilePath;
		java.io.File baseFolderFile, systemFile;

		baseFolderFile = new java.io.File(getAbsoluteFilePath(baseFolder));
		baseFolderFile.mkdirs();

		relativeFilePath = baseFolder + java.io.File.separator + filename;
		systemFile = new java.io.File(getAbsoluteFilePath(relativeFilePath));
		
		Files.copy(tmpFile.toPath(), systemFile.toPath(), REPLACE_EXISTING);
		
		FileSystemFileEntity file = new FileSystemFileEntity();
		initFile(file);
		file.setTitle(String.valueOf(invoiceNumber));
		file.setRelativeFilePath(relativeFilePath);
		file.setFilename(filename);
		file.setMimeType(mimeType);
		file.setSize(tmpFile.length());

		generateThumbnail(file);
		file = (FileSystemFileEntity) eao.saveFile(file);

		return file;
	}
	
	/*
	 * 

	@Override
	public FileSystemFile saveInvoice(PDDocument doc, int invoiceNumber) throws IOException {

		String filename = String.valueOf(invoiceNumber) +".pdf";
		
		String mimeType = "application/pdf";
				
		// generate BaseFolder
		String baseFolder = "invoice";
		String relativeFilePath;
		java.io.File baseFolderFile, systemFile;

		baseFolderFile = new java.io.File(getAbsoluteFilePath(baseFolder));
		baseFolderFile.mkdirs();

		relativeFilePath = baseFolder + java.io.File.separator + filename;
		systemFile = new java.io.File(getAbsoluteFilePath(relativeFilePath));
		
		doc.save(systemFile);
		
		FileSystemFileEntity file = new FileSystemFileEntity();
		initFile(file);
		file.setTitle(String.valueOf(invoiceNumber));
		file.setRelativeFilePath(relativeFilePath);
		file.setFilename(filename);
		file.setMimeType(mimeType);
		file.setSize(systemFile.length());

		generateThumbnail(file);
		file = (FileSystemFileEntity) eao.saveFile(file);

		return file;
	}
	 */

	private FileSystemFile saveFileSystemFile(InputStream is, String filename, String relativeFilePath, String mimeType, long size) {

		if(is == null || filename == null || mimeType == null || size == 0){
			System.out.println("Error while save new Filee to File System: "+filename+ " "+mimeType);
			return null;
		}


		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		FileSystemFileEntity file = null;
//		String relativeFilePath = null;
		java.io.File targetFile = null;

		try {

			file = new FileSystemFileEntity();
			initFile(file);
			file.setTitle(getTitleByFilename(filename));

//			relativeFilePath = getFreeRelativeFilePath(getExtensionByFilename(filename));

			targetFile = new java.io.File(getAbsoluteFilePath(relativeFilePath));

			// if file doesnt exists, then create it
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}

			bis = new BufferedInputStream( is);
			fos = new FileOutputStream(targetFile);
			bos = new BufferedOutputStream(fos);

			int b;
			while ((b = bis.read()) != -1) {
				bos.write(b);
			}
			bos.flush();

			file.setRelativeFilePath(relativeFilePath);
			file.setFilename(filename);
			file.setMimeType(mimeType);
			file.setSize(size);


			generateThumbnail(file);
			file = (FileSystemFileEntity) eao.saveFile(file);

		}
		catch(IOException ex) {

			System.err.println(ex.getMessage());
			ex.printStackTrace();
			file = null;
		}

		finally {

			try{
				if(bis!=null) bis.close();
				if(fos!=null) fos.close();
				if(bos!=null) bos.close();
			}
			catch(IOException ex) {
			}

		}


		return file;

	}

	@Override
	public List<? extends FileActivity> getOwnerFileList(ActivityOwner owner, Base fileRelation, boolean onlyVisible) {
		return eao.getOwnerFileList(owner, fileRelation, onlyVisible);
	}

	@Override
	public long getFileConsumption(){
		return eao.getFileConsumption();
	}

	@Override
	public long getStorageLimit(){
		return propertyController.getStorageLimit();
	}

	@Override
	public void deleteFileActivity(FileActivity fileActivity) {
		if(fileActivity.getFile() instanceof FileSystemFile){
			FileSystemFileEntity fsFile = (FileSystemFileEntity) fileActivity.getFile();


    		java.io.File localFile = new java.io.File(getAbsoluteFilePath(fsFile.getRelativeFilePath()));

    		if(!localFile.exists()){
    			System.out.println(localFile.getName() + " not existing in Filesystem!");
    			deleteFileActivityEntity((FileActivityEntity) fileActivity);
    		}else if(localFile.delete()){
    			System.out.println(localFile.getName() + " is deleted!");
    			deleteFileActivityEntity((FileActivityEntity) fileActivity);

    		}else{
    			System.out.println("Delete operation is failed.");
    		}

		}
	}

	@Override
	public DBFile getDBFileEntity(long id) {
		return eao.getDBFile(id);
	}

	private void deleteDBFileEntity(DBFileEntity file) {
		eao.deleteDBFile(file);
	}

	private void deleteFileSystemFileEntity(FileSystemFileEntity file) {
		eao.deleteFileSystemFile(file);
	}

	private void deleteFileActivityEntity(FileActivityEntity fileActivity) {
		eao.deleteFileActivityEntity(fileActivity);
	}
}
