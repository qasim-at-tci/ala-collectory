package uk.org.nbn.collectory

import au.org.ala.collectory.Institution
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(InstitutionController)
@Mock(Institution)
class InstitutionControllerSpec extends ProviderGroupControllerSpec {

    def setup() {
        new Institution(uid: "uid1", name: "name1", acronym: "acronym1", userLastModified: "user1").save(flush: true, failOnError: true)
        new Institution(uid: "uid2", name: "name2", acronym: "acronym2", userLastModified: "user2").save(flush: true, failOnError: true)
    }

    def cleanup() {
    }

    void "test list by like name"() {
        when:
        params.q="name1"
        def model = controller.list()

        then:
        model.institutionInstanceList.size() == 1
        model.institutionInstanceList.get(0).name == "name1"
    }

    void "test list by like acronym"() {
        when:
        params.q="acronym2"
        def model = controller.list()

        then:
        model.institutionInstanceList.size() == 1
        model.institutionInstanceList.get(0).acronym == "acronym2"
    }

    void "test list all"() {
        when:
        def model = controller.list()

        then:
        model.institutionInstanceList.size() == 2
    }
}
