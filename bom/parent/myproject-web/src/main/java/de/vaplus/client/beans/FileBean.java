package de.vaplus.client.beans;

//import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.ActivityControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.client.applicationbeans.ResourceBean;
import de.vaplus.client.backingbeans.UserGroupBean;

@ManagedBean(name="fileBean")
@SessionScoped
public class FileBean implements Serializable {

	private static final long serialVersionUID = -8451438196751285549L;

	@EJB
	private FileControllerInterface fileController;

	@EJB
	private ActivityControllerInterface activityController;

	@EJB
	private ResourceBean resourceBean;
	
	@Inject
	private FacesContext facesContext;

	private File file;

	private ActivityOwner selectedFileOwner;

	private Base selectedFileRelation;

	private List<Part> importFiles;
	
	private List<FileActivity> edittingFileActivity;

    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

    @ManagedProperty(value="#{permissionBean}")
    private PermissionBean permissionBean;

	public List<? extends FileActivity> getOwnerFileList(ActivityOwner owner, Base selectedFileRelation){
		boolean onlyVisible = ! permissionBean.hasPermission("file", "read_all");;
		return fileController.getOwnerFileList(owner, selectedFileRelation, onlyVisible);
	}

	public List<Part> getImportFiles() {
		return importFiles;
	}

	public void setImportFiles(List<Part> importFiles) {
		this.importFiles = importFiles;
	}

	public ActivityOwner getSelectedFileOwner() {
		return selectedFileOwner;
	}

	public void setSelectedFileOwner(ActivityOwner selectedFileOwner) {
//		System.out.println("setSelectedFileOwner: "+selectedFileOwner);
		this.selectedFileOwner = selectedFileOwner;
		setSelectedFileRelation(null);
	}
	
	public boolean isStorageLimitExceeded(){
		
		if(this.getFileConsumption() > this.getStorageLimit())
			return true;
		return false;
	}

	public void proceedImport(){
//		System.out.println("proceedImport");


		Iterator<Part> i = importFiles.iterator();
		Part p;
		while(i.hasNext()){
			p = i.next();

//			System.out.println("Next File: "+p.getContentType()+" "+p.getSize());

			if(p.getSize() == 0)
				continue;

			if(p.getSize() > 1024 * 1024 * 10){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dateigröße über 10MB!", null));
				
				System.out.println("Dateigröße über 10MB!");
				continue;
			}

			if(! p.getContentType().equalsIgnoreCase("image/jpeg")
					&& ! p.getContentType().equalsIgnoreCase("image/png")
					&& ! p.getContentType().equalsIgnoreCase("application/pdf")
					&& ! p.getContentType().equalsIgnoreCase("application/msword")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.ms-word.document.macroEnabled.12")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.ms-excel")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.ms-excel.sheet.macroEnabled.12")
					&& ! p.getContentType().equalsIgnoreCase("application/vnd.ms-excel.sheet.binary.macroEnabled.12")){

				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falscher Dateityp!", null));
				System.out.println("Import Fehler: Falscher Dateityp!");
				continue;
			}

			try {


				activityController.createFileActivity(userBean.getActiveUser(), selectedFileOwner, selectedFileRelation, p.getInputStream(), p.getSubmittedFileName(), p.getContentType(), p.getSize());

				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Datei "+p.getSubmittedFileName()+" erfolgreich importiert", null));

			} catch (Exception e) {

				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage(), null));
				System.out.println("Import Fehler bei Datei: "+p.getSubmittedFileName()+" Fehler:"+e.getMessage());
			}


		}


		resourceBean.updateFileConsumption();
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public PermissionBean getPermissionBean() {
		return permissionBean;
	}

	public void setPermissionBean(PermissionBean permissionBean) {
		this.permissionBean = permissionBean;
	}

	public long getFileConsumption(){
		return resourceBean.getFileConsumption();
	}

	public long getStorageLimit(){
		return fileController.getStorageLimit();
	}

	public void deleteFileActivity(FileActivity fileActivity) {
		fileController.deleteFileActivity(fileActivity);
	}

	public Object getSelectedFileRelation() {
		return selectedFileRelation;
	}

	public void setSelectedFileRelation(Base selectedFileRelation) {
//		System.out.println("setSelectedFileRelation: "+selectedFileRelation);
		this.selectedFileRelation = selectedFileRelation;
	}
	
	private List<FileActivity> getEdittingFileActivity(){
		if(edittingFileActivity == null)
			edittingFileActivity = new LinkedList<FileActivity>();
		return edittingFileActivity;
	}

	public boolean isFileActivityEditting(FileActivity fileActivity) {
		return getEdittingFileActivity().contains(fileActivity);
	}

	public void setFileActivityEditting(FileActivity fileActivity) {
		getEdittingFileActivity().add(fileActivity);
	}

	public void saveFileActivityEditting(FileActivity fileActivity) {
		getEdittingFileActivity().remove(fileActivity);
		
//		System.out.println("fileActivity: "+fileActivity.getFile().getTitle());
		
		activityController.saveFileActivity(fileActivity);
	}
	
}
