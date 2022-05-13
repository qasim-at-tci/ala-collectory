package uk.org.nbn.collectory

import au.org.ala.collectory.Address
import au.org.ala.collectory.DataResourceSummary
import groovy.json.JsonSlurper

class DataResourceNbn extends au.org.ala.collectory.DataResource{

    static mapping = {
        discriminator "au.org.ala.collectory.DataResource"
        keywords type: "text"
    }

    //FFTF_NOT_NEEDED LEFT OUT DECOUPED VERSION
//    String addrCity = ""
//    String addrCountry = ""
//    String addrPostcode = ""
//    String addrState = ""
//    String addrStreet = ""

//    static constraints = {
//        addrCountry(nullable:true, maxSize:255)
//        addrPostcode(nullable:true, maxSize:255)
//        addrState(nullable:true, maxSize:255)
//        addrStreet(nullable:true, maxSize:255)
//    }
    //END FFTF_NOT_NEEDED LEFT OUT DECOUPED VERSION

    @Override
    DataResourceSummary buildSummary() {
        def drs = super.buildSummary()
        drs.dateCreated = dateCreated
        drs.lastUpdated = lastUpdated
        return drs
    }

    //FFTF_NOT_NEEDED LEFT OUT DECOUPED VERSION
//    boolean setAddressFromEMLfields() {
//        if (address == null) {
//            address = new Address()
//        }
//        address.city = addrCity
//        address.country = addrCountry
//        address.postcode = addrPostcode
//        address.street = addrStreet
//        address.state = addrState
//        return true
//    }
    //END FFTF_NOT_NEEDED LEFT OUT DECOUPED VERSION

    List listNetworkMembership() {
        def list = []
        if (dataProvider?.networkMembership) {
            list = new JsonSlurper().parseText(dataProvider.networkMembership)
        }
        list
    }

}
