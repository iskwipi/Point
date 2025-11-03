data class ListType(
    val type: BaseType,
    val isNullable: Boolean,
    override val position: Position
): Type