

function loadChosenElements(){
    var config = {
            'select.chosen-select'           : {include_empty: true},
            'select.chosen-select-xs'        : {inherit_select_classes:true, include_empty: true},
            'select.chosen-select-deselect'  : {allow_single_deselect:true, include_empty: true},
            'select.chosen-select-no-search' : {disable_search:true, include_empty: true},
            'select.chosen-select-no-single' : {disable_search_threshold:10, include_empty: true},
            'select.chosen-select-no-single-class' : {disable_search_threshold:10,inherit_select_classes:true, include_empty: true},
            'select.chosen-select-no-results': {no_results_text:'Oops, nothing found!', include_empty: true},
            'select.chosen-select-width'     : {width:"95%", include_empty: true},
            'select.chosen-select-full-width'     : {disable_search_threshold:10,width:"100%", include_empty: true}
        }
    for (var selector in config) {
        $(selector).chosen(config[selector]);
    }
}


