object Utilities {
    fun getStatement(): String {
        val lines = mutableListOf<String>()
        var line = ""
        while (!line.contains(';')) {
            print("> ")
            line = readln()
            lines.add(line)
        }
        return lines.joinToString("\n", "", "\n")
    }

    fun printTokens(tokens: List<Token>) {
        for (token in tokens) println(token)
    }

    private val primitiveTypes = setOf(
        Int::class.java,
        Float::class.java,
        Boolean::class.java,
        String::class.java,
        Char::class.java,
        List::class.java,
        UnaryOp::class.java,
        BinaryOp::class.java,
        TypeName::class.java,
        Integer::class.java,
        java.lang.Float::class.java,
        java.lang.Boolean::class.java
    )
    private fun printObject(obj: Any, tabs: Int) {
        if (obj::class.java in primitiveTypes) {
            println("${"  ".repeat(tabs)}(${obj::class.java.simpleName}) $obj")
        } else {
            printNode(obj as ASTNode, tabs)
        }
    }
    private fun printNode(node: ASTNode, tabs: Int) {
        val javaClass = node.javaClass
        println("${"  ".repeat(tabs)}${javaClass.name}(")
        javaClass.declaredFields.forEach {
            it.isAccessible = true
            val value = it.get(node)
            if (value != null && it.name != "position") {
                if (value is List<*>) {
                    if (value.isNotEmpty()) {
                        println("${"  ".repeat(tabs + 1)}${it.name}:")
                        value.forEach { item ->
                            printObject(item ?: "", tabs + 2)
                        }
                    }
                } else {
                    printObject(value, tabs+1)
                }
            }
        }
        println("${"  ".repeat(tabs)}),")
    }
    fun printProgram(program: Program) {
        printNode(program, 0)
    }
}