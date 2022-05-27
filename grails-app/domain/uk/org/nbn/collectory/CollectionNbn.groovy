package uk.org.nbn.collectory

class CollectionNbn extends au.org.ala.collectory.Collection {

    String groupClassification  // classification of provider

     static mapping = {
        discriminator "au.org.ala.collectory.Collection"
        groupClassification type: "text"
    }

    static constraints = {
        groupClassification(nullable:true, maxSize:256)
    }

}
