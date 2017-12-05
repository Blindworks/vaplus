package de.vaplus.client.calendar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.beans.HelperBean;

/**
 * Servlet implementation class fullCalendarSocket
 */
@WebServlet("/sockets/calendar/fullCalendar3.json")
public class fullCalendar3Socket extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private enum ResponseType {
	    USER, SHOP, HOLIDAY
	} 

	@EJB
	private UserControllerInterface userController;
	
	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private EventControllerInterface eventController;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private HolidayControllerInterface holidayController;
       
	private ResponseType responseType;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fullCalendar3Socket() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("##### fullCalendar3Socket doGet ####"); 
		
		response.setContentType("application/json");
        final JsonGenerator generator = Json.createGenerator(response.getWriter());

		DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
		
//		Enumeration<String> en = request.getParameterNames();
//		while(en.hasMoreElements()){
//			String param = en.nextElement();
//			System.out.println(param+" : "+request.getParameter(param));
//		}
		

		Date effectiveDate,expiryDate;
		User user = null;
		Shop shop = null;

        String startTime = request.getParameter("start");
        String endTime = request.getParameter("end");
        String userUUID = request.getParameter("user");
        String shopUUID = request.getParameter("shop");
        String holiday = request.getParameter("holiday");

        
        if(startTime == null){
//    		System.out.println("ERROR - startTime"); 
        	response.setStatus(400);
        	return;
        }
        if(endTime == null){
//    		System.out.println("ERROR - endTime"); 
        	response.setStatus(400);
        	return;
        }
        
        if(userUUID == null && shopUUID == null){
//    		System.out.println("ERROR - NO Entity UUID"); 
        	response.setStatus(400);
        	return;
        }

        try {
			effectiveDate = inputFormatter.parse(startTime);
	        expiryDate = inputFormatter.parse(endTime);
		} catch (ParseException e) {
//    		System.out.println("Time Parse Exception: "+e.getMessage()); 
        	response.setStatus(400);
        	return;
		}
        

        List<? extends Event> eventList = new ArrayList<Event>();
        List<? extends Event> holidayList = new ArrayList<Event>();
        
        if(shopUUID != null){
        	
        	responseType = ResponseType.SHOP;
        	
        	shop = shopController.getShopByUUID(shopUUID);
        	
        	if(shop != null){
        		
//        		System.out.println("get Events by Shop: "+shop.getName());
        		
        		// get all shop events
        		eventList = eventController.getEvents(effectiveDate, expiryDate, shop);
        		
        		holidayList = holidayController.getHolidayEvents(effectiveDate, expiryDate, shop.getState());
        		
//        		System.out.println("got "+eventList.size()+" Events");
        	}
        }

        
        /*
        
        if(userUUID != null){
	        user = userController.getUserByUUID(userUUID);
        }

		
        List<? extends Event> eventList = null;
        
        if(shopUUID != null){
        	
        	if(holiday != null && holiday.equalsIgnoreCase("true")){
        		
        		if(shopUUID.equalsIgnoreCase(propertyController.getStringProperty("externalCalendarId", UUID.randomUUID().toString()))){
        			// return no holiday, cause no regional holiday possible
        			eventList = new ArrayList<Event>();
        		}else if(shopUUID.equalsIgnoreCase("0")){
        			// return user holidays
        			if(user == null)
            			eventList = new ArrayList<Event>();
        			else
    	        		eventList = holidayController.getHolidayEvents(user.getState());
        			
        		}else if(shopUUID.equalsIgnoreCase(propertyController.getStringProperty("yearViewCalendarId", UUID.randomUUID().toString()))){
        			// return no holiday, cause no regional holiday possible
        			eventList = holidayController.getAllHolidayEvents();
        			
        		}else{
	        		shop = shopController.getShopByUUID(shopUUID);
	        		if(shop == null){
	            		System.out.println("shopUUID SET | ERROR: no shop found"); 
			        	response.setStatus(400);
			        	return;
			        }	    
	        		eventList = holidayController.getHolidayEvents(shop.getState());
        		}
        		
        	}else if(shopUUID.equalsIgnoreCase(propertyController.getStringProperty("yearViewCalendarId", UUID.randomUUID().toString()))){
        		// YEAR View Calendar    shop = null
    	        if(user == null){
            		System.out.println("shopUUID SET | ERROR: no user found"); 
    	        	response.setStatus(400);
    	        	return;
    	        }
        		eventList = eventController.getYearViewEvents(effectiveDate, expiryDate, user);
        		
        	}else if(shopUUID.equalsIgnoreCase(propertyController.getStringProperty("externalCalendarId", UUID.randomUUID().toString()))){
        		// External Calendar    shop = null
    	        if(user == null){
            		System.out.println("shopUUID SET | ERROR: no user found"); 
    	        	response.setStatus(400);
    	        	return;
    	        }
        		shop = shopController.factoryNewShop();
        		eventList = eventController.getEvents(effectiveDate, expiryDate, user, shop);
        	}else{

//        		System.out.println("CAL D: " + ((new Date()).getTime() - debugDuration) ); 
		        shop = shopController.getShopByUUID(shopUUID);

//				System.out.println("CAL E: " + ((new Date()).getTime() - debugDuration) ); 
		        
		        if(shop == null){
            		System.out.println("shopUUID SET | ERROR: no shop found"); 
		        	response.setStatus(400);
		        	return;
		        }	        
		        if(user == null){
            		System.out.println("shopUUID SET | ERROR: no user found"); 
		        	response.setStatus(400);
		        	return;
		        }
		        
		        eventList = eventController.getEvents(effectiveDate, expiryDate, user, shop);
        	}
        }
        
        */

		
        try{

	        generator.writeStartArray();
	        Event event;
	        Iterator<? extends Event> i;
	        
	        i = eventList.iterator();
	        while(i.hasNext()){
	        	event = i.next();
	        	generateJSONEvent(generator, event, responseType);
	        }
	        
	        i = holidayList.iterator();
	        while(i.hasNext()){
	        	event = i.next();
	        	generateJSONEvent(generator, event, ResponseType.HOLIDAY);
	        }
	
	        generator.writeEnd();
	        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	generator.close();
        }
	
	}
	
	private void generateJSONEvent(JsonGenerator generator, Event event, ResponseType responseType){
		DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Calendar c = Calendar.getInstance();
		HelperBean helperBean = new HelperBean();

    	generator.writeStartObject();
        generator.write("id", event.getUuid());
        
        c.setTime(event.getExpiryDate());

        if(event.getEventType() != null){
        	generator.write("title", event.getEventType().getTitle());
        	if(event.getEventType().getShortName().equalsIgnoreCase("U") || event.getEventType().getShortName().equalsIgnoreCase("EZ")){
	        	generator.write("readonly", true);
        	}
	        	
        	if(event.isAllDay())
        		c.add(Calendar.SECOND, 1);
        		
        }
        else
        	generator.write("title", event.getTitle());
        
        switch(responseType){
	    	case HOLIDAY:
	    		generator.write("holiday", true);
	    		break;
	    	case USER:
//	    		generator.write("color", helperBean.getCssColorAsHex(event.getShop().getColor()));
	    		generator.write("colorName", event.getShop().getColor());
	    		break;
	    	case SHOP:
//	    		generator.write("color", helperBean.getCssColorAsHex(event.getUser().getColor()));
	    		generator.write("colorName", event.getUser().getColor());
	    		break;
        }
        

        if(responseType != ResponseType.HOLIDAY){
	    	if(event.getUser() != null)
	    		generator.write("user", event.getUser().getName());
	    	
	        if(event.getShop() != null)
	        	generator.write("shop", event.getShop().getName());
	        else{
	        	generator.write("shop", "außer Haus");
	        }
        }
        
	    generator.write("allDay", event.isAllDay());
	    
        generator.write("start", outputFormatter.format( event.getEffectiveDate() ));
        
        
        generator.write("end", outputFormatter.format( c.getTime() ));

        
        generator.writeEnd();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
