package uk.org.nbn.collectory

import au.org.ala.collectory.DataResourceSummary

class DataResourceSummaryNbn extends DataResourceSummary {
    Date dateCreated
    Date lastUpdated

    DataResourceSummaryNbn(DataResourceSummary drs) {
        //ProviderGroupSummary
        id = drs.id
        uid = drs.uid
        uri = drs.uri
        name = drs.name
        acronym = drs.acronym
        shortDescription = drs.shortDescription
        lsid = drs.lsid

        //DataResourceSummary
        taxonomyCoverageHints = taxonomyCoverageHints
        dataProvider = drs.dataProvider
        dataProviderId = drs.dataProviderId
        dataProviderUid = drs.dataProviderUid
        institution = drs.institution
        institutionUid = drs.institutionUid
        downloadLimit = drs.downloadLimit
        relatedCollections = drs.relatedCollections
        relatedInstitutions = drs.relatedInstitutions
        hubMembership = drs.hubMembership
    }

}