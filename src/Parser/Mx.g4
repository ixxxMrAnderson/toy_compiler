grammar Mx;

INT: 'int';
BOOL: 'bool';
STRING: 'string';
NULL: 'null';
VOID: 'void';
TRUE: 'true';
FALSE: 'false';
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';
NEW: 'new';
CLASS: 'class';
THIS: 'this';
L_BRACKET: '[';
R_BRACKET: ']';

INTEGER: [0-9]+;
IDENTIFIER: ([a-zA-Z]) ([a-zA-Z] | INTEGER | '_')*;
STRING_CONSTANT: '"' (~[\u0000-\u001f\\"] | '\\'  [tbnr\\"])* '"';
BLANK: [ \r\t\n] -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;


program: (section)* EOF;
section: class_definition | function_definition | variable_definition;

class_definition: 'class' IDENTIFIER '{' (variable_definition | function_definition | class_constructor)* '}'';';
function_definition: type IDENTIFIER '(' function_parameter_list ')' suite;
variable_definition: type single_var_def (',' single_var_def)* ';';

type: array_type | basic_type;
basic_type: INT | BOOL | VOID | STRING | IDENTIFIER;
array_type: basic_type (blank_bracket)+;
function_parameter_list: (type single_var_def (',' type single_var_def)*)?;
single_var_def: IDENTIFIER ('=' expression)?;
class_constructor: IDENTIFIER '(' function_parameter_list ')' suite;
suite : '{' statement* '}';
bracket_expr : L_BRACKET expression? R_BRACKET;
blank_bracket : L_BRACKET R_BRACKET;

statement
    : suite                                                 # block
    | variable_definition                                   # var_def_stmt
    | IF '(' expression ')' if_true = statement 
        (ELSE if_false = statement)?                        # if_stmt
    | WHILE '(' expression ')' statement        			# while_stmt
    | FOR '(' for_init = expression? ';' 
    	for_gate = expression? ';'
    	for_it =  expression? ')' statement			        # for_stmt
    | RETURN expression? ';'                                # return_stmt
    | BREAK ';' 										 	# break_stmt
    | CONTINUE ';' 											# continue_stmt
    | expression ';'                                        # expr_stmt
    | ';'                                                   # empty_stmt
    ;

expression
    : expression op = ('++' | '--') 						# suffix_expr
	| NEW basic_type (bracket_expr)+ 	                    # new_expr
	| NEW basic_type ('('')')? 								# new_expr
	| expression '(' (expression (',' expression)* )? ')' 	# fun_call_expr
	| expression'.'IDENTIFIER								# member_expr
	| expression L_BRACKET expression R_BRACKET 			# array_expr
	| '(' expression ')'									# par_expr
	| <assoc=right> op = ('!' | '+' | '-' | '~') expression # prefix_expr
	| <assoc=right> op = ('++' | '--') expression 			# prefix_expr
	| expression op = ('*' | '/' | '%') expression 			# binary_expr
	| expression op = ('+' | '-') expression 				# binary_expr
	| expression op = ('<<' | '>>') expression 				# binary_expr
	| expression op = ('>' | '<' | '>=' | '<=') expression 	# binary_expr
	| expression op = ('==' | '!=' ) expression 			# binary_expr
	| expression op = '&' expression 						# binary_expr
	| expression op = '^' expression 						# binary_expr
	| expression op = '|' expression 						# binary_expr
	| expression op = '&&' expression 						# binary_expr
	| expression op = '||' expression 						# binary_expr
	| <assoc=right> expression '=' expression 				# assign_expr
	| THIS 													# this_expr
    | IDENTIFIER											# id_expr
    | constant                                              # const_expr
    ;

constant
    : INTEGER
    | STRING_CONSTANT
    | NULL
    | TRUE
    | FALSE
    ;
