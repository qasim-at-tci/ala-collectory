package uk.org.nbn.collectory

import au.org.ala.collectory.DataHub
import au.org.ala.collectory.DataLink
import au.org.ala.collectory.DataResource
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(DataResourceNbn)
@Mock([DataHub, DataLink])
class DataResourceNbnSpec extends Specification{
    def dr

    def setup() {
        dr = new DataResourceNbn(uid: "dr101", name: "name1", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)
    }

    void "test build summary"() {
        expect:
        def summary = dr.buildSummary()
        summary.dateCreated
        summary.lastUpdated
    }
}
