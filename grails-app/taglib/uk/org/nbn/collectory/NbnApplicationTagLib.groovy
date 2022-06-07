package uk.org.nbn.collectory

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

class NbnApplicationTagLib extends ApplicationTagLib{

    public static final String PLUGIN_NAME = "collectory"

    /**
     * resource(absolute:"true", dir:"data/network/",file:"nbn.png") will pull the resource from the collectory plugin
     * resource(nbn:"true", absolute:"true", dir:"data/network/",file:"nbn.png") will pull the resource from the nbn web-app
     */
    Closure resource = { attrs ->
        if (!attrs.plugin && !attrs.nbn) {
            attrs.plugin = PLUGIN_NAME
        }
        super.resource.call(attrs)
    }

}
