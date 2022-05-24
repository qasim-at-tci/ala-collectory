package uk.org.nbn.collectory

import au.org.ala.collectory.ContactFor
import au.org.ala.collectory.DataProvider

class DataProviderNbn extends au.org.ala.collectory.DataProvider {

    static mapping = {
        discriminator "au.org.ala.collectory.DataProvider"
    }

    static hasMany = [approvals: ApprovedAccess]


}
