package uk.org.nbn.collectory

import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.Collection
import au.org.ala.collectory.Contact
import au.org.ala.web.AuthService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PublicController)
@Mock([Collection, ActivityLog, Contact])
class PublicControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test showDataProvider"() {
        setup:
        params["id"]="co1"
        def collection = new Collection(uid:"co1", userLastModified: "u1", name:'dog').save(flush: true, failOnError: true)
        controller.collectoryAuthService = Mock(uk.org.nbn.collectory.CollectoryAuthService)
        controller.collectoryAuthService.authService >> Mock(AuthService)
        controller.collectoryAuthService.authService.getUserId() >> 1
        controller.collectoryAuthService.isUserAuthorisedEditorForEntity(*_) >> [authorised:true, reason:""]
        controller.collectoryAuthService.username() >> "u1"
        controller.collectoryAuthService.userInRole(*_) >> true

        grailsApplication.config.sensitive.hideManagementPanel >> true
        grailsApplication.config.dataprovider.showAdminLink >> true

        expect:
        def model = controller.showDataProvider()
        model.instance == collection
        model.hideSensitiveManagement
        model.viewerIsAdmin

    }
}
