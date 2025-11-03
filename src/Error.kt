class Error(
    line: Int,
    file: String,
    message: String
) {
    init {
        println("[line $line] $file error: $message")
    }
}