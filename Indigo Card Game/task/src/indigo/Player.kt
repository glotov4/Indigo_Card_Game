package indigo

import kotlin.system.exitProcess

class Player {
    inner class Hand {

    }
    var hand = MutableList<Card>(0) { DEFAULT_CARD }

    private fun getHandsSize(): Int {
        return hand.size
    }

    private fun takeCardsFromDeck() {
        hand.addAll(Deck.getCardFromDeck(Table.Rules.CARDS_PER_TURN))
    }

    private fun takeCardsFromTable() {
        Pocket().pocketedCards.addAll(Table.cards)
        Table.cards.clear()
    }

    private fun printCardsInHand() {
        print("Cards in hand:")
        hand.forEachIndexed { index, card -> print(" ${index + 1})$card") }
        println()
    }

    private fun playCardFromHand(): Card {
        var card = DEFAULT_CARD
        if (Table.playersTurn) {
            println("Choose a card to play (1-${hand.size}):")
            val input = readln()
            println()
            if (input.matches("[1-${hand.size}]".toRegex())) {
                val index = input.toInt()
                card = hand[index - 1]
                Table.cards.add(hand[index - 1])
                hand.removeAt(index - 1)
            } else if (input.lowercase() == "exit") {
                println("Game Over")
                exitProcess(0)
            } else playCardFromHand()
        } else {
            println("Computer plays ${hand.first()}\n")
            card = hand.first()
            Table.cards.add(hand.first())
            hand.removeFirst()
        }
        return card
    }

    fun makeTurn() {
        var playedCard = DEFAULT_CARD
        Table.printNumAndTopCards()
        if (getHandsSize() < 1) takeCardsFromDeck()
        if (Table.playersTurn) printCardsInHand()
        playedCard = playCardFromHand()
        if (Table.checkTopCard().hasSameRankOrSuite(playedCard)) takeCardsFromTable()
        Table.playersTurn = !Table.playersTurn // switch turns
    }

    inner class Pocket {
        var pocketedCards = MutableList<Card> (0) { DEFAULT_CARD }
        var points = 0

        fun pocketCards(cards: MutableList<Card>) {
            pocketedCards.addAll(cards)
        }

        private fun countCards() : Int {
            return pocketedCards.size
        }

        private fun countPointsForRanks() {
            pocketedCards.forEach {
                if (Table.Rules.TOP_RANKS.contains(it.rank)) points += Table.Rules.POINTS_FOR_TOP_RANK_CARD
            }
        }

        fun compareSize() {

        }
    }

}