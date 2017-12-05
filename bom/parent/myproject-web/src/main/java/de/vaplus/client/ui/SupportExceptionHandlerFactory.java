package de.vaplus.client.ui;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class SupportExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;
 
    public SupportExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }
	
	
	@Override
	public ExceptionHandler getExceptionHandler() {

        ExceptionHandler result = parent.getExceptionHandler();
        result = new SupportExceptionHandler(result);
 
        return result;
		
	}

}
