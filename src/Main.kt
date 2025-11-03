fun main() {
    while (true) {
        val lines = mutableListOf<String>()
        while (true) {
            print("> ")
            val line = readln()
            if (line == "END") break
            lines.add(line)
        }
        val scanner = Scanner(lines.joinToString("\n", "", "\n"))
        val tokenList = scanner.scanString()
        for (token in tokenList) println(token)
        println("--------------------------------------------------------------------------------")
    }
}