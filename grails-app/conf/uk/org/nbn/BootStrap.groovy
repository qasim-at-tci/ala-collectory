package uk.org.nbn

import au.org.ala.collectory.Collection

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        log.warn "2222config.security.cas = ${grailsApplication.config.security.cas}"
        log.info "config.skin.layout = ${grailsApplication.config.skin.layout}"
        log.info "config.serverName = ${grailsApplication.config.serverName}"
        log.info "config.grails.serverURL = ${grailsApplication.config.grails.serverURL}"

        initCollectionDomain()

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


    def destroy = {
    }
}
