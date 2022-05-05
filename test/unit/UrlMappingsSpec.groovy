
import au.org.ala.collectory.SensitiveAccessController
import uk.org.nbn.collectory.DataProviderController
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UrlMappings)
@Mock([SensitiveAccessController,DataProviderController])
class UrlMappingsSpec extends Specification {

    void "test sensitiveAccess mappings"() {
        expect:
        assertForwardUrlMapping("/ws/sensitiveAccess/1", controller:'sensitiveAccess',action:'lookup') {
            userId = '1'
        }
    }

    void "test dataProvider/sensitiveSpecies mappings"() {
        expect:
        assertForwardUrlMapping("/ws/dataProvider/sensitiveSpecies/1", controller:'dataProvider',action:'sensitiveSpeciesForDataProvider', method: 'GET') {
            uid = '1'
        }
    }

    void "test dataProvider/speciesRecords for lsid mappings"() {
        expect:
        assertForwardUrlMapping("/ws/dataProvider/3/speciesRecords/4", controller:'dataProvider',action:'speciesRecordsForDataProvider', method: 'GET') {
            uid = '3'
            lsid = '4'
        }
    }
}
