package de.vaplus.client.calendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.vaplus.api.CalendarControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

/**
 * Servlet implementation class fullCalendarSocket
 */
@WebServlet("/sockets/ical/*")
public class iCalendarSocket extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@EJB
	private UserControllerInterface userController;
	
	@EJB
	private ShopControllerInterface shopController;
	
	@EJB
	private CalendarControllerInterface calendarController;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public iCalendarSocket() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getPathInfo() == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		String[] pathParts = request.getPathInfo().split("/");
		
		if(pathParts == null || pathParts.length < 3){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			System.out.println("Path to short");
			return;
		}
		
		if(! pathParts[pathParts.length -1].equalsIgnoreCase("calendar.ics")){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			System.out.println("Path not ending with calendar.ics");
			return;
		}

		String type = pathParts[pathParts.length -3];
		String uuid = pathParts[pathParts.length -2];
		
		
		if(! type.equalsIgnoreCase("user") && ! type.equalsIgnoreCase("shop")){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			System.out.println("Path containing wrong type: "+type);
			return;
		}

		User user;
		Shop shop;

		PrintWriter pw = response.getWriter();
		response.setHeader("Content-Type", "text/calendar;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=calendar.ics");
		
		try{
			
			if(type.equalsIgnoreCase("user")){
				user = userController.getUserByUUID(uuid);
				
				// External Calendar    shop = null
		        if(user == null){
//					System.out.println("User by ID not found: "+uuid);
		        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		        	return;
		        }
		        calendarController.printUserCalendar(pw, user);
		        
			}else if(type.equalsIgnoreCase("shop")){
				shop = shopController.getShopByUUID(uuid);
				
				// External Calendar    shop = null
		        if(shop == null){
//					System.out.println("Shop by ID not found: "+uuid);
		        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		        	return;
		        }
		        calendarController.printShopCalendar(pw, shop);
				
			}else{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	//			System.out.println("Path containing wrong type: "+type);
				return;
			}
		
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println(e.getLocalizedMessage());
			return;
		}
		
//		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
