package uk.org.nbn.collectory

class GroupClassification {

    String name // 'Biological Records Centre' etc.
    Date dateCreated
    Date lastUpdated


    public String toString() {
        "${name}"
    }


}
