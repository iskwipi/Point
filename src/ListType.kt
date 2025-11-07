data class ListType(
    val type: Type,
    val isNullable: Boolean,
    override val position: Position
): Type