// parameter -> typedParam ("<-" literal)?
data class Parameter(
    val declaration: TypedParam,
    val default: Literal,
    override val position: Position
): ASTNode