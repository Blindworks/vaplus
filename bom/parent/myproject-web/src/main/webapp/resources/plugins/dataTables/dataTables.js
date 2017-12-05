
$(document).ready(function () {
	
	$('.bsDataTable').dataTable({
		"language": {
            "url": "/javax.faces.resource/german.json.xhtml?ln=plugins/dataTables"
        }
    });
	
    $('.bsDataTableSearch').on( 'keyup', function () {
    	$('.bsDataTable').dataTable().api().search( this.value ).draw();
	} );
});


