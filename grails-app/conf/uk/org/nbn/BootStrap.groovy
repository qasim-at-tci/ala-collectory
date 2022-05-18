package uk.org.nbn

import au.org.ala.collectory.Collection
import au.org.ala.collectory.resources.DarwinCoreFields

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        log.warn "config.security.cas = ${grailsApplication.config.security.cas}"
        log.info "config.skin.layout = ${grailsApplication.config.skin.layout}"
        log.info "config.serverName = ${grailsApplication.config.serverName}"
        log.info "config.grails.serverURL = ${grailsApplication.config.grails.serverURL}"

        initCollectionDomain()

        initDarwinCoreFields()

    }

    static def initCollectionDomain = {
        def oldList = Collection.metaClass.getMetaMethod 'list'
        Collection.metaClass.static.list = { Map params ->
            if (params?.q) {
                delegate.findAllByNameLikeOrAcronymLike('%' + params.q + '%', '%' + params.q + '%')
            } else {
                oldList.invoke params
            }
        }
    }

    static def initDarwinCoreFields = {
        def dcf = DarwinCoreFields.fields.find {it.name == "georeferenceVerificationStatus"}
        dcf.values = ["","Accepted", "Accepted - correct", "Accepted - considered correct", "Unconfirmed", "Unconfirmed - plausible", "Unconfirmed - not reviewed"]
        dcf = DarwinCoreFields.fields.find {it.name == "identificationVerificationStatus"}
        dcf.values = ["","Accepted", "Accepted - correct", "Accepted - considered correct", "Unconfirmed", "Unconfirmed - plausible", "Unconfirmed - not reviewed"]
    }


    def destroy = {
    }
}
