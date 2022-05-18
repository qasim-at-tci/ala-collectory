package au.org.ala.collectory

import au.org.ala.collectory.resources.DarwinCoreFields
import spock.lang.Specification
import uk.org.nbn.BootStrap

class DarwinCoreFieldsSpec extends Specification{

    def setup() {
        BootStrap.initDarwinCoreFields()
    }

    void "test NBN fields added"() {
        expect:
        def dcf = DarwinCoreFields.fields.find{it.name == "georeferenceVerificationStatus"}
        def dcf2 = DarwinCoreFields.fields.find{it.name == "identificationVerificationStatus"}

        dcf.values == ["","Accepted", "Accepted - correct", "Accepted - considered correct", "Unconfirmed", "Unconfirmed - plausible", "Unconfirmed - not reviewed"]

        dcf2.values == ["","Accepted", "Accepted - correct", "Accepted - considered correct", "Unconfirmed", "Unconfirmed - plausible", "Unconfirmed - not reviewed"]
    }


}
