package uk.org.nbn.collectory

import au.org.ala.collectory.Contact
import au.org.ala.collectory.DataResource
import au.org.ala.collectory.ProviderGroup
import grails.transaction.Transactional

@Transactional
class CollectoryAuthService extends au.org.ala.collectory.CollectoryAuthService{

    /**
     * A user is an ADMIN if they have ROLE_COLLECTION_ADMIN roles.
     *
     * @return
     */
    @Override
    def isAdmin() {
        def adminFlag = super.isAdmin()
        return adminFlag || (authService && authService.userInRole(ProviderGroup.ROLE_COLLECTION_ADMIN))
    }

    @Override
    def authorisedForUser(Contact contact) {
        def map = super.authorisedForUser(contact)
        map.sorted.each {
            def pg = ProviderGroup._get(it.uid)
            it.entityType=pg.entityType()
        }
        map
    }

    @Override
    protected boolean isAuthorisedToEdit(uid) {
        //customised version had taken this out. To realign with ALA it was put back in
        //but now we override it to make sure if its used, it returns false (is safe)
        return false
    }

    /**
     * A user is an EDITOR if they have either the ROLE_ADMIN or ROLE_COLLECTION_ADMIN roles.
     *
     * @return
     */
    def isEditor() {
        def adminFlag = false
        if(grailsApplication.config.security.cas.bypass.toBoolean()) {
            adminFlag = true
        } else {
            if(authService) {
                adminFlag = authService.userInRole(ProviderGroup.ROLE_COLLECTION_EDITOR) ||
                        authService.userInRole(ProviderGroup.ROLE_ADMIN) ||
                        authService.userInRole(ProviderGroup.ROLE_COLLECTION_ADMIN)
            }
        }
        return adminFlag
    }

    def getRoles(){
        def roles = []
        ProviderGroup.COLLECTORY_ROLES.each {
            if(authService.userInRole(it)){
                roles << it
            }
        }
        roles
    }

    /**
     * If a logged in user is an administrator for a data resource then they can edit.
     * Likewise, if they are the administrator of a provider or institution they can edit
     * an institution/provider metadata and any resources underneath that institution/provider.
     *
     * @param userId
     * @param instance A dataresource, collection, provider or institution
     * @return
     */
    def isUserAuthorisedEditorForEntity(userId, instance){
        def authorised = false
        def reason = ""
        if(instance) {
            def contacts = instance.getContacts()
            contacts.each {
                if (it.contact.userId == userId && it.administrator) {
                    //CAS contact
                    authorised = true
                    reason = "User is an administrator for ${instance.entityType()} : ${instance.uid} : ${instance.name}"
                }
            }
        }

        if(instance instanceof DataResource){
            if(instance.getInstitution()){
                //check institution contacts
                def contacts = instance.getInstitution().getContacts()
                contacts.each {
                    if (it.contact.userId == userId && it.administrator) {
                        //CAS contact
                        authorised = true
                        reason = "User is an administrator for parent entity ${instance.entityType()} : ${instance.id} : ${instance.name}"
                    }
                }
            }
            if(instance.getDataProvider()){
                //check data provider contacts
                //check institution contacts
                def contacts = instance.getDataProvider().getContacts()
                contacts.each {
                    if (it.contact.userId == userId && it.administrator) {
                        //CAS contact
                        authorised = true
                        reason = "User is an administrator for parent entity ${instance.entityType()} : ${instance.id} : ${instance.name}"
                    }
                }
            }
        }
        [authorised:authorised, reason:reason]
    }
}
