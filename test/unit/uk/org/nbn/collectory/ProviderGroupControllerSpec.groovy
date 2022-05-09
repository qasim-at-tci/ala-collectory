package uk.org.nbn.collectory

import au.org.ala.collectory.CollectoryAuthService
import au.org.ala.collectory.ProviderGroup
import au.org.ala.web.AuthService
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
//@TestFor(EntityController)
abstract class ProviderGroupControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test not authenticated if not Admin and not Collection Editor"() {
        setup:
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.authService >> Mock(AuthService)
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN) >> false
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_COLLECTION_EDITOR) >> false
        controller.collectoryAuthService.authService.getUserId() >>1
        controller.isUserAuthorisedEditorForEntity()>> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        !controller.auth()
    }

    void "test authenticated if Admin"() {
        setup:
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.authService >> Mock(AuthService)
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN) >> true
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_COLLECTION_EDITOR) >> false
        controller.collectoryAuthService.authService.getUserId() >>1
        controller.isUserAuthorisedEditorForEntity()>> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        controller.auth()
    }

    void "test authenticated if Collection Editor"() {
        setup:
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.authService >> Mock(AuthService)
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN) >> true
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_COLLECTION_EDITOR) >> false
        controller.collectoryAuthService.authService.getUserId() >>1
        controller.isUserAuthorisedEditorForEntity()>> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        controller.auth()
    }
}
