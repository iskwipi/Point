// funcPremise -> identifier "(" (paramList)? ")" ":" type
data class FuncPremise(
    val name: Identifier,
    val parameters: List<Parameter>,
    val type: Type?,
    override val position: Position
): ASTNode