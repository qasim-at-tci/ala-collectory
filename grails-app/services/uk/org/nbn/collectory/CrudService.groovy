package uk.org.nbn.collectory

import au.org.ala.collectory.DataHub
import au.org.ala.collectory.DataProvider
import au.org.ala.collectory.DataResource
import au.org.ala.collectory.Institution
import au.org.ala.collectory.Collection
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class CrudService extends au.org.ala.collectory.CrudService{

    CrudService(){
        if (!baseStringProperties.contains("groupClassification")) {
            baseStringProperties << "groupClassification"
        }
        if (!dataResourceStringProperties.contains("dateCreated")) {
            dataResourceStringProperties << "dateCreated"
        }
        if (!dataResourceStringProperties.contains("lastUpdated")) {
            dataResourceStringProperties << "lastUpdated"
        }
    }

    @Override
    def readDataProvider(DataProvider p) {
        def jsonBuilder = super.readDataProvider(p)
        def json = JSON.parse(jsonBuilder.toString())
        json.put("groupClassification", p.groupClassification?:'')
        addNbnNetworkMembership(json.networkMembership, p.networkMembership )
        json as JSON
    }

    @Override
    def readDataHub(DataHub p) {
        def jsonBuilder = super.readDataHub(p)
        def json = JSON.parse(jsonBuilder.toString())
        json.put("groupClassification", p.groupClassification?:'')
        addNbnNetworkMembership(json.networkMembership, p.networkMembership )
        json as JSON
    }

    @Override
    def readDataResource(DataResource p) {
        def jsonBuilder = super.readDataResource(p)
        def json = JSON.parse(jsonBuilder.toString())
        addNbnNetworkMembership(json.networkMembership, p.networkMembership )
        json as JSON
    }

    @Override
    def readInstitution(Institution p) {
        def jsonBuilder = super.readInstitution(p)
        def json = JSON.parse(jsonBuilder.toString())
        addNbnNetworkMembership(json.networkMembership, p.networkMembership )
        json as JSON
    }
    @Override
    def readCollection(Collection p) {
        def jsonBuilder = super.readCollection(p)
        def json = JSON.parse(jsonBuilder.toString())
        addNbnNetworkMembership(json.networkMembership, p.networkMembership )
        json as JSON
    }

    def addNbnNetworkMembership(networkMembershipList, rawNetworkMembershipJsonListStr) {
        if (rawNetworkMembershipJsonListStr) {
            def rawNetworkMembershipList = JSON.parse(rawNetworkMembershipJsonListStr)
            def i = 0;
            rawNetworkMembershipList.each {
                if (it == "NBN") {
                    networkMembershipList[i] = [name: 'National Biodiversity Network', acronym: it,
                                            logo: grailsApplication.config.grails.serverURL + "/data/network/nbn.png"]
                }
                i++
            }
        }
    }
}
