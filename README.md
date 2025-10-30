# >.< (Point)
![Manny Pacquiao: Official Point Mascot](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBTTAzWnT9BRJBPdpnMYze_wvBaQwivXt9EQ&s)

## Creator
Adriel Neyro S. Caraig

## Language Overview
\>.< or Point is a functional programming language designed to take advantage of arrows (>>, =>, ->, ~>) as operators. The most important symbol, though, is ">>", or the forwarder operator, as it allows the user to pass the value from its left side as the first argument of the function being called on its right side. This feature aims to improve the readability of chained functions, representing them as an easily traceable pipeline.

## Keywords
The currently reserved keywords are the following:
- Special Values: True, False, Nothing
- Data Types: Bool, Int, Float, String, Function, Any
- Logical Operators: and, or, xor, not
- Default Case Keyword: otherwise

## Operators
The currently implemented operators are the following:
- Arithmetic: +, -, *, /, %, ^
- Comparison: <, <=, >, >=, ==, !=
- Function Definition: =
- Type Declarator: :
- Nullable Signifier: ?
- Variable Assignment: <-
- Conditional Lambda: ->
- Unconditional Lambda: =>
- Forwarder Operator: >>
- Argument Separator: ,
- Function Call Operator: ()
- Array Declaration Operator: []
- Condition Group Operator: {}
- Statement Terminator: ;

## Literals
Each literal can be declared as such:
```
var1 <- False;   ~> Bool
var2 <- 0;      ~> Integer
var3 <- 1.23;   ~> Float
var4 <- "ABC";  ~> String
```

## Identifiers
The rules for naming an identifier are the following:
- It should only contain alphanumeric characters or '_'.
- The first character of the name should be a letter.
- It cannot be one of the reserved keywords.
- All names are case-sensitive.

The conventions for naming an identifier are the following:
- Variables follow the snake_case
- Functions follow the camelCase

## Comments
Single-line comments are denoted by the "\~>" symbol. Multiline comments are denoted by the "\~{" symbol at the start and by the "}\~" symbol at the end. Both types of comments can be written as such:
```
~> This is a single line comment
~{
    This is a
    multiline
    comment
}~
```

## Syntax Style
Important notes on the language's syntax:
- Whitespace is only significant between operator symbols.
- Statements are always terminated by a semicolon.
- All data (variables and functions) are immutable.
- Helper functions are implemented to provide more functionality.
- Defined functions can be passed to some helper functions using their identifiers without parentheses as arguments.

## Sample Code
```
~{ 
    These are the runnable lines of code. It is important
    to note that all variables are immutable and function
    calls cannot change the value of a variable in any way.
}~

~> normal function call
print("Hello World!");

~> variable declarations
int_array1 <- [-1, 0, 1, 2, 3];
int_array2 <- [10, 11, 12];
out_string <- "Non-negative sum is: ";

~> passing variables through function pipelines
int_array1
    >> filter(isPositive)                 ~> previous value passed as first argument
    >> print()                            ~> print value then pass to next function
    >> fold(plus, 0)                      ~> built-in array reducing functions
    >> sum: Int => plus(out_string, sum)  ~> lambda function to rearrange arguments
    >> print();                           ~> original array variable not changed
int_array2
    >> sort(minus, True)                  ~> built-in array processing functions..
    >> map(factorial)                     ~> that take other functions as arguments..
    >> print();                           ~> will also be implemented for ease of use

~{
    These are the sample function definitions and the
    declarations of the implemented helper functions.
}~

~> complete syntax for function definition
~> parameter and return type should always be declared
isPositive(num: Int): Bool = (num >= 0);

~> overloading the plus() function to work with different types
plus(left: Int, right: Any): Int = left + int(right);
plus(left: Float, right: Any): Float = left + float(right);
plus(left: String, right: Any): String = concatenate(left, string(right));

~> otherwise keyword to signify default case
~> return Nothing to signify invalid input
~> type declaration considers Nothing with '?'
factorial(num: Int): Int? = {
    num > 0 -> num * factorial(num - 1);
    num == 0 -> 1;
    otherwise -> Nothing;
};

~> printing helper functions
~> note: most helper functions just return original first argument
print(object: Any): Any;
print(object: [Any]): [Any];
display(object: [Any], formatting: String): [Any];
concatenate(left: String, right: Any): String;

~> array handling helper functions
~> note: arrays must only contain one type
concatenate(left: [Any], right: [Any]): [Any];
flatten(collection: [[Any]]): [Any];

~> array processing helper functions
~> note: default value permitted in parameter declaration
map(collection: [Any], transformer: Function): [Any];
sort(collection: [Any], transformer: Function, descending: Bool <- False): [Any];

~> array reducing helper functions
~> note: function as argument should always be defined first before use
filter(collection: [Any], reducer: Function): [Any];
fold(collection: [Any], reducer: Function, initial: Any): Any;
```

## Design Rationale
The design rationale for Point is that sometimes, it is confusing when calling multiple functions continuously for one variable, making the code hard to read. This is where Point comes in, allowing the user to form a pipeline of functions where the variable can just pass through. This design choice makes the program flow easier to understand, even more so for people who are not used to the syntax of imperative languages.