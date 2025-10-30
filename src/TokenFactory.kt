object TokenFactory {
    fun createEnd(line: Int): Token {
        return Token("EOF", "", null, line)
    }
    fun createInt(num: Int, line: Int): Token {
        return Token("INT", "", num, line)
    }
    fun createFloat(num: Float, line: Int): Token {
        return Token("FLOAT", "", num, line)
    }
    fun createString(string: String, line: Int): Token {
        return Token("STRING", "", string, line)
    }
    fun createIdentifier(string: String, line: Int): Token {
        return Token("IDENTIFIER", "", string, line)
    }
    fun createKeyword(keyword: Keyword?, line: Int): Token {
        return Token(
            keyword?.name ?: "INVALID",
            keyword?.word.toString(),
            null,
            line
        )
    }
    fun createOperator(operator: Operator?, line: Int): Token {
        return Token(
            operator?.name ?: "INVALID",
            operator?.symbol.toString(),
            null,
            line
        )
    }
    fun createGrouper(grouper: Grouper?, line: Int): Token {
        return Token(
            grouper?.name ?: "INVALID",
            grouper?.symbol.toString(),
            null,
            line
        )
    }
}