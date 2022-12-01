package uk.org.nbn.collectory

import au.org.ala.collectory.Contact
import au.org.ala.collectory.Licence
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import groovy.util.slurpersupport.GPathResult
import org.apache.commons.lang.StringUtils



@Transactional
class EmlImportService extends au.org.ala.collectory.EmlImportService{

    EmlImportService() {
        def addtionalEmlFields = [
        pubShortDescription: { eml -> StringUtils.left(eml.dataset.abstract?.para[0]?.toString(), 99) },
        techDescription: {eml -> this.collectParas(eml.dataset.additionalInfo?.para) },

        //FFTF_NOT_NEEDED LEFT OUT DECOUPLED VERSION
//        addrCity: { eml -> eml.dataset.contact?.address?.city?:'' },
//        addrCountry: { eml -> eml.dataset.contact?.address?.country?:'' },
//        addrPostcode: { eml -> eml.dataset.contact?.address?.postalCode?:'' },
//        addrState: { eml -> eml.dataset.contact?.address?.administrativeArea?:'' },
//        addrStreet: { eml -> eml.dataset.contact?.address?.deliveryPoint?:'' },
        //END FFTF_NOT_NEEDED LEFT OUT DECOUPLED VERSION

        keywords: { eml -> this.collectKeywords(eml.dataset.keywordSet?.keyword) },
        ]

        emlFields.putAll(addtionalEmlFields)

    }

    protected def collectKeywords(GPathResult keywordSet) {
        keywordSet?.list().inject(null, { acc, keyword -> acc == null ? (keyword.text()?.trim() ?: "") : acc + " " + (keyword.text()?.trim() ?: "") })
    }

    @Override
    def getLicence(eml){

        def licenceInfo = [licenseType:'', licenseVersion:'']
        //try and match the acronym to licence
        def rights = this.collectParas(eml.dataset.intellectualRights?.para)

        def matchedLicence = Licence.findByAcronym(rights)
        if(!matchedLicence) {
            //attempt to match the licence
            def licenceUrl = eml.dataset.intellectualRights?.para?.ulink?.@url.text().trim() //NBN added
            if (licenceUrl.endsWith("/")) licenceUrl = licenceUrl[0..-2] //standardise without trailing '/' NBN added
            def licence = Licence.findByUrl(licenceUrl)
            if (licence == null) {
                if (licenceUrl.contains("http://")) {
                    matchedLicence = Licence.findByUrl(licenceUrl.replaceAll("http://", "https://"))
                } else {
                    matchedLicence = Licence.findByUrl(licenceUrl.replaceAll("https://", "http://"))
                }
            } else {
                matchedLicence = licence
            }
        }

        if(matchedLicence){
            licenceInfo.licenseType = matchedLicence.acronym
            licenceInfo.licenseVersion = matchedLicence.licenceVersion
        }

        licenceInfo
    }

    @Override
    @NotTransactional
    //overriding transactional methods and calling super produces stackoverflow ( fixed in > Gorm 6.1.4)
    def extractFromEml(eml, dataResource) {
        extractFromEmlTransactional(eml, dataResource)
    }

    def extractFromEmlTransactional(eml, dataResource) {
        def contacts = super.extractFromEml(eml, dataResource)

        contacts.each { contact ->
            contact.firstName = contact.firstName?:''
            contact.lastName = contact.lastName?:''
            contact.email = contact.email?:''
            if (!contact.phone || !contact.mobile) {
                if (!addPhoneToContact(eml.dataset.creator, contact))
                    addPhoneToContact(eml.dataset.metadataProvider, contact)
            }
        }
        contacts
    }


    private addPhoneToContact(contactElements, contact) {
        if (contactElements) {
            contactElements.each {
                if (it.electronicMailAddress == contact.email) {
                    contact.phone = (it.phone.find { it.@phonetype == "voice" } + it.phone.find { it.attributes().size() == 0 }).join(' ')
                    //default phone type is voice if no attribute defining type
                    contact.mobile = it.phone.find { it.@phonetype == "mobile" }
                    return true
                }
            }
        }
        return false
    }
}



