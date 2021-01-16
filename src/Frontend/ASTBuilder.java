package Frontend;

import AST.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.Type;
import Util.error.semanticError;
import Util.position;
import org.antlr.v4.runtime.ParserRuleContext;
import AST.binaryExprNode.Op;
import Util.Type.type;
import java.util.ArrayList;


public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    public programNode program_node;

    @Override public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        this.program_node = new programNode(new position(ctx));
        ArrayList<typeNode> tmp_1 = new ArrayList<>();
        classDefNode array_class = new classDefNode(new position(ctx), "*ARRAY");
        array_class.defineFunction(new position(ctx), "size", type.INT, tmp_1);
        classDefNode string_class = new classDefNode(new position(ctx), "*STRING");
        string_class.defineFunction(new position(ctx), "length", type.INT, tmp_1);
        string_class.defineFunction(new position(ctx), "parseInt", type.INT, tmp_1);
        this.program_node.defineFunction(new position(ctx), "main", type.INT, tmp_1);
        this.program_node.defineFunction(new position(ctx), "getInt", type.INT, tmp_1);
        this.program_node.defineFunction(new position(ctx), "getString", type.STRING, tmp_1);
        tmp_1.add(new typeNode(new position(ctx), new Type(type.STRING)));
        this.program_node.defineFunction(new position(ctx), "print", type.VOID, tmp_1);
        this.program_node.defineFunction(new position(ctx), "println", type.VOID, tmp_1);
        ArrayList<typeNode> tmp_2 = new ArrayList<>();
        tmp_2.add(new typeNode(new position(ctx), new Type(type.INT)));
        string_class.defineFunction(new position(ctx), "ord", type.INT, tmp_2);
        this.program_node.defineFunction(new position(ctx), "toString", type.STRING, tmp_2);
        this.program_node.defineFunction(new position(ctx), "printInt", type.VOID, tmp_2);
        this.program_node.defineFunction(new position(ctx), "printlnInt", type.VOID, tmp_2);
        tmp_2.add(new typeNode(new position(ctx), new Type(type.INT)));
        string_class.defineFunction(new position(ctx), "substring", type.STRING, tmp_2);
        this.program_node.defined_class_name.add("*STRING");
        this.program_node.defined_class.add(string_class);
        this.program_node.defined_class_name.add("*ARRAY");
        this.program_node.defined_class.add(array_class);
        if (ctx.section() != null) {
            for (ParserRuleContext section : ctx.section())
                program_node.sections.add((SectionNode) visit(section));
        }
        return this.program_node;
    }

    @Override public ASTNode visitSection(MxParser.SectionContext ctx) {
        if (ctx.variable_definition() != null) return visit(ctx.variable_definition());
        if (ctx.function_definition() != null) return visit(ctx.function_definition());
        if (ctx.class_definition() != null) return visit(ctx.class_definition());
        throw new semanticError("Semantic Error: section", new position(ctx));
    }

    @Override public ASTNode visitClass_definition(MxParser.Class_definitionContext ctx) {
        classDefNode node = new classDefNode(new position(ctx), ctx.IDENTIFIER().toString());
        if (ctx.variable_definition() != null){
            for (MxParser.Variable_definitionContext member : ctx.variable_definition()) {
                node.members.add((varDefNode) visit(member));
            }
        }
        if (ctx.function_definition() != null){
            for (MxParser.Function_definitionContext fun_def : ctx.function_definition())
                node.functions.add((funDefNode) visitFunction_definition(fun_def, true));
        }
        if (ctx.class_constructor() != null){
            for (MxParser.Class_constructorContext cons : ctx.class_constructor())
                node.constructor.add((classConstructorNode) visit(cons));
        }
        this.program_node.defined_class.add(node);
        if (this.program_node.defined_class_name.contains(node.id) || this.program_node.defined_function_name.contains(node.id))
            throw new semanticError("Semantic Error: class_def", node.pos);
        this.program_node.defined_class_name.add(node.id);
        return node;
    }

    public ASTNode visitFunction_definition(MxParser.Function_definitionContext ctx, boolean isMember) {
        if (ctx.type() == null)
            throw new semanticError("Semantic Error: fun_def", new position(ctx));
        funDefNode node = new funDefNode(new position(ctx), ctx.IDENTIFIER().toString(), (blockStmtNode) visit(ctx.suite()), (typeNode) visit(ctx.type()));
        if (ctx.function_parameter_list().single_var_def() != null){
            for (MxParser.Single_var_defContext single_var_def : ctx.function_parameter_list().single_var_def())
                node.parameters.add((singleVarDefNode) visit(single_var_def));
        }
        if (ctx.function_parameter_list().type() != null){
            for (MxParser.TypeContext type : ctx.function_parameter_list().type())
                node.parameters_type.add((typeNode) visit(type));
        }
//        System.out.println("building"+ node.name +"___" + node.type.type.dimension);
//        System.out.println(System.identityHashCode(node.type.type.dimension));
        return node;
    }

    @Override public ASTNode visitFunction_definition(MxParser.Function_definitionContext ctx) {
        if (ctx.type() == null)
            throw new semanticError("Semantic Error: fun_def", new position(ctx));
        funDefNode node = new funDefNode(new position(ctx), ctx.IDENTIFIER().toString(), (blockStmtNode) visit(ctx.suite()), (typeNode) visit(ctx.type()));
        if (ctx.function_parameter_list().single_var_def() != null){
            for (MxParser.Single_var_defContext single_var_def : ctx.function_parameter_list().single_var_def())
                node.parameters.add((singleVarDefNode) visit(single_var_def));
        }
        if (ctx.function_parameter_list().type() != null){
            for (MxParser.TypeContext type : ctx.function_parameter_list().type())
                node.parameters_type.add((typeNode) visit(type));
        }
        this.program_node.defined_function_name.add(node.name);
        this.program_node.defined_function.add(node);
        return node;
    }

    @Override public ASTNode visitVariable_definition(MxParser.Variable_definitionContext ctx) {
        varDefNode node = new varDefNode(new position(ctx), (typeNode) visit(ctx.type()));
        if (ctx.single_var_def() != null){
            for (MxParser.Single_var_defContext single_var_def : ctx.single_var_def())
                node.variables.add((singleVarDefNode) visit(single_var_def));
        }
        return node;
    }

    @Override public ASTNode visitType(MxParser.TypeContext ctx) {
        if (ctx.basic_type() != null) return visit(ctx.basic_type());
        if (ctx.array_type() != null) return visit(ctx.array_type());
        throw new semanticError("Semantic Error: type", new position(ctx));
    }

    @Override public ASTNode visitBasic_type(MxParser.Basic_typeContext ctx) {
        Type type_ = new Type();
        if (ctx.INT() != null) type_.type = type.INT;
        else if (ctx.BOOL() != null) type_.type = type.BOOL;
        else if (ctx.STRING() != null) type_.type = type.STRING;
        else if (ctx.VOID() != null) type_.type = type.VOID;
        else if (ctx.IDENTIFIER() != null) {
            type_.type = type.CLASS;
            type_.class_id = ctx.IDENTIFIER().toString();
//            System.out.println("!!" + type_.class_id);
        }
        else
            throw new semanticError("Semantic Error: basic_type", new position(ctx));
        return new typeNode(new position(ctx), type_);
    }

    @Override public ASTNode visitArray_type(MxParser.Array_typeContext ctx) {
        typeNode node = (typeNode) visit(ctx.basic_type());
        if (node.type.type == type.VOID)
            throw new semanticError("Semantic Error: array_type", new position(ctx));
        if (ctx.blank_bracket() != null){
            node.type.dimension = ctx.blank_bracket().size();
//            System.out.println(node.type.dimension + "___" + node.type.class_id);
        }
        return node;
    }

    @Override public ASTNode visitSingle_var_def(MxParser.Single_var_defContext ctx) {
        if (ctx.expression() == null)
            return new singleVarDefNode(new position(ctx), ctx.IDENTIFIER().toString(), null);
        else
            return new singleVarDefNode(new position(ctx), ctx.IDENTIFIER().toString(), (ExprNode) visit(ctx.expression()));
    }

    @Override public ASTNode visitClass_constructor(MxParser.Class_constructorContext ctx) {
        classConstructorNode node = new classConstructorNode(new position(ctx), ctx.IDENTIFIER().toString(), (blockStmtNode) visit(ctx.suite()));
        if (ctx.function_parameter_list().single_var_def() != null){
            for (MxParser.Single_var_defContext single_var_def : ctx.function_parameter_list().single_var_def())
                node.parameters.add((singleVarDefNode) visit(single_var_def));
        }
        if (ctx.function_parameter_list().type() != null){
            for (MxParser.TypeContext type : ctx.function_parameter_list().type())
                node.parameters_type.add((typeNode) visit(type));
        }
        return node;
    }

    @Override public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        blockStmtNode node = new blockStmtNode(new position(ctx));
        if (!ctx.statement().isEmpty()) {
            for (ParserRuleContext stmt : ctx.statement()) {
                StmtNode tmp = (StmtNode)visit(stmt);
                if (tmp != null) node.stmts.add(tmp);
            }
        }
        return node;
    }

    @Override public ASTNode visitBlock(MxParser.BlockContext ctx) {
        return visit(ctx.suite());
    }
    @Override public ASTNode visitVar_def_stmt(MxParser.Var_def_stmtContext ctx) {
        varDefNode tmp = (varDefNode) visit(ctx.variable_definition());
        varDefStmtNode node = new varDefStmtNode(new position(ctx), tmp.type);
        if (tmp.variables != null){
            for (singleVarDefNode var : tmp.variables)
                node.variables.add(var);
        }
        return node;
    }

    @Override public ASTNode visitIf_stmt(MxParser.If_stmtContext ctx) {
        if (ctx.statement().size() > 2) throw new semanticError("Semantic Error: if", new position(ctx));
        if (ctx.if_true == null) throw new semanticError("Semantic Error: if", new position(ctx));
        if (ctx.if_false == null)
            return new ifStmtNode((ExprNode) visit(ctx.expression()), (StmtNode) visit(ctx.if_true), null, new position(ctx));
        else
            return new ifStmtNode((ExprNode) visit(ctx.expression()), (StmtNode) visit(ctx.if_true), (StmtNode) visit(ctx.if_false), new position(ctx));
    }

    @Override public ASTNode visitWhile_stmt(MxParser.While_stmtContext ctx) {
        return new whileStmtNode(new position(ctx), (ExprNode) visit(ctx.expression()), (StmtNode) visit(ctx.statement()));
    }

    @Override public ASTNode visitFor_stmt(MxParser.For_stmtContext ctx) {
        ExprNode init = null, gate = null, step = null;
        if (ctx.for_init != null) init = (ExprNode) visit(ctx.for_init);
        if (ctx.for_gate != null) gate = (ExprNode) visit(ctx.for_gate);
        if (ctx.for_it != null) step = (ExprNode) visit(ctx.for_it);
        return new forStmtNode(new position(ctx), init, gate, step, (StmtNode) visit(ctx.statement()));
    }

    @Override public ASTNode visitReturn_stmt(MxParser.Return_stmtContext ctx) {
        ExprNode value = null;
        if (ctx.expression() != null) value = (ExprNode) visit(ctx.expression());
        return new returnStmtNode(value, new position(ctx));
    }

    @Override public ASTNode visitBreak_stmt(MxParser.Break_stmtContext ctx) {
        return new breakStmtNode(new position(ctx));
    }

    @Override public ASTNode visitContinue_stmt(MxParser.Continue_stmtContext ctx) {
        return new continueStmtNode(new position(ctx));
    }

    @Override public ASTNode visitExpr_stmt(MxParser.Expr_stmtContext ctx) {
        return new exprStmtNode((ExprNode) visit(ctx.expression()), new position(ctx));
    }

    @Override public ASTNode visitEmpty_stmt(MxParser.Empty_stmtContext ctx) {
        return null;
    }

    @Override public ASTNode  visitPar_expr(MxParser.Par_exprContext ctx) {
        return visit(ctx.expression());
    }

    @Override public ASTNode visitMember_expr(MxParser.Member_exprContext ctx) {
        return new memberExprNode(new position(ctx), (ExprNode) visit(ctx.expression()), ctx.IDENTIFIER().getText());
    }

    @Override public ASTNode visitBinary_expr(MxParser.Binary_exprContext ctx) {
        if (ctx.expression().size() != 2) throw new semanticError("Semantic Error: binary_expr", new position(ctx));
        ExprNode lhs = (ExprNode) visit(ctx.expression(0)), rhs = (ExprNode) visit(ctx.expression(1));
        binaryExprNode.Op op;
        switch (ctx.op.getText()){
            case "*" : op = Op.MUL; break;
            case "/" : op = Op.DIV; break;
            case "%" : op = Op.MOD; break;
            case "+" : op = Op.ADD; break;
            case "-" : op = Op.SUB; break;
            case "<<": op = Op.SLA; break;
            case ">>": op = Op.SRA; break;
            case "<" : op = Op.LTH; break;
            case ">" : op = Op.GTH; break;
            case "<=": op = Op.LEQ; break;
            case ">=": op = Op.GEQ; break;
            case "==": op = Op.EQ; break;
            case "!=": op = Op.NEQ; break;
            case "&": op = Op.BITWISE_AND; break;
            case "^": op = Op.BITWISE_XOR; break;
            case "|": op = Op.BITWISE_OR; break;
            case "&&": op = Op.AND; break;
            case "||": op = Op.OR; break;
            default: throw new semanticError("Semantic Error: binary_expr", new position(ctx));
        }
        return new binaryExprNode(new position(ctx), op, lhs, rhs);
    }

    @Override public ASTNode visitThis_expr(MxParser.This_exprContext ctx) {
        return new thisExprNode(new position(ctx));
    }

    @Override public ASTNode visitId_expr(MxParser.Id_exprContext ctx) {
        return new varExprNode(ctx.IDENTIFIER().toString(), new position(ctx));
    }

    @Override public ASTNode visitSuffix_expr(MxParser.Suffix_exprContext ctx) {
        suffixExprNode.Op op;
        switch (ctx.op.getText()) {
            case "++": op = suffixExprNode.Op.INCREASE; break;
            case "--": op = suffixExprNode.Op.DECREASE; break;
            default: throw new semanticError("Semantic Error: suffix", new position(ctx));
        }
        return new suffixExprNode(new position(ctx), op, (ExprNode) visit(ctx.expression()));
    }

    @Override public ASTNode visitNew_expr(MxParser.New_exprContext ctx) {
        typeNode type = ((typeNode) visit(ctx.basic_type()));
        newExprNode node = new newExprNode(new position(ctx), type);
        if (ctx.bracket_expr() != null) {
            type.type.dimension = ctx.bracket_expr().size();
            boolean flag = false;
            for (MxParser.Bracket_exprContext expr : ctx.bracket_expr()) {
                if (expr.expression() != null) {
                    if (flag) throw new semanticError("Semantic Error: new", new position(ctx));
                node.size.add(((ExprNode) visit(expr.expression())));
                } else flag = true;
            }
        }
        return node;
    }

    @Override public ASTNode visitAssign_expr(MxParser.Assign_exprContext ctx) {
        if (ctx.expression().size() != 2) throw new semanticError("Semantic Error: assign_expr", new position(ctx));
        ExprNode lhs = (ExprNode) visit(ctx.expression().get(0)), rhs = (ExprNode) visit(ctx.expression().get(1));
        return new assignExprNode(lhs, rhs, new position(ctx));
    }

    @Override public ASTNode visitPrefix_expr(MxParser.Prefix_exprContext ctx) {
        prefixExprNode.Op op;
        switch (ctx.op.getText()) {
            case "++": op = prefixExprNode.Op.INCREASE; break;
            case "--": op = prefixExprNode.Op.DECREASE; break;
            case "+" : op = prefixExprNode.Op.POSITIVE; break;
            case "-" : op = prefixExprNode.Op.NEGATIVE; break;
            case "!" : op = prefixExprNode.Op.NOT; break;
            case "~" : op = prefixExprNode.Op.BITWISE_NOT; break;
            default: throw new semanticError("Semantic Error: prefix", new position(ctx));
        }
        return new prefixExprNode(new position(ctx), op, (ExprNode) visit(ctx.expression()));
    }

    @Override public ASTNode visitConst_expr(MxParser.Const_exprContext ctx) {
        return visit(ctx.constant());
    }

    @Override public ASTNode visitFun_call_expr(MxParser.Fun_call_exprContext ctx) {
        ExprNode name = ((ExprNode) visit(ctx.expression().get(0)));
        funCallExprNode node = new funCallExprNode(new position(ctx), name);
        for (int i = 1; i < ctx.expression().size(); ++i){
            ExprNode tmp = (ExprNode) visit(ctx.expression(i));
            if (tmp != null) node.parameters.add(tmp);
        }
//        System.out.println(((varExprNode) node.function_id).id + ": " + node.parameters.size());
        return node;
    }

    @Override public ASTNode visitArray_expr(MxParser.Array_exprContext ctx) {
        if (ctx.expression().size() != 2) throw new semanticError("Semantic Error: array", new position(ctx));
        ExprNode array = (ExprNode) visit(ctx.expression(0)), index = (ExprNode) visit(ctx.expression(1));
        return new arrayExprNode(new position(ctx), array, index);
    }

    @Override public ASTNode visitConstant(MxParser.ConstantContext ctx) {
        if (ctx.INTEGER() != null)
            return new constExprNode(Integer.parseInt(ctx.INTEGER().toString()), new position(ctx));
        if (ctx.STRING_CONSTANT() != null)
            return new constExprNode(ctx.STRING_CONSTANT().getText(), new position(ctx));
        if (ctx.NULL() != null) return new constExprNode(new position(ctx));
        if (ctx.TRUE() != null) return new constExprNode(true, new position(ctx));
        if (ctx.FALSE() != null) return new constExprNode(false, new position(ctx));
        throw new semanticError("Semantic Error: const", new position(ctx));
    }
}
