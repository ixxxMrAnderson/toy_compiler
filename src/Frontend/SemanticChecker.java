package Frontend;

import AST.*;
import Util.Scope;
import Util.Type;
import Util.error.semanticError;
import Util.Type.type;
import Util.position;

import java.util.ArrayList;
import java.util.HashSet;

public class SemanticChecker implements ASTVisitor {
    public Scope currentScope;
    public Type current_return = null;
    public boolean return_flag = false;
    public boolean loop_flag = false;
    public String current_class = null;
    public HashSet<classDefNode> defined_class;
    public HashSet<String> defined_class_name;
    public HashSet<funDefNode> defined_function;
    public HashSet<String> defined_function_name;
    public boolean main_called;

    public boolean checkFunctionDefined(String name, position pos){
        if (current_class != null){
            classDefNode node_ = getClass(current_class, pos);
            if (node_.containFunction(name)) return true;
        }
        if (defined_function_name.contains(name)) return true;
        else return false;
    }

    public funDefNode getFunction(String name, position pos){
        if (current_class != null){
            classDefNode node_ = getClass(current_class, pos);
            if (node_.containFunction(name))
//                System.out.println("get_many_in_get");
                return node_.getFunction(name, pos);
        }
        if (!defined_function_name.contains(name)) throw new semanticError("Semantic Error: function_undefine", pos);
        for (funDefNode fun : defined_function) {
            if (fun.name.equals(name)){
//                System.out.println("getting_many: "+fun.type.type.dimension);
                return new funDefNode(fun);
            }
        }
        return null;
    }

    public classDefNode getClass(String name, position pos){
        if (!defined_class_name.contains(name)) throw new semanticError("Semantic Error: class_undefine", pos);
        for (classDefNode class_ : defined_class){
            if (class_.id.equals(name)){
                return new classDefNode(class_);
            }
        }
        return null;
    }

