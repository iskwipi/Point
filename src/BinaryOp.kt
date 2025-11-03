enum class BinaryOp(val operator: String) {
    OR("or"), XOR("xor"), AND("and"),
    LESS("<"), GREAT(">"),
    LESS_EQUAL("<="), GREAT_EQUAL(">="),
    EQUAL_EQUAL("=="), NOT_EQUAL("!="),
    PLUS("+"), MINUS("-"),
    TIMES("*"), DIVIDE("/"),
    MODULO("%"), POWER("^");
}