package uk.org.nbn.collectory


class PublicController extends au.org.ala.collectory.PublicController {

    @Override
    def showDataProvider() {
        def model = super.showDataProvider()
        if (model) {
            log.info("user id = " + collectoryAuthService?.authService.getUserId())

            def isCollectionEditor = collectoryAuthService?.isUserAuthorisedEditorForEntity(collectoryAuthService?.authService.getUserId(), model.instance)
            log.info("isCollectionEditor = " + isCollectionEditor["authorised"] )
            model.hideSensitiveManagement = (grailsApplication.config.sensitive?.hideManagementPanel?:'true').toBoolean()
            model.viewerIsAdmin = isCollectionEditor["authorised"].asBoolean() || ((grailsApplication.config.dataprovider?.showAdminLink?:'false')=='true')
        }
        model
    }
}
