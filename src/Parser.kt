class Parser(private val tokens: List<Token>) {
    private var index = 0
    val errorList = mutableListOf<Error>()

    // helper functions for parsing
    private fun peek(): Token {
        if (index < tokens.size) return tokens[index]
        return tokens.last()
    }
    private fun advance(): Token {
        val token = peek()
        index++
        return token
    }
    private fun match(vararg types: TokenType): Boolean {
        val current = peek()
        if (types.contains(current.type)) {
            advance()
            return true
        }
        return false
    }
    private fun expect(type: TokenType, errorMsg: String = "Unexpected token"): Token {
        val current = peek()
        if (current.type != type) errorList.add(Error(current.line, "Parser", errorMsg))
        return advance()
    }

    // parsing functions
    fun parse(): Program {
        // program -> (statement)*
        val statements = mutableListOf<Statement>()
        while (peek().type != MiscToken.EOF) {
            statements.add(parseStatement())
        }
        return Program(statements, Position(tokens.first().line, tokens.last().line))
    }
    private fun parseStatement(): Statement {
        // statement -> (varAssignment | funcDefinition) ";"
        val statement = when (match(KeywordToken.DEFINE)) {
            true -> parseFuncDefinition()
            false -> parseVarAssignment()
        }
        expect(OperatorToken.SEMICOLON, "Expected ';' after statement")
        return statement
    }
    private fun parseVarAssignment(): VarAssignment {
        // varAssignment -> (identifier "<-")? expression (">>" pipeSegment)*
        val startLine = peek().line
        var expression = parseExpression()
        var identifier: Identifier? = null
        val pipes = mutableListOf<PipeSegment>()
        if (expression is Identifier) {
            if (peek().type == OperatorToken.LEFT_ARROW) {
                advance()
                identifier = expression.copy()
                expression = parseExpression()
            } else if (peek().type == OperatorToken.SEMICOLON) {
                // continue
            } else if (peek().type != OperatorToken.FORWARDER) {
                expect(OperatorToken.LEFT_ARROW, "Expected '<-' after variable name in variable assignment")
            }
        }
        while (match(OperatorToken.FORWARDER)) {
            pipes.add(parsePipeSegment())
        }
        val endLine = pipes.lastOrNull()?.position?.end ?: startLine
        return VarAssignment(identifier, expression, pipes, Position(startLine, endLine))
    }
    private fun parsePipeSegment(): PipeSegment {
        // pipeSegment -> (identifier "=>")? funcCall
        val startLine = peek().line
        var token = expect(VariableToken.IDENTIFIER, "Invalid pipe segment")
        var identifier = Identifier(token.lexeme as String, Position(token.line, token.line))
        var parameter: Identifier? = null
        if (match(OperatorToken.LAMBDA_ARROW)) {
            parameter = identifier.copy()
            token = expect(VariableToken.IDENTIFIER, "Invalid lambda function")
            identifier = Identifier(token.lexeme as String, Position(token.line, token.line))
        }
        val call = parseFuncCall(identifier)
        val endLine = call.position.end
        return PipeSegment(parameter, call, Position(startLine, endLine))
    }
    private fun parseFuncDefinition(): FuncDefinition {
        // funcDefinition -> "define" funcPremise "=" funcLogic
        val premise = parseFuncPremise()
        expect(OperatorToken.EQUAL, "Expected '=' in function definition")
        val logic = parseFuncLogic()
        return FuncDefinition(premise, logic, Position(premise.position.start, logic.position.end))
    }
    private fun parseFuncPremise(): FuncPremise {
        // funcPremise -> identifier "(" (paramList)? ")" ":" type
        val token = expect(VariableToken.IDENTIFIER, "Expected function name in function definition")
        val name = Identifier(token.lexeme as String, Position(token.line, token.line))
        expect(GrouperToken.LEFT_PAREN, "Expected '(' after function name")
        val parameters = if (peek().type != GrouperToken.RIGHT_PAREN) parseParamList() else emptyList()
        expect(GrouperToken.RIGHT_PAREN, "Expected ')' after parameter list")
        expect(OperatorToken.COLON, "Expected ':' before function return type")
        val type = parseType()
        return FuncPremise(name, parameters, type, Position(name.position.start, type.position.end))
    }
    private fun parseParamList(): List<Parameter> {
        // paramList -> parameter ("," parameter)*
        val parameters = mutableListOf<Parameter>()
        parameters.add(parseParameter())
        while (match(OperatorToken.COMMA)) {
            parameters.add(parseParameter())
        }
        return parameters.toList()
    }
    private fun parseParameter(): Parameter {
        // parameter -> typedParam ("<-" literal)?
        val declaration = parseTypedParam()
        var default: Literal? = null
        if (match(OperatorToken.LEFT_ARROW)) {
            default = parseLiteral()
        }
        return Parameter(declaration, default, Position(declaration.position.start, default?.position?.end ?: declaration.position.end))
    }
    private fun parseTypedParam(): TypedParam {
        // typedParam -> identifier ":" type
        val token = expect(VariableToken.IDENTIFIER, "Expected parameter name in function parameters")
        val name = Identifier(token.lexeme as String, Position(token.line, token.line))
        expect(OperatorToken.COLON, "Expected ':' before parameter return type")
        val type = parseType()
        return TypedParam(name, type, Position(name.position.start, type.position.end))
    }
    private fun parseType(): Type {
        // type -> (baseType | listType) ("?")?
        // baseType -> "Bool" | "Int" | "Float" | "String" | "Any"
        // listType -> "[" type "]"
        val startLine = peek().line
        var type: Type = when (peek().type) {
            KeywordToken.BOOL_DEF,
            KeywordToken.INT_DEF,
            KeywordToken.FLOAT_DEF,
            KeywordToken.STRING_DEF,
            KeywordToken.ANY_DEF -> {
                val token = advance()
                val tokenType = token.type as KeywordToken
                val name = TypeName.valueOf(tokenType.getWord().uppercase())
                BaseType(name, false, Position(token.line, token.line))
            }
            GrouperToken.LEFT_SQUARE -> {
                advance()
                val inner = parseType()
                val endLine = expect(GrouperToken.RIGHT_SQUARE, "Expected ']' after list type").line
                ListType(inner, false, Position(startLine, endLine))
            }
            else -> {
                val endLine = expect(MiscToken.INVALID, "Missing parameter return type").line
                InvalidType(Position(startLine, endLine))
            }
        }
        if (match(OperatorToken.QUESTION)) {
            type = when (type) {
                is BaseType -> type.copy(isNullable = true)
                is ListType -> type.copy(isNullable = true)
                else -> type
            }
        }
        return type
    }
    private fun parseFuncLogic(): FuncLogic {
        // funcLogic -> expression | caseList
        return if (peek().type == GrouperToken.LEFT_CURLY) {
            parseCaseList()
        } else {
            val expression = parseExpression()
            FuncExpression(expression, expression.position)
        }
    }
    private fun parseCaseList(): FuncCaseList {
        // caseList -> "{" (normalCase)+ "otherwise" "->" expression "}"
        val startLine = expect(GrouperToken.LEFT_CURLY).line
        val cases = mutableListOf<CaseClause>()
        while (peek().type != KeywordToken.OTHERWISE && peek().type != GrouperToken.RIGHT_CURLY) {
            cases.add(parseCaseClause())
        }
        val default: Expression = when (peek().type) {
            GrouperToken.RIGHT_CURLY -> {
                errorList.add(Error(peek().line, "Parser", "Missing otherwise clause in case list"))
                NothingLit(Position(peek().line, peek().line))
            }
            else -> {
                expect(KeywordToken.OTHERWISE)
                expect(OperatorToken.RIGHT_ARROW, "Expected '->' after otherwise keyword")
                parseExpression()
            }
        }
        val endLine = expect(GrouperToken.RIGHT_CURLY, "Expected '}' after case list").line
        return FuncCaseList(cases, default, Position(startLine, endLine))
    }
    private fun parseCaseClause(): CaseClause {
        // caseClause -> expression "->" expression ","
        val startLine = peek().line
        val condition = parseExpression()
        expect(OperatorToken.RIGHT_ARROW, "Expected '->' after condition in case clause")
        val output = parseExpression()
        val endLine = expect(OperatorToken.COMMA, "Expected ',' after case clause").line
        return CaseClause(condition, output, Position(startLine, endLine))
    }
    private fun parseExpression(): Expression {
        // expression -> orLogic
        return parseOrLogic()
    }
    private fun parseOrLogic(): Expression {
        // orLogic -> xorLogic ("or" xorLogic)*
        var left = parseXorLogic()
        while (match(KeywordToken.OR)) {
            val right = parseXorLogic()
            left = BinaryExpr(left, BinaryOp.OR, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseXorLogic(): Expression {
        // xorLogic -> andLogic ("xor" andLogic)*
        var left = parseAndLogic()
        while (match(KeywordToken.XOR)) {
            val right = parseAndLogic()
            left = BinaryExpr(left, BinaryOp.XOR, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseAndLogic(): Expression {
        // andLogic -> notLogic ("and" notLogic)*
        var left = parseNotLogic()
        while (match(KeywordToken.AND)) {
            val right = parseNotLogic()
            left = BinaryExpr(left, BinaryOp.AND, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseNotLogic(): Expression {
        // notLogic -> ("not")* comparison
        val startLine = peek().line
        if (match(KeywordToken.NOT)) {
            val expression = parseNotLogic()
            val endLine = expression.position.end
            return UnaryExpr(UnaryOp.NOT, expression, Position(startLine, endLine))
        }
        return parseComparison()
    }
    private fun parseComparison(): Expression {
        // comparison -> mathExpression (comparator mathExpression)?
        // comparator -> "<" | ">" | "<=" | ">=" | "==" | "!="
        var left = parseMathExpression()
        val comparator = when (peek().type) {
            OperatorToken.LESS -> BinaryOp.LESS
            OperatorToken.GREAT -> BinaryOp.GREAT
            OperatorToken.LESS_EQUAL -> BinaryOp.LESS_EQUAL
            OperatorToken.GREAT_EQUAL -> BinaryOp.GREAT_EQUAL
            OperatorToken.EQUAL_EQUAL -> BinaryOp.EQUAL_EQUAL
            OperatorToken.NOT_EQUAL -> BinaryOp.NOT_EQUAL
            else -> null
        }
        if (comparator != null) {
            advance()
            val right = parseMathExpression()
            left = BinaryExpr(left, comparator, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseMathExpression(): Expression {
        // mathExpression -> term (("+" | "-") term)*
        var left = parseTerm()
        while (peek().type == OperatorToken.PLUS || peek().type == OperatorToken.MINUS) {
            val operator = if (advance().type == OperatorToken.PLUS) BinaryOp.PLUS else BinaryOp.MINUS
            val right = parseTerm()
            left = BinaryExpr(left, operator, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseTerm(): Expression {
        // term -> factor (("*" | "/" | "%") factor)*
        var left = parseFactor()
        while (peek().type == OperatorToken.TIMES || peek().type == OperatorToken.DIVIDE || peek().type == OperatorToken.MODULO) {
            val operator = when (advance().type) {
                OperatorToken.TIMES -> BinaryOp.TIMES
                OperatorToken.DIVIDE -> BinaryOp.DIVIDE
                else -> BinaryOp.MODULO
            }
            val right = parseFactor()
            left = BinaryExpr(left, operator, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseFactor(): Expression {
        // factor -> ("+" | "-")* exponent
        val startLine = peek().line
        if (peek().type == OperatorToken.PLUS || peek().type == OperatorToken.MINUS) {
            val operator = if (advance().type == OperatorToken.PLUS) UnaryOp.PLUS else UnaryOp.MINUS
            val exponent = parseFactor()
            val endLine = exponent.position.end
            return UnaryExpr(operator, exponent, Position(startLine, endLine))
        }
        return parseExponent()
    }
    private fun parseExponent(): Expression {
        // exponent -> value ("^" exponent)?
        val left = parseValue()
        if (match(OperatorToken.POWER)) {
            val right = parseExponent()
            return BinaryExpr(left, BinaryOp.POWER, right, Position(left.position.start, right.position.end))
        }
        return left
    }
    private fun parseValue(): Value {
        // value -> literal | identifier | funcCall | "(" expression ")"
        return when (peek().type) {
            VariableToken.INT,
            VariableToken.FLOAT,
            VariableToken.STRING,
            KeywordToken.TRUE,
            KeywordToken.FALSE,
            KeywordToken.NOTHING,
            GrouperToken.LEFT_SQUARE -> parseLiteral()
            VariableToken.IDENTIFIER -> {
                val token = expect(VariableToken.IDENTIFIER)
                val identifier = Identifier(token.lexeme as String, Position(token.line, token.line))
                if (peek().type == GrouperToken.LEFT_PAREN) parseFuncCall(identifier) else identifier
            }
            GrouperToken.LEFT_PAREN -> {
                val startLine = advance().line
                val expression = parseExpression()
                val endLine = expect(GrouperToken.RIGHT_PAREN, "Expected ')' after expression").line
                ParenExpr(expression, Position(startLine, endLine))
            }
            else -> {
                val line = expect(MiscToken.INVALID, "Missing value token").line
                InvalidVal(Position(line, line))
            }
        }
    }
    private fun parseLiteral(): Literal {
        // literal -> int | float | string | "True" | "False" | "Nothing" | "[" argList "]"
        val type = peek().type
        val line = peek().line
        return when (type) {
            VariableToken.INT -> IntLit(advance().literal as Int, Position(line, line))
            VariableToken.FLOAT -> FloatLit(advance().literal as Float, Position(line, line))
            VariableToken.STRING -> StringLit(advance().literal as String, Position(line, line))
            KeywordToken.TRUE, KeywordToken.FALSE -> BoolLit(advance().lexeme.toBoolean(), Position(line, line))
            KeywordToken.NOTHING -> {
                advance()
                NothingLit(Position(line, line))
            }
            GrouperToken.LEFT_SQUARE -> {
                val startLine = advance().line
                val items = if (peek().type != GrouperToken.RIGHT_SQUARE) parseArgList() else emptyList()
                val endLine = expect(GrouperToken.RIGHT_SQUARE, "Expected ']' after list literal").line
                ListLit(items, Position(startLine, endLine))
            }
            else -> {
                expect(MiscToken.INVALID, "Missing literal value")
                InvalidLit(Position(line, line))
            }
        }
    }
    private fun parseFuncCall(identifier: Identifier): FuncCall {
        // funcCall -> identifier "(" (argList)? ")"
        expect(GrouperToken.LEFT_PAREN, "Expected '(' after function name")
        val arguments = if (peek().type != GrouperToken.RIGHT_PAREN) parseArgList() else emptyList()
        val endLine = expect(GrouperToken.RIGHT_PAREN, "Expected ')' after argument list")
        return FuncCall(identifier, arguments, Position(identifier.position.start, endLine.line))
    }
    private fun parseArgList(): List<Expression> {
        // argList -> expression ("," expression)*
        val list = mutableListOf<Expression>()
        list.add(parseExpression())
        while (match(OperatorToken.COMMA)) list.add(parseExpression())
        return list.toList()
    }
}