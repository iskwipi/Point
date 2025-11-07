fun main() {
    while (true) {
        val lines = mutableListOf<String>()
        var line = ""
        while (!line.contains(';')) {
            print("> ")
            line = readln()
            lines.add(line)
        }
        val scanner = Scanner(lines.joinToString("\n", "", "\n"))
        val tokenList = scanner.scan()
        for (token in tokenList) println(token)
        val parser = Parser(tokenList)
        val program = parser.parse()
        parser.printProgram(program)
        println("--------------------------------------------------------------------------------")
    }
}