package indigo

var turn = 0

fun main() {
    println("Indigo Card Game")

    Deck.create()
    Deck.shuffle()

    Table.start()

    val player = User()
    val computer = User()

    /** If not all cards are played, take turns **/
    while (turn < Table.Rules.TOTAL_TURNS) {
        turn++
        if (Table.playersTurn) player.makeTurn() else computer.makeTurn()
        if (Table.cards.isEmpty() && turn != Table.Rules.TOTAL_TURNS) Table.scoresIntermediate(player, computer)
    }
    Table.countFinalScore(player, computer)
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