package uk.org.nbn.collectory

import au.com.bytecode.opencsv.CSVReader
import au.org.ala.collectory.*
import au.org.ala.collectory.resources.PP
import grails.converters.JSON
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.util.StreamCharBuffer

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

class CollectoryTagLib extends au.org.ala.collectory.CollectoryTagLib{


    static namespace = 'cl'

    def isAdmin = { attrs, body ->
        if (collectoryAuthService.isAdmin()) {
            out << body()
        }
    }

    def ifAnyGranted = { attrs, body ->
        def granted = false
        if (grailsApplication.config.security.cas.bypass.toBoolean()) {
            granted = true
        } else {
            def roles = []
            if(attrs.role){
                roles = attrs.role.toString().tokenize(',')
            } else {
                roles = attrs.roles
            }

            roles.each {
                if (request.isUserInRole(it)) {
                    granted = true
                }
            }
        }
        if (granted) {
            out << body()
        }
    }

    def isEditor = { attrs, body ->
        if (collectoryAuthService.isEditor()) {
            out << body()
        }
    }

    def whyCanISeeThis = { attrs ->
        def authReason = ""
        if(grailsApplication.config.security.cas.bypass.toBoolean()){
            authReason += "CAS is currently bypassed for all users;"
        } else {
            if(collectoryAuthService.userInRole(ProviderGroup.ROLE_ADMIN)){
                authReason += "Logged in user has ${ProviderGroup.ROLE_ADMIN};"
            }
            if(collectoryAuthService.userInRole(ProviderGroup.ROLE_COLLECTION_ADMIN)){
                authReason += "Logged in user has ${ProviderGroup.ROLE_COLLECTION_ADMIN};"
            }
            if(collectoryAuthService.userInRole(ProviderGroup.ROLE_COLLECTION_EDITOR)){
                authReason += "Logged in user has ${ProviderGroup.ROLE_COLLECTION_EDITOR};"
            }
            def result = collectoryAuthService.isUserAuthorisedEditorForEntity(collectoryAuthService.authService.getUserId(), attrs.entity)
            if(result.authorised){
                authReason += result.reason
            }
        }

        log.info("Auth reason: " + authReason)
        //add some info - why can i see this page ?
        out << authReason
    }

    /**
     * Writes two lines with date created and date updated.
     *
     * @attr created
     * @attr updated
     */
    def createdAndUpdated = { attrs ->
        out << "<p>" +
                g.message(code: 'metadata.dateCreated') + ": <b>" + g.formatDate(date: attrs.created, format: "yyyy-MM-dd") + "</b>" +
                "<br/>" + g.message(code: 'metadata.dateUpdated') + ": <b>" + g.formatDate(date: attrs.updated, format: "yyyy-MM-dd") + "</b>" +
                (attrs.published ? "<br/>" + g.message(code: 'metadata.dataCurrency') + ": <b>" + g.formatDate(date: attrs.published, format: "yyyy-MM-dd") + "</b>" : "") +
                "</p>"
    }



    //@Override the method in au.org.ala.collectory.CollectoryTagLib
    def isAuth = { attrs, body ->
       //nbn customised this method by removing it from the tag library so added it back and override
        // as an empty method
        log.error("isAuth tag called but its not implemented")
    }

    //@Override the method in au.org.ala.collectory.CollectoryTagLib
    def roles = {
        //nbn customised this method by removing it from the tag library so added it back and override
        //as an empty method
    }

