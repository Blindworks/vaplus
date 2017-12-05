package de.vaplus.client.calendar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

/**
 * Servlet implementation class fullCalendarSocket
 */
@WebServlet("/sockets/calendar/fullCalendar.json")
public class fullCalendarSocket extends HttpServlet {
	private static final long serialVersionUID = 1L;


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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fullCalendarSocket() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("#####"); 
		
		long debugDuration = (new Date()).getTime();
		
		response.setContentType("application/json");
        final JsonGenerator generator = Json.createGenerator(response.getWriter());

		DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		
//		Enumeration<String> en = request.getParameterNames();
//		while(en.hasMoreElements()){
//			String param = en.nextElement();
//			System.out.println(param+" : "+request.getParameter(param));
//		}
		

//		System.out.println("CAL A: " + ((new Date()).getTime() - debugDuration) ); 
		
		Date effectiveDate,expiryDate;
		User user = null;
		Shop shop = null;

        String startTime = request.getParameter("start");
        String endTime = request.getParameter("end");
        String userUUID = request.getParameter("user");
        String shopUUID = request.getParameter("shop");
        String holiday = request.getParameter("holiday");

        if(startTime == null){
        	response.setStatus(400);
        	return;
        }
        if(endTime == null){
        	response.setStatus(400);
        	return;
        }
        
        if(userUUID == null && shopUUID == null){
        	response.setStatus(400);
        	return;
        }

        try {
			effectiveDate = inputFormatter.parse(startTime);
	        expiryDate = inputFormatter.parse(endTime);
		} catch (ParseException e) {
        	response.setStatus(400);
        	return;
		}

//		System.out.println("CAL B: " + ((new Date()).getTime() - debugDuration) ); 
		
        if(userUUID != null){
	        user = userController.getUserByUUID(userUUID);

        }

//		System.out.println("CAL C: " + ((new Date()).getTime() - debugDuration) ); 
		
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
			        	response.setStatus(400);
			        	return;
			        }	    
	        		eventList = holidayController.getHolidayEvents(shop.getState());
        		}
        		
        	}else if(shopUUID.equalsIgnoreCase(propertyController.getStringProperty("yearViewCalendarId", UUID.randomUUID().toString()))){
        		// External Calendar    shop = null
    	        if(user == null){
    	        	response.setStatus(400);
    	        	return;
    	        }
        		eventList = eventController.getYearViewEvents(effectiveDate, expiryDate, user);
        		
        	}else if(shopUUID.equalsIgnoreCase(propertyController.getStringProperty("externalCalendarId", UUID.randomUUID().toString()))){
        		// External Calendar    shop = null
    	        if(user == null){
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
		        	response.setStatus(400);
		        	return;
		        }	        
		        if(user == null){
		        	response.setStatus(400);
		        	return;
		        }
		        
		        eventList = eventController.getEvents(effectiveDate, expiryDate, user, shop);
        	}
        }
        

//		System.out.println("CAL F: " + ((new Date()).getTime() - debugDuration) ); 
        try{

	        generator.writeStartArray();
	        Calendar c = Calendar.getInstance();
	
	        
	        Iterator<? extends Event> i = eventList.iterator();
	        Event event;
	        while(i.hasNext()){
	        	event = i.next();
	        	
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
	
	        	if(event.getUser() != null)
	        		generator.write("user", event.getUser().getName());
	        	
		        if(event.getShop() != null)
		        	generator.write("shop", event.getShop().getName());
		        else{
		        	generator.write("shop", "außer Haus");
		        }
		        
			    generator.write("allDay", event.isAllDay());
			    
		        generator.write("start", outputFormatter.format( event.getEffectiveDate() ));
		        
		        
		        if(holiday != null && holiday.equalsIgnoreCase("true")){
		        	generator.write("holiday", true);
		        }
		        
		        generator.write("end", outputFormatter.format( c.getTime() ));
		        
		        
		        generator.writeEnd();
	        	
	        }
	
	        generator.writeEnd();
	        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	generator.close();
        }
	
//		System.out.println("CAL G: " + ((new Date()).getTime() - debugDuration) ); 
	        

//		System.out.println("#####"); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
