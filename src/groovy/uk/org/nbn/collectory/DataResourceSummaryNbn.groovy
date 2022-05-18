package uk.org.nbn.collectory

import au.org.ala.collectory.DataResourceSummary

class DataResourceSummaryNbn extends DataResourceSummary {
    Date dateCreated
    Date lastUpdated

    DataResourceSummaryNbn(DataResourceSummary drs) {
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