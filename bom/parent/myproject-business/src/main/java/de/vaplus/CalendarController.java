package de.vaplus;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.util.UidGenerator;
import de.vaplus.api.CalendarControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.entity.EventEntity;

@Stateless
public class CalendarController implements CalendarControllerInterface {
	
	private static final long serialVersionUID = -2443637984492596945L;
	
	private enum View {USER,SHOP}

	@EJB
	private EventControllerInterface eventController;

	public CalendarController(){
		// System.out.println("### INIT HolidayController ###");
	}
	
	@Override
	public void printUserCalendar(Writer out, User user) throws Exception{
		List<? extends Event> eventList = eventController.getAllEvents(user);
		
		printCalendar(out, View.USER, eventList);
	}
	
	@Override
	public void printShopCalendar(Writer out, Shop shop) throws Exception{
		List<? extends Event> eventList = eventController.getAllEvents(shop);
		
		printCalendar(out, View.SHOP, eventList);
	}
	
	private void printCalendar(Writer out, View view, List<? extends Event> eventList) throws Exception{
		
		
		// Init Calendar
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//User Calendar XYZ//VAPlus//DE"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		// Init Timezone
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("Europe/Berlin");
		VTimeZone tz = timezone.getVTimeZone();
		
		
        Iterator<? extends Event> i = eventList.iterator();
        Event event;
        DateTime dtSTART = null, dtEND = null;
        VEvent vEvent;
        String title = null;
        
        while(i.hasNext()){
        	event = i.next();
        	
        	switch(view){
	        	case USER:
	        		if(event.getShop() == null)
	        			title = event.getEventType().getTitle();
	        		else
        				title = event.getShop().toString();
	        		break;
	        	case SHOP:
	        		title = event.getUser().toString();
	        		break;
        	}
        	
//        	if(event.isAllDay()){
//        		if(event.isSingleDay()){
//        			
//                	dtSTART = new DateTime(event.getEffectiveDate());
//                	dtSTART.setTimeZone(timezone);
//                	
//                	vEvent = new VEvent(dtSTART, title);
//                	
//                	vEvent.getProperties().add(new Uid(event.getUuid()));
//                	vEvent.getProperties().add(tz.getTimeZoneId());
//                	
//            		calendar.getComponents().add(vEvent);
//        			
//        		}else{
//        			System.out.println("Multi All Day");
//        		}
//        	}else{
//            	dtSTART = new DateTime(event.getEffectiveDate());
//            	dtSTART.setTimeZone(timezone);
//            	
//            	dtEND = new DateTime(event.getExpiryDate());
//            	dtEND.setTimeZone(timezone);
//            	
//            	vEvent = new VEvent(dtSTART, dtEND, title);
//            	
//            	vEvent.getProperties().add(new Uid(event.getUuid()));
//            	vEvent.getProperties().add(tz.getTimeZoneId());
//            	
//        		calendar.getComponents().add(vEvent);
//        	}
        	
        	dtSTART = new DateTime(event.getEffectiveDate());
        	dtSTART.setTimeZone(timezone);
        	
        	dtEND = new DateTime(event.getExpiryDate());
        	dtEND.setTimeZone(timezone);
        	
        	vEvent = new VEvent(dtSTART, dtEND, title);
        	
        	vEvent.getProperties().add(new Uid(event.getUuid()));
        	vEvent.getProperties().add(tz.getTimeZoneId());
        	
    		calendar.getComponents().add(vEvent);

        }
		
		CalendarOutputter outputter = new CalendarOutputter();
		
		outputter.output(calendar, out);
	}
	
}
