package uk.org.nbn.collectory

import au.org.ala.collectory.Contact
import au.org.ala.collectory.CollectoryAuthService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ManageController)
@Mock(Contact)
class ManageControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test list when isEditor"() {
        setup:
        def contact = new Contact(email: "a@a.com", userLastModified:"user").save(flush: true, failOnError: true)
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.username() >> "a@a.com"
        controller.collectoryAuthService.authorisedForUser(*_) >> []
        controller.collectoryAuthService.getRoles()>>["ADMIN"]
        controller.collectoryAuthService.isEditor()>>true

        when:
        controller.list()

        then:
        view == "/manage/adminList"
        model == [entities: [], user: contact, userRoles:["ADMIN"]]
//        controller.render(view:"adminList", model: [entities: [], user: contact, userRoles:["ADMIN"]] )
    }

    void "test list when not isEditor"() {
        setup:
        def contact = new Contact(email: "a@a.com", userLastModified:"user").save(flush: true, failOnError: true)
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.username() >> "a@a.com"
        controller.collectoryAuthService.authorisedForUser(*_) >> []
        controller.collectoryAuthService.getRoles()>>["USER"]
        controller.collectoryAuthService.isEditor()>>false


        when:
        controller.list()

        then:
        view == "/manage/list"
        model == [entities: [], user: contact, userRoles:["USER"]]
//        controller.render(view:"adminList", model: [entities: [], user: contact, userRoles:["ADMIN"]] )
    }
}
