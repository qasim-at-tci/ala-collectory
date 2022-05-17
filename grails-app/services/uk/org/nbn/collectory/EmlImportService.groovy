package uk.org.nbn.collectory

import au.org.ala.collectory.Contact
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
    def extractFromEml(eml, dataResource) {
        def contacts = super.extractFromEml(eml, dataResource)

        contacts.each { contact ->
            if (!contact.phone || !contact.mobile) {
                if (!addPhoneToContact(eml.dataset.creator, contact))
                    addPhoneToContact(eml.dataset.metadataProvider, contact)
            }
        }

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


