package uk.org.nbn.collectory

import au.org.ala.collectory.Contact
import au.org.ala.collectory.ProviderGroup
import grails.converters.JSON
import groovy.json.JsonSlurper

class ContactController extends au.org.ala.collectory.ContactController{

    @Override
    def auth() {
        if (!collectoryAuthService?.userInRole(ProviderGroup.ROLE_ADMIN) && !grailsApplication.config.security.cas.bypass.toBoolean()) {
            render "You are not authorised to access this page."
            return false
        }
        return true
    }

    @Override
    def list() {
        if(params.q){
            def results = Contact.findAllByEmailLikeOrLastNameLikeOrFirstNameLike('%' + params.q + '%', params.q, params.q)
            [contactInstanceList: results, contactInstanceTotal: results.size()]
        } else {
            super.list()
        }
    }

    def syncWithAuth(){
        def count = 0
        ContactNbn.findAll().each {
//            if(!it.userId){
            if(it.email) {
                def url = (grailsApplication.config.userdetails?.url?:"http://set-this-url/") + "userDetails/findUser?q=" + it.email //params.q
                log.info("Querying ${url}")
                def js = new JsonSlurper().parse(new URL(url))
                if(js.results){
                    it.userId = js.results[0].userId
                    it.firstName = js.results[0].firstName
                    it.lastName = js.results[0].lastName
                    it.save(flush:true)
                    count += 1
                }
            }
//            }
        }
        def result = [updated: count]
        render result as JSON
    }
}
