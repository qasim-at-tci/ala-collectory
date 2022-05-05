package uk.org.nbn.collectory

import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.DataResource
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DataResourceController)
@Mock([DataResource, ActivityLog])
class DataResourceControllerSpec extends Specification {

    def setup() {
        new DataResource(uid: "uid", resourceType: 'website',
                name: "name1", websiteUrl: "websiteUrl", userLastModified: 'bulk load from BIE').save(flush: true, failOnError: true)
        new DataResource(uid: "uid", resourceType: 'website',
                name: "name2", websiteUrl: "websiteUrl", userLastModified: 'bulk load from BIE').save(flush: true, failOnError: true)
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



    void "test list all"() {
        when:
        def model = controller.list()

        then:
        model.instanceList.size() == 2
    }
}
