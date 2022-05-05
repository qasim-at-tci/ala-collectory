package uk.org.nbn.collectory

import au.org.ala.collectory.Institution

class InstitutionController extends au.org.ala.collectory.InstitutionController {

    @Override
    def list() {
        if (params.q) {
            if (params.message) {
                flash.message = params.message
            }
            def results = Institution.findAllByNameLikeOrAcronymLike('%' + params.q + '%', '%' + params.q + '%')
            [institutionInstanceList: results, institutionInstanceTotal: results.size()]
        } else {
            super.list()
        }
    }


}
