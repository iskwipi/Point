class Scanner(private val string: String) {
    val errorList = mutableListOf<Error>()

    fun scan(): List<Token> {
        // global vars
        val tokenList = mutableListOf<Token>()
        var index = 0
        var line = 1
        val grouperStack = mutableListOf<Pair<String, Int>>()

        // main loop
        while (index < string.length) {
            var token = string[index++].toString()

            if (token == " " || token == "\t"){
                // whitespace
            } else if (token == "\n") {
                // newline
                if (index < string.length - 1) {
                    line++
                } else {
                    tokenList.add(TokenFactory.createEnd(line))
                }
            }else if (token == "~") {
                // comments
                if (string[index] == '>') {
                    while (string[index] != '\n') {
                        index++
                    }
                } else if (string[index] == '{') {
                    while (index < string.length && string[index] != '}') {
                        index++
                    }
                    if (index >= string.length || string[index++ - 1] != '}' && string[index++] != '~') {
                        errorList.add(Error(line, "Scanner", "Unterminated comment"))
                    }
                } else {
                    errorList.add(Error(line, "Scanner", "Unidentified symbol '$token'"))
                }
            } else if (GrouperToken.isGrouper(token)) {
                if (GrouperToken.isOpener(token[0])) {
                    grouperStack.addFirst(Pair(token, line))
                } else {
                    if (grouperStack.isNotEmpty() && GrouperToken.isPair(token, grouperStack[0].first)) {
                        grouperStack.removeFirst()
                    } else {
                        errorList.add(Error(line, "Scanner", "Mismatched grouping symbol '$token'"))
                    }
                }
                tokenList.add(TokenFactory.createGrouper(GrouperToken.fromSymbol(token), line))
            } else if (OperatorToken.isValidStart(token[0])) {
                // operators
                if (index < string.length && OperatorToken.isOperator(token + string[index])) {
                    token += "${string[index++]}"
                } else if (!OperatorToken.isOperator(token)) {
                    errorList.add(Error(line, "Scanner", "Unidentified symbol '$token'"))
                    continue
                }
                tokenList.add(TokenFactory.createOperator(OperatorToken.fromSymbol(token), line))
            } else if (token == "\"") {
                // strings
                token = ""
                while (index < string.length && string[index] != '"') {
                    token += string[index++]
                }
                if (index++ == string.length) {
                    errorList.add(Error(line, "Scanner", "Unterminated string"))
                    break
                }
                tokenList.add(TokenFactory.createString('"' + token + '"', line))
            } else if (token[0].isDigit()) {
                // digits
                while (string[index].isDigit()) {
                    token += string[index++]
                }
                if (string[index] != '.') {
                    tokenList.add(TokenFactory.createInt(token, line))
                } else {
                    token += string[index++]
                    while (string[index].isDigit()) {
                        token += string[index++]
                    }
                    tokenList.add(TokenFactory.createFloat(token, line))
                }
            } else if (token[0].isLetter()) {
                // keywords / identifiers
                while (string[index].isLetter() || string[index].isDigit() || string[index] ==  '_') {
                    token += string[index++]
                }
                if (KeywordToken.isKeyword(token)) {
                    tokenList.add(TokenFactory.createKeyword(KeywordToken.fromWord(token), line))
                } else {
                    tokenList.add(TokenFactory.createIdentifier(token, line))
                }
            } else {
                errorList.add(Error(line, "Scanner", "Unidentified symbol '$token'"))
            }
        }
        if (grouperStack.isNotEmpty()) {
            errorList.add(Error(grouperStack[0].second, "Scanner", "Last unclosed grouping symbol '${grouperStack[0].first}'"))
        }
        return tokenList.toList()
    }
}

