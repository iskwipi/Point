// varAssignment -> (identifier "<-")? expression (">>" pipeSegment)*
data class VarAssignment(
    val identifier: Identifier?,
    val expression: Expression,
    val pipes: List<PipeSegment>,
    override val position: Position
): Statement