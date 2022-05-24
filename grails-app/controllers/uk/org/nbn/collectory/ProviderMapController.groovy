package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup

class ProviderMapController extends au.org.ala.collectory.ProviderMapController{

    @Override
    def auth() {
        if (!collectoryAuthService?.userInRole(ProviderGroup.ROLE_ADMIN) && !grailsApplication.config.security.cas.bypass.toBoolean()) {
            render "You are not authorised to access this page."
            return false
        }
        return true
    }
}
