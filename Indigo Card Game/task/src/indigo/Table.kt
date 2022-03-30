package indigo

object Table {
    object Rules {
        /** Deck & Cards **/
        private const val DEFAULT_DECK_SIZE = 52
        const val STARTING_CARDS = 4
        const val CARDS_PER_TURN = 6

        /** Turns **/
        const val TOTAL_TURNS = (DEFAULT_DECK_SIZE - STARTING_CARDS)

        /** Points **/
        val TOP_RANKS = setOf("A", "10", "J", "Q", "K")
        const val POINTS_FOR_RANK = 1
        const val POINTS_FOR_MAX_CARDS = 3
//        const val TOTAL_POINTS = 23
    }

    var cards = MutableList<Card>(Rules.STARTING_CARDS) { Deck.DEFAULT_CARD } // list of cards on the table
    var turn = 0 // turn #
    var playerWonLast: Boolean? = null // player won last round?
    var playersTurn = true // is it is a player's turn right now?
    var playersTurnFirst = true // first turn was player's?

    /**                                  Basic commands                               **/

    /** 1. Ask who plays first, 2. Print initial cards 3. Put starting cards on the table **/
    fun start() {
        /** Ask wo plays first **/
        askWhoPlaysFirst()
        /** Prints initial cards. Puts 4 starting cards from deck on the table **/
        print("Initial cards on the table:")
        cards = Deck.getCardsFromDeck(Rules.STARTING_CARDS)
        for (index in 0 until Rules.STARTING_CARDS) {
            print(" ${cards[index]}")
        }
        println()
    }

    /** 1. Ask who plays first 2. Store that info **/
    private fun askWhoPlaysFirst() {
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

    /** 1. If cards is not empty 2. Print number of cards on the table and the top card, else print "No cards" **/
    fun printNumOfCardsAndTopCardOnTheTable() {
        println()
        if (cards.isNotEmpty()) println("${cards.size} cards on the table, and the top card is ${cards.last()}")
        else println("No cards on the table")
    }

    /**                                  Scores & Points                               **/

    /** 1. Count points for ranks, 2. Sum rank points + size points, 3. Print it **/
    fun countAndPrintScores(_player: User, _computer: User) {
        // countPointsForRanks
        _player.pocket.countPointsForRanks()
        _computer.pocket.countPointsForRanks()
        // update total points with points for ranks only (because we don't count points for size yet)
        _player.pocket.updatePlayerScores()
        _computer.pocket.updatePlayerScores()

        // print scores & info on cards won = cards in the pocket
        println("Score: Player ${_player.pocket.points} - Computer ${_computer.pocket.points}")
        println("Cards: Player ${_player.pocket.pocketedCards.size} - Computer ${_computer.pocket.pocketedCards.size}")
    }

    /** Called on final turn. 1. Print cards on the table, 2. give leftover cards to somebody according to the rules **/
    fun finalRoundPocketing(_player: User, _computer: User) {
        printNumOfCardsAndTopCardOnTheTable() // print info on cards on the table (or "No cards")
        // Clear the table:
        when (playerWonLast) { // Who won last round gets leftover cards (if any)
            true -> _player.pocket.pocketCardsFromTheTable(false)
            false -> _computer.pocket.pocketCardsFromTheTable(false)
            // if nobody won any rounds in the game - give all cards from the table to User who made the first turn
            null -> if (playersTurnFirst) _player.pocket.pocketCardsFromTheTable(false) else
                _computer.pocket.pocketCardsFromTheTable(false)
        }
    }
}