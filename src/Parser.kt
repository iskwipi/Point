class Parser(private val tokens: List<Token>) {
    private var index = 0
    val errorList = mutableListOf<Error>()

    // helper functions for parsing
    private fun peek(): Token {
        if (index < tokens.size) return tokens[index]
        return tokens.last()
    }
    private fun advance(): Token {
        val token = peek()
        index++
        return token
    }
    private fun match(vararg types: TokenType): Boolean {
        val current = peek()
        if (types.contains(current.type)) {
            advance()
            return true
        }
        return false
    }
    private fun expect(type: TokenType, errorMsg: String = "Unexpected token"): Token? {
        val current = peek()
        if (current.type == type) return advance()
        errorList.add(Error(current.line, "Parser", errorMsg))
        return null
    }
}