package uk.org.nbn.collectory

import au.org.ala.collectory.ContactFor
import au.org.ala.collectory.DataProvider

class DataProviderNbn extends au.org.ala.collectory.DataProvider {

    String groupClassification  // classification of provider

    static mapping = {
        discriminator "au.org.ala.collectory.DataProvider"
        groupClassification type: "text"
    }

    static constraints = {
        groupClassification(nullable:true, maxSize:256)
    }

    static hasMany = [approvals: ApprovedAccess]


}
