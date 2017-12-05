package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.User;

@ManagedBean(name="stockManagementBean")
@SessionScoped
public class StockManagementBean implements Serializable {

	private static final long serialVersionUID = 7411088798140454140L;


	@EJB
	private StockControllerInterface stockController;
	
	
	@Inject
	private FacesContext facesContext;

	public void setNewDocImage(List<Part> files) throws IOException {

		if(files != null && files.size() > 0){
			Part file = files.get(0);

			if(!file.getContentType().equalsIgnoreCase("image/png") && !file.getContentType().equalsIgnoreCase("image/jpeg")){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nur png oder jpeg Bilder erlaubt!", null));
				return;
			}

			if(file.getSize() > 200*1024){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Max 200kb pro Bild erlaubt!", null));
				return;
			}
			
			stockController.updateDocImage(file.getInputStream(), file.getContentType(), file.getSize());
		}
	}

	public List<Part> getNewDocImage(){
		return null;
	}


	public DBFile getDocImage() {
		return  stockController.getDocImage();
	}
	
}
