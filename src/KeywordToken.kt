enum class KeywordToken(private val word: String): TokenType {
    TRUE("True"),
    FALSE("False"),
    NOTHING("Nothing"),
    BOOL_DEF("Bool"),
    INT_DEF("Int"),
    FLOAT_DEF("Float"),
    STRING_DEF("String"),
    ANY_DEF("Any"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    NOT("not"),
    DEFINE("define"),
    OTHERWISE("otherwise");

    fun getWord(): String = word

    companion object {
        private val wordToKeyword: Map<String, KeywordToken> =
            entries.toTypedArray().associateBy { it.word }

        fun fromWord(word: String): KeywordToken? = wordToKeyword[word]

        fun isKeyword(word: String): Boolean = fromWord(word) != null
    }
}