package uk.org.nbn.collectory

class LicenceController extends au.org.ala.collectory.LicenceController{

    @Override
    def save() {
        if (params.url?.endsWith("/")) params.url = params.url[0..-2] //standardise without trailing '/'
        super.save()
    }

    @Override
    def update(Long id, Long version) {
        if (params.url?.endsWith("/")) params.url = params.url[0..-2] //standardise without trailing '/'
        super.update(id, version)
    }

    private def standariseUrl(url) {
        params.url?.endsWith("/")? url[0..-2] : url
    }

}
