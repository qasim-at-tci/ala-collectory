package uk.org.nbn.collectory

import au.org.ala.collectory.Licence
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LicenceController)
@Mock(Licence)
class LicenceControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test save Licence with website url that has trailing /"() {
        setup:
        params.acronym = 'CC0'
        params.name = "Creative Commons Zero"
        params.url = "http://test.com/"
        params.imageUrl = "https://licensebuttons.net/l/zero/1.0/88x31.png"
        params.licenceVersion = "1.0"

        when:
        controller.save()

        then:
        Licence.findAll().get(0).url == "http://test.com"

    }

    void "test save Licence with website url that does not have a trailing /"() {
        setup:
        params.acronym = 'CC0'
        params.name = "Creative Commons Zero"
        params.url = "http://test.com"
        params.imageUrl = "https://licensebuttons.net/l/zero/1.0/88x31.png"
        params.licenceVersion = "1.0"

        when:
        controller.save()

        then:
        Licence.findAll().get(0).url == "http://test.com"

    }

    void "test update Licence with website url that has trailing /"() {
        setup:
        Licence licence = new Licence(acronym : 'CC0', name : "Creative Commons Zero", url : "http://test.com",
                imageUrl : "https://licensebuttons.net/l/zero/1.0/88x31.png",
                licenceVersion : "1.0").save(flush: true, failOnError: true)
        params.url = "http://modified.com/"

        when:
        controller.update(licence.id, null)

        then:
        Licence.findAll().get(0).url == "http://modified.com"

    }

    void "test update Licence with website url that does not have a trailing /"() {
        setup:
        Licence licence = new Licence(acronym : 'CC0', name : "Creative Commons Zero", url : "http://test.com",
                imageUrl : "https://licensebuttons.net/l/zero/1.0/88x31.png",
                licenceVersion : "1.0").save(flush: true, failOnError: true)
        params.url = "http://modified.com"

        when:
        controller.update(licence.id, null)

        then:
        Licence.findAll().get(0).url == "http://modified.com"

    }
}
