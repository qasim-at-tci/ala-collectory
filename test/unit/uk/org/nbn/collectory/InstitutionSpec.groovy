package uk.org.nbn.collectory

import au.org.ala.collectory.Institution
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Institution)
class InstitutionSpec extends Specification{


    void "test keywords can be null"() {
        setup:
        def inst = new Institution(uid: "in101", name: "name1", acronym: "acronym1", userLastModified: "username")

        expect:
        inst.validate()
    }
}
