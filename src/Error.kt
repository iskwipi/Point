class Error(
    val line: Int,
    val file: String,
    val message: String
) {
    override fun toString(): String {
        return "Error(line $line: $message)"
    }
    init {
        println("[line $line] $file error: $message")
    }
}