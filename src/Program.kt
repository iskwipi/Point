// program -> (statement)*
data class Program(
    val statements: List<Statement>,
    override val position: Position
): ASTNode