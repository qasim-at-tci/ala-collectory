package uk.org.nbn.collectory

class DataHubNbn extends au.org.ala.collectory.DataHub {

    String groupClassification  // classification of provider

    static mapping = {
        discriminator "au.org.ala.collectory.DataHub"
        groupClassification type: "text"
    }

    static constraints = {
        groupClassification(nullable:true, maxSize:256)
    }

}
