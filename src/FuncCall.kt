data class FuncCall(
    val identifier: Identifier,
    val arguments: List<Expression>,
    override val position: Position
): Value