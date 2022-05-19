package uk.org.nbn.collectory

import au.org.ala.collectory.Contact
import au.org.ala.collectory.ContactFor
import au.org.ala.collectory.DataProvider
import au.org.ala.collectory.ProviderGroup
import au.org.ala.web.AuthService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification
import uk.org.nbn.collectory.CollectoryAuthService

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CollectoryAuthService)
@Mock([Contact,ContactFor,DataProvider])
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

    void "test entityType added to authorisedForUser"() {
        def contact

        setup:
        contact = new Contact(email: "a@a.com", userLastModified:"user", lastName:"lastNamea", firstName:"firstNamea").save(flush: true, failOnError: true)
        def dp = new DataProvider(guid: "ABC", uid:'dp13', name: "XYZ", userLastModified: "test").save(flush: true, failOnError: true)
        def ncf = new ContactFor(contact: contact, entityUid: dp.uid, role: "Manager", administrator: true, primaryContact: true, userLastModified: "test").save(flush: true, failOnError: true)

        expect:
        def model = service.authorisedForUser(contact)
        model.sorted[0].entityType == "DataProvider"
    }
}
