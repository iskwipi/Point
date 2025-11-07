object TokenFactory {
    fun createEnd(line: Int): Token {
        return Token(MiscToken.EOF, "", null, line)
    }
    fun createInt(num: String, line: Int): Token {
        return Token(VariableToken.INT, num, num.toInt(), line)
    }
    fun createFloat(num: String, line: Int): Token {
        return Token(VariableToken.FLOAT, num, num.toFloat(), line)
    }
    fun createString(string: String, line: Int): Token {
        return Token(VariableToken.STRING, string, string, line)
    }
    fun createIdentifier(string: String, line: Int): Token {
        return Token(VariableToken.IDENTIFIER, string, null, line)
    }
    fun createKeyword(keyword: KeywordToken?, line: Int): Token {
        return Token(
            keyword ?: MiscToken.INVALID,
            keyword?.getWord().toString(),
            null,
            line
        )
    }
    fun createOperator(operator: OperatorToken?, line: Int): Token {
        return Token(
            operator ?: MiscToken.INVALID,
            operator?.getSymbol().toString(),
            null,
            line
        )
    }
    fun createGrouper(grouper: GrouperToken?, line: Int): Token {
        return Token(
            grouper ?: MiscToken.INVALID,
            grouper?.getSymbol().toString(),
            null,
            line
        )
    }
}