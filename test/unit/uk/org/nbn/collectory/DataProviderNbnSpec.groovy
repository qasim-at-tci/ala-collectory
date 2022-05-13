package uk.org.nbn.collectory

import au.org.ala.collectory.DataProvider
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(DataProviderNbn)
class DataProviderNbnSpec extends Specification{

    def setup() {
        new DataProviderNbn(uid: "nbn", name: "name1", acronym: "acronym1", userLastModified: "username").save(flush: true, failOnError: true)
    }

    void "test that find on base class returns the derived class"() {
        when:
        def nbn = DataProvider.findByUid("nbn")

        then:
        nbn instanceof DataProviderNbn
    }
}
