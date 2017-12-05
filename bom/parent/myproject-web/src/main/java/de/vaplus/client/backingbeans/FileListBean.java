package de.vaplus.client.backingbeans;

//import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.ActivityControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;

@ManagedBean(name="fileListBean")
@ViewScoped
public class FileListBean implements Serializable {

	private static final long serialVersionUID = 712835083056990980L;

	@EJB
	private FileControllerInterface fileController;

	@EJB
	private ActivityControllerInterface activityController;

	@Inject
	private FacesContext facesContext;
	
	private FileActivity selectedFileActivity;

	public boolean showEditButton(FileActivity fileActivity){
		if(selectedFileActivity == null)
			return true;
		else 
			return false;
	}

	public boolean showSaveButton(FileActivity fileActivity){
		if(selectedFileActivity == null)
			return false;
		
		return selectedFileActivity.equals(fileActivity);
	}

	public boolean showForm(FileActivity fileActivity){
		if(selectedFileActivity == null)
			return false;
		
		return selectedFileActivity.equals(fileActivity);
	}

	public FileActivity getSelectedFileActivity() {
		return selectedFileActivity;
	}

	public void setSelectedFileActivity(FileActivity selectedFileActivity) {
		this.selectedFileActivity = selectedFileActivity;
	}

	public void saveSelectedFileActivity() {
		
		if(selectedFileActivity == null)
			return;
		
		activityController.saveFileActivity(selectedFileActivity);
		
		selectedFileActivity = null;
	}
	
}
