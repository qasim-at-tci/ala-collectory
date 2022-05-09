package uk.org.nbn.collectory

import au.org.ala.collectory.Collection
import uk.org.nbn.BootStrap
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(Collection)
class CollectionSpec extends Specification {

    def setup() {
        BootStrap.initCollectionDomain()
        new Collection(uid:1, userLastModified: "u1", name:'dog').save(flush: true, failOnError: true)
        new Collection(uid:2, userLastModified: "u2", name:'cat').save(flush: true, failOnError: true)
        new Collection(uid:3, userLastModified: "u3", name:'monkey').save(flush: true, failOnError: true)
    }

    void "test list (list everything)"() {
        expect:
        Collection.list().size() == 3
    }

    void "test list with q param that matches"() {
        expect:
        def res = Collection.list(q:"cat")
        res.size() == 1
        res[0].name == "cat"
    }

    void "test list with q param that does not match"() {
        expect:
        Collection.list(q:"donkey").size() == 0
    }

    void "test list all with max param"() {
        expect:
        def res = Collection.list(max:2).size() == 2

    }

    void "test list all with offset param"() {
        expect:
        def res = Collection.list(offset:2).size() == 1

    }
}
