fun main() {
    while (true) {
        // statement interpreter mode
        val statement = Utilities.getStatement()
        val scanner = Scanner(statement)
        val tokens = scanner.scan()
        Utilities.printTokens(tokens)
        val parser = Parser(tokens)
        val program = parser.parse()
        Utilities.printProgram(program)
        val evaluator = Evaluator(program)
        evaluator.evaluate()
        println("--------------------------------------------------------------------------------")
    }
}