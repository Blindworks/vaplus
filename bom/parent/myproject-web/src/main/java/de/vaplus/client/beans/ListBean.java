package de.vaplus.client.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean(name="listBean")
@SessionScoped
public class ListBean implements Serializable {
	
	private static final long serialVersionUID = -1290891978575261000L;

	private boolean listEditable;

	@Inject
	private FacesContext facesContext;
	
	public ListBean() {
		// TODO Auto-generated constructor stub
		
	}


	public boolean isListEditable() {
		return listEditable;
	}

	public void toggleListEditable() {
		listEditable = listEditable ? false : true;
	}
}
