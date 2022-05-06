package uk.org.nbn.collectory

class LookupController extends au.org.ala.collectory.LookupController{

    @Override
    def buildCitation(pg, format) {
        def result = super.buildCitation(pg, format)
        switch (format) {
            case "tab separated":
                result = buildCitationEmailLink(pg, result)
                return result
            case "map":
                result.link = buildCitationEmailLink(pg, result.link)
                return result
            case "array":
                result[3] = buildCitationEmailLink(pg, result[3])
                return result;
        }
    }

    private def buildCitationEmailLink(pg, link) {
        //TODO This is stupid. The template should have the text with just a place holder for the email
        return link.replaceAll("@email@", pg.email ? "email " + pg.email + ", or" : "")
    }
}
