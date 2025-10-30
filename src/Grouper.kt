enum class Grouper(val symbol: String, val pair: Int) {
    LEFT_PAREN("(", 1),
    RIGHT_PAREN(")", 1),
    LEFT_SQUARE("[", 2),
    RIGHT_SQUARE("]", 2),
    LEFT_CURLY("{", 3),
    RIGHT_CURLY("}", 3);

    companion object {
        private const val OPENERS = "([{"
        private val symbolToGrouper: Map<String, Grouper> =
            Grouper.entries.toTypedArray().associateBy { it.symbol }

        fun isOpener(char: Char): Boolean = char in OPENERS

        fun fromSymbol(symbol: String): Grouper? = symbolToGrouper[symbol]

        fun isGrouper(symbol: String): Boolean = fromSymbol(symbol) != null
    }
}