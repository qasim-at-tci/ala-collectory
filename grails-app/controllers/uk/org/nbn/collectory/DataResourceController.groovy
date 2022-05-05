package uk.org.nbn.collectory

import au.org.ala.collectory.Action
import au.org.ala.collectory.ActivityLog
import au.org.ala.collectory.DataResource

class DataResourceController extends au.org.ala.collectory.DataResourceController{

    @Override
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
            super.list()
        }
    }
}
