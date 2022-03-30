package indigo

class Card(_rank: String, _suite: String) {
    var rank = _rank
    var suite = _suite

    /** Check if Rank & Suite are allowed. If not - set to default **/
    init {
        rank = if (Deck.RANKS.contains(_rank)) _rank else {
            println("Unknown rank. Switching to default: \"${Deck.DEFAULT_CARD.rank}\"")
            Deck.DEFAULT_CARD.rank
        }
        suite = if (Deck.SUITES.contains(_suite)) _suite else {
            println("Unknown suite. Switching to default: \"${Deck.DEFAULT_CARD.suite}\"")
            Deck.DEFAULT_CARD.suite
        }
    }

    fun hasSameRankOrSuiteAs(cardToCompare: Card): Boolean {
        return hasSameRankAs(cardToCompare) || hasSameSuiteAs(cardToCompare)
    }

    fun hasSameRankAs(cardToCompare: Card): Boolean {
        return this.rank == cardToCompare.rank
    }

    fun hasSameSuiteAs(cardToCompare: Card): Boolean {
        return this.suite == cardToCompare.suite
    }

    override fun toString(): String {
        return "$rank$suite"
    }
}