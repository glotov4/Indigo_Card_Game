package indigo

import kotlin.random.Random

object Deck {
    const val DEFAULT_DECK_SIZE = 52
    val RANKS = setOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    val SUITES = setOf("♥", "♦", "♠", "♣")
    val DEFAULT_CARD = Card("A", "♥")


    var deck: MutableList<Card> = create()

    fun create(): MutableList<Card> { // creates a deck = list of Cards. Suite(Rank)
        val _deck = MutableList<Card>(DEFAULT_DECK_SIZE) { DEFAULT_CARD }
        var count = 0
        for (suite in SUITES)
            for (rank in RANKS) {
                if (count == DEFAULT_DECK_SIZE) break
                _deck[count] = Card(rank, suite)
                count++
            }
        return _deck
    }

//    fun reset() { // just creates new deck with defaultsize
//        deck = create()
//        println("Card deck is reset.")
//    }

    /** Shuffles every card giving it random index **/
    fun shuffle() {
        for (index in 0 until deck.size) {
            val shuffler = deck[index]
            val randomIndex = Random.nextInt(0, deck.size)
            deck[index] = deck[randomIndex]
            deck[randomIndex] = shuffler
        }
//        println("Card deck is shuffled.")
    }

    fun getCardsFromDeck(numberOfCards: Int): MutableList<Card> {
        val listOfCardsToGet = MutableList<Card>(numberOfCards) { DEFAULT_CARD }
        val numberInvalidError = "Invalid number of cards."
        val insufficientError = "The remaining cards are insufficient to meet the request."

        try {  // try .. if input != int then error. then check if it in range
            if (numberOfCards < 1 || numberOfCards > 52) println(numberInvalidError) else
                if (numberOfCards > deck.size) println(insufficientError) else {
                    repeat(numberOfCards) { // take numberOfCards from the deck, put them in a list (FILO)
                        listOfCardsToGet[it] = deck.last()
                        deck.removeLast()
                    }
                }
        } catch (e: java.lang.Exception) {
            println(numberInvalidError)
        }
        return listOfCardsToGet
    }

}