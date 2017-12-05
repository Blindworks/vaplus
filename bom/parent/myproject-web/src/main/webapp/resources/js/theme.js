
function arangePagePanel(){
    if ($(this).width() < 769) {

    	if(! $('body').hasClass('force-right-sidebar-closed'))
    		$('body').addClass('right-sidebar-closed')
    		
    	if(! $('body').hasClass('force-mini-navbar')){
	        $('body').addClass('body-small');
	        $('body').removeClass('fixed-sidebar').addClass('mini-navbar');
    	}
        
    } 
    else if ($(this).width() < 1200) {
    	if(! $('body').hasClass('force-right-sidebar-closed'))
    		$('body').removeClass('right-sidebar-closed')
    		
    	if(! $('body').hasClass('force-mini-navbar')){
    		$('body').removeClass('body-small');
        	$('body').removeClass('fixed-sidebar').addClass('mini-navbar');
    	}
        
    } else {

    	if(! $('body').hasClass('force-right-sidebar-closed'))
    		$('body').removeClass('right-sidebar-closed')
    		
    	if(! $('body').hasClass('force-mini-navbar')){	
    		$('body').removeClass('body-small');
    		$('body').removeClass('mini-navbar').addClass('fixed-sidebar');
    	}
    }
}


$(document).ready(function () {
	
	 // Enable/disable fixed sidebar
	arangePagePanel();
	
	setContentHeight();

	setTimeout(function(){
		setContentHeight();
	}, 1000);
	setTimeout(function(){
		setContentHeight();
	}, 2000);
	setTimeout(function(){
		setContentHeight();
	}, 10000);
	
	$('[data-toggle="tooltip"]').tooltip(); 


    // Collapse ibox function
    $('.collapse-link').click( function() {
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
    });

    // Close ibox function
    $('.close-link').click( function() {
        var content = $(this).closest('div.ibox');
        content.remove();
    });

    // Close menu in canvas mode
    $('.close-canvas-menu').click( function() {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });

    // Open close right sidebar
    $('.right-sidebar-toggle').click(function(){
        $('#right-sidebar').toggleClass('sidebar-open');
    });



    // Open close small chat
    $('.open-small-chat').click(function(){
        $(this).children().toggleClass('fa-comments').toggleClass('fa-remove');
        $('.small-chat-box').toggleClass('active');
    });


    // Small todo handler
    $('.check-link').click( function(){
        var button = $(this).find('i');
        var label = $(this).next('span');
        button.toggleClass('fa-check-square').toggleClass('fa-square-o');
        label.toggleClass('todo-completed');
        return false;
    });


    // Minimalize menu
    $('.navbar-minimalize').click(function () {
        $("body").toggleClass("fixed-sidebar");
        $("body").toggleClass("mini-navbar");
        $("body").toggleClass("force-mini-navbar");
        SmoothlyMenu();

    });
    
    $('.sidebar-minimalize').click(function () {
        $("body").toggleClass("right-sidebar-closed");
        $("body").toggleClass("force-right-sidebar-closed");
    });

    // Tooltips demo
    $('.tooltip-demo').tooltip({
        selector: "[data-toggle=tooltip]",
        container: "body"
    });

    // Move modal to body
    // Fix Bootstrap backdrop issu with animation.css
    $('.modal').appendTo("body");

    // Full height of sidebar
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");

        var navbarHeigh = $('nav.navbar-default').height();
        var wrapperHeigh = $('#page-wrapper').height();

        if(navbarHeigh > wrapperHeigh){
            $('#page-wrapper').css("min-height", navbarHeigh + "px");
        }

        if(navbarHeigh < wrapperHeigh){
            $('#page-wrapper').css("min-height", $(window).height()  + "px");
        }

    }
    fix_height();



    // Move right sidebar top after scroll
    $(window).scroll(function(){
        if ($(window).scrollTop() > $('#navbar-top').height() && !$('body').hasClass('fixed-nav') ) {
            $('#right-sidebar').addClass('sidebar-top');
        } else {
            $('#right-sidebar').removeClass('sidebar-top');
        }
        
        if ($(window).scrollTop() > $('#navbar-top').height()) {
            $('#calendar-draggables').addClass('fixed-top');
        } else {
            $('#calendar-draggables').removeClass('fixed-top');
        }
    });

    $(document).bind("load resize scroll", function() {
        if(!$("body").hasClass('body-small')) {
            fix_height();
        }
    });

    $("[data-toggle=popover]")
        .popover();
    

    
    // Username Input Filter
    $("input.usernameCharFilter").keydown(function (e) {
    	
    	console.log("usernameCharFilter "+this);
    	
        // Allow: backspace, delete, tab, escape and enter
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) || 
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        
        if (!e.shiftKey && (e.keyCode >= 48 && e.keyCode <= 57)){
        	return;
        }
        
        if (e.keyCode >= 65 && e.keyCode <= 90){
        	return;
        }
        
        if (e.keyCode == 189){
        	return;
        }
        
        if (e.keyCode == 190){
        	return;
        }
        
        e.preventDefault();
    });
});


$(window).bind("resize", function () {

	// Minimalize menu when screen is less than 768px
	arangePagePanel();
	
	// set content height
	setContentHeight();
});

function setContentHeight(){

	var content_height = $('#page-wrapper').height();

	content_height -= $('#navbar-top').height();
	content_height -= $('.page-heading').height();
	content_height -= $('.footer').height();
	
    $('#content').css("height", content_height  + "px");
}

