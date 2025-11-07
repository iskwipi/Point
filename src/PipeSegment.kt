// pipeSegment -> (identifier "=>")? funcCall
data class PipeSegment(
    val parameter: Identifier?,
    val call: FuncCall,
    override val position: Position
): ASTNode