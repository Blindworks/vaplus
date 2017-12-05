package de.vaplus.client.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Theming {

public static String theme(String template, Map params) {
        
        // setup a response catcher
        FacesContext faces = FacesContext.getCurrentInstance();
        ExternalContext context = faces.getExternalContext();
        ServletRequest request = (ServletRequest) faces.getExternalContext().
                getRequest();
        HttpServletResponse response = (HttpServletResponse) 
                context.getResponse();
        ResponseCatcher catcher = new ResponseCatcher(response);
        
        // hack the request state
        UIViewRoot oldView = faces.getViewRoot();
        Map oldAttributes = null;
        if (params != null) {
            oldAttributes = new HashMap(params.size() * 2); // with buffer
            for (String key : (Set<String>) params.keySet()) {
                oldAttributes.put(key, request.getAttribute(key));
                request.setAttribute(key, params.get(key));
            }
        }
        request.setAttribute("emailClient", true);
        context.setResponse(catcher);
        
        try {
            // build a JSF view for the template and render it
            ViewHandler views = faces.getApplication().getViewHandler();
            UIViewRoot view = views.createView(faces, template);
            faces.setViewRoot(view);
            views.getViewDeclarationLanguage(faces, template).
                    buildView(faces, view);
            views.renderView(faces, view);
        } catch (IOException ioe) {
            String msg = "Failed to render " + template;
            faces.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, msg, msg));
            return null;
        } finally {
        
            // restore the request state
            if (oldAttributes != null) {
                for (String key : (Set<String>) oldAttributes.keySet()) {
                    request.setAttribute(key, oldAttributes.get(key));
                }
            }
            request.setAttribute("emailClient", null);
            context.setResponse(response);
            faces.setViewRoot(oldView);
        }
        return catcher.toString();
    }
	
}
