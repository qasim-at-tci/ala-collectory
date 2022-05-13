package uk.org.nbn.collectory

import au.org.ala.collectory.ProviderGroup

class ContactForNbn extends au.org.ala.collectory.ContactFor{
    static mapping = {
        discriminator "au.org.ala.collectory.ContactFor"
    }

    def grailsApplication

    Boolean hasAnnotationAlert() {
        try {
            //get dataresource name from entityUid - could be dataprovider too
            ProviderGroup pg = ProviderGroup._get(entityUid)
            def drName = pg.getName()
            def userId = contact.getUserId()

            def alertUrl = grailsApplication.config.alertUrl
            def url = alertUrl + '/wsopen/alerts/user/' + userId
            def user_alerts = new URL(url).text

            def alert_exists = false
            if (user_alerts.contains('"New annotations on records for ' + drName + '"')) alert_exists = true
            alert_exists
        } catch(Exception ex) {
            false
        }
    }
}