    @Override
    public programNode visit(programNode it) {
        main_called = false;
        currentScope = new Scope(null);
        this.defined_class = it.defined_class;
        this.defined_class_name = it.defined_class_name;
        this.defined_function = it.defined_function;
        this.defined_function_name = it.defined_function_name;
        ArrayList<SectionNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.sections.size(); ++i)
            tmp.add((SectionNode) it.sections.get(i).accept(this));
        it.sections = tmp;
        if (main_called == false) throw new semanticError("Semantic Error: main_sema", it.pos);
        return it;
    }

    @Override
    public classDefNode visit(classDefNode it) {
        currentScope = new Scope(currentScope);
        current_class = it.id;
        if (it.members != null) {
            ArrayList<varDefNode> tmp = new ArrayList<>();
            for (int i = 0; i < it.members.size(); ++i)
                tmp.add(it.members.get(i).accept(this));
            it.members = tmp;
        }
        if (it.functions != null) {
            ArrayList<funDefNode> tmp = new ArrayList<>();
            for (int i = 0; i < it.functions.size(); ++i)
                tmp.add(it.functions.get(i).accept(this));
            it.functions = tmp;
        }
        if (it.constructor != null) {
            ArrayList<classConstructorNode> tmp = new ArrayList<>();
            for (int i = 0; i < it.constructor.size(); ++i)
                tmp.add(it.constructor.get(i).accept(this));
            it.constructor = tmp;
        }
        currentScope = currentScope.parentScope();
        current_class = null;
        return it;
    }

    @Override
    public classConstructorNode visit(classConstructorNode it) {
        current_return = new Type();
        current_return.type = type.CLASS;
        current_return.class_id = "*" + it.name;
        if (!it.name.equals(current_class)) throw new semanticError("Semantic Error: constructor_sema", it.pos);
        for (int i = 0; i < it.parameters_type.size(); ++i){
            currentScope.defineVariable(it.parameters.get(i).id, it.parameters_type.get(i).type, it.pos);
        }
        it.suite = it.suite.accept(this);
        current_return = null;
        return it;
    }

    @Override
    public funDefNode visit(funDefNode it) {
        current_return = it.type.type;
        return_flag = false;
//        System.out.println(it.type.type.dimension + "!___" + it.name);
        if (defined_class_name.contains(it.name))
            throw new semanticError("Semantic Error: fun_def_sema", it.pos);
        if (it.name.equals("main")) {
            if (it.type.type.type != type.INT || it.parameters_type.size() > 0)
                throw new semanticError("Semantic Error: fun_def_sema1" + it.type.type.type, it.pos);
            main_called = true;
        }
        currentScope = new Scope(currentScope);
        for (int i = 0; i < it.parameters_type.size(); ++i){
            currentScope.defineVariable(it.parameters.get(i).id, it.parameters_type.get(i).type, it.pos);
        }
        it.suite = it.suite.accept(this);
        currentScope = currentScope.parentScope();
        current_return = null;
        if (return_flag == false && it.type.type.type != type.VOID && !it.name.equals("main"))
            throw new semanticError("Semantic Error: fun_def_sema_return" + it.type.type.type, it.pos);
        return it;
    }

    @Override
    public varDefNode visit(varDefNode it) {
        ArrayList<singleVarDefNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.variables.size(); ++i) {
            singleVarDefNode tmp_node = it.variables.get(i).accept(this);
            tmp.add(tmp_node);
            currentScope.defineVariable(tmp_node.id, it.type.type, it.pos);
//            System.out.println("define var: " + tmp_node.id);
        }
        it.variables = tmp;
        return it;
    }

    @Override
    public singleVarDefNode visit(singleVarDefNode it) {
        if (it.expr != null) {
            it.expr = (ExprNode) it.expr.accept(this);
            if (it.expr.expr_type.type == type.VOID || defined_class_name.contains(it.id) || checkFunctionDefined(it.id, it.pos))
                throw new semanticError("Semantic Error: single_var_def_sema", it.expr.pos);
        }
        return it;
    }

    @Override
    public typeNode visit(typeNode it) {
        if (it.type.type == type.CLASS && !defined_class_name.contains(it.type.class_id))
            throw new semanticError("Semantic Error: type_sema: ", it.pos);
        return it;
    }

    @Override
    public blockStmtNode visit(blockStmtNode it) {
        currentScope = new Scope(currentScope);
        ArrayList<StmtNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.stmts.size(); ++i) {
            StmtNode tmp_node = (StmtNode) it.stmts.get(i).accept(this);
            tmp.add(tmp_node);
            if (tmp_node instanceof returnStmtNode) it.stmt_type = tmp_node.stmt_type;
        }
        it.stmts = tmp;
        currentScope = currentScope.parentScope();
        return it;
    }

    @Override
    public varDefStmtNode visit(varDefStmtNode it) {
        if (it.type.type.type == type.VOID)
            throw new semanticError("Semantic Error: var_def_sema", it.pos);
        ArrayList<singleVarDefNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.variables.size(); ++i) {
            singleVarDefNode tmp_node = it.variables.get(i).accept(this);
            tmp.add(tmp_node);
            currentScope.defineVariable(tmp_node.id, it.type.type, it.pos);
//            System.out.println("define var: " + tmp_node.id);
        }
        it.variables = tmp;
        return it;
    }

    @Override
    public returnStmtNode visit(returnStmtNode it) {
        return_flag = true;
        if (current_return == null)
            throw new semanticError("Semantic Error: return_sema1", it.pos);
        if (it.value != null) {
            if (current_return.type == type.VOID)
                throw new semanticError("Semantic Error: return_sema2", it.pos);
            it.value.accept(this);
            if (!current_return.cmp(it.value.expr_type) && it.value.expr_type.type != type.NULL) {
//                System.out.println(it.value.expr_type.dimension + "_!__" + current_return.dimension);
                throw new semanticError("Semantic Error: return_sema3" + current_return.type, it.pos);
            }
        } else {
            if (current_return.type != type.VOID && !(current_return.type == type.CLASS && current_return.class_id.substring(0, 1).equals( "*"))) {
//                System.out.println(current_return.class_id.substring(0, 1));
                throw new semanticError("Semantic Error: return_sema4", it.pos);
            }
        }
        return it;
    }

    @Override
    public whileStmtNode visit(whileStmtNode it) {
        boolean last_loop = loop_flag;
        loop_flag = true;
        it.condition = (ExprNode) it.condition.accept(this);
        if (it.condition.expr_type.type != type.BOOL)
            throw new semanticError("Semantic Error: while_sema", it.condition.pos);
        currentScope = new Scope(currentScope);
        if (it.body != null) it.body = (StmtNode) it.body.accept(this);
        currentScope = currentScope.parentScope();
        loop_flag = last_loop;
        return it;
    }

    @Override
    public forStmtNode visit(forStmtNode it) {
        boolean last_loop = loop_flag;
        loop_flag = true;
//        System.out.println("init");
        if (it.init != null) it.init = (ExprNode) it.init.accept(this);
//        System.out.println("cond");
        if (it.condition != null) {
            it.condition = (ExprNode) it.condition.accept(this);
            if (it.condition.expr_type.type != type.BOOL) {
                throw new semanticError("Semantic Error: for_sema", it.condition.pos);
            }
        }
//        System.out.println("step");
        if (it.step != null) it.step = (ExprNode) it.step.accept(this);
//        System.out.println("body");
        currentScope = new Scope(currentScope);
        if (it.body != null) it.body = (StmtNode) it.body.accept(this);
        currentScope = currentScope.parentScope();
        loop_flag = last_loop;
        return it;
    }

    @Override
    public breakStmtNode visit(breakStmtNode it) {
        if (!loop_flag)
            throw new semanticError("Semantic Error: break_sema", it.pos);
        return it;
    }

    @Override
    public continueStmtNode visit(continueStmtNode it) {
        if (!loop_flag)
            throw new semanticError("Semantic Error: break_sema", it.pos);
        return it;
    }

    @Override
    public exprStmtNode visit(exprStmtNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        return it;
    }

    @Override
    public ifStmtNode visit(ifStmtNode it) {
        it.condition = (ExprNode) it.condition.accept(this);
        if (it.condition.expr_type.type != type.BOOL) {
//            System.out.println(it.condition.expr_type.type);
            throw new semanticError("Semantic Error: if_sema", it.condition.pos);
        }
        currentScope = new Scope(currentScope);
        if (it.then_stmt != null) it.then_stmt = (StmtNode) it.then_stmt.accept(this);
        currentScope = currentScope.parentScope();
        currentScope = new Scope(currentScope);
        if (it.else_stmt != null) it.else_stmt = (StmtNode) it.else_stmt.accept(this);
        currentScope = currentScope.parentScope();
        return it;
    }

    @Override
    public suffixExprNode visit(suffixExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        if (!it.expr.isAssignable)
            throw new semanticError("Semantic Error: suffix_sema1", it.expr.pos);
        if (it.expr.expr_type.type != type.INT)
            throw new semanticError("Semantic Error: suffix_sema2", it.expr.pos);
        it.expr_type.type = type.INT;
        it.isAssignable = false;
        return it;
    }

    @Override
    public prefixExprNode visit(prefixExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        switch(it.op) {
            case NEGATIVE:
            case POSITIVE:
            case BITWISE_NOT:
                if (it.expr.expr_type.type != type.INT)
                    throw new semanticError("Semantic Error: prefix_sema1", it.expr.pos);
                it.expr_type.type = type.INT;
                break;
            case DECREASE: case INCREASE:
//                if (!it.expr.isAssignable)
//                    throw new semanticError("Semantic Error: prefix_sema2", it.expr.pos);
                if (it.expr.expr_type.type != type.INT)
                    throw new semanticError("Semantic Error: prefix_sema3", it.expr.pos);
                it.expr_type.type = type.INT;
                break;
            case NOT:
                if (it.expr.expr_type.type != type.BOOL)
                    throw new semanticError("Semantic Error: prefix_sema", it.expr.pos);
                it.expr_type.type = type.BOOL;
                break;
        }
        it.isAssignable = true;
//        System.out.println("pre_"+it.expr_type.type);
        return it;
    }

    @Override
    public thisExprNode visit(thisExprNode it) {
        it.expr_type.type = type.CLASS;
        it.expr_type.class_id = current_class;
        return it;
    }

    @Override
    public newExprNode visit(newExprNode it) {
        ArrayList<ExprNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.size.size(); ++i) {
            ExprNode tmp_node = (ExprNode) it.size.get(i).accept(this);
            tmp.add(tmp_node);
            if (tmp_node.expr_type.type != type.INT || tmp_node.expr_type.dimension > 0)
                throw new semanticError("Semantic Error: new_sema_1", it.pos);
        }
        it.size = tmp;
        it.type = it.type.accept(this);
        if (it.type.type.type == type.VOID)
            throw new semanticError("Semantic Error: new_sema_2", it.pos);
        if (it.type.type.type == type.CLASS && !defined_class_name.contains(it.type.type.class_id))
            throw new semanticError("Semantic Error: new_sema_3", it.pos);
        it.expr_type = new Type(it.type.type);
