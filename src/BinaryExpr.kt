data class BinaryExpr(
    val left: Expression,
    val operator: BinaryOp,
    val right: Expression,
    override val position: Position
): Expression