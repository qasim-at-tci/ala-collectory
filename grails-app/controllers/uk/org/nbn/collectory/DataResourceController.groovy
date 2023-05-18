package uk.org.nbn.collectory

import au.org.ala.collectory.Action
import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.DataResource
import uk.org.nbn.lib.dao.DataResourceNbnDAO
import uk.org.nbn.lib.model.DataResourceNbn
import uk.org.nbn.lib.model.DataResourceNbnUpdateBuilder

class DataResourceController extends au.org.ala.collectory.DataResourceController implements ProviderGroupControllerTrait{

    DataResourceNbnDAO dataResourceNbnDAO

    def list() {
        if(params.q){
            if (params.message) {
                flash.message = params.message
            }
            ActivityLog.log username(), isAdmin(), Action.LIST
            def search = DataResource.findAllByNameIlike('%' + params.q +'%')
            [instanceList: search, entityType: 'DataResource', instanceTotal: search.size()]
        }
        else {
            super.list.call()
        }
    }

    def show = {
        def model = super.show.call()
        model.dataResourceNbn = dataResourceNbnDAO.getByUid(model.instance.uid)
        model
    }


    def accessControls() {
        def pg = get(params.id)
        if (!pg) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: "${entityNameLower}.label", default: entityNameLower), params.id])}"
            redirect(action: "list")
        } else {
            // are they allowed to edit
            if (isAuthorisedToEdit(pg.uid)) {
                def publicResolutionMap = ["0":0, "1000":"1km", "2000":"2km", "5000":"5km", "10000":"10km"]
                DataResourceNbn dataResourceNbn = dataResourceNbnDAO.getByUid(pg.uid)
                [publicResolutionMap:publicResolutionMap, dataResourceNbn : dataResourceNbn, command: pg]
            } else {
                response.setHeader("Content-type", "text/plain; charset=UTF-8")
                render(message(code: "provider.group.controller.04", default: "You are not authorised to access this page."))
            }
        }

    }

    def updateAccessControls() {
        def pg = get(params.id)
        if (!pg) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: "${entityNameLower}.label", default: entityNameLower), params.id])}"
            redirect(action: "list")
        } else {
            if (isAuthorisedToEdit(pg.uid)) {
                if (!dataResourceNbnDAO.getByUid(pg.uid)) {
                    DataResourceNbn dataResourceNbn = new DataResourceNbn(pg.uid, params.publicResolutionToBeApplied.toInteger(), true)
                    dataResourceNbnDAO.create(dataResourceNbn)
                }
                else{
                    DataResourceNbnUpdateBuilder dataResourceNbnUpdateBuilder = new DataResourceNbnUpdateBuilder();
                    dataResourceNbnUpdateBuilder
                            .setPublicResolutionToBeApplied(params.publicResolutionToBeApplied.toInteger())
                            .setNeedToReload(true);
                    dataResourceNbnDAO.update(dataResourceNbnUpdateBuilder.build(),pg.uid)
                }
                redirect(action: "show", id: params.uid ?: params.id)
            } else {
                response.setHeader("Content-type", "text/plain; charset=UTF-8")
                render(message(code: "provider.group.controller.04", default: "You are not authorised to access this page."))
            }
        }
    }

}