    /**
     * Outputs a button to link to the edit page if the user is authorised to edit.
     *
     * @params uid the entity uid
     * @params action optional action - defaults to edit
     * @params controller optional controller - defaults to not specified (current)
     * @params id optional id to edit if it is different to the uid
     * @params any other attrs are passed to link as url params
     * @params notAuthorisedMessage written in place of button if not authorised - defaults to blank
     * @body the label for the button - defaults to 'Edit' if not specified
     */
    //@Override the method in au.org.ala.collectory.CollectoryTagLib
    def editButton = { attrs, body ->
        if (isAuthorisedToEdit(attrs.uid, request.getUserPrincipal()?.attributes?.email)) {
            def paramsMap
            // anchor class
            paramsMap = [class:'edit btn btn-default']
            // action
            paramsMap << [action: (attrs.containsKey('action')) ? attrs.remove('action').toString() : 'edit']
            // optional controller
            if (attrs.containsKey('controller')) { paramsMap << [controller: attrs.remove('controller').toString()] }
            // id of target
            paramsMap << [id: (attrs.containsKey('id')) ? attrs.remove('id').toString() : attrs.uid]
            attrs.remove('uid')
            // add any remaining attrs as params
            paramsMap << [params: attrs]

            out << "<div><span class='buttons'>"
            out << link(paramsMap) {body() ?: 'Edit'}
            out << "</span></div>"
        } else {
            out << attrs.notAuthorisedMessage
        }
    }

    //@Override the method in au.org.ala.collectory.CollectoryTagLib
    def membershipWithGraphics = { attrs ->
        ProviderGroup pg = attrs.coll
        if (pg) {
            // check collection's membership
            ProviderGroup.networkTypes.each {
                if (pg.isMemberOf(it)) {
                    out << "<span class='label' style='color:#333'>Member of ${it} </span> <br/>"
                    // this will be tidied up when hubs are entities
                    if (it == "CHAH") {
                        out << "<img class='img-polaroid' style='padding-left:25px;' src='" + resource(absolute:"true", dir:"data/network/",file:"CHAH_logo_col_70px_white.gif") + "'/>"
                    }
                    if (it == "CHAEC") {
                        out << "<img class='img-polaroid' src='" + resource(absolute:"true", dir:"data/network/",file:"chaec-logo.png") + "'/>"
                    }
                    if (it == "CHAFC") {
                        out << "<img class='img-polaroid' src='" + resource(absolute:"true", dir:"data/network/",file:"chafc.png") + "'/>"
                    }
                    if (it == "CHACM") {
                        out << "<img class='img-polaroid' src='" + resource(absolute:"true", dir:"data/network/",file:"chacm.png") + "'/>"
                    }
                    if (it == "NBN") {
                        out << "<img class='img-polaroid' src='" + resource(absolute:"true", dir:"data/network/",file:"nbn.png") + "'/>"
                    }
                    out << "<br/>"
                }
            }
            // check institution membership
            /*if (coll.institution) {
                ProviderGroup.networkTypes.each {
                    if (coll.institution.isMemberOf(it)) {
                        out << it
                    }
                }
            }*/
        }
    }

    //@Override the method in au.org.ala.collectory.CollectoryTagLib
    def createAlertsLink(attrs, urlPath) {

        if(!grailsApplication.config.disableAlertLinks.toBoolean()){
            def link = grailsApplication.config.alertUrl + urlPath
            link += '?webserviceQuery=/occurrences/search?q=' + attrs.query
            link += '&uiQuery=/occurrences/search?q=' + attrs.query
            link += '&queryDisplayName=' + attrs.displayName
            link += '&baseUrlForWS=' + grailsApplication.config.biocacheServicesUrl
            link += '&baseUrlForUI=' + grailsApplication.config.biocacheUiURL
            link += '&resourceName=' + grailsApplication.config.alertResourceName
            if (attrs.userId) {
                link += '&userId=' + attrs.userId
                link += '&redirect=' + request.getScheme() + '://' + request.getServerName() + ':' + request.getServerPort() + request.getContextPath() + '/' + params.controller + '/' + params.action + '/' + params.id + (request.getQueryString()? '?' + request.getQueryString() : '')
            }
            out << "<a href=\"" + link +"\" class='btn btn-default' alt='"+attrs.altText+"'><i class='glyphicon glyphicon-bell'></i> "+ attrs.linkText + "</a>"
        }
    }

    boolean isAuthorisedToEdit(uid, email) {
        if (collectoryAuthService.isEditor()) {
            return true
        }
        else if (email) {
            ProviderGroup pg = ProviderGroup._get(uid)
            if (pg) {
                return pg.isAuthorised(email)
            }
        }
        return false
    }

}
