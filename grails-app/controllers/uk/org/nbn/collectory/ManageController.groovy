package uk.org.nbn.collectory

class ManageController extends au.org.ala.collectory.ManageController{

    def list() {
        def model = super.list.call()
        model.remove("show")
        model.put("userRoles", collectoryAuthService.getRoles())

        def view = "list"
        if(collectoryAuthService.isEditor()){
            view = "adminList"
        }

        render(view: view, model: model)
    }
}
