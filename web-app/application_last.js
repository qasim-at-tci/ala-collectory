

/************************************************************\
 * Go to 'species' page for selected node
 *
 * The ALA copy and paste this function in 3 js files. This will override them all.
 \************************************************************/
function showBie(node) {
    var rank = node.attr('rank');
    if (rank == 'kingdoms') return;
    var name = node.attr('id');
    var sppUrl = CHARTS_CONFIG.bieWebappUrl;
    if (rank != 'species') {
        sppUrl += "/search?q=" + name + "&fq=rank:" + rank;
    } else {
        sppUrl += "/species/" + name;
    }
    document.location.href = sppUrl;

}
