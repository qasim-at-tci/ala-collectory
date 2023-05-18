import au.org.ala.collectory.ExtendedPluginAwareResourceBundleMessageSource
import uk.org.nbn.lib.dao.supabase.DataResourceNbnDAOImpl
import grails.util.Holders

// Place your Spring DSL code here
beans = {
    // Custom message source
    messageSource(ExtendedPluginAwareResourceBundleMessageSource) {
        basenames = ["WEB-INF/grails-app/i18n/messages"] as String[]
        cacheSeconds = (60 * 60 * 6) // 6 hours
        useCodeAsDefaultMessage = false
    }
}

beans = {
    def config = Holders.config
    dataResourceNbnDAO(DataResourceNbnDAOImpl,config.supabase.url, config.supabase.servicekey)

}
