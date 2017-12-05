package de.vaplus.client.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.picketlink.http.AuthenticationRequiredException;

import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.MailControllerInterface;
import de.vaplus.api.PropertyControllerInterface;

public class SupportExceptionHandler extends ExceptionHandlerWrapper {
	 
    private ExceptionHandler wrapped;
 
    public SupportExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }
 
    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
    	
//    	System.out.println("HANDLE Exception GROUP");
    	
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            

        	System.out.println("GOT Exception!!! ("+t.getMessage()+")");
            
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nav = fc.getApplication().getNavigationHandler();
            
            if (t instanceof ViewExpiredException || t instanceof AuthenticationRequiredException) {
                try {
                    nav.handleNavigation(fc, null, "/login");
                    fc.renderResponse();
 
                } finally {
                    i.remove();
                }
            } else if(t.getMessage().contains("Session not found")){
            	// Skip Exception
            	
            } else{

            	
            	try {
            		
            		if(!fc.getExternalContext().getRequestServerName().equalsIgnoreCase("localhost")){
            		
            			ExceptionControllerInterface exceptionController = (ExceptionControllerInterface) InitialContext.doLookup("java:app/VAPlus_Client-ejb/ExceptionController");

            			if(! fc.getExternalContext().getRequestServerName().equalsIgnoreCase("TEST LIZENZ"))
            				exceptionController.logException(t, "Host: "+fc.getExternalContext().getRequestServerName()+"<br/>ViewId: "+fc.getViewRoot().getViewId()+"<br/>");
	            		
//	            		MailControllerInterface mailController = (MailControllerInterface) InitialContext.doLookup("java:app/VAPlus_Client-ejb/MailController");
//	            		
//	            		PropertyControllerInterface propertyController = (PropertyControllerInterface) InitialContext.doLookup("java:app/VAPlus_Client-ejb/PropertyController");
//	            		
//	            		StringWriter sw = new StringWriter();
//	            		PrintWriter pw = new PrintWriter(sw);
//	
//	            		pw.println("Tenant: "+propertyController.getLizenseName()+"<br/>");
//	            		pw.println("Host: "+fc.getExternalContext().getRequestServerName()+"<br/>");
//	            		pw.println("Date: "+new Date()+"<br/>");
//	            		pw.println("Exception: "+t.getClass().getSimpleName()+"<br/>");
//	            		pw.println("ViewId: "+fc.getViewRoot().getViewId()+"<br/>");
//	
//	            		pw.println("<br/><br/><br/>");
//	            		pw.println("Stacktrace: <br/>");
//	            		t.printStackTrace(pw);
//	            		
//	            		
//	            		mailController.sendMailToSupport(t.getClass().getSimpleName()+" in Host "+propertyController.getLizenseName(), sw.toString());
            		}
					
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
//                    i.remove();
                }
            	
                nav.handleNavigation(fc, null, "/error/500");
                fc.renderResponse();
            }
            
        }
        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
 
    }
}
