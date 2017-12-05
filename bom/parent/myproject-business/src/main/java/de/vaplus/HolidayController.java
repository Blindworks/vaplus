package de.vaplus;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.Holiday;
import de.vaplus.api.entity.State;
import de.vaplus.client.eao.HolidayEao;
import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.HolidayEntity;
import de.vaplus.client.entity.StateEntity;
import de.vaplus.helper.Executor;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.Calendars;

@Singleton
@Startup
public class HolidayController implements HolidayControllerInterface {
	
	private static final long serialVersionUID = 413367160198769707L;

	@EJB
	private StateControllerInterface stateController;

    @EJB
    private Executor executor;

    private Future construct;
	
	@Inject
    private HolidayEao holidayEao;
	
	private Calendar calendar;
	
	public HolidayController(){
		// System.out.println("### INIT HolidayController ###");
	}
	
	private String dateToStringDay(Date date){
		
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		
		String year = String.valueOf(c.get(java.util.Calendar.YEAR));
		
		String month = String.valueOf(c.get(java.util.Calendar.MONTH) +1);
		
		if(month.length() == 1)
			month = "0"+month;
		
		String day = String.valueOf(c.get(java.util.Calendar.DAY_OF_MONTH));
		
		if(day.length() == 1)
			day = "0"+day;
		
		return year+month+day;
	}

	@Override
	public boolean isHoliday(Date date, State state){
		
		String day = dateToStringDay(date);

		Holiday h = holidayEao.getHoliday(state, day);
		
		if(h != null)
			return true;
		else
			return false;
	}
	
	@Override
	public List<? extends Event> getHolidayEvents(State state){
		
		List<EventEntity> eventList = new LinkedList<EventEntity>();
		
		if(state == null)
			return eventList;
		
		List<HolidayEntity> holidayList = getHolidayCalendar(state);
		Iterator<HolidayEntity> iterator = holidayList.iterator();
		HolidayEntity holidayEntity;
		EventEntity event;
		
		while(iterator.hasNext()){
			holidayEntity = iterator.next();
			
			event = new EventEntity();
			event.setUuid(UUID.randomUUID().toString());
			event.setTitle(holidayEntity.getName());
			event.setEffectiveDate(holidayEntity.getStartDate());
			event.setExpiryDate(holidayEntity.getEndDate());
			event.setAllDay(true);
			eventList.add(event);
		}
		return eventList;
	}


	@Override
	public List<? extends Event> getHolidayEvents(Date from, Date to, State state) {
		
		List<EventEntity> eventList = new LinkedList<EventEntity>();
		
		if(state == null)
			return eventList;
		
		List<HolidayEntity> holidayList = getHolidayCalendar(from, to, state);
		Iterator<HolidayEntity> iterator = holidayList.iterator();
		HolidayEntity holidayEntity;
		EventEntity event;
		
		while(iterator.hasNext()){
			holidayEntity = iterator.next();
			
			event = new EventEntity();
			event.setUuid(UUID.randomUUID().toString());
			event.setTitle(holidayEntity.getName());
			event.setEffectiveDate(holidayEntity.getStartDate());
			event.setExpiryDate(holidayEntity.getEndDate());
			event.setAllDay(true);
			eventList.add(event);
		}
		return eventList;
	}

	private List<HolidayEntity> getHolidayCalendar(Date from, Date to, State state) {
		return holidayEao.getHolidayList(from, to, state);
	}

	@Override
	public List<? extends Event> getAllHolidayEvents() {
		
		List<EventEntity> eventList = new LinkedList<EventEntity>();
		
		List<HolidayEntity> holidayList = getAllHolidayCalendar();
		Iterator<HolidayEntity> iterator = holidayList.iterator();
		HolidayEntity holidayEntity;
		EventEntity event;
		
		while(iterator.hasNext()){
			holidayEntity = iterator.next();
			
			event = new EventEntity();
			event.setUuid(UUID.randomUUID().toString());
			event.setTitle(holidayEntity.getName());
			event.setEffectiveDate(holidayEntity.getStartDate());
			event.setExpiryDate(holidayEntity.getEndDate());
			event.setAllDay(true);
			eventList.add(event);
		}
		return eventList;
	}
	
