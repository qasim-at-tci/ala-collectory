if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

function showVerifiedRecordCount (wsQuery, facetVerified, labelTxt) {
	// verification status: count verified records
	wsQuery = wsQuery + "&facets=" + facetVerified.split(":")[0];
	//console.log(wsQuery);
	$.ajax({
		url: wsQuery,
		dataType: 'jsonp',
		timeout: 30000,
		complete: function(jqXHR, textStatus) {
			if (textStatus == 'timeout') {
				noData();
				alert('Sorry - the request was taking too long so it has been cancelled.');
			}
			if (textStatus == 'error') {
				noData();
				alert('Sorry - the records breakdowns are not available due to an error.');
			}
		},
		success: function(data) {
			// check for errors
			if (data.length == 0 || data.totalRecords == undefined) {
				noData();
			} else {
				setNumbers(data.totalRecords);
				var verifiedRecs = 0;
				if(data.facetResults.length>0 && data.facetResults[0].fieldResult !== undefined){
					var verifiedVals  = facetVerified.split(":")[1];
					$.each(verifiedVals.split("|"), function(idx2, vval) {
						var vvalWithFacet = facetVerified.split(":")[0] + ':' + vval;
						$.each(data.facetResults[0].fieldResult, function(idx, facet) {
							var facetName = facet.fq.replace(/["]/g, '');
							if (facetName == vvalWithFacet) {
								verifiedRecs += facet.count;
							}
						});
					});
				}
				setNumbers(verifiedRecs);

				if (data.totalRecords > 0) {
					//show as percentage
					$('#totalVerifiedRecordCount').html("<b>" + (verifiedRecs/data.totalRecords).toLocaleString(undefined,{style: 'percent', maximumFractionDigits:0}) + "</b>" + " " + labelTxt);
				} else {
					//show raw number
					$('#totalVerifiedRecordCount').html("<b>" + verifiedRecs.toLocaleString() + "</b>" + " " + labelTxt);
				}

			}
		}
	});
}

$(document).ready(function() {

	//Note: COLLECTORY_CONF is available as its defined in the master/outer template layouts/ala.gsp
	$('head').append( $('<link rel="stylesheet" type="text/css" />').attr('href', COLLECTORY_CONF.contextPath+'/nbn.css') );
	$.getScript( COLLECTORY_CONF.contextPath+'/application_last.js' );

})
