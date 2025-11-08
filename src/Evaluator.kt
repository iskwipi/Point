import kotlin.math.pow

class Evaluator(private val program: Program) {
    val errorList = mutableListOf<Error>()

    fun evaluate() {
        val result = evaluate(program)
        println(result.toString().replace("true", "True").replace("false", "False"))
    }
    private fun evaluate(node: ASTNode): Any {
        return when (node) {
            is Program -> evaluate(node.statements.first())
            is VarAssignment -> evaluateExpression(node.expression)
            is Literal -> evaluateLiteral(node)
            is UnaryExpr -> evaluateUnaryExpr(node)
            is BinaryExpr -> evaluateBinaryExpr(node)
            is ParenExpr -> evaluateExpression(node.expression)
            else -> Nothing
        }

    }
    private fun evaluateLiteral(literal: Literal): Any {
        return when (literal) {
            is IntLit -> literal.value
            is FloatLit -> literal.value
            is StringLit -> literal.value
            is BoolLit -> literal.value
            is ListLit -> literal.items.map(::evaluateExpression)
            is NothingLit -> Nothing
            else -> errorList.add(Error(
                literal.position.start,
                "Evaluator",
                "Invalid literal"
            ))
        }
    }
    private fun evaluateUnaryExpr(node: UnaryExpr): Any {
        val expression = evaluateExpression(node.expression)
        return when {
            node.operator == UnaryOp.NOT && expression is Boolean -> !expression
            node.operator == UnaryOp.PLUS && (expression is Int || expression is Float) -> expression
            node.operator == UnaryOp.MINUS && expression is Int -> -expression
            node.operator == UnaryOp.MINUS && expression is Float -> -expression
            else -> {
                errorList.add(Error(
                    node.position.start,
                    "Evaluator",
                    "(Runtime Warning) Unary expression ${node.operator.name}(${expression.javaClass.simpleName}) evaluated to Nothing"
                ))
                Nothing
            }
        }
    }
    private fun evaluateBinaryExpr(node: BinaryExpr): Any {
        val left = evaluateExpression(node.left)
        val right = evaluateExpression(node.right)
        return when {
            node.operator == BinaryOp.AND && left is Boolean && right is Boolean -> left && right
            node.operator == BinaryOp.OR && left is Boolean && right is Boolean -> left || right
            node.operator == BinaryOp.XOR && left is Boolean && right is Boolean -> left != right
            node.operator == BinaryOp.LESS && left is Int && right is Int -> left < right
            node.operator == BinaryOp.LESS && left is Float && right is Float -> left < right
            node.operator == BinaryOp.LESS && left is String && right is String -> left < right
            node.operator == BinaryOp.GREAT && left is Int && right is Int -> left > right
            node.operator == BinaryOp.GREAT && left is Float && right is Float -> left > right
            node.operator == BinaryOp.GREAT && left is String && right is String -> left > right
            node.operator == BinaryOp.LESS_EQUAL && left is Int && right is Int -> left <= right
            node.operator == BinaryOp.LESS_EQUAL && left is Float && right is Float -> left <= right
            node.operator == BinaryOp.LESS_EQUAL && left is String && right is String -> left <= right
            node.operator == BinaryOp.GREAT_EQUAL && left is Int && right is Int -> left >= right
            node.operator == BinaryOp.GREAT_EQUAL && left is Float && right is Float -> left >= right
            node.operator == BinaryOp.GREAT_EQUAL && left is String && right is String -> left >= right
            node.operator == BinaryOp.EQUAL_EQUAL && left is Int && right is Int -> left == right
            node.operator == BinaryOp.EQUAL_EQUAL && left is Float && right is Float -> left == right
            node.operator == BinaryOp.EQUAL_EQUAL && left is String && right is String -> left == right
            node.operator == BinaryOp.NOT_EQUAL && left is Int && right is Int -> left != right
            node.operator == BinaryOp.NOT_EQUAL && left is Float && right is Float -> left != right
            node.operator == BinaryOp.NOT_EQUAL && left is String && right is String -> left != right
            node.operator == BinaryOp.PLUS && left is Int && right is Int -> left + right
            node.operator == BinaryOp.PLUS && left is Float && right is Float -> left + right
            node.operator == BinaryOp.MINUS && left is Int && right is Int -> left - right
            node.operator == BinaryOp.MINUS && left is Float && right is Float -> left - right
            node.operator == BinaryOp.TIMES && left is Int && right is Int -> left * right
            node.operator == BinaryOp.TIMES && left is Float && right is Float -> left * right
            node.operator == BinaryOp.DIVIDE && left is Int && right is Int && right != 0 -> left / right
            node.operator == BinaryOp.DIVIDE && left is Float && right is Float  && right != 0.toFloat() -> left / right
            node.operator == BinaryOp.MODULO && left is Int && right is Int -> left % right
            node.operator == BinaryOp.POWER && left is Int && right is Int -> left.toFloat().pow(right).toInt()
            node.operator == BinaryOp.POWER && left is Float && right is Float -> left.pow(right)
            else -> {
                if (node.operator == BinaryOp.DIVIDE && (right is Int && right == 0 || right is Float && right == 0.toFloat())) {
                    errorList.add(
                        Error(
                            node.position.start,
                            "Evaluator",
                            "(Runtime Warning) Division by 0 evaluated to Nothing"
                        )
                    )
                } else {
                    errorList.add(
                        Error(
                            node.position.start,
                            "Evaluator",
                            "(Runtime Warning) Binary expression ${node.operator.name}(${left.javaClass.simpleName}, ${right.javaClass.simpleName}) evaluated to Nothing"
                        )
                    )
                }
                Nothing
            }
        }
    }
    private fun evaluateExpression(expression: Expression): Any {
        return evaluate(expression)
    }
}