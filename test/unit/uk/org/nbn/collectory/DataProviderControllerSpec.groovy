package uk.org.nbn.collectory

import au.org.ala.web.AuthService
import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.Contact
import au.org.ala.collectory.ContactFor
import au.org.ala.collectory.DataProvider
import au.org.ala.collectory.Institution
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DataProviderController)
@Mock([DataProvider, ActivityLog, ContactNbn, ContactForNbn])
class DataProviderControllerSpec extends ProviderGroupControllerSpec {
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
        model.instanceList.size() == 1
        model.instanceList.get(0).name == "name1"
    }

    void "test list by like acronym"() {
        when:
        params.q="acronym1"
        def model = controller.list()

        then:
        model.instanceList.size() == 1
        model.instanceList.get(0).acronym == "acronym1"
    }

    void "test list all"() {
        when:
        def model = controller.list()

        then:
        model.instanceList.size() == 2
    }

    void "test myList"() {
        setup:
        def c1 = new ContactNbn(userId:"1", firstName: "Peter", lastName: "Flemming", publish: true, email: "a@a.com", userLastModified: "test").save(flush: true, failOnError: true)
        def c2 = new ContactNbn(userId:"2", firstName: "Dave", lastName: "Jones", publish: true, email: "b@b.com", userLastModified: "test").save(flush: true, failOnError: true)
        def dp = new DataProvider(guid: "ABC", uid:'dp13', name: "XYZ", userLastModified: "test").save(flush: true, failOnError: true)
        def ncf = new ContactFor(contact: c2, entityUid: dp.uid, role: "Manager", administrator: true, primaryContact: true, userLastModified: "test").save(flush: true, failOnError: true)
        controller.authService = Mock(AuthService)
        controller.authService.getUserId() >> 2

        when:
        controller.myList()

        then:
        model == [instanceList: [dp], entityType: 'DataProvider', instanceTotal: 1]


    }
}
