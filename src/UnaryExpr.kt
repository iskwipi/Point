data class UnaryExpr(
    val operator: UnaryOp,
    val expression: Expression,
    override val position: Position
): Expression