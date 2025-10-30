enum class Operator(private val symbol: String) {
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    MODULO("%"),
    POWER("^"),
    LESS("<"),
    GREAT(">"),
    EQUAL("="),
    COLON(":"),
    QUESTION("?"),
    COMMA(","),
    SEMICOLON(";"),

    LESS_EQUAL("<="),
    GREAT_EQUAL(">="),
    EQUAL_EQUAL("=="),
    NOT_EQUAL("!="),
    LEFT_ARROW("<-"),
    RIGHT_ARROW("->"),
    LAMBDA_ARROW("=>"),
    FORWARDER(">>");

    fun getSymbol(): String = symbol

    companion object {
        private const val VALID_START = "+-*/%^<>=!:?,()[]{};"
        private val symbolToOperator: Map<String, Operator> =
            entries.toTypedArray().associateBy { it.symbol }

        fun isValidStart(char: Char): Boolean = char in VALID_START

        fun fromSymbol(symbol: String): Operator? = symbolToOperator[symbol]

        fun isOperator(symbol: String): Boolean = fromSymbol(symbol) != null
    }
}
