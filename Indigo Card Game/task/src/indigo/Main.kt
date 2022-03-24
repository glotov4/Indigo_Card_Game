package indigo

val DEFAULT_CARD = Card("A", "♥")

fun main() {
    println("Indigo Card Game")

    Deck.create()
    Deck.shuffle()

    Table.start()
    val player = Player()
    val playersPocket = player.Pocket()
    val computer = Player()
    val computersPocket = computer.Pocket()

//    /** Take 6 cards in hand **/
//    playersHand.takeCards()
//    computersHand.takeCards()
    /** If not all cards are played, take turns **/
    while (Table.cards.size < 52) {
        when (Table.playersTurn) {
            true -> player.makeTurn()
            false -> computer.makeTurn()
        }
    }
    Table.printNumAndTopCards()
    println("Game Over")
}


//    while (true) {
//        println("Choose an action (reset, shuffle, get, exit):")
//        when (readln()) {
//            "reset" -> Deck.reset()
//            "shuffle" -> Deck.shuffle()
//            "get" -> Deck.get()
//            "exit" -> {
//                println("Bye")
//                break
//            }
//            else -> println("Wrong action.")
//        }
//    }