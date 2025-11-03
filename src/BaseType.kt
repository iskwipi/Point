data class BaseType(
    val name: TypeName,
    val isNullable: Boolean,
    override val position: Position
): Type