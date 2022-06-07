package uk.org.nbn.collectory

class CollectionController extends au.org.ala.collectory.CollectionController implements ProviderGroupControllerTrait{

    def list() {
        super.list.call()
        //super.list() was customised. To extract the customisation, the Collection domain class was modified
        //by overriding the the MetaMethod list();
        // //See uk.org.nbn.BootStrap class and the method initCollectionDomain()
    }
}
