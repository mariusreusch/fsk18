package ch.zuehlke.hatch

data class WatchedTwitterTerm(val term: String) {
    var color: String

    init {
        var hashCode = this.term.hashCode()
        if (hashCode < 0){
            hashCode = hashCode() * (-1)
        }
        val hashCodeAsString = "$hashCode"
        this.color = "#${hashCodeAsString.substring(0, 6)}"
    }
}