//        it.expr_type.dimension = it.size.size();
//        System.out.println("new: " + it.expr_type.class_id);
        return it;
    }

    @Override
    public assignExprNode visit(assignExprNode it) {
//        System.out.println("in_assign: " + it.rhs.expr_type.type);
        it.lhs = (ExprNode) it.lhs.accept(this);
        if (!it.lhs.isAssignable)
            throw new semanticError("Semantic Error: assign_sema1", it.lhs.pos);
//        System.out.println("!!l: " + it.lhs.expr_type.type);
        it.rhs = (ExprNode) it.rhs.accept(this);
//        System.out.println("!!r: " + it.rhs.expr_type.type);
        if (!it.lhs.expr_type.cmp(it.rhs.expr_type) && !(it.rhs.expr_type.type == type.NULL && (it.lhs.expr_type.type == type.CLASS || it.lhs.expr_type.dimension > 0)))
            throw new semanticError("Semantic Error: assign_sema2", it.pos);
        return it;
    }

    @Override
    public binaryExprNode visit(binaryExprNode it) {
//        System.out.println("lhs");
        it.lhs = (ExprNode) it.lhs.accept(this);
//        System.out.println("rhs");
        it.rhs = (ExprNode) it.rhs.accept(this);
//        System.out.println("rhs_over" + it.op);
        switch (it.op) {
            case MUL: case DIV: case MOD: case SUB: case SLA: case SRA: case BITWISE_AND: case BITWISE_XOR: case BITWISE_OR:
                if (it.lhs.expr_type.type != type.INT)
                    throw new semanticError("Semantic Error: binary_sema", it.lhs.pos);
                if (it.rhs.expr_type.type != type.INT)
                    throw new semanticError("Semantic Error: binary_sema", it.rhs.pos);
                it.expr_type.type = type.INT;
                break;
            case ADD:
                if (it.lhs.expr_type.type == type.INT && it.rhs.expr_type.type == type.INT)
                    it.expr_type.type = type.INT;
                else if (it.lhs.expr_type.type == type.STRING & it.rhs.expr_type.type == type.STRING)
                    it.expr_type.type = type.STRING;
                else throw new semanticError("Semantic Error: binary_sema", it.pos);
                break;
            case GTH: case GEQ: case LTH: case LEQ:
                if (it.lhs.expr_type.type == type.INT && it.rhs.expr_type.type == type.INT)
                    it.expr_type.type = type.BOOL;
                else if (it.lhs.expr_type.type == type.STRING & it.rhs.expr_type.type == type.STRING)
                    it.expr_type.type = type.BOOL;
                else throw new semanticError("Semantic Error: binary_sema", it.pos);
                break;
            case NEQ: case EQ:
                if (it.lhs.expr_type.cmp(it.rhs.expr_type) || it.lhs.expr_type.type == type.NULL || it.rhs.expr_type.type == type.NULL)
                    it.expr_type.type = type.BOOL;
                else throw new semanticError("Semantic Error: binary_sema", it.pos);
                break;
            case AND: case OR:
                if (it.lhs.expr_type.type != type.BOOL)
                    throw new semanticError("Semantic Error: binary_sema", it.lhs.pos);
                if (it.rhs.expr_type.type != type.BOOL)
                    throw new semanticError("Semantic Error: binary_sema", it.rhs.pos);
                it.expr_type.type = type.BOOL;
                break;
        }
        return it;
    }

    @Override
    public constExprNode visit(constExprNode it) {
        return it;
    }

    @Override
    public varExprNode visit(varExprNode it) {
        Type type_ = currentScope.getType(it.id, true);
        if (type_ != null) {
            it.isAssignable = true;
            it.expr_type = new Type(type_);
            if (it.expr_type.type == type.VOID || defined_class_name.contains(it.id))
                throw new semanticError("Semantic Error: var_expr_sema1: " + it.id, it.pos);
        } else if (!checkFunctionDefined(it.id, it.pos)) {
            throw new semanticError("Semantic Error: var_expr_sema2: " + it.id, it.pos);
        }
        return it;
    }

    @Override
    public funCallExprNode visit(funCallExprNode it) {
        String function_id;
        funDefNode node = new funDefNode(it.pos);
        if (it.function_id instanceof varExprNode) {
            function_id = ((varExprNode) it.function_id).id;
            node = getFunction(function_id, it.pos);
            if (!checkFunctionDefined(function_id, it.pos))
                throw new semanticError("Semantic Error: fun_call_sema1", it.pos);
        } else if (it.function_id instanceof memberExprNode){
            ExprNode expr_ = ((memberExprNode) it.function_id).expr;
            String member = ((memberExprNode) it.function_id).member;
            expr_ = (ExprNode) expr_.accept(this);
            if (expr_.expr_type.type == type.CLASS && defined_class_name.contains(expr_.expr_type.class_id)) {
                classDefNode node_ = getClass(expr_.expr_type.class_id, it.pos);
                if (node_.containFunction(member)) {
                    node = node_.getFunction(member, it.pos);
                } else if (expr_.expr_type.dimension > 0) {
                    classDefNode node__ = getClass("*ARRAY", it.pos);
                    node = node__.getFunction(member, it.pos);
                } else {
                    throw new semanticError("Semantic Error: fun_call_sema", it.pos);
                }
            } else if (expr_.expr_type.dimension > 0) {
                classDefNode node_ = getClass("*ARRAY", it.pos);
                node = node_.getFunction(member, it.pos);
            } else if (expr_.expr_type.type == type.STRING) {
                classDefNode node_ = getClass("*STRING", it.pos);
                node = node_.getFunction(member, it.pos);
            } else if (expr_ instanceof thisExprNode) {
                classDefNode node_ = getClass(current_class, it.pos);
                node = node_.getFunction(member, it.pos);
            }
        } else
            throw new semanticError("Semantic Error: fun_call_sema0", it.pos);
        int def_ = 0;
        if (node.parameters_type != null)  def_ = node.parameters_type.size();
        int call_ = it.parameters.size();
        if (def_ == call_) {
            ArrayList<ExprNode> tmp = new ArrayList<>();
            for (int i = 0; i < def_; ++i){
                ExprNode tmp_node = (ExprNode) it.parameters.get(i).accept(this);
                tmp.add(tmp_node);
                if (!node.parameters_type.get(i).type.cmp(tmp_node.expr_type) && tmp_node.expr_type.type != type.NULL)
                    throw new semanticError("Semantic Error: fun_call_sema2", it.pos);
            }
            it.expr_type = new Type(node.type.type);
        } else
            throw new semanticError("Semantic Error: fun_call_sema3", it.pos);
        it.expr_type = new Type(node.type.type);
        return it;
    }

    @Override
    public memberExprNode visit(memberExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        if (it.expr.expr_type.type == type.CLASS && defined_class_name.contains(it.expr.expr_type.class_id)) {
            classDefNode node = getClass(it.expr.expr_type.class_id, it.pos);
            if (node.containMember(it.member)){
                it.isAssignable = true;
                it.expr_type = new Type(node.getMemberType(it.member).type);
            } else if (node.containFunction(it.member)) {
                it.expr_type = new Type(node.getFunction(it.member, it.pos).type.type);
            } else {
                throw new semanticError("Semantic Error: member_sema1", it.pos);
            }
        } else if (it.expr.expr_type.dimension > 0) {
            classDefNode node = getClass("*ARRAY", it.pos);
            it.expr_type = new Type(node.getFunction(it.member, it.pos).type.type);
        } else if (it.expr.expr_type.type == type.STRING) {
            classDefNode node = getClass("*STRING", it.pos);
            it.expr_type = new Type(node.getFunction(it.member, it.pos).type.type);
        } else if (it.expr instanceof thisExprNode) {
            classDefNode node = getClass(current_class, it.pos);
            it.expr_type = new Type(node.getMemberType(it.member).type);
        } else {
            throw new semanticError("Semantic Error: member_sema2", it.pos);
        }
        return it;
    }

    @Override
    public arrayExprNode visit(arrayExprNode it) {
        it.array = (ExprNode) it.array.accept(this);
        it.index = (ExprNode) it.index.accept(this);
        if (it.array.expr_type.dimension == 0)
            throw new semanticError("Semantic Error: array_expr_sema1", it.pos);
        if (it.index.expr_type.type != type.INT || it.index.expr_type.dimension > 0)
            throw new semanticError("Semantic Error: array_expr_sema2", it.pos);
        it.isAssignable = true;
        it.expr_type = new Type(it.array.expr_type);
        it.expr_type.dimension -= 1;
//        System.out.println("out_array_expr: " + it.expr_type.type + it.expr_type.dimension);
        return it;
    }
}
