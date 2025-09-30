# >.< (Point)

## Creator
Adriel Neyro S. Caraig

## Language Overview
\>.< or Point is a programming language designed to take advantage of an arrow as a symbol (->). Its main distinguishing feature is how it can use an arrow either to pass a variable through a series of functions, to declare a range of values, to assign a value to a variable, or to point to a statement where a condition would lead to.

## Keywords
The currently reserved keywords are the following:
```
Variable declaration: var
Function declaration: func
Class declaration: class
Conditional keywords: if, else, when
Loop keywords: for, while, do, break, continue
```

## Operators
The currently implemented operators are the following:
```
Arithmetic: +, -, *, /, %, ^
Logical: /\, \/, (+), !
Comparison: <, <=, >, >=, ==, !=
Assignment: =, +=, -=, *=, /=, %=, ^=, <-
Forwarder operator: ->
Range operator: ->
Property operator: .
Iterable operator: :
Unused operators: @, #, $, &, |, ?
```

## Literals
There is no additional formatting for literals except for strings. Each literal can be declared as such:
```
Boolean: var bool = true;
Integer: var int = 1;
Float: var float = 1.01;
String: var str = "ABC";
```

## Identifiers
The rules for naming an identifier are the following:
- It should only contain alphanumeric characters or '_'
- The first character of the name should be a letter
- It cannot be one of the reserved keywords
- All names are case-sensitive

## Comments
Single line comments are denoted by the '~' symbol. Multiline comments are denoted by "~<" symbols at the start and by ">~" at the end. Single and multiline comments can be written as such:
```
~ This is a single line comment
~~~ This is also a single line comment
~<
This is a
multiline
comment
>~
```

## Syntax Style
Important notes for the syntax:
- Whitespace is significant especially for operators
- Statements are terminated by a semicolon
- Curly brackets are used for statement grouping
- Square brackets are used for collections
- Parentheses are used for arithmetic and logical grouping and to signify functions

## Sample Code
```
~< This creates a collection of numbers from 0 to 5 >~
var nums <- [0];
for (x: 1 -> 5) {
    nums += x;
}

~< Some magic happens here (precedence to be reviewed) >~
var sum <- ([3, 9] -> avg() + nums) -> sort() -> print() -> sum();

~< Conditional statements >~
if (sum > 0) -> print("The sum of nums is " + sum);
else -> print("The sum of nums is zero");

~< Manual looping through a collection of strings >~
var fruits <- ["Apple", "Banana", "Coconut"];
var index <- 0;
var running <- true;

~< AND boolean operator represented by "/\" symbol >~
while (running /\ index <= fruits -> length()) {
    when (fruits[index]) {
        "Apple" -> print("I like apples!");
        "Banana" -> print("I don't like bananas!");
        else -> {
            print("I don't know this fruit.");
            running <- false
        }
    } ~~~ WHEN expression totally not based on Kotlin ;)
    index += 1
}
```

## Design Rationale
The design rationale for Point is that sometimes it is confusing when calling multiple functions continuously for one variable, making it hard to read. This is where Point comes in with a multipurpose arrow symbol that does not just fill in the gap previously mentioned but performs other helpful functions too! (even though using the arrow symbol for assignment is more hassle than just using an equal sign but it definitely looks cooler!)