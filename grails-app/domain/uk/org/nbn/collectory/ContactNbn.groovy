package uk.org.nbn.collectory

import au.org.ala.collectory.ContactFor
import au.org.ala.collectory.ProviderGroup

class ContactNbn extends au.org.ala.collectory.Contact{
    String userId           // CAS user ID - this will be null for a lot contacts...

    static mapping = {
        discriminator "au.org.ala.collectory.Contact"
        //We use Contact for the descriminator instead of ContactNBN because if we didnt,
        // everywhere a Contact was created in the plugin, would have to be changed to ContactNBN.create
    }

    static constraints = {
        userId(nullable: true, maxSize: 45)
    }

    /**
     * Returns the list of provider groups that this contact is a contact for.
     *
     * @return list of ProviderGroup or empty list
     */
    @Override
    List<ProviderGroup> getContactsFor() {
        List<ProviderGroup> result = []
        ContactFor.findAllByContact(this).each {
            result << [
                    entity: ProviderGroup._get(it.entityUid),
                    isAdmin: it.administrator,
                    role: it.role
            ]
        }
        //TODO there may be a problem with contacts for dataresources not being removed when the dataresource is removed
        def nonNullResults = result.findAll({ item ->
            item.entity != null
        })
        return nonNullResults
    }


    static hasMany = [approvedAccess: ApprovedAccess]

    def test() {
        return "whoop whoop"
    }
}
