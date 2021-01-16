package Parser;// Generated from Mx.g4 by ANTLR 4.9
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#section}.
	 * @param ctx the parse tree
	 */
	void enterSection(MxParser.SectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#section}.
	 * @param ctx the parse tree
	 */
	void exitSection(MxParser.SectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#class_definition}.
	 * @param ctx the parse tree
	 */
	void enterClass_definition(MxParser.Class_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#class_definition}.
	 * @param ctx the parse tree
	 */
	void exitClass_definition(MxParser.Class_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void enterFunction_definition(MxParser.Function_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void exitFunction_definition(MxParser.Function_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variable_definition}.
	 * @param ctx the parse tree
	 */
	void enterVariable_definition(MxParser.Variable_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variable_definition}.
	 * @param ctx the parse tree
	 */
	void exitVariable_definition(MxParser.Variable_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#basic_type}.
	 * @param ctx the parse tree
	 */
	void enterBasic_type(MxParser.Basic_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#basic_type}.
	 * @param ctx the parse tree
	 */
	void exitBasic_type(MxParser.Basic_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#array_type}.
	 * @param ctx the parse tree
	 */
	void enterArray_type(MxParser.Array_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#array_type}.
	 * @param ctx the parse tree
	 */
	void exitArray_type(MxParser.Array_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#function_parameter_list}.
	 * @param ctx the parse tree
	 */
	void enterFunction_parameter_list(MxParser.Function_parameter_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#function_parameter_list}.
	 * @param ctx the parse tree
	 */
	void exitFunction_parameter_list(MxParser.Function_parameter_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#single_var_def}.
	 * @param ctx the parse tree
	 */
	void enterSingle_var_def(MxParser.Single_var_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#single_var_def}.
	 * @param ctx the parse tree
	 */
	void exitSingle_var_def(MxParser.Single_var_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#class_constructor}.
	 * @param ctx the parse tree
	 */
	void enterClass_constructor(MxParser.Class_constructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#class_constructor}.
	 * @param ctx the parse tree
	 */
	void exitClass_constructor(MxParser.Class_constructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#suite}.
	 * @param ctx the parse tree
	 */
	void enterSuite(MxParser.SuiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#suite}.
	 * @param ctx the parse tree
	 */
	void exitSuite(MxParser.SuiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#bracket_expr}.
	 * @param ctx the parse tree
	 */
	void enterBracket_expr(MxParser.Bracket_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#bracket_expr}.
	 * @param ctx the parse tree
	 */
	void exitBracket_expr(MxParser.Bracket_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#blank_bracket}.
	 * @param ctx the parse tree
	 */
	void enterBlank_bracket(MxParser.Blank_bracketContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#blank_bracket}.
	 * @param ctx the parse tree
	 */
	void exitBlank_bracket(MxParser.Blank_bracketContext ctx);
	/**
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code var_def_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVar_def_stmt(MxParser.Var_def_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code var_def_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVar_def_stmt(MxParser.Var_def_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(MxParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(MxParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(MxParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(MxParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code for_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_stmt(MxParser.For_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code for_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_stmt(MxParser.For_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(MxParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code return_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(MxParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code break_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(MxParser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code break_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(MxParser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continue_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue_stmt(MxParser.Continue_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continue_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue_stmt(MxParser.Continue_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expr_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(MxParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expr_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(MxParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code empty_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEmpty_stmt(MxParser.Empty_stmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code empty_stmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEmpty_stmt(MxParser.Empty_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code par_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPar_expr(MxParser.Par_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code par_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPar_expr(MxParser.Par_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code member_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMember_expr(MxParser.Member_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code member_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMember_expr(MxParser.Member_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binary_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinary_expr(MxParser.Binary_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binary_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinary_expr(MxParser.Binary_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code this_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterThis_expr(MxParser.This_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code this_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitThis_expr(MxParser.This_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterId_expr(MxParser.Id_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitId_expr(MxParser.Id_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffix_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSuffix_expr(MxParser.Suffix_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffix_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSuffix_expr(MxParser.Suffix_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code new_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNew_expr(MxParser.New_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code new_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNew_expr(MxParser.New_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssign_expr(MxParser.Assign_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssign_expr(MxParser.Assign_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefix_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefix_expr(MxParser.Prefix_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefix_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefix_expr(MxParser.Prefix_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code const_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConst_expr(MxParser.Const_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code const_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConst_expr(MxParser.Const_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fun_call_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFun_call_expr(MxParser.Fun_call_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fun_call_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFun_call_expr(MxParser.Fun_call_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code array_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArray_expr(MxParser.Array_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code array_expr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArray_expr(MxParser.Array_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(MxParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(MxParser.ConstantContext ctx);
}