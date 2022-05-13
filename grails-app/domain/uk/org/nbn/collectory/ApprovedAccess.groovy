package uk.org.nbn.collectory

import au.org.ala.collectory.Contact

class ApprovedAccess implements Serializable {

    static auditable = [ignore: ['version','dateCreated','lastUpdated','userLastModified']]

    Contact contact
    DataProviderNbn dataProvider
    //String dataResourceUids = "[]" //JSON array of dataResourceUids
    //String taxonIDs = "[]" //JSON array of taxonIDs
    String dataResourceTaxa = "[]" //JSON  [{"lsid":lsid,"data_resource_uid":[data_resource_uid, data_resource_uid...]},{...}]

    Date dateCreated
    Date lastUpdated
    String userLastModified

    static mapping  = {
        //dataResourceUids type: "text"
        //taxonIDs type: "text"
        dataResourceTaxa type: "text"
    }

    static constraints = {}

    //def findByContactAndDataProvider(Contact contact, Long instance) {
    //
    //}

    static findAllByContact(Contact contact) {
        def approvedList = []
        ApprovedAccess.executeQuery("select aa.dataProvider, aa.dataResourceTaxa from ApprovedAccess aa where aa.contact.userId = ?",[contact.userId]).each {
            approvedList << [dataProvider: it[0], dataResourceTaxa: it[1]]
        }
        approvedList
    }
}

//findAllByContact(contact)
//findByContactAndDataProvider(contact, instance)
//findAllByDataProvider(instance)
//save(flush:true)
//executeQuery("select distinct aa.contact.userId from ApprovedAccess aa where aa.dataProvider.id = ?",[Long.valueOf(params.id)])
