package de.vaplus.client.converter;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import com.sun.faces.util.MessageFactory;

public class NamedDateTimeConverter extends DateTimeConverter{
	
	private Date getMidnightDate(Date date){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		return cal.getTime();
	}
	
	private long getWeeksDiff(Object value){

		Calendar cal = Calendar.getInstance();
		
		cal.setTime((Date) value);
		cal.set(Calendar.DAY_OF_WEEK,1);
		
		Date valueDate = getMidnightDate(cal.getTime());
		
		long diff = 0;
		cal.setTime(getMidnightDate(new Date()));
		cal.set(Calendar.DAY_OF_WEEK,1);
		
		while(true){
			
			if(cal.getTime().compareTo(valueDate) == 0){
				return diff;
			}
			
			diff++;
			cal.add(Calendar.WEEK_OF_YEAR,-1);
			
			
			if(diff > 5)
				return 100;
			
		}
		
	}
	
	private long getMonthDiff(Object value){

		Calendar cal = Calendar.getInstance();
		
		cal.setTime((Date) value);
		cal.set(Calendar.DAY_OF_MONTH,1);
		
		Date valueDate = getMidnightDate(cal.getTime());
		
		long diff = 0;
		cal.setTime(getMidnightDate(new Date()));
		cal.set(Calendar.DAY_OF_MONTH,1);
		
		while(true){
			
			if(cal.getTime().compareTo(valueDate) == 0){
				return diff;
			}
			
			diff++;
			cal.add(Calendar.MONTH,-1);
			
			
			if(diff > 12)
				return 100;
			
		}
	}
	
	private long getYearsDiff(Object value){

		Calendar cal = Calendar.getInstance();
		
		cal.setTime((Date) value);
		cal.set(Calendar.DAY_OF_YEAR,1);
		
		Date valueDate = getMidnightDate(cal.getTime());
		
		long diff = 0;
		cal.setTime(getMidnightDate(new Date()));
		cal.set(Calendar.DAY_OF_YEAR,1);
		
		while(true){
			
			if(cal.getTime().compareTo(valueDate) == 0){
				return diff;
			}
			
			diff++;
			cal.add(Calendar.YEAR,-1);
			
			
			if(diff > 12)
				return 100;
			
		}
	}

	private long getDiffDays(Object value){

		Date today = getMidnightDate(new Date());
		Date valueDate = getMidnightDate((Date) value);
		
		long diffInMillies = today.getTime() - valueDate.getTime();
		
		
		return TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	private long getDiffHours(Object value){

		Date today = new Date();
		
		long diffInMillies = today.getTime() - ((Date)value).getTime();
		
		
		return TimeUnit.HOURS.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	private long getDiffMinutes(Object value){

		Date today = new Date();
		
		long diffInMillies = today.getTime() - ((Date)value).getTime();
		
		
		return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component,
            Object value){
		
        if (context == null || component == null) {
            throw new NullPointerException();
        }

        try {

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
            long weeks = getWeeksDiff(value);
            long month = getMonthDiff(value);
            long years = getYearsDiff(value);

            
            if(years > 0){
            	if(years == 1){
        			return "letzes Jahr";
            	}
            	else if(years == 2){
        			return "vorletzes Jahr";
            	}
            	else{
        			return "vor "+years+" Jahren";
            	}
            }
            else if(month > 0){
            	if(month == 1){
        			return "letzen Monat";
            	}
            	else if(month == 2){
        			return "vorletzen Monat";
            	}
            	else{
        			return "vor "+month+" Monaten";
            	}
            }
            else if(weeks > 0){
            	if(weeks == 1){
        			return "letzte Woche";
            	}
            	else if(weeks == 2){
        			return "vorletzte Woche";
            	}
            	else{
        			return "vor "+weeks+" Wochen";
            	}
            }
            else if(days == 0){
            	long hours = getDiffHours(value);
            	if(hours == 0){
                	long minutes = getDiffMinutes(value);
                	if(minutes < 2){
            			return "vor 1 Minute";
                	}else{
            			return "vor "+minutes+" Minuten";
                	}
            		
            		
            	}else if(hours == 1){
        			return "vor 1 Stunde";
            	}else{
        			return "vor "+hours+" Stunden";
            	}
            }
            else if(days == 1){
    			return "Gestern";
            }
            else if(days == 2){
    			return "Vorgestern";
            }
            else{
    			return "vor "+days+" Tagen";
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
