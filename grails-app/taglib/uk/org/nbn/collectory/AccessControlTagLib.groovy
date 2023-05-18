package uk.org.nbn.collectory

class AccessControlTagLib {
    static defaultEncodeAs = [taglib:'html']
    static namespace = 'nbnac'
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def metersToKmAndFormat = { attrs, body ->
        def meters = attrs.meters
        def km = meters / 1000
        def formattedKm = String.format("%dkm", (int) km)
        out << formattedKm
    }

//    def displayPublicResolution = { attrs, body ->
//        if (!attrs.dataResourceNbn) {
//            out << "No public resolution has been set for this data resource"
//        } else {
//            if (attrs.dataResourceNbn.publicResolutionToBeApplied && attrs.dataResourceNbn.publicResolutionToBeApplied>0) {
//                out << "Public resolution that will be applied next data processing: ${attrs.dataResourceNbn.publicResolutionToBeApplied}m"
//            }
//            if (attrs.dataResourceNbn.publicResolution && attrs.dataResourceNbn.publicResolution>0) {
//                out << "Public resolution that will be applied next data processing: ${attrs.dataResourceNbn.publicResolutionToBeApplied}m"
//            }
//        }
//    }
}
