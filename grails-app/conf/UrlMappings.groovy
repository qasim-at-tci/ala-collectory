class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/ws/sensitiveAccess/$userId"(controller:'sensitiveAccess',action:'lookup')

        "/ws/dataProvider/sensitiveSpecies/$uid" (controller:'dataProvider', action:[GET:'sensitiveSpeciesForDataProvider'])
        "/ws/dataProvider/$uid/speciesRecords/$lsid" (controller:'dataProvider',action:'speciesRecordsForDataProvider')

        //"/"(view:"/index")
        "500"(view:'/error')
	}
}
