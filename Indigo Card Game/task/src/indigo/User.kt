package indigo

import kotlin.system.exitProcess

enum class WEIGHTS(var weight: Int) {
    // TODO: make unique weight for each code?
    ZERO(0), WEAK(1), MEDIUM(2), STRONG(3), TOP(4),
}

class User {

    private var hand = MutableList<Card>(0) { Deck.DEFAULT_CARD }
    private var candidateCardsMap = mutableMapOf<Int, Int>()
    var pocket = Pocket()

    /**                                 Default commands                                 **/

    private fun takeCardsFromDeck() {
        hand.addAll(Deck.getCardsFromDeck(Table.Rules.CARDS_PER_TURN))
    }

    private fun printCardsInHand() {
        if (Table.playersTurn) {
            print("Cards in hand:")
            hand.forEachIndexed { index, card -> print(" ${index + 1})$card") }
        } else hand.forEach { print("$it ") }
        println()
    }

    private fun askForAndPlayTheCard() {

        /** On player's turn offer a card to play from hand, put it on the table **/
        if (Table.playersTurn) println("Choose a card to play (1-${hand.size}):")
        if (Table.playersTurn) {
            val input = readln()
            // if number entered is in range of 1-handSize - put the card of that number (not index) on the table
            if (input.matches("[1-${hand.size}]".toRegex())) playCardFromHandAtIndex(input.toInt() - 1)
            else if (input.lowercase() == "exit") { // account for exit command
                println("Game Over")
                exitProcess(0)
            } else askForAndPlayTheCard() // if input isn't in range - ask again
        } else { // if it's computer's turn:
            assesCardsInHand()
            val index = findIndexOfBestCandidateCard()
            println("Computer plays ${hand[index]}")
            playCardFromHandAtIndex(index)
        }
    }

    /** 1. Take cards if necessary & possible, 2. Print what's on the table, 3. Print cards in hand, prompt
     * 4. If there are cards - check if player wins cards, then pocket (save) them, 5. Switch turns **/
    fun makeTurn() {
        if (hand.isEmpty() && Deck.deck.isNotEmpty()) takeCardsFromDeck()  // if hand is empty - take cards
        Table.printNumOfCardsAndTopCardOnTheTable() // print what's on the table

        printCardsInHand() // print hand to choose from. different for player and computer

        if (Table.cards.isNotEmpty()) { // if there ARE cards on the table
            // remember top card
            val topCardOnTable = Table.cards.last()
            // play a card (it becomes top card. remember it)
            askForAndPlayTheCard()
            val playedCard = Table.cards.last()
            // compare top card and played card. if ranks or suites are same - pocket cards from the table
            if (topCardOnTable.hasSameRankOrSuiteAs(playedCard)) pocket.pocketCardsFromTheTable(true)
        } else askForAndPlayTheCard() // if there are no cards - just play the card
        Table.playersTurn = !Table.playersTurn // switch turns
    }

    /** Play Card From Hand At Index **/
    private fun playCardFromHandAtIndex(index: Int) {
        Table.cards.add(hand[index])
        hand.removeAt(index)
    }

    /**                                 Finding Candidate Cards                                 **/

