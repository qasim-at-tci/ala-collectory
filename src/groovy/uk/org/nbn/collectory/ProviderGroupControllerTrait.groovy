package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup
import grails.web.Action

/**
 * Suggested Reading: Traits can call methods and use properties in the implementing class.
 * https://docs.groovy-lang.org/next/html/documentation/core-traits.html
 */
trait ProviderGroupControllerTrait {
    /**
    * Access control
    *
    * All methods require EDITOR role.
    * Edit methods require ADMIN or the user to be an administrator for the entity.
    */
    @Action
    def auth() {
        if (
        !collectoryAuthService?.userInRole(ProviderGroup.ROLE_ADMIN)
                && !collectoryAuthService?.userInRole(ProviderGroup.ROLE_COLLECTION_EDITOR)
                && !grailsApplication.config.security.cas.bypass.toBoolean()
                && !isUserAuthorisedEditorForEntity(collectoryAuthService.authService.getUserId(), (params.id != null? params.id : params.uid))
        ) {
            response.setHeader("Content-type", "text/plain; charset=UTF-8")
            render message(code: "provider.group.controller.01", default: "You are not authorised to access this page. You do not have '${ProviderGroup.ROLE_ADMIN}' rights.")
            return false
        } else {

            def authReason = ""
            if(collectoryAuthService?.userInRole(ProviderGroup.ROLE_ADMIN)){
                authReason += "User has ${ProviderGroup.ROLE_ADMIN};"
            }
            if(grailsApplication.config.security.cas.bypass.toBoolean()){
                authReason += "CAS is currently bypassed for all users;"
            }
            if(isUserAuthorisedEditorForEntity(collectoryAuthService.authService.getUserId(), (params.id != null? params.id : params.uid))){
                authReason += "User is a contact for resource, and is marked as administrator;"
            }

            log.info("Auth reason: " + authReason)
            //add some info - why can i see this page ?
            response.setHeader("Collectory-Auth-Reason", authReason)
            return true
        }
    }

    /**
     * If a logged in user is an administrator for a data resource then they can edit.
     * Likewise if they are the administrator of a provider or institution they can edit
     * an institution/provider metadata and any resources underneath that institution/provider.
     *
     * @param userId
     * @param params
     * @return
     */
    def isUserAuthorisedEditorForEntity(userId, entityId){
        def authorised = false
        if(entityId){
            def result = collectoryAuthService.isUserAuthorisedEditorForEntity(userId, get(entityId))
            authorised = result.authorised
        }
        authorised
    }
    /*
     End access control
     */
}
