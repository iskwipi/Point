// typedParam -> identifier ":" type
data class TypedParam(
    val name: Identifier,
    val type: Type?,
    override val position: Position
): ASTNode