object Scanner {
    fun scanString(input: String): MutableList<Token> {
        // global vars
        val tokenList = mutableListOf<Token>()
        val string = input + "\n"
        var index = 0
        var line = 1

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
                if (string[index] != '<') {
                    while (string[index] != '\n') {
                        index++
                    }
                } else {
                    while (index < string.length && string[index] != '>') {
                        index++
                    }
                    if (index >= string.length || string[index++ - 1] != '>' && string[index++] != '~') {
                        println("Error at line $line: Unterminated comment")
                    }
                }
            } else if (Operator.isValidStart(token[0])) {
                // operators
                if (index + 1 < string.length && Operator.isSymbol(token + string[index] + string[index + 1])) {
                    token += "${string[index++]}${string[index++]}"
                } else if (index < string.length && Operator.isSymbol(token + string[index])) {
                    token += "${string[index++]}"
                }
                tokenList.add(TokenFactory.createOperator(Operator.fromSymbol(token), line))
            } else if (token == "\"") {
                // strings
                token = ""
                while (index < string.length && string[index] != '"') {
                    token += string[index++]
                }
                if (index++ == string.length) {
                    println("Error at line $line: Unterminated string")
                    break
                }
                tokenList.add(TokenFactory.createString(token, line))
            } else if (token[0].isDigit()) {
                // digits
                while (string[index].isDigit()) {
                    token += string[index++]
                }
                if (string[index] != '.') {
                    tokenList.add(TokenFactory.createInt(token.toInt(), line))
                } else {
                    token += string[index++]
                    while (string[index].isDigit()) {
                        token += string[index++]
                    }
                    tokenList.add(TokenFactory.createFloat(token.toFloat(), line))
                }
            } else if (token[0].isLetter()) {
                // keywords / identifiers
                while (string[index].isLetter() || string[index].isDigit() || string[index] ==  '_') {
                    token += string[index++]
                }
                if (Keyword.isKeyword(token)) {
                    tokenList.add(TokenFactory.createKeyword(Keyword.fromString(token), line))
                } else {
                    tokenList.add(TokenFactory.createIdentifier(token, line))
                }
            } else {
                println("Error at line $line: Unidentified character ($token)")
                continue
            }
        }
        return tokenList
    }
}

