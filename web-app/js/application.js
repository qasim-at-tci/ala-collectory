if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
$(document).ready(function() {
	$('head').append( $('<link rel="stylesheet" type="text/css" />').attr('href', COLLECTORY_CONF.contextPath+'/nbn.css') );
	$.getScript( COLLECTORY_CONF.contextPath+'/nbn.js' );
})
