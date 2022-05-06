package uk.org.nbn.collectory

import au.org.ala.collectory.DataProvider
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.util.Holders

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
/**
 * NOTE: In LookupController, there is  a method defined (in the Groovy way) like this:
 * def name = {
 *
 * that causes a GroovyCastException to be thrown when the tests are run. If the method is defined like this:
 * * def name(){
 *
 * no exception is thrown and the tests run ok.
 */
@TestFor(LookupController)
@Mock(DataProvider)
class LookupControllerSpec extends Specification {
    def dp

    def setup() {
        dp = new DataProvider(uid: "uid", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)
        Holders.grailsApplication.config.citation.link.template =  "For more information: @email@ @link@"
    }

    def cleanup() {
    }

    void "test buildCitation tab separated"() {
        when:
        def result = controller.buildCitation(dp, "tab separated")

        then:
        println result
        result.indexOf("For more information: email hmj@test.com, or")>-1
    }

    void "test buildCitation map"() {
        when:
        def result = controller.buildCitation(dp, "map")

        then:
        result.link.startsWith("For more information: email hmj@test.com, or")
    }

    void "test buildCitation array"() {
        when:
        def result = controller.buildCitation(dp, "array")

        then:
        result[3].startsWith("For more information: email hmj@test.com, or")
    }
}
