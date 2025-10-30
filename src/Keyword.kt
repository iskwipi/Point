enum class Keyword(val word: String) {
    TRUE("True"),
    FALSE("False"),
    NOTHING("Nothing"),
    BOOL_DEF("Bool"),
    INT_DEF("Int"),
    FLOAT_DEF("Float"),
    STRING_DEF("String"),
    FUNCTION_DEF("Function"),
    ANY_DEF("Any"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    NOT("not"),
    OTHERWISE("otherwise");

    companion object {
        private val wordToKeyword: Map<String, Keyword> =
            entries.toTypedArray().associateBy { it.word }

        fun fromWord(word: String): Keyword? = wordToKeyword[word]

        fun isKeyword(word: String): Boolean = fromWord(word) != null
    }
}