// Local Storage functions
// Set proper body class and plugins based on user configuration
$(document).ready(function() {
    if (localStorageSupport) {

        var collapse = localStorage.getItem("collapse_menu");
        var fixedsidebar = localStorage.getItem("fixedsidebar");
        var fixednavbar = localStorage.getItem("fixednavbar");
        var boxedlayout = localStorage.getItem("boxedlayout");
        var fixedfooter = localStorage.getItem("fixedfooter");

        var body = $('body');

        if (fixedsidebar == 'on') {
            body.addClass('fixed-sidebar');
            $('.sidebar-collapse').slimScroll({
                height: '100%',
                railOpacity: 0.9
            });
        }

        if (collapse == 'on') {
            if(body.hasClass('fixed-sidebar')) {
                if(!body.hasClass('body-small')) {
                    body.addClass('mini-navbar');
                }
            } else {
                if(!body.hasClass('body-small')) {
                    body.addClass('mini-navbar');
                }

            }
        }

        if (fixednavbar == 'on') {
            $(".navbar-static-top").removeClass('navbar-static-top').addClass('navbar-fixed-top');
            body.addClass('fixed-nav');
        }

        if (boxedlayout == 'on') {
            body.addClass('boxed-layout');
        }

        if (fixedfooter == 'on') {
            $(".footer").addClass('fixed');
        }
    }
});

// check if browser support HTML5 local storage
function localStorageSupport() {
    return (('localStorage' in window) && window['localStorage'] !== null)
}

// For demo purpose - animation css script
function animationHover(element, animation){
    element = $(element);
    element.hover(
        function() {
            element.addClass('animated ' + animation);
        },
        function(){
            //wait for animation to finish before removing classes
            window.setTimeout( function(){
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
        // Hide menu in order to smoothly turn on when maximize menu
        $('#side-menu').hide();
        // For smoothly turn on menu
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 100);
    } else if ($('body').hasClass('fixed-sidebar')) {
        $('#side-menu').hide();
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 300);
    } else {
        // Remove all inline style from jquery fadeIn function to reset menu state
        $('#side-menu').removeAttr('style');
    }
}

// Dragable panels
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable(
        {
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8
        })
        .disableSelection();
}

$(document).ready(function () {
	updatePlugins();
});


function bindNumericOnly(){
	$("input.numeric_only").unbind("keydown");
	$("input.numeric_only").bind("keydown", function (event) {
	    if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
	             // Allow: Ctrl+A
	            (event.keyCode == 65 && event.ctrlKey === true) || 
	 
	        // Allow: home, end, left, right
	            (event.keyCode >= 35 && event.keyCode <= 39)) {
	              return;
	        } else {
	            // Ensure that it is a number and stop the keypress
	            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
	                event.preventDefault();
	            }
	        }
	   });
}

function bindDecimalOnly(){
	$("input.decimal_only").unbind("keydown");
	$("input.decimal_only").bind("keydown", function (event) {
		  
		
		  
	    if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
	             // Allow: Ctrl+A
	            (event.keyCode == 65 && event.ctrlKey === true) || 
	 
	        // Allow: home, end, left, right
	            (event.keyCode >= 35 && event.keyCode <= 39)) {
	              return;
	        } else {
	        	
	        	 // change comma to point
	        	 if(event.keyCode == 188){
	        		   event.preventDefault(); 
	        		   
	        		   $(this).val($(this).val() + '.');
	        		   return;
	        	 }
	        	 
	        	 
	            // Ensure that it is a number and stop the keypress
	            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 ) && event.keyCode != 190 && event.keyCode != 189) {
	                event.preventDefault();
	            }
	        }
	   });
}


function updatePlugins(){
	bindDecimalOnly();
	bindNumericOnly();

	loadChosenElements(); 
	loadICheckElements(); 
	loadDatepickerElements();
	
	loadSimpleColorPicker();
	
	if (typeof loadAutocomplete == 'function') { 
		loadAutocomplete();
	}
    
}

function updatePluginsEvent(data){
	if(data.status == "success"){
		updatePlugins();
	}
}

function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};

var openedAccountingAdjustmentItemIDs;
var scrollPosition;

function editCommissionTypeAssociation(data){

	if(data.status == "success"){
		$('#adjustmentEditModal').modal('show');
		updatePlugins();
	}
}

function saveSelectedCommissionTypeAssociation(data){
	
	if(data.status == "begin"){
		$('#adjustmentEditModal').modal('hide');
	}
	
	refreshAccountingAdjustmentItemList(data);
}

function refreshAccountingAdjustmentItemList(data){
	
	if(data.status == "begin"){
		
		openedAccountingAdjustmentItemIDs = [];
		scrollPosition = $(window).scrollTop();
		
		$('.accounting-item .panel-collapse[aria-expanded="true"]').each(function(){
			
			openedAccountingAdjustmentItemIDs.push($(this).attr("id"));
		});
		
		waitingDialog.show();
	}

	if(data.status == "success"){
		
		openedAccountingAdjustmentItemIDs.forEach(function(id) {
		    $('#'+id).collapse("show");
		});
		
		$(window).scrollTop(scrollPosition);
		
		waitingDialog.hide();
	}
}

function ajaxLoad(data, cb){

	console.log("ajaxLoadWrapper "+data.status);
	
	if(data.status == "begin"){
		waitingDialog.show();
	}

	if(data.status == "success"){
		waitingDialog.hide();
		updatePlugins();
		
		if(cb !== undefined){
			console.log("call callback "+cb);
			cb();
		}
	}
}

function flotPieceFormatter(val, axis) {
    return (Math.round(val * 100) / 100) + " Stk.";
}

function customerVisitFormatter(val, axis) {
    return (Math.round(val / 100) / 10) + "k Kd.";
}

function flotPointFormatter(val, axis) {
    return (Math.round(val * 100) / 100) + " <i class=\"fa fa-star-o\"></i>";
}


