
$(document).ready(function () {
	
	// PIEs
    $("span.pie").peity("pie", {
        fill: ['#3498DB', '#d7d7d7', '#ffffff']
    })
    $("span.pie_warning").peity("pie", {
        fill: ['#f8ac59', '#d7d7d7', '#ffffff']
    })
    $("span.pie_danger").peity("pie", {
        fill: ['#ed5565', '#d7d7d7', '#ffffff']
    })
    
    $("span.line").peity("line",{
        fill: '#3498DB',
        stroke:'#2980b9',
    })

});


