fun main() {
    while (true) {
        print("> ")
        val scanner = Scanner(readln())
        val tokenList = scanner.scanString()
        for (token in tokenList) println(token)
    }
}