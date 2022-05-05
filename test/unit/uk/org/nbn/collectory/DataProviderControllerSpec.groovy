package uk.org.nbn.collectory

import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.DataProvider
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DataProviderController)
@Mock([DataProvider, ActivityLog])
class DataProviderControllerSpec extends Specification {
    def setup() {
        new DataProvider(uid: "uid", name: "name1", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)
        new DataProvider(uid: "uid", name: "name2", acronym: "acronym2", userLastModified: "username").save(flush: true, failOnError: true)
    }

    def cleanup() {
    }

    void "test list by like name"() {
        when:
        params.q="name1"
        def model = controller.list()

        then:
        model.instanceList.size() ==1
        model.instanceList.get(0).name == "name1"
    }

    void "test list by like acronym"() {
        when:
        params.q="acronym1"
        def model = controller.list()

        then:
        model.instanceList.size() ==1
        model.instanceList.get(0).acronym == "acronym1"
    }

    void "test list all"() {
        when:
        def model = controller.list()

        then:
        model.instanceList.size() ==2
    }
}
