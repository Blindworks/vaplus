
$(document).ready(function () {
	initSlimScroll();
});


function initSlimScroll(){
    // Initialize slimscroll for content
    $('#content-wrapper').slimScroll({
        height: '100%',
        railOpacity: 0.4,
        wheelStep: 10
    });
    
    // Initialize slimscroll for right sidebar
    $('.sidebar-container').slimScroll({
        height: '100%',
        railOpacity: 0.4,
        wheelStep: 10
    });
    

    // Initialize slimscroll for small chat
    $('.small-chat-box .content').slimScroll({
        height: '234px',
        railOpacity: 0.4
    });
    
    // Fixed Sidebar
    $(window).bind("load", function () {
        if ($("body").hasClass('fixed-sidebar')) {
            $('.sidebar-collapse').slimScroll({
                height: '100%',
                railOpacity: 0.9
            });
        }
    })
    
    // Add slimscroll to element
    $('.full-height-scroll').slimscroll({
        height: '100%'
    })
}
