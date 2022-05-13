package uk.org.nbn.collectory

import au.org.ala.collectory.CrudService
import au.org.ala.collectory.MetadataService
import au.org.ala.collectory.Institution
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import sun.security.x509.OtherName

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DataController)
@Mock(Institution)
class DataControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test getEntity for tempDataResource "() {
        setup:
        params.entity = 'tempDataResource'

        when:
        controller.getEntity()

        then:
        controller.forward(controller: 'tempDataResource', action: 'getEntity')
    }

    void "test getEntity for ProviderGroup "() {
        setup:
        def now = new Date()
        def obj =[]
        def entityInJson = ""
        params.entity = "Institution"
        controller.crudService = Mock(CrudService)
        controller.crudService.readInstitution(*_) >> obj
        controller.metadataService = Mock(MetadataService)
        controller.metadataService.convertAnyLocalPaths(*_) >> entityInJson
        def eTag = ("uid2:"+now).encodeAsMD5()
        params.pg = new Institution(uid: "uid1", name: "name1", acronym: "acronym1", userLastModified: "user1").save(flush: true, failOnError: true)
        params.pg.lastUpdated = now
        new Institution(uid: "uid2", name: "name2", acronym: "acronym2", userLastModified: "user2").save(flush: true, failOnError: true)
        controller.cacheAwareRender = { var1, var2, var3 -> true}

        when:
        controller.getEntity()

        then:
        controller.cacheAwareRender entityInJson, params.pg.lastUpdated, eTag

    }
}
