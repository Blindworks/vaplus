var eventSources;

function reloadEventSources(){
		
			eventSources = [];
		
			$('.calendar').each(function(){
				
				var ownderId;
				if($(this).data('shopid') != undefined){
					ownderId = $(this).data('shopid');
				}
				if($(this).data('userid') != undefined){
					ownderId = $(this).data('userid');
				}
				if(ownderId == undefined){
					return true;
				}

				var editable = $(this).data('editable');

				eventSources[ownderId] = [];
				
				$(this).find('.user').each(function(){
				
					var tmp = new Object();
					tmp['url'] = '/sockets/calendar/fullCalendar.json';
					tmp['editable'] = editable;
					tmp['data'] = new Object();
					tmp['data']['shop'] = ownderId;
					tmp['data']['user'] = $(this).data('userid');
					tmp['data']['holiday'] = $(this).data('holiday');
					
					if($(this).data('color') == undefined)
						tmp['className'] = 'color-iron';
					else
						tmp['className'] = 'color-'+$(this).data('color');
					
					eventSources[ownderId].push(tmp);
				});
				
				$(this).find('.shop').each(function(){
				
					var tmp = new Object();
					tmp['url'] = '/sockets/calendar/fullCalendar.json';
					tmp['editable'] = editable;
					tmp['data'] = new Object();
					tmp['data']['shop'] = $(this).data('shopid');
					tmp['data']['user'] = ownderId;
					tmp['data']['holiday'] = $(this).data('holiday');
					
					if($(this).data('color') == undefined)
						tmp['className'] = 'color-iron';
					else
						tmp['className'] = 'color-'+$(this).data('color');
					eventSources[ownderId].push(tmp);
				});
			
			});
			
		}

var calendarDefaultDate;

function setCalendarDefaultDate(){
	calendarDefaultDate = $('#calendarDraggableListForm\\:calendarRangeStart').val();
}

