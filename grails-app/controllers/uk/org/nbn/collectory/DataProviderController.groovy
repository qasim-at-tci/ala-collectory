package uk.org.nbn.collectory

import au.com.bytecode.opencsv.CSVWriter
import au.org.ala.collectory.Action
import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.Collection
import au.org.ala.collectory.Contact
import au.org.ala.collectory.DataProvider
import grails.converters.JSON
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class DataProviderController extends au.org.ala.collectory.DataProviderController implements ProviderGroupControllerTrait{

    def sensitiveDataService

    @Override
    def list() {
        if(params.q){
            if (params.message) {
                flash.message = params.message
            }
            ActivityLog.log username(), isAdmin(), Action.LIST
            def results = DataProvider.findAllByNameLikeOrAcronymLike('%' + params.q +'%', '%' + params.q +'%');

            [instanceList: results, entityType: 'DataProvider', instanceTotal: results.size()]
        }
        else {
            super.list()
        }
    }

    @Override
    def show() {
        def model = super.show()
        model.hideSensitiveManagement = (grailsApplication.config.sensitive?.hideManagementPanel?:'true').toBoolean()
        model
    }

    def myList = {
        def currentUserId = authService.getUserId()
        def contact = ContactNbn.findByUserId(currentUserId)
        def results = []
        if(contact){
            def contactsFor = contact.getContactsFor()
            contactsFor.each {
                if(it.entity.entityType() == DataProvider.getENTITY_TYPE()){
                    results << it.entity
                }
            }
        }
        render (view: 'list', model: [instanceList: results, entityType: 'DataProvider', instanceTotal: results.size()])
    }

    def manageAccess = {
        def instance = get(params.id)
        [instance: instance]
    }

    def specifyAccess = {
        def instance = get(params.id)
        def contact = ContactNbn.findByUserId(params.userId)
        def approvedAccess = ApprovedAccess.findByContactAndDataProvider(contact, instance)

        def sensitiveSpecies = sensitiveDataService.getSensitiveSpeciesForDataProvider(instance.uid)

        def approvedAccessDataResourceTaxa = approvedAccess.dataResourceTaxa?:"[]"

        [instance:instance, contact: contact, sensitiveSpecies: sensitiveSpecies, approvedAccessDataResourceTaxa: approvedAccessDataResourceTaxa]
    }

    boolean isCollectionOrArray(object) {
        [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
    }

    def updateSpecifiedAccess = {
        def instance = get(params.id)
        def contact = ContactNbn.findByUserId(params.userId)
        def approvedAccess = ApprovedAccess.findByContactAndDataProvider(contact, instance)

        def dr_taxa_list = params.dataResourceTaxa

        approvedAccess.dataResourceTaxa = dr_taxa_list

        approvedAccess.userLastModified = username()
        approvedAccess.save(flush:true)

        redirect(action:"manageAccess", id:params.id, params:[justSavedUser:params.userId])
    }

    def addUserToApprovedList = {
        def dataProvider = get(params.id)

        log.info("userId " + params.userId)

        //find a user in the collectory - search by CAS ID
        def contact = ContactNbn.findByUserId(params.userId)
        if(!contact){

            contact = Contact.findByEmailIlike(params.email)
            if(contact){
                //update the CAS ID
                contact.setUserId(params.userId)
                contact.userLastModified = "Modified by ${collectoryAuthService.username()})"
                contact.save(flush:true)
            }
        }
        if(!contact){
            //create a new contact in the collectory for this user, we havent seen them before...
            contact = new Contact(
                    [
                            userId:params.userId,
                            email:params.email,
                            firstName:params.firstName,
                            lastName: params.lastName,
                            userLastModified: "Modified by ${collectoryAuthService.username()})"
                    ]
            )
            contact.save(flush:true)
        }

        def access = new ApprovedAccess()
        access.contact = contact
        access.dataProvider = dataProvider
        access.userLastModified = username()
        access.save(flush:true)

        def result = [success: true]

        //retrieve a list of users with access...
        render result as JSON
    }

    def removeUserToApprovedList = {
        def instance = get(params.id)

        log.info("userId " + params.userId)

        def contact = ContactNbn.findByUserId(params.userId)
        def dataProvider = get(params.id)

        def aa = ApprovedAccess.findByContactAndDataProvider(contact, dataProvider)
        def result = [:]

        if(aa){
            aa.delete(flush:true)
            result.success = true
        } else {
            result.success = false
        }

        //retrieve a list of users with access...
        render result as JSON
    }

    def findUser = {

        //proxy request to user details
        response.setContentType("application/json")
        def url = (grailsApplication.config.userdetails?.url?:"http://set-this-url/") + "userDetails/findUser" + "?q=" + params.q
        log.info("Querying ${url}")
        def js = new JsonSlurper().parse(new URL(url))

        //retrieve a list of IDs of users with access for this provider
        def list = ApprovedAccess.executeQuery("select distinct aa.contact.userId from ApprovedAccess aa where aa.dataProvider.id = ?",
                [Long.valueOf(params.id)])

        js.results.each {
            if(list.contains(it.userId)){
                it.hasAccess = true
            } else {
                it.hasAccess = false
            }
        }
        if (grailsApplication.config.sensitive?.wildcardUserSearch?:'true' == 'false') {
            //only return exact match on email
            js.results.retainAll { it.email == params.q }
        }

        render JsonOutput.toJson(js)
    }

    def findApprovedUsers = {
        def instance = get(params.id)
        def approvedAccess = ApprovedAccess.findAllByDataProvider(instance)
        def contacts = []
        approvedAccess.each {
            contacts << it.contact
        }
        render contacts as JSON
    }

    def downloadApprovedList = {
        def instance = get(params.id)
        def approvedAccess = ApprovedAccess.findAllByDataProvider(instance)
        response.setContentType("text/csv")
        response.setCharacterEncoding("UTF-8")
        response.setHeader("Content-disposition", "attachment;filename=download-approved-users-${instance.uid}.csv")

        def csvWriter = new CSVWriter(new OutputStreamWriter(response.outputStream))
        String[] header = [
                "userID",
                "email",
                "first name",
                "last name"
        ]
        csvWriter.writeNext(header)

        approvedAccess.each {
            def contact =  it.contact
            String[] row = [
                    contact.userId,
                    contact.email,
                    contact.firstName,
                    contact.lastName
            ]
            csvWriter.writeNext(row)
        }
        csvWriter.flush()
    }

    /**
     * Return JSON representation of sensitive species held in data provider's datasets
     *
     * @param uid - uid of data provider
     */
    def sensitiveSpeciesForDataProvider = {
        if (params.uid) {
            def sensitiveSpecies = sensitiveDataService.getSensitiveSpeciesForDataProvider(params.uid)
            render sensitiveSpecies as JSON
        } else {
            render(status:400, text: "sensitiveSpeciesForDataProvider: must specify a uid")
        }
    }

    /**
     * Return JSON summary of records for a given species held in data provider's datasets
     *
     * @param uid - uid of data provider
     * @param lsid - lsid of species
     */
    def speciesRecordsForDataProvider = {
        if (params.uid && params.lsid) {
            def speciesRecords = sensitiveDataService.getSpeciesRecordsForDataProvider(params.lsid, params.uid)
            render speciesRecords as JSON
        } else {
            render(status:400, text: "speciesRecordsForDataProvider: must specify uid and lsid")
        }
    }
}
