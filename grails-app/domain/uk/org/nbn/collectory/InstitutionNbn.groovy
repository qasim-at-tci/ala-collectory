package uk.org.nbn.collectory

class InstitutionNbn extends au.org.ala.collectory.Institution {

    String groupClassification  // classification of provider

    static mapping = {
        discriminator "au.org.ala.collectory.Institution"
    }

}
