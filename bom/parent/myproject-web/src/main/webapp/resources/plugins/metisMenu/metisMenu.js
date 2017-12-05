
$(document).ready(function () {
	initSideMenu();
});

function initSideMenu(){
	$("#"+viewId).addClass("active");
	$("#"+viewId).parentsUntil( "#side-menu" ).filter( "li" ).addClass("active");
	$('#side-menu').metisMenu();
}