	private List<HolidayEntity> getAllHolidayCalendar(){
		
		return holidayEao.getAllHolidayCalendar();
	}
	
	
	private List<HolidayEntity> getHolidayCalendar(State state){
		
		return holidayEao.getHolidayList(state, null);
	}
	
	
	private List<HolidayEntity> getHolidayCalendar(State state, int year){
		
		return holidayEao.getHolidayList(state, year+"%");
	}

	@PostConstruct
	@SuppressWarnings("unchecked")
	@Asynchronous
	public void construct() {
        try {
			construct = executor.submit(new Callable() {
			    @Override
			    @Asynchronous
			    public Object call() throws Exception {
					long sleep = (long) (60000 + Math.floor(Math.random() * 100000));
//        		System.out.println("wait "+(sleep / 1000)+"s for HolidayController update Calendar Feeds" );
//        		Thread.sleep(sleep);
					HolidayController.this.importHolidayCalendar();
			        return null;
			    }
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	@Asynchronous
//	@PostConstruct
	@Schedule( dayOfMonth="1", persistent=false)
	public void importHolidayCalendar(){
		
		// import Holidays -1 Year +3 Years
//		System.out.println("HolidayController | import Holidays -1 Year +3 Years");
		
//		long duration = (new Date()).getTime();
		
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(new Date());
		int currentYear = c.get(java.util.Calendar.YEAR);
		
		List<? extends State> stateList = stateController.getStateList();
		Iterator<? extends State> stateIterator;
		State state;
		URL icsUrl;
		Calendar cal = null;
		HolidayEntity holiday;
		
		for(int year = currentYear -1 ; year <= currentYear + 1; year++){
//			System.out.println("get Calendar for year: "+year);
			stateIterator = stateList.iterator();
			

			
			while(stateIterator.hasNext()){
				state = stateIterator.next();
				icsUrl = null;

				
				List<HolidayEntity> existingHolidays = holidayEao.getHolidayList(state, year+"%");
				if(existingHolidays.size() > 0){
//					System.out.println("GOT "+existingHolidays.size()+" Holidays for state: "+state.getName()+" and year: "+year);
//					System.out.println("SKIP loading");
					continue;
				}
				
				
				
				try{
					if(state.getName().equalsIgnoreCase("Baden-Württemberg")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-baden-wuerttemberg-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Bayern")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-bayern-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Berlin")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-berlin-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Brandenburg")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-brandenburg-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Bremen")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-bremen-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Hamburg")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-hamburg-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Hessen")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-hessen-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Mecklenburg-Vorpommern")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-mecklenburg-vorpommern-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Niedersachsen")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-niedersachsen-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Nordrhein-Westfalen")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-nordrhein-westfalen-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Rheinland-Pfalz")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-rheinland-pfalz-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Saarland")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-saarland-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Sachsen")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-sachsen-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Sachsen-Anhalt")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-sachsen-anhalt-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Schleswig-Holstein")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-schleswig-holstein-"+year+".ics");
					}else if(state.getName().equalsIgnoreCase("Thüringen")){
						icsUrl = new URL("http://www.schulferien-deutschland.net/ical/feiertage-thueringen-"+year+".ics");
					}
					
					

//					System.out.println("load CAL from: "+icsUrl);
					
					
					if(icsUrl != null){
						
						cal = Calendars.load(icsUrl);
						
						for (Iterator calIterator = cal.getComponents(Component.VEVENT).iterator(); calIterator.hasNext();) {
							  VEvent event = (VEvent) calIterator.next();
							  
							  holiday = holidayEao.getHoliday(state, event.getStartDate().getValue());

							  if(holiday == null){
								  holiday = new HolidayEntity();
								  holiday.setName(event.getSummary().getValue());
								  holiday.setState((StateEntity) state);
								  holiday.setDay(event.getStartDate().getValue());
								  
								  holidayEao.saveHoliday(holiday);
								  
							  }else{

							  }

						}
						
					}
				
				} catch (IOException | ParserException e) {
					System.out.println("Error in Kalendar: "+state.getName()+ " "+year+" "+icsUrl);
				}
				
				
			}
			
		}

//		duration = (new Date()).getTime() - duration;
//		System.out.println("HolidayController | import Holidays done");

	}
}