function submitCalendarSaveEvent(data){
	
			
			if(data.status == "success"){
			
				if($("#eventModalForm\\:global_flag_validation_failed").length === 0){
				
					var reloadAllEvents = $( data.responseText ).find( "input#eventModalForm\\:reloadAllEvents" ).first().val();
					
					if(reloadAllEvents == "true"){
						refetchCalendarEvents();
					}else{
					
						var eventId = $( data.responseText ).find( "input#eventModalForm\\:eventId" ).first().val();
						var eventTmpId = $( data.responseText ).find( "input#eventModalForm\\:eventTmpId" ).first().val();
						var calendarId = $( data.responseText ).find( "input#eventModalForm\\:calendarId" ).first().val();
						
						var allDay = $( data.responseText ).find( "input#eventModalForm\\:allDay" ).first().is(':checked') ? true : false;
						
						var effectiveDate = $( data.responseText ).find( ".effectiveDateField" ).first().text();
						var expiryDate = $( data.responseText ).find( ".expiryDateField" ).first().text();
						var effectiveDateMoment = $.fullCalendar.moment(effectiveDate.trim());
						var expiryDateMoment = $.fullCalendar.moment(expiryDate.trim());
						
						var eventType = $( data.responseText ).find( ".eventTypeField" ).first().text();
						var userName = $( data.responseText ).find( ".userNameField" ).first().text();
						var shopName = $( data.responseText ).find( ".shopNameField" ).first().text();
						
						
						var calEventId = eventTmpId;
						
						if(calEventId != undefined && calEventId.length == 0)
							calEventId = eventId;
						
						var events = $(calendarId).fullCalendar( 'clientEvents', calEventId);
						
						if(events.length && eventId != null){
							var event = events[0];

							event.tmp = false;
							event.editable = true;
							event.title = eventType;
							event.user = userName;
							event.shop = shopName;
							event.allDay = allDay;
							
							if(eventTmpId.length > 0){
								event.id = eventId;
								event._id = eventId;
							}
							

							var duration = moment.duration(expiryDateMoment.diff(effectiveDateMoment));
							var days = duration.asDays();

							if(days > 1){
								refetchCalendarEvents();
							}else{
								
								console.log("update single Event");
								
								event._start = effectiveDateMoment;
								event._end = expiryDateMoment;
								
								event.start = effectiveDateMoment;
								event.end = expiryDateMoment;
								
								$(calendarId).fullCalendar('updateEvent', event);
							}
						}

					}
					
					// Messages
					var alert = $( data.responseText ).find( ".alertField" ).first().text();
					var warning = $( data.responseText ).find( ".warningField" ).first().text();
					var cause = $( data.responseText ).find( ".causeField" ).first().text();
					
					if(alert != undefined && alert.length > 0){
				            swal({
				                title: alert,
				                text: cause,
				                type: "alert",
				            });
						
					}
					if(warning != undefined && warning.length > 0){
						swal(warning);
				            swal({
				                title: warning,
				                text: cause,
				                type: "warning",
				            });
					}

					reloadEventDraggable();
				
					hideEventModal();	
				}else{
					updatePlugins();
				}
			}
		}
		
		function prevCalendarTimeSpan(){
			$('.calendar').fullCalendar( 'prev' );
			refreshCalendarDateElement();
		}
		function nextCalendarTimeSpan(){
			$('.calendar').fullCalendar( 'next' );
			refreshCalendarDateElement();
		}
		function todayCalendarTimeSpan(){
			$('.calendar').fullCalendar( 'today' );
			refreshCalendarDateElement();
		}
		
		function refreshCalendarDateElement(){
			$('h2.calendarDuration').text($('.calendar').first().fullCalendar('getView').title);
		
		}
		
		function reinitCalendar(data){
		
			if(data.status == "success"){
				initCalendar();
				loadChosenElements();
				initSlimScroll();
			}
		}

		function refetchCalendarEvents(){
			$('.calendar').fullCalendar('refetchEvents');
		}
		function showEventModal(){
			updatePlugins();
			setTimeout(function(){
				$('#eventModal').modal('show');
			}, 50);
		}
		function hideEventModal(){
			$('#eventModal').modal('hide');
		}
		function hideEventModalFetchEvents(){
			$('#eventModal').modal('hide');
			refetchCalendarEvents();
		}
		
	    $(document).ready(function() {
			initCalendar();
	    });
	    
	    function removeEvent(data){
			
			if(data.status == "success"){
	    		var eventId = $( data.responseText ).find( "input#eventModalForm\\:eventId" ).first().val();
	    		
	    		$(".calendar").fullCalendar( 'removeEvents', eventId);
	    		
				reloadEventDraggable();
				hideEventModal();
	    	}
	    }
    
    
    	var calendarRangeStart,calendarRangeEnd;
    		
    	function changeDraggableTimeRange(start, end){
    	
    		if(start.isSame(calendarRangeStart) && end.isSame(calendarRangeEnd)){
    			return;
    		}
    		
 			$('#calendarDraggableListForm\\:calendarRangeStart').val(start.format());
			$('#calendarDraggableListForm\\:calendarRangeEnd').val(end.format());
    		reloadEventDraggable();
    		
    		calendarRangeStart = start;
    		calendarRangeEnd = end;
    	}
    		
    	function reloadEventDraggable(){
    		
			$('#calendarDraggableListForm\\:calendarRangeSubmit').click();
    	}
    
    	function initEventDraggablesEvent(data){
    		if(data.status == "success"){
    			initEventDraggables();
    			setCalendarDefaultDate();
    		}
    	}
    
    	function initEventDraggables(){
    		
	    		$('.sidebar-container div.event-item').each(function() {

	    			var fromArray = [];
	    			var toArray = [];
	    			
	    			fromArray[0] = $(this).data('from_0');
	    			fromArray[1] = $(this).data('from_1');
	    			fromArray[2] = $(this).data('from_2');
	    			fromArray[3] = $(this).data('from_3');
	    			fromArray[4] = $(this).data('from_4');
	    			fromArray[5] = $(this).data('from_5');
	    			fromArray[6] = $(this).data('from_6');
	    			toArray[0] = $(this).data('to_0');
	    			toArray[1] = $(this).data('to_1');
	    			toArray[2] = $(this).data('to_2');
	    			toArray[3] = $(this).data('to_3');
	    			toArray[4] = $(this).data('to_4');
	    			toArray[5] = $(this).data('to_5');
	    			toArray[6] = $(this).data('to_6');
		
		             $(this).data('event', {
		                 title: $.trim($(this).data('title')), 
		                 stick: false,
		                 className: "color-"+$(this).data('color'),
		                 userId: $(this).data('userid'),
		                 shopId: $(this).data('shopid'),
		                 from:$(this).data('from'),
		                 to:$(this).data('to'),
		                 fromArray: fromArray,
		             	 toArray: toArray
		             });
		
		             // make the event draggable using jQuery UI
		             $(this).draggable({
		                 zIndex: 1111999,
		                 revert: true, 
		                 revertDuration: 300 ,
		                 helper: 'clone',
		                 appendTo: 'body',
						containment: 'window',
						scroll: false,
		             });
		
		         });
    	}
    
    	function initCalendar(){    			
    			
   			reloadEventSources();

			setCalendarDefaultDate();
    		
    		initEventDraggables();
	
	         
	         $('.calendar').each(function( index ) {
	         	var from = [];
	         	var to = [];
	         	var view;
	         	var defaultView = "basicWeek";
	         	var eventLimit = false;
	         					
	         	var ownderId;
				if($(this).data('shopid') != undefined){
					ownderId = $(this).data('shopid');
					view = "shop";
				}
				if($(this).data('userid') != undefined){
					ownderId = $(this).data('userid');
					view = "user";
				}
				
				if($(this).data('defaultview') != undefined){
					defaultView = $(this).data('defaultview')
					if(defaultView == "year"){
						eventLimit = true;
					}
				}
				
    			from[0] = $(this).data('from_0');
    			from[1] = $(this).data('from_1');
    			from[2] = $(this).data('from_2');
    			from[3] = $(this).data('from_3');
    			from[4] = $(this).data('from_4');
    			from[5] = $(this).data('from_5');
    			from[6] = $(this).data('from_6');
    			to[0] = $(this).data('to_0');
    			to[1] = $(this).data('to_1');
    			to[2] = $(this).data('to_2');
    			to[3] = $(this).data('to_3');
    			to[4] = $(this).data('to_4');
    			to[5] = $(this).data('to_5');
    			to[6] = $(this).data('to_6');
    			var calEditable	= $(this).data('editable');
    			var askForEventType	= $(this).data('askforeventtype');
    			
    			if(askForEventType == undefined)
    				askForEventType = false;
    			
    			
    			
			  	
			  	initSingleCalendar('#calendar_'+ownderId,ownderId,view,calEditable,from,to,askForEventType, defaultView, eventLimit);
			 });
	
	        
	        refreshCalendarDateElement();
    	
    	}
    	
    	function initSingleCalendar(calendarId, ownerId, viewStyle,calEditable,fromArray,toArray,askForEventType, defaultView, eventLimit){
    	
    	
    		$(calendarId).fullCalendar({
	            header: false,
	            allDayDefault: false,
	            droppable: calEditable,
	            editable: calEditable,
	            displayEventEnd: true,
	            fixedWeekCount: true,
	            defaultView : defaultView,
	            height: 'auto',
	            defaultDate: calendarDefaultDate,
	    		yearColumns: 2,
	    		firstDay: 1,
	    		eventLimit: eventLimit,
	            
	            eventReceive : function( event ) { 
	            
	            	// take event duration from calendar
	            	
	            	var weekday = event.start.format('e');
	            	
	            	var from = fromArray[weekday];
	            	var to = toArray[weekday];
	            	
	            	if(from == undefined)
	            		from = "0900";
	            	if(to == undefined)
	            		to = "1800";
	            	
					event.start.hour(String(from).substring(0, 2));
					event.start.minute(String(from).substring(2, 4));
	
					event.end = event.start.clone();
					event.end.hour(String(to).substring(0, 2));
					event.end.minute(String(to).substring(2, 4));
					
					
					// override event duration from calendar
					
					if(event.fromArray[weekday] != undefined){
						event.start.hour(String(event.fromArray[weekday]).substring(0, 2));
						event.start.minute(String(event.fromArray[weekday]).substring(2, 4));
						
					}else if(event.from){
						event.start.hour(String(event.from).substring(0, 2));
						event.start.minute(String(event.from).substring(2, 4));
					}
					
					if(event.toArray[weekday] != undefined){
						event.end.hour(String(event.toArray[weekday]).substring(0, 2));
						event.end.minute(String(event.toArray[weekday]).substring(2, 4));
					}else if(event.to){
						event.end.hour(String(event.to).substring(0, 2));
						event.end.minute(String(event.to).substring(2, 4));
					}
					
					event.stick = false;
					
					event.id = generateUUID();
					event._id = event.id;
					
					event.tmp = true;
					event.editable = false;
					
					if(event.userId != undefined){
						$('#eventModalForm\\:userId').val(event.userId);
						$('#eventModalForm\\:shopId').val(ownerId);
					}
					else if(event.shopId != undefined){
						$('#eventModalForm\\:userId').val(ownerId);
						$('#eventModalForm\\:shopId').val(event.shopId);
					}
					
					$('#eventModalForm\\:eventTypeShortName').val(null);
					$('#eventModalForm\\:eventId').val(null);
					$('#eventModalForm\\:calendarId').val(calendarId);
					$('#eventModalForm\\:eventTmpId').val(event.id);
					$('#eventModalForm\\:title').val(event.title);
					$('#eventModalForm\\:allDay').val(false);
					$('#eventModalForm\\:workingTime').val(false);
					$('#eventModalForm\\:effectiveDate').val(event.start.format("DD.MM.YYYY HH:mm:ss"));
					$('#eventModalForm\\:expiryDate').val(event.end.format("DD.MM.YYYY HH:mm:ss"));
					
					if(askForEventType){
						$('#eventModalForm\\:clearEventBtn').click();
						$('#eventModalForm\\:prepareNewEventBtn').click();
						showEventModal();
					}else{
						$('#eventModalForm\\:clearEventBtn').click();
						$('#eventModalForm\\:eventTypeShortName').val("A");
						$('#eventModalForm\\:saveEventBtn').click();
					}
					
					this.calendar.updateEvent(event);
					
	            },
	            
				eventSources: eventSources[ownerId],
				
	            
	            eventDrop: function(event, delta, revertFunc) {
	
					$('#eventForm\\:eventId').val(event.id);
					$('#eventForm\\:calendarId').val(calendarId);
					$('#eventForm\\:effectiveDate').val(event.start.format("DD.MM.YYYY HH:mm:ss"));
					$('#eventForm\\:expiryDate').val(event.end.format("DD.MM.YYYY HH:mm:ss"));
	
					$('#eventForm\\:updateEventBtn').click();
	
	            },
	            eventClick: function(event, jsEvent, view) {
	            	$('#eventModal .modal-header, #eventModal .modal-body, #eventModal .modal-footer').empty();
	            	showEventModal();
                	$('#eventModalForm\\:calendarId').val(calendarId);
                	$('#eventModalForm\\:eventTmpId').val(null);
                	$('#eventModalForm\\:eventId').val(event.id);
                	$('#eventModalForm\\:loadEventBtn').click();
	            },
			  	eventRender: function(event, element, view) {
			  	
			  		if(event.readonly == true)
			  			 event.editable = false;
			  		
			   	 	if(event.tmp){
				      	element.addClass('waiting');
				    }

				    if(viewStyle == "shop" && event.user != undefined)
				    	element.find('.fc-title').append("<br/>" + event.user); 
				    if(viewStyle == "user" && event.shop != undefined){
				    	element.find('.fc-title').append("<br/>" + event.shop); 
				    }
				    
				    if (event.holiday == true) {
				      	element.addClass('holiday');
				        var dateString = event.start.format("YYYY-MM-DD");
				        $(view.el[0]).find('.fc-day[data-date=' + dateString + ']').addClass('holiday');
				    }
				},
			  	viewRender: function(view, element) {
			  		if(view.intervalUnit != "year")
			  			changeDraggableTimeRange(view.start, view.end);
				}
	        });
    	
    	}
    	
    	
    	