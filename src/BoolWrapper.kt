data class BoolWrapper(val value: Boolean) {
    override fun toString(): String {
        return if (value) "True" else "False"
    }
}