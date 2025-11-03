// lambdaExpression -> lambdaParamList "=>" funcCall
data class PipeLambda(
    val parameters: List<TypedParam>,
    val call: FuncCall,
    override val position: Position
): PipeSegment