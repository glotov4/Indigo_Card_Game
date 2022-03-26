package indigo

class Card(_rank: String, _suite: String) {
    var rank = _rank
    private val defaultRank = "A"

    var suite = _suite
    private val defaultSuite = "♥"

    init { // check if Rank & Suite is available
        rank = if (Deck.RANKS.contains(_rank)) _rank else {
            println("Unknown rank. Switching to default: \"A\"")
            defaultRank
        }
        suite = if (Deck.SUITES.contains(_suite)) _suite else {
            println("Unknown suite. Switching to default: \"♥\"")
            defaultSuite
        }
    }

    fun hasSameRankOrSuite(cardToCompare: Card): Boolean {
        return compareRank(cardToCompare) || compareSuite(cardToCompare)
    }

    private fun compareRank(cardToCompare: Card): Boolean {
        return this.rank == cardToCompare.rank
    }

    private fun compareSuite(cardToCompare: Card): Boolean {
        return this.suite == cardToCompare.suite
    }

    override fun toString(): String {
        return "$rank$suite"
    }
}