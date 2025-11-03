object TokenFactory {
    fun createEnd(line: Int): Token {
        return Token(MiscToken.EOF, "", null, line)
    }
    fun createInt(num: Int, line: Int): Token {
        return Token(VariableToken.INT, "", num, line)
    }
    fun createFloat(num: Float, line: Int): Token {
        return Token(VariableToken.FLOAT, "", num, line)
    }
    fun createString(string: String, line: Int): Token {
        return Token(VariableToken.STRING, "", string, line)
    }
    fun createIdentifier(string: String, line: Int): Token {
        return Token(VariableToken.IDENTIFIER, "", string, line)
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