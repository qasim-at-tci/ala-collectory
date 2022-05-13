package uk.org.nbn.collectory

class DataController extends au.org.ala.collectory.DataController{

    def brief = {[name: it.name, uri: it.buildUri(), uid: it.uid, groupClassification: (it.groupClassification?:''), networkMembership: it.networkMembership, dateCreated: (it.dateCreated?:''), lastUpdated: (it.lastUpdated?:'')]}
    def briefWithDates = {[name: it.name, uri: it.buildUri(), uid: it.uid, groupClassification: (it.groupClassification?:''), networkMembership: it.networkMembership, dateCreated: (it.dateCreated?:''), lastUpdated: (it.lastUpdated?:''), dataCurrency: (it.dataCurrency?:''), lastChecked: (it.lastChecked?:'') ,resourceType:(it.resourceType?:''), status:(it.status?:'')]}

    def getEntity = {

        if (params.pg || params.entity == "tempDataResource") {
            super.getEntity()
        } else {
            def urlForm = params.entity
            def clazz = capitalise(urlForm)

            // return list of entities
            addContentLocation "/ws/${urlForm}"
            def domain = grailsApplication.getClassForName("au.org.ala.collectory.${clazz}")

            def list = (urlForm == 'dataResource') ?
                                                    complexEntityQuery(domain) :
                                                    domain.list([sort:'name', order: 'asc'])
            list = filter(list)
            def last = latestModified(list)
            def detail = params.summary ? summary : urlForm == 'dataResource' ? briefWithDates : brief
            def summaries = list.collect(detail)
            def eTag = summaries.toString().encodeAsMD5()
        //                response.setCharacterEncoding("UTF-8")
            response.setContentType("application/json")
            renderAsJson summaries, last, eTag



        }
    }


    //this is lifted from the customisation. Refactor another time
    def complexEntityQuery(domain) {
        // define additional behaviour parameters for dataResource only - potential for this to be extended to other classes too?

        // convert to a command pattern to leverage some kind of platform-based validation rather than inline?
        def sortParam = ["name", "dateCreated", "lastUpdated", "dataCurrency", "lastChecked"].contains(params.sort) ? params.sort : 'name'
        def sortOrder = ['asc', 'desc'].contains(params.order) ? params.order : 'asc'
        def limit = params.int('limit', -1)

        def query = domain
        def queryParams = [sort:sortParam, order:sortOrder, max:limit]

        // check for species list filter
        if(["species-list", "records", "document"].contains(params.resourceType)){
            query = query.where{eq 'resourceType', params.resourceType}
        }

        //check for Integration Status filter
        if(['dataAvailable', 'linksAvailable', 'identified', 'inProgress'].contains(params.status)){
            query = query.where{eq 'status', params.status}
        }

        //date filters
        def paramDate = {String name -> params.date(name, "yyyy-MM-dd")}
        if(paramDate('lastUpdatedFrom') && paramDate('lastUpdatedTo')){
            query = query
                    .where{gte 'lastUpdated', paramDate('lastUpdatedFrom')}
                    .where{lte 'lastUpdated', paramDate('lastUpdatedTo')}
        }
        if(paramDate('dateCreatedFrom') && paramDate('dateCreatedTo')){
            query = query
                    .where{gte 'dateCreated', paramDate('dateCreatedFrom')}
                    .where{lte 'dateCreated', paramDate('dateCreatedTo')}
        }
        if(paramDate('dataCurrencyFrom') && paramDate('dataCurrencyTo')){
            query = query
                    .where{gte 'dataCurrency', paramDate('dataCurrencyFrom')}
                    .where{lte 'dataCurrency', paramDate('dataCurrencyTo')}
        }
        if(paramDate('lastCheckedFrom') && paramDate('lastCheckedTo')){
            query = query
                    .where{gte 'lastChecked', paramDate('lastCheckedFrom')}
                    .where{lte 'lastChecked', paramDate('lastCheckedTo')}
        }

        query.list(queryParams)
    }
}
