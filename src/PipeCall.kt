data class PipeCall(
    val call: FuncCall,
    override val position: Position
): PipeSegment