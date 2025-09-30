enum class Operator(val symbol: String) {
    DOT("."),
    COLON(":"),
    SEMICOLON(";"),
    LEFT_CURLY("{"),
    RIGHT_CURLY("}"),
    LEFT_SQUARE("["),
    RIGHT_SQUARE("]"),
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    PERCENT("%"),
    CARET("^"),
    BANG("!"),
    LESS("<"),
    GREAT(">"),
    EQUAL("="),
    PLUS_EQUAL("+="),
    MINUS_EQUAL("-="),
    STAR_EQUAL("*="),
    SLASH_EQUAL("/="),
    PERCENT_EQUAL("%="),
    CARET_EQUAL("^="),
    WEDGE("/\\"),
    VEE("\\/"),
    LEFT_ARROW("<-"),
    RIGHT_ARROW("->"),
    XOR("(+)");

    companion object {
        private const val VALID_START = ".:;{}[])+-*/%^\\(!<>="
        private val symbolToEnum: Map<String, Operator> =
            entries.toTypedArray().associateBy { it.symbol }

        fun isValidStart(char: Char): Boolean = char in VALID_START

        fun fromSymbol(symbol: String): Operator? = symbolToEnum[symbol]

        fun isSymbol(symbol: String): Boolean = fromSymbol(symbol) != null
    }
}
