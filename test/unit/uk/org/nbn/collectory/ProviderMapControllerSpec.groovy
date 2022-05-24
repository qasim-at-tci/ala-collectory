package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ProviderMapController)
class ProviderMapControllerSpec extends Specification {


    void "test authenticated if Admin"() {
        setup:
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN) >> true
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        controller.auth()
    }

    void "test not authenticated if not Admin"() {
        setup:
        controller.collectoryAuthService = Mock(au.org.ala.collectory.CollectoryAuthService)
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN) >> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        !controller.auth()
    }
}
