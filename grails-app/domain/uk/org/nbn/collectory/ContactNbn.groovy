package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup

class ContactNbn extends au.org.ala.collectory.Contact{
    static mapping = {
        discriminator "au.org.ala.collectory.Contact"
        //We use Contact for the descriminator instead of ContactNBN because if we didnt,
        // everywhere a Contact was created in the plugin, would have to be changed to ContactNBN.create
    }

    static hasMany = [approvedAccess: ApprovedAccess]

    def test() {
        return "whoop whoop"
    }
}
