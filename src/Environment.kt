object Environment {
    private val variables = mutableMapOf<String, Any>()
    val errorList = mutableListOf<Error>()

    fun define(name: String, value: Any, position: Position): Error? {
        if (variables.containsKey(name)) {
            val error = Error(
                position.start,
                "Environment",
                "Variable '$name' has already been defined and cannot be redefined"
            )
            errorList.add(error)
            return error
        }
        variables[name] = value
        return null
    }

    fun get(name: String, position: Position): Any {
        if (variables.containsKey(name)) return variables[name] ?: Nothing
        val error = Error(
            position.start,
            "Environment",
            "Variable '$name' has not been defined"
        )
        errorList.add(error)
        return error
    }

    fun printVars(){
        println(variables)
    }
}