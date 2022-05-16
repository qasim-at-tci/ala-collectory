package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup
import au.org.ala.web.AuthService
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification
import uk.org.nbn.collectory.CollectoryAuthService

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CollectoryAuthService)
class CollectoryAuthServiceSpec extends Specification {



    def setup() {
        service.authService = Mock(AuthService)
    }

    def cleanup() {
    }

    void "test is admin if has ROLE_COLLECTION_ADMIN"() {
        setup:
        service.authService.userInRole(ProviderGroup.ROLE_ADMIN) >> false
        service.authService.userInRole(ProviderGroup.ROLE_COLLECTION_ADMIN) >> true
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        service.isAdmin()
    }

    void "test is admin if has ROLE_ADMIN"() {
        setup:
        service.authService.userInRole(ProviderGroup.ROLE_ADMIN) >> true
        service.authService.userInRole(ProviderGroup.ROLE_COLLECTION_ADMIN) >> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        service.isAdmin()
    }

    void "test is NOT admin if not has ROLE_ADMIN or ROLE_COLLECTION_ADMIN"() {
        setup:
        service.authService.userInRole(ProviderGroup.ROLE_ADMIN) >> false
        service.authService.userInRole(ProviderGroup.ROLE_COLLECTION_ADMIN) >> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        !service.isAdmin()
    }
}
