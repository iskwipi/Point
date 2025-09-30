enum class Keyword() {
    VAR,
    FUNC,
    CLASS,
    IF,
    ELSE,
    WHEN,
    FOR,
    WHILE,
    DO,
    BREAK,
    CONTINUE;

    companion object {
        fun isKeyword(word: String): Boolean = word in Keyword.entries.map{ it.toString().lowercase() }

        fun fromString(word: String): Keyword = Keyword.valueOf(word.uppercase())
    }
}