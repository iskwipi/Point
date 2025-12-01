import java.io.File
import java.nio.charset.StandardCharsets

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        while (true) {
            // statement interpreter mode
            val statement = Utilities.getStatement()
            val scanner = Scanner(statement)
            val tokens = scanner.scan()
//        Utilities.printTokens(tokens)
            val parser = Parser(tokens)
            val program = parser.parse()
//        Utilities.printProgram(program)
            val evaluator = Evaluator(program)
            evaluator.evaluate()
            println("--------------------------------------------------------------------------------")
        }
    } else {
        val filePath = args[0]
        try {
            val file = File(filePath)
            val content = file.readText() + "\n"
            val scanner = Scanner(content)
            val tokens = scanner.scan()
//            Utilities.printTokens(tokens)
            val parser = Parser(tokens)
            val program = parser.parse()
            Utilities.printProgram(program)
            val evaluator = Evaluator(program)
            evaluator.evaluate()
        } catch (e: Exception) {
            println("Error reading file: ${e.message}")
        }
    }
}