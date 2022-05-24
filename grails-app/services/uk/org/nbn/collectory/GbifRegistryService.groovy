package uk.org.nbn.collectory

import au.com.bytecode.opencsv.CSVWriter
import au.org.ala.collectory.DataLink
import au.org.ala.collectory.DataResource
import au.org.ala.collectory.Institution
import grails.transaction.Transactional
import groovy.json.JsonSlurper

@Transactional
class GbifRegistryService extends au.org.ala.collectory.GbifRegistryService {

    @Override
    def writeCSVReportForGBIF(outputStream) {

        log.debug("NBN Starting report.....")
        def url = grailsApplication.config.biocacheServicesUrl + "/occurrences/search?q=*:*&facets=data_resource_uid&pageSize=0&facet=on&flimit=-1"

        def js = new JsonSlurper()
        def biocacheSearch = js.parse(new URL(url), "UTF-8")

        def csvWriter = new CSVWriter(new OutputStreamWriter(outputStream))

        String[] header = [
                "UID",
                "Data resource",
                "Data resource GBIF ID",
                "Record count",

                "Data provider UID",
                "Data provider name",
                "Data provider GBIF ID",
                "Data provider classification",
                "Data provider memberships",

                "Institution UID",
                "Institution name",
                "Institution GBIF ID",

                "Licence",

                "Shareable with GBIF",
                "Licence Issues (preventing sharing)",

                "Not Shareable (no owner)",
                "Flagged as Not-Shareable",
                "Provided by GBIF",

                "Linked to Data Provider",
                "Linked to Institution",

                "Verified",

                "dateCreated",
                "dataCurrency"
        ]

        csvWriter.writeNext(header)

        biocacheSearch.facetResults[0].fieldResult.each { result ->
            def uid = result.fq.replaceAll("\"","").replaceAll("data_resource_uid:","")

            //retrieve current licence
            def dataResource = DataResourceNbn.findByUid(uid)
            if(dataResource) {

                def isShareable = true
                def licenceIssues = false
                def flaggedAsNotShareable = false
                def providedByGBIF = false
                def notShareableNoOwner = false

                //retrieve current licence
                def dataProvider
                def institution

                //get the data provider if available...
                def dataLinks = DataLink.findAllByProvider(uid)
                def institutionDataLink

                if(dataLinks){
                    //do we have institution link ????
                    institutionDataLink = dataLinks.find { it.consumer.startsWith("in")}
                    if(institutionDataLink){
                        //we have an institution
                        institution = Institution.findByUid(institutionDataLink.consumer)
                    }
                }

                if(!institutionDataLink) {
                    dataProvider = dataResource.getDataProvider()
                    if(!dataProvider){
                        notShareableNoOwner = true
                        isShareable = false //no institution and no data provider
                    }
                }

                if (dataResource.licenseType == null || !getGBIFCompatibleLicence(dataResource.licenseType)) {
                    licenceIssues = true
                    isShareable = false
                }

                if (!dataResource.isShareableWithGBIF) {
                    flaggedAsNotShareable = true
                    isShareable = false
                }

                if (dataResource.gbifDataset) {
                    providedByGBIF = true
                    isShareable = false
                }

                String[] row = [
                        dataResource.uid,
                        dataResource.name,
                        dataResource.gbifRegistryKey,

                        result.count,

                        dataProvider?.uid,
                        dataProvider?.name,
                        dataProvider?.gbifRegistryKey,
                        dataProvider?.groupClassification,
                        dataResource.listNetworkMembership().join(" "),

                        institution?.uid,
                        institution?.name,
                        institution?.gbifRegistryKey,

                        dataResource.licenseType,

                        isShareable ? "yes" : "no",
                        licenceIssues ? "yes" : "no",
                        notShareableNoOwner ? "yes" : "no",
                        flaggedAsNotShareable ? "yes" : "no",
                        providedByGBIF ? "yes" : "no",

                        institution ? "yes" : "no",
                        dataProvider ? "yes" : "no",

                        dataResource.isVerified() ? "yes" : "no",

                        dataResource.dateCreated,
                        dataResource.dataCurrency?:""

                ]
                csvWriter.writeNext(row)
            }
        }
        csvWriter.close() //otherwise file arbitrarily truncated
    }
}
