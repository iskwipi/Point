// type -> (baseType | listType) ("?")?
// baseType -> "Bool" | "Int" | "Float" | "String" | "Any"
// listType -> "[" type "]"
sealed interface Type: ASTNode