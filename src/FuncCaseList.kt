// caseList -> "{" (normalCase)+ "otherwise" "->" expression "}"
data class FuncCaseList(
    val cases: List<CaseClause>,
    val default: Expression,
    override val position: Position
): FuncLogic