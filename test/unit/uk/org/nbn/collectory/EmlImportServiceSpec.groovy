package uk.org.nbn.collectory

import au.org.ala.collectory.Licence
import au.org.ala.collectory.Contact
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import au.org.ala.util.TestUtil

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(EmlImportService)
@Mock([DataResourceNbn, Licence, Contact])
class EmlImportServiceSpec extends Specification implements TestUtil{

    def setup() {
    }

    def cleanup() {
    }

    void "test additional eml fields added"() {
        expect:
        service.emlFields.pubShortDescription
        service.emlFields.techDescription
        service.emlFields.keywords
        //FFTF_NOT_NEEDED LEFT OUT DECOUPLED VERSION
//        service.emlFields.addrCity
//        service.emlFields.addrCountry
//        service.emlFields.addrPostcode
//        service.emlFields.addrState
//        service.emlFields.addrStreet
        //END FFTF_NOT_NEEDED LEFT OUT DECOUPLED VERSION


    }

    void "test add phone to contact"() {
        setup:
        def tmp = copyToTempFile("eml.xml")
        def xml = new XmlSlurper().parse(tmp)
        service.collectoryAuthService = Mock(CollectoryAuthService)
        service.collectoryAuthService.username() >> "user"
        new Contact(email: "a@a.com", userLastModified:"user", lastName:"lastNamea", firstName:"firstNamea").save(flush: true, failOnError: true)

        expect:

        def dataResource = new DataResourceNbn()
        def contacts = service.extractFromEml(xml, dataResource)
        contacts.size() == 2
        contacts[0].phone == "123456"
        contacts[1].phone == "543"
    }

}
