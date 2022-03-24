package indigo

import kotlin.random.Random

object Deck {
    const val defaultSize = 52
    val RANKS = setOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    val SUITES = setOf("♥", "♦", "♠", "♣")

    var deck: MutableList<Card> = create()

    fun create() : MutableList<Card> { // creates a deck = list of Cards. Suite(Rank)
        val _deck = MutableList<Card>(defaultSize) { DEFAULT_CARD }
        var count = 0
        for (suite in SUITES)
            for (rank in RANKS) {
                if (count == defaultSize) break
                _deck[count] = Card(rank, suite)
                count++
            }
        return _deck
    }

    fun reset() { // just creates new deck with defaultsize
        deck = create()
        println("Card deck is reset.")
    }

    fun shuffle() { // shuffles every card giving it random index
        for (index in 0 until deck.size) {
            val shuffler = deck[index]
            val randomIndex = Random.nextInt(0, deck.size)
            deck[index] = deck[randomIndex]
            deck[randomIndex] = shuffler
        }
//        println("Card deck is shuffled.")
    }

    fun getCardFromDeck(numberOfCards: Int) : MutableList<Card> { // prints and takes top card out of the deck, puts it on the table
//        println("Number of cards:")
        val cardsToGet = MutableList<Card> (numberOfCards) { DEFAULT_CARD }
        val numberInvalidError = "Invalid number of cards."
        val insufficientError = "The remaining cards are insufficient to meet the request."

        try {  // try .. if input != int then error. then check if it in range
            if (numberOfCards < 1 || numberOfCards > 52) println(numberInvalidError) else
                if (numberOfCards > deck.size) println(insufficientError) else {
                repeat (numberOfCards) { // take numberOfCards from the deck, put them in a list (FILO)
                    cardsToGet[it] = deck.last()
                    deck.removeLast()
                }
            }
        }
        catch (e: java.lang.Exception) { println(numberInvalidError) }
        return cardsToGet
    }

}