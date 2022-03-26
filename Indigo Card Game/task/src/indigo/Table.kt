package indigo

object Table {

    object Rules {
        /** Deck & Cards **/
        const val DEFAULT_DECK_SIZE = 52
        const val STARTING_CARDS = 4
        const val CARDS_PER_TURN = 6

        val TOP_RANKS = setOf("A", "10", "J", "Q", "K")
        const val POINTS_FOR_RANK = 1
        const val POINTS_FOR_MAX_CARDS = 3
        const val TOTAL_POINTS = 23
    }

    var cards = MutableList<Card>(Rules.STARTING_CARDS) { Deck.DEFAULT_CARD }
    var playerWonLast: Boolean? = null
    var playersTurnFirst = true

    /** Ask wo plays first **/
    var playersTurn = true
    private fun playFirst() {
        println("Play first?")
        while (true) {
            when (readln().lowercase()) {
                "yes" -> {
                    playersTurn = true
                    playersTurnFirst = true
                    break
                }
                "no" -> {
                    playersTurn = false
                    playersTurnFirst = false
                    break
                }
                else -> println("Play first?")
            }
        }
    }

    fun start() {
        /** Ask wo plays first **/
        playFirst()
        /** Prints initial cards. Puts 4 starting cards from deck to the table **/
        print("Initial cards on the table:")
        cards = Deck.getCardsFromDeck(Rules.STARTING_CARDS)
        for (index in 0 until Rules.STARTING_CARDS) {
            print(" ${cards[index]}")
        }
        println()
    }

    fun printNumAndTopCards() { // print number of cards on the table and top card
        if (cards.size > 0) println("${cards.size} cards on the table, and the top card is ${cards.last()}") else
            println("No cards on the table")
    }

    /** 1. Compare size, 2. Count Top Ranks cards, 3. Count total points, 4. Print **/
    fun scores(_player: User, _computer: User) {
        if (Deck.deck.size == 0 && _player.hand.size == 0 && _computer.hand.size == 0) {
            when (playerWonLast) {
                true -> _player.pocket.pocketCards()
                false -> _computer.pocket.pocketCards()
                null -> if (playersTurnFirst) _player.pocket.pocketCards() else _computer.pocket.pocketCards()
            }
        }
        _player.pocket.compareSizeForPoints(_computer) // compare size, add points
        // count top ranks cards
        _player.pocket.countPointsForRanks()
        _computer.pocket.countPointsForRanks()

        _player.pocket.countTotalPoints()
        _computer.pocket.countTotalPoints()

        println("Score: Player ${_player.pocket.points} - Computer ${_computer.pocket.points}")
        println("Cards: Player ${_player.pocket.pocketedCards.size} - Computer ${_computer.pocket.pocketedCards.size}")
    }


//    fun printScore(player: Player, computer: Player) {
//        println("Score: Player ${player.Pocket.countCards()} - Computer ${computer.Pocket().countCards()}")
//    }
//
//    fun printNumberOfCards() {
//
//    }

}