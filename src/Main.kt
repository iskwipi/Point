fun main() {
    while (true) {
        print("> ")
        val tokenList = Scanner.scanString(readln())
        for (token in tokenList) println(token)
    }
}