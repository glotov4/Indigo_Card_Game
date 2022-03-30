package indigo

/** TODO: Create interface "Pack of cards": Deck, Hand, Pocket, Table.Cards? : take, put, last. Init candidatesMap **/

fun main() {
    println("Indigo Card Game")

    Deck.create()
    Deck.shuffle()

    Table.start()

    val player = User()
    val computer = User()

    /** If not all cards are played, take turns **/
    while (Table.turn < Table.Rules.TOTAL_TURNS) {
        Table.turn++
        if (Table.playersTurn) player.makeTurn() else computer.makeTurn()
        // if last round was won by anyone (table is empty) && it's not final turn - print scores
        if (Table.cards.isEmpty() && Table.turn != Table.Rules.TOTAL_TURNS) {
            Table.countAndPrintScores(player, computer)
        }
    }
    Table.finalRoundPocketing(player, computer)
    player.pocket.comparePocketSizeForPoints(computer)
    Table.countAndPrintScores(player, computer)
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