package indigo

object Table {

    object Rules {
        const val STARTING_CARDS = 4
        const val CARDS_PER_TURN = 6

        val TOP_RANKS = setOf( "A", "10", "J", "Q", "K" )
        const val POINTS_FOR_TOP_RANK_CARD = 1
        const val POINTS_FOR_MAX_CARDS = 3
        const val TOTAL_POINTS = 23
    }

    var cards = MutableList<Card>(Rules.STARTING_CARDS) { DEFAULT_CARD }
    var playersTurn = false

    fun start() {
        playFirst()
        print("Initial cards on the table:")
        cards = Deck.getCardFromDeck(Rules.STARTING_CARDS)
        for (index in 0 until Rules.STARTING_CARDS) {
            print(" ${cards[index]}")
        }
        println()
    }

    private fun playFirst() {
        println("Play first?")
        while (true) {
            when (readln().lowercase()) {
                "yes" -> {
                    playersTurn = true
                    break
                }
                "no" -> {
                    playersTurn = false
                    break
                }
                else -> println("Play first?")
            }
        }
    }

    fun checkTopCard() : Card {
        return cards.last()
    }

    fun printNumAndTopCards() {
        println("${cards.size} cards on the table, and the top card is ${checkTopCard()}")
    }

//    fun printScore(player: Player, computer: Player) {
//        println("Score: Player ${player.Pocket.countCards()} - Computer ${computer.Pocket().countCards()}")
//    }
//
//    fun printNumberOfCards() {
//
//    }

}