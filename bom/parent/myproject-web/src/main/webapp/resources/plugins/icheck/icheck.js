
function loadICheckElements(){
	
    $('input.i-checks, table.i-checks input').not(".icheck-input").icheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
    
    $('input.i-checks-eye-inverted').icheck({
        checkboxClass: 'icheckbox_eye_inverted fa',
    });
}

