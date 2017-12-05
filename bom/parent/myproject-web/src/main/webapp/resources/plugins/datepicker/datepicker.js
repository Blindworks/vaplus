
function loadDatepickerElements() {
	
    $('.datepicker').datepicker({
    	format: "dd.mm.yyyy",
        todayBtn: "linked",
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });
    
	$('.input-daterange').datepicker({
    	format: "dd.mm.yyyy",
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });
	

    $('.datepicker-time').datepicker({
    	format: "dd.mm.yyyy HH:mm",
        todayBtn: "linked",
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });

}


