
function swalConfirm(action){
	swal({
        title: "Sind sie sich sicher?",
        text: "Sie können diese Aktion nicht rückgängig machen!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Ja, löschen!",   
        cancelButtonText: "Abbrechen",
        closeOnConfirm: false
    }, function(isConfirm){   
		if (isConfirm) { 
			eval((new Function(action))());
			swal.close();
		} 
	});
}

$(document).ready(function () {
	
	$('.confirm-btn').each(function(){
		
		action = $(this).attr('onclick');
		$(this).attr('onclick','');
		
		$(this).onclick = null;
		
		$(this).click(function() {
			
			swalConfirm(action);
			return false;
		});
	});
	
});