    /** Set weights for cards when the table is empty: same suits -> same rank -> any card **/
    private fun assesHandAsTableIsEmpty() {
        // Check each card with next cards
        for (index in 0 until hand.lastIndex) for (nextIndices in index + 1..hand.lastIndex) {
            val card = hand[index]
            val nextCards = hand[nextIndices]
            when {
                card.hasSameSuiteAs(nextCards) -> {
                    candidateCardsMap[index] = maxOf(WEIGHTS.MEDIUM.weight, candidateCardsMap[index]!!)
                    candidateCardsMap[nextIndices] = maxOf(WEIGHTS.MEDIUM.weight, candidateCardsMap[nextIndices]!!)
                }
                card.hasSameRankAs(nextCards) -> {
                    candidateCardsMap[index] = maxOf(WEIGHTS.WEAK.weight, candidateCardsMap[index]!!)
                    candidateCardsMap[nextIndices] = maxOf(WEIGHTS.WEAK.weight, candidateCardsMap[nextIndices]!!)
                }
                else -> {
                    candidateCardsMap[index] = maxOf(WEIGHTS.ZERO.weight, candidateCardsMap[index]!!)
                }
            }
        }
//            if (card.hasSameSuiteAs(nextCards)) {
//                candidateCardsMap[index] = WEIGHTS.MEDIUM.weight
//                candidateCardsMap[nextIndices] = WEIGHTS.MEDIUM.weight
//            }
//            if (card.hasSameRankAs(nextCards)) {
//                // if there are other cards with same suite = medium weight
//                if (hand[index].hasSameSuiteAs(hand[nextIndices])) candidateCardsMap[index] = WEIGHTS.MEDIUM.weight
//                // if there are no cards with the same suite but there are other cards with same rank = weak weight
//                else if (hand[index].hasSameRankAs(hand[nextIndices])) candidateCardsMap[index] = WEIGHTS.WEAK.weight
//            }
//
//
//
//        for ((index, card) in hand.withIndex()) for (nextIndex in index .. hand.lastIndex) {
//            if (index == hand.lastIndex) continue else{
//                nextIndex
//            }
//            if (card.hasSameSuiteAs(hand[nextIndex])) {
//                candidateCardsMap[index] = WEIGHTS.MEDIUM.weight
//                candidateCardsMap[nextIndex] = WEIGHTS.MEDIUM.weight
//            }
//        }
//
//        hand.forEachIndexed { index, card ->
//            candidateCardsMap[index] = WEIGHTS.ZERO.weight // set initial weight for each card to zero
//
//            if (index != hand.lastIndex) for (nextIndex in index + 1 .. hand.lastIndex)
//                if (card.hasSameSuiteAs(hand[nextIndex])) {
//                    candidateCardsMap[index] = WEIGHTS.MEDIUM.weight
//                    candidateCardsMap[nextIndex] = WEIGHTS.MEDIUM.weight
//                }
//        }
//
//        for (index in hand.indices) {
//            candidateCardsMap[index] = WEIGHTS.ZERO.weight // set initial weight for each card to zero
//            for (nextIndices in index until hand.size) { // look at each of next cards
//                // if there are other cards with same suite = medium weight
//                if (hand[index].hasSameSuiteAs(hand[nextIndices])) candidateCardsMap[index] = WEIGHTS.MEDIUM.weight
//                // if there are no cards with the same suite but there are other cards with same rank = weak weight
//                else if (hand[index].hasSameRankAs(hand[nextIndices])) candidateCardsMap[index] = WEIGHTS.WEAK.weight
//            }
//        }
    }

    /** Set weights for cards.
     * If no candidate cards - asses them as you did when the table was empty: same suits -> same rank -> any card **/
    private fun assesCardsInHand() {
        // set candidateCardsMap size to hand.size & put zeroes in
        candidateCardsMap = mutableMapOf<Int, Int>()
        hand.forEachIndexed { index, _ -> candidateCardsMap[index] = WEIGHTS.ZERO.weight }

        // 1. If one card in hand, 2. If table is empty, 3. If table is not empty
        when {
            // Only one card in hand -> put it on the table
            hand.size == 1 -> candidateCardsMap[0] = WEIGHTS.STRONG.weight

            // Set weights for cards when the table is empty: same suits -> same rank -> any card
            Table.cards.isEmpty() -> {
                assesHandAsTableIsEmpty()
            }
            // 1. Compare every card with the top card. Same suite = STRONG, rank = MEDIUM,
            // 2. Check for scenarios: a: all zeroes, b: 1 same suite card, 2+ same rank cards
            Table.cards.isNotEmpty() -> {
                assesHandAsTableIsNotEmpty()
            }
        }
    }

