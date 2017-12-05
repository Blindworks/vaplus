package de.vaplus.client.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import com.sun.faces.util.MessageFactory;

public class MinimalDateTimeConverter extends DateTimeConverter{
	
	private Date getMidnightDate(Date date){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		return cal.getTime();
	}

	private long getDiffDays(Object value){

		Date today = getMidnightDate(new Date());
		Date valueDate = getMidnightDate((Date) value);
		
		long diffInMillies = today.getTime() - valueDate.getTime();
		
		
		return TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component,
            Object value){
		
        if (context == null || component == null) {
            throw new NullPointerException();
        }

        try {
        	
        	DateFormat timeFormat = new SimpleDateFormat( "HH:mm" );
        	DateFormat dateFormat = new SimpleDateFormat( "dd.MM.yy" );

            // If the specified value is null, return a zero-length String
            if (value == null) {
                return "";
            }

            // If the incoming value is still a string, play nice
            // and return the value unmodified
            if (value instanceof String) {
                return (String) value;
            }

            long days = getDiffDays(value);
            
            if(days == 0){
            	return timeFormat.format((Date) value);
            }else{
            	return dateFormat.format((Date) value);
            }

            

        } catch (ConverterException e) {
            throw new ConverterException(MessageFactory.getMessage(
                 context, STRING_ID, value,
                 MessageFactory.getLabel(context, component)), e);
        } catch (Exception e) {
            throw new ConverterException(MessageFactory.getMessage(
                 context, STRING_ID, value,
                 MessageFactory.getLabel(context, component)), e);
        }

	}
}
