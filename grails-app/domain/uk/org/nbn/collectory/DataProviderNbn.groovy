package uk.org.nbn.collectory

import au.org.ala.collectory.ContactFor
import au.org.ala.collectory.DataProvider

class DataProviderNbn extends au.org.ala.collectory.DataProvider {
    static mapping = {
        discriminator "au.org.ala.collectory.DataProvider"
    }

    static hasMany = [approvals: ApprovedAccess]

    /**
     * Gets a list of contacts along with their role and admin status for this group
     *
     */
    @Override
    List<ContactFor> getContacts() {
        // handle this being called before it has been saved (and therefore doesn't have an id - and can't have contacts)
        if (dbId()) {
            return ContactForNbn.findAllByEntityUid(uid)
//            return ContactFor.findAllByEntityUid(uid)
        } else {
            []
        }
    }

}
