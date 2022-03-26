package indigo

import kotlin.system.exitProcess

class User {

    var hand = MutableList<Card>(0) { Deck.DEFAULT_CARD }
    var pocket = Pocket()

    private fun takeCardsFromDeck() {
        hand.addAll(Deck.getCardsFromDeck(Table.Rules.CARDS_PER_TURN))
    }

    private fun printCardsInHand() {
        print("Cards in hand:")
        hand.forEachIndexed { index, card -> print(" ${index + 1})$card") }
        println()
    }

    private fun playCardFromHand(): Card {
        var card = Deck.DEFAULT_CARD
        /** On player's turn offer a card to play from hand, put it on the table **/
        if (Table.playersTurn) {
            println("Choose a card to play (1-${hand.size}):")
            val input = readln()
            println()
            // if number entered is in range of 1-handSize - put the #card on the table
            if (input.matches("[1-${hand.size}]".toRegex())) {
                val index = input.toInt() - 1
                card = hand[index]
                Table.cards.add(hand[index])
                hand.removeAt(index)
            } else if (input.lowercase() == "exit") { // account fir exit
                println("Game Over")
                exitProcess(0)
            } else playCardFromHand() // if input isn't in range - ask again
        } else { // if it's computer's turn:
            println("Computer plays ${hand.first()}\n")
            card = hand.first()
            Table.cards.add(hand.first())
            hand.removeFirst()
        }
        return card
    }

    fun makeTurn() {
        if (hand.size == 0 && Deck.deck.size != 0) takeCardsFromDeck() // if hand is empty - take cards
        Table.printNumAndTopCards() // print what's on the table

        if (Table.playersTurn) printCardsInHand() // if it's player's turn - print hand to choose from

        if (Table.cards.size > 0) { // if there ARE cards on the table
            val topCard = Table.cards.last()
            val playedCard: Card = playCardFromHand()
            // compare top card and played card. if ranks or suites are same - pocket cards from the table
            if (topCard.hasSameRankOrSuite(playedCard)) pocket.pocketCards()
        } else playCardFromHand() // if there are no cards - just play the card
        Table.playersTurn = !Table.playersTurn // switch turns
    }

    inner class Pocket {
        var pocketedCards = MutableList<Card>(0) { Deck.DEFAULT_CARD }
        var pointsForRanks = 0
        var pointsForSize = 0
        var points = 0

        fun pocketCards() {
            pocket.pocketedCards.addAll(Table.cards)
            Table.cards.clear()
            if (Table.playersTurn) {
                println("Player wins cards")
                Table.playerWonLast = true
            } else {
                println("Computer wins cards")
                Table.playerWonLast = false
            }
        }

        /** Compare size and add 1 point to player with most cards **/
        fun compareSizeForPoints(user: User) {
            if (pocketedCards.size > user.pocket.pocketedCards.size) {
                pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS
                user.pocket.pointsForSize = 0
            } else if (pocketedCards.size < user.pocket.pocketedCards.size) {
                pointsForSize = 0
                user.pocket.pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS
            } else {
                if (Table.playersTurnFirst) pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS else {
                    user.pocket.pointsForSize = Table.Rules.POINTS_FOR_MAX_CARDS
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
        fun countTotalPoints() {
            points = pointsForRanks + pointsForSize
        }
    }

}