// funcDefinition -> "define" funcPremise "=" funcLogic
data class FuncDefinition(
    val premise: FuncPremise,
    val logic: FuncLogic,
    override val position: Position
): Statement