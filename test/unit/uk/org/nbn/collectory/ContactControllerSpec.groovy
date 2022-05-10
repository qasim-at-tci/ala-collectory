package uk.org.nbn.collectory

import au.org.ala.collectory.CollectoryAuthService
import au.org.ala.collectory.Contact
import au.org.ala.collectory.ProviderGroup
import au.org.ala.web.AuthService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ContactController)
@Mock(Contact)
class ContactControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

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
        controller.collectoryAuthService = Mock(CollectoryAuthService)
        controller.collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN) >> false
        Holders.grailsApplication.config.security.cas.bypass = "false"

        expect:
        !controller.auth()
    }

    void "test list by like email"() {
        setup:
        new Contact(email: "a@a.com", userLastModified:"user", lastName:"lastNamea", firstName:"firstNamea").save(flush: true, failOnError: true)
        new Contact(email: "b@b.com", userLastModified:"user", lastName:"lastNameb", firstName:"firstNameb").save(flush: true, failOnError: true)

        when:
        params.q="b@b.com"
        def model = controller.list()

        then:
        model.contactInstanceTotal == 1
        model.contactInstanceList.get(0).email == "b@b.com"
    }

    void "test list by like firstName"() {
        setup:
        new Contact(email: "a@a.com", userLastModified:"user", lastName:"lastNamea", firstName:"firstNamea").save(flush: true, failOnError: true)
        new Contact(email: "b@b.com", userLastModified:"user", lastName:"lastNameb", firstName:"firstNameb").save(flush: true, failOnError: true)

        when:
        params.q="firstNameb"
        def model = controller.list()

        then:
        model.contactInstanceTotal == 1
        model.contactInstanceList.get(0).email == "b@b.com"
    }

    void "test list all"() {
        setup:
        new Contact(email: "a@a.com", userLastModified:"user", lastName:"lastNamea", firstName:"firstNamea").save(flush: true, failOnError: true)
        new Contact(email: "b@b.com", userLastModified:"user", lastName:"lastNameb", firstName:"firstNameb").save(flush: true, failOnError: true)

        when:
        def model = controller.list()

        then:
        model.contactInstanceTotal == 2
        model.contactInstanceList.get(0).email == "a@a.com"
    }
}
