package uk.org.nbn.collectory

import au.org.ala.collectory.DataHub
import au.org.ala.collectory.DataLink
import au.org.ala.collectory.DataProvider
import au.org.ala.collectory.DataResource
import au.org.ala.collectory.ExternalIdentifier
import au.org.ala.collectory.Institution
import au.org.ala.collectory.Collection
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import grails.util.Holders
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CrudService)
@TestMixin(ControllerUnitTestMixin)
@Mock([DataProvider, DataLink, ExternalIdentifier, DataHub, DataResource, Institution, Collection])
class CrudServiceSpec extends Specification {

    def setup() {
    }
//
//    def tearDown(){
//        java.util.LinkedHashMap.metaClass.asType = null
//    }

    def cleanup() {
    }

    void "test groupClassification added to DataProvider JSON"() {
        setup:
        def dp = new DataProvider(uid: "dp101", groupClassification:"dp101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataProvider(dp)
        def json = JSON.parse(res.toString())
        json.groupClassification == "dp101_groupClassification"
    }

    void "test NBN networkMembership added to DataProvider JSON"() {
        setup:
        def dp = new DataProvider(uid: "dp101", networkMembership:"['CHACM','NBN']", groupClassification:"dp101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataProvider(dp)
        def json = JSON.parse(res.toString())
        println json.networkMembership
        json.networkMembership[1].name == 'National Biodiversity Network'
    }

    void "test when DataProvider has no networkMembership specified, read should not error"() {
        setup:
        def dp = new DataProvider(uid: "dp101", groupClassification:"dp101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataProvider(dp)
        def json = JSON.parse(res.toString())
        !json.networkMembership
    }

    void "test groupClassification added to DataHub JSON"() {
        setup:
        def dp = new DataHub(uid: "dh101", groupClassification:"dh101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataHub(dp)
        def json = JSON.parse(res.toString())
        json.groupClassification == "dh101_groupClassification"
    }

    void "test NBN networkMembership added to DataHub JSON"() {
        setup:
        def dp = new DataHub(uid: "dh101", networkMembership:"['CHACM','NBN']", groupClassification:"dh101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataHub(dp)
        def json = JSON.parse(res.toString())
        println json.networkMembership
        json.networkMembership[1].name == 'National Biodiversity Network'
    }

    void "test when DataHub has no networkMembership specified, read should not error"() {
        setup:
        def dp = new DataHub(uid: "dh101",groupClassification:"dh101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataHub(dp)
        def json = JSON.parse(res.toString())
        !json.networkMembership
    }

    void "test NBN networkMembership added to DataResource JSON"() {
        setup:
        def dp = new DataResource(uid: "dr101", networkMembership:"['CHACM','NBN']", groupClassification:"dr101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataResource(dp)
        def json = JSON.parse(res.toString())
        println json.networkMembership
        json.networkMembership[1].name == 'National Biodiversity Network'
    }

    void "test when DataResource has no networkMembership specified, read should not error"() {
        setup:
        def dp = new DataResource(uid: "dr101",groupClassification:"dr101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readDataResource(dp)
        def json = JSON.parse(res.toString())
        !json.networkMembership
    }


    void "test NBN networkMembership added to Collection JSON"() {
        setup:
        def dp = new Collection(uid: "co101", networkMembership:"['CHACM','NBN','UNKOWN']", groupClassification:"co101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readCollection(dp)
        def json = JSON.parse(res.toString())
        println json.networkMembership
        json.networkMembership[1].name == 'National Biodiversity Network'
    }

    void "test when Collection has no networkMembership specified, read should not error"() {
        setup:
        def dp = new Collection(uid: "co101",groupClassification:"co101_groupClassification", name: "name1", email: "hmj@test.com", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)

        expect:
        def res = service.readCollection(dp)
        def json = JSON.parse(res.toString())
        !json.networkMembership
    }


    void "test nbn customised static fields"() {
        expect:
        service.baseStringProperties.contains("groupClassification")
        service.dataResourceStringProperties.contains("dateCreated")
        service.dataResourceStringProperties.contains("lastUpdated")
    }
}
