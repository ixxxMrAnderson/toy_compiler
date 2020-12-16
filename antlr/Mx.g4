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

INTEGER: [0-9]]+;
IDENTIFIER: ([a-zA-Z]) ([a-zA-Z] | DIGIT | '_')*;
STRING_CONSTANT: '"' (~[\u0000-\u001f\\"] | '\\'  [tbnr\\"])* '"';
BLANK: [ \r\t\n] -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

program: 'int main()' suite EOF;

var_def : type IDENTIFIER ('=' expression)? (',' IDENTIFIER ('=' expression)?)* ';';

type: array_type | basic_type;

basic_type: INT | BOOL | VOID | STRING | IDENTIFIER;
array_type: basic_type ('['']')+;

suite : '{' statement* '}';

statement
    : suite                                                 # block
    | var_def                                               # var_def_stmt
    | IF '(' expression ')' true_stmt=statement 
        (ELSE false_stmt=statement)?                        # if_stmt
    | WHILE '(' expression ')' statement;					# while_stmt
    | FOR '(' init = expression? ';' 
    	condition = expression? ';'
    	step =  expression? ')' statement;					# for_stmt
    | RETURN expression? ';'                                # return_stmt
    | BREAK ';' 										 	# break_stmt
    | CONTINUE ';' 											# continue_stmt
    | expression ';'                                        # expr_stmt
    | ';'                                                   # empty_stmt
    ;

expression
    : primary                                               # atom_expr
    | expression op = ('++' | '--') 						# suffix_expr
	| NEW basic_type ('[' expression? ']')+ 				# new_expr
	| NEW basic_type ('('')')? 								# new_expr
	| expression '(' (expression (',' expression)* )? ')' 	# fun_call_expr
	| expression'['expression']' 							# array_expr
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
	| expression '=' expression 							# assign_expr
	| THIS 													# this_expr
	;

primary
    : '(' expression ')'
    | expression'.'IDENTIFIER
    | IDENTIFIER
    | constant
    ;

constant
    : INTEGER
    | STRING_CONSTANT
    | NULL
    | TRUE
    | FALSE
    ;
