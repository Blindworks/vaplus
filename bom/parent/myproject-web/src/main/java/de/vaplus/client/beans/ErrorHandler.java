package de.vaplus.client.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class ErrorHandler {

	public String getStatusCode(){
		
		String val = String.valueOf((Integer)FacesContext.getCurrentInstance().getExternalContext().
				getRequestMap().get("javax.servlet.error.status_code"));
		return val;
	}

	public String getMessage(){
		String val =  (String)FacesContext.getCurrentInstance().getExternalContext().
			getRequestMap().get("javax.servlet.error.message");
		return val;
	}

	public String getExceptionType(){
		Object obj = FacesContext.getCurrentInstance().getExternalContext().
			getRequestMap().get("javax.servlet.error.exception_type");
		
		if(obj != null)
			return obj.toString();

		return "";
	}

	public String getException(){
		Object obj = FacesContext.getCurrentInstance().getExternalContext().
			getRequestMap().get("javax.servlet.error.exception");
		
		if(obj != null)
			return obj.toString();

		return "";
	}

	public String getRequestURI(){
		Object obj = FacesContext.getCurrentInstance().getExternalContext().
				getRequestMap().get("javax.servlet.error.request_uri");
			
			if(obj != null)
				return obj.toString();

			return "";
	}

	public String getServletName(){
		Object obj = FacesContext.getCurrentInstance().getExternalContext().
				getRequestMap().get("javax.servlet.error.servlet_name");
			
			if(obj != null)
				return obj.toString();

			return "";
	}

}