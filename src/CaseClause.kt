// caseClause -> expression "->" expression ","
data class CaseClause(
    val condition: Expression,
    val output: Expression,
    override val position: Position
): ASTNode