    /** 1. Compare every card with the top card. Same suite = STRONG, rank = MEDIUM,
     * 2. Check for scenarios: a: all zeroes, b: 1 same suite card, 2+ same rank cards **/
    private fun assesHandAsTableIsNotEmpty() {
        val topCardOnTheTable = Table.cards.last() // save top card on the table to compare with it
        for (index in hand.indices) {
//            candidateCardsMap[index] = WEIGHTS.ZERO.weight // set initial weight for each card to zero
            // if card has same suite as top card set strong weight
            if (hand[index].hasSameSuiteAs(topCardOnTheTable)) candidateCardsMap[index] = WEIGHTS.STRONG.weight
            // if card has same rank as top card set medium weight
            else if (hand[index].hasSameRankAs(topCardOnTheTable)) candidateCardsMap[index] =
                WEIGHTS.MEDIUM.weight
        }
        // In some scenarios we need to adjust weights:
        when {
            // if all weights are zero (no matches with the top card) - asses hand as table is empty
            isThereNoCandidates() -> assesHandAsTableIsEmpty()
            // If there are only 1 STRONG candidate (same suit), AND 2 or more MEDIUM candidates (same rank)
            // set those MEDIUM candidates as TOP candidates
            isThereOneSameSuiteAndMoreSameRank() -> makeMediumWeightsTop()
        }
    }

    /** If all weights are zero (no matches with the top card) - asses hand as table is empty **/
    private fun isThereNoCandidates() = candidateCardsMap.all { it.value == WEIGHTS.ZERO.weight }

    /** If there are only 1 STRONG candidate (same suit), AND 2 or more MEDIUM candidates (same rank),
     * set those medium candidates as TOP candidates **/
    private fun isThereOneSameSuiteAndMoreSameRank() =
        (candidateCardsMap.count { it.value == WEIGHTS.STRONG.weight } == 1
                && candidateCardsMap.count { it.value == WEIGHTS.MEDIUM.weight } > 1)

    /** Set MEDIUM candidates as TOP **/
    private fun makeMediumWeightsTop() {
        for (entry in candidateCardsMap)
            if (entry.value == WEIGHTS.MEDIUM.weight) entry.setValue(WEIGHTS.TOP.weight)
    }

    private fun findIndexOfBestCandidateCard(): Int {
        return candidateCardsMap.maxByOrNull { it.value }!!.key
    }


    /**                                 Pocket: Class to store cards that User has won                               **/
    inner class Pocket {
        var pocketedCards = MutableList<Card>(0) { Deck.DEFAULT_CARD }
        private var pointsForRanks = 0
        private var pointsForSize = 0
        var points = 0

        /** Save won cards **/
        fun pocketCardsFromTheTable(printMessage: Boolean) {
            pocket.pocketedCards.addAll(Table.cards)
            Table.cards.clear()

            if (printMessage) {
                if (Table.playersTurn) {
                    println("Player wins cards")
                    Table.playerWonLast = true
                } else {
                    println("Computer wins cards")
                    Table.playerWonLast = false
                }
            }

        }

        /** Compare size of the pockets and add 1 point to player with most cards **/
        fun comparePocketSizeForPoints(userToCompareWith: User) {
            // Compare size of the pockets: Bigger pocket = 1 point
            if (pocketedCards.size > userToCompareWith.pocket.pocketedCards.size) {
                pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS
                userToCompareWith.pocket.pointsForSize = 0
            } else if (pocketedCards.size < userToCompareWith.pocket.pocketedCards.size) {
                pointsForSize = 0
                userToCompareWith.pocket.pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS
                // If pockets are of the same size - give point to player who played first
            } else {
                if (Table.playersTurnFirst) pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS else {
                    userToCompareWith.pocket.pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS
                }
            }
        }

        /** Count points for cards of Top Ranks **/
        fun countPointsForRanks() {
            pointsForRanks = 0
            for (card in pocketedCards) {
                // if this card's rank is one of TOP_RANKS that give points - add 1 point
                if (Table.Rules.TOP_RANKS.contains(card.rank)) pointsForRanks += Table.Rules.POINTS_FOR_RANK
            }
        }


        /** Count total points **/
        fun updatePlayerScores() {
            points = pointsForRanks + pointsForSize
        }
    }

}