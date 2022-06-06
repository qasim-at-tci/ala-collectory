package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup

class ProviderGroupNbn implements Serializable{
    static final String ROLE_COLLECTION_ADMIN = 'ROLE_COLLECTION_ADMIN'   //Collectory admin privilege
    static final String ROLE_COLLECTION_EDITOR = 'ROLE_COLLECTION_EDITOR' //Collectory metadata editing permission (without data connection)
    static final String[] COLLECTORY_ROLES = [ProviderGroup.ROLE_ADMIN, ROLE_COLLECTION_ADMIN, ROLE_COLLECTION_EDITOR]
    static final String ROLE_ADMIN = ProviderGroup.ROLE_ADMIN
    static networkTypes = ["NBN"]

}
