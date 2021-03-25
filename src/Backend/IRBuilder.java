package Backend;

import AST.*;
import MIR.*;
import Util.Scope;
import Util.Type;
import Util.Type.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class IRBuilder implements ASTVisitor {
    private block currentBlock, currentRetBlk, curMainBlk;
    private Scope currentScope;
    private String currentClass, currentFun;
    public HashSet<classDefNode> defined_class;
    public HashSet<String> defined_class_name;
    public HashSet<funDefNode> defined_function;
    public HashSet<String> defined_function_name;
    public HashMap<String, block> blocks;
    public HashMap<String, Integer> spillPara;
    public Stack<block> loopDes = new Stack<>();
    public Stack<Integer> loopFlag = new Stack<>(); // 0: while, 1: for

    // todo: all the builtin shit
    // a.size() be like 'int Mx_array_size(int address_of_a)'
    // builtin function for string: 'Mx_string_' + name and take the address of string as the 1st parameter
    // other builtin function: just the same

    public funDefNode getFunction(String name){
        if (currentClass != null){
            classDefNode node_ = getClass(currentClass);
            if (node_.containFunction(name))
                return node_.getFunction(name, null);
        }
        for (funDefNode fun : defined_function) {
            if (fun.name.equals(name)){
                return new funDefNode(fun);
            }
        }
        return null;
    }

    public classDefNode getClass(String name){
//        System.out.println("finding: " + name);
        for (classDefNode class_ : defined_class){
//            System.out.println("has: " + class_.id);
            if (class_.id.equals(name)){
                return new classDefNode(class_);
            }
        }
        return null;
    }

    public IRBuilder(HashMap<String, block> blocks, HashMap<String, Integer> spillPara) {
        this.blocks = blocks;
        this.spillPara = spillPara;
        this.blocks.put("_VAR_DEF", new block());
        this.spillPara.put("_VAR_DEF", 0);
        this.blocks.put("main", new block());
        this.spillPara.put("main", 0);
    }

    @Override
    public programNode visit(programNode it) {
        currentScope = new Scope(null);
        this.defined_class = it.defined_class;
        this.defined_class_name = it.defined_class_name;
        this.defined_function = it.defined_function;
        this.defined_function_name = it.defined_function_name;
        ArrayList<SectionNode> tmp = new ArrayList<>();
        curMainBlk = blocks.get("main");
//        System.out.println(curMainBlk + "_1");
        for (int i = 0; i < it.sections.size(); ++i) {
            if (it.sections.get(i) instanceof varDefNode){
                currentBlock = curMainBlk;
//                System.out.println(curMainBlk + "_2");
                varDefNode v = (varDefNode) it.sections.get(i);
                for (singleVarDefNode single_v : v.variables){
                    blocks.get("_VAR_DEF").push_back(new define(new entity("@" + single_v.id), null));
                }
            }
            tmp.add((SectionNode) it.sections.get(i).accept(this));
            if (it.sections.get(i) instanceof varDefNode) curMainBlk = currentBlock;
//            System.out.println(curMainBlk);
        }
        it.sections = tmp;
        return it;
    }

    @Override
    public classDefNode visit(classDefNode it) {
        currentScope = new Scope(currentScope);
        currentClass = it.id;
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
        currentClass = null;
        return it;
    }

    @Override
    public classConstructorNode visit(classConstructorNode it) {
        currentBlock = new block();
        currentFun = currentClass + "_memberFn_" + it.name;
        blocks.put(currentFun, currentBlock);
        spillPara.put(currentFun, 0);
        currentBlock.push_back(new define(new entity("_THIS"), new entity("_A0")));
        currentScope = new Scope(currentScope);
        for (int i = 0; i < it.parameters_type.size(); ++i){
            currentScope.defineVariable(it.parameters.get(i).id, it.parameters_type.get(i).type, it.pos);
        }
        block retBlk = new block();
        retBlk.push_back(new jump(null));
        currentRetBlk = retBlk;
        it.suite = it.suite.accept(this);
        currentBlock.push_back(new ret(null, currentRetBlk));
        blocks.get(currentClass + "_memberFn_" + it.name).tailBlk = retBlk;
        currentScope = currentScope.parentScope();
        currentFun = null;
        return it;
    }

    @Override
    public funDefNode visit(funDefNode it) {
        currentFun = it.name;
        Integer paraNum = 0;
        if (currentFun.equals("main")){
            currentBlock = curMainBlk;
        } else {
            currentBlock = new block();
            if (currentClass != null) {
                currentFun = currentClass + "_memberFn_" + it.name;
                currentBlock.push_back(new define(new entity("_THIS"), new entity("_A" + paraNum++)));
            }
            blocks.put(currentFun, currentBlock);
            spillPara.put(currentFun, 0);
        }
        currentScope = new Scope(currentScope);
        block retBlk = new block();
        retBlk.push_back(new jump(null));
        currentRetBlk = retBlk;
        entity tmp = new entity();
        for (int i = 0; i < it.parameters_type.size(); ++i){
            if (paraNum <= 6){
                currentBlock.push_back(new define(new entity(it.parameters.get(i).id), new entity("_A" + paraNum)));
            } else {
                currentBlock.push_back(
                    new binary(new entity(tmp), new entity("_S0"), new entity((paraNum - 7) * 4), binaryExprNode.Op.ADD)
                );
                currentBlock.push_back(new load(new entity(tmp), new entity(tmp)));
                currentBlock.push_back(new define(new entity(it.parameters.get(i).id), new entity(tmp)));
            }
            paraNum ++;
            currentScope.defineVariable(it.parameters.get(i).id, it.parameters_type.get(i).type, it.pos);
        }
        it.suite = it.suite.accept(this);
        currentBlock.push_back(new ret(null, currentRetBlk));
        blocks.get(currentFun).tailBlk = retBlk;
        currentFun = null;
        currentScope = currentScope.parentScope();
        return it;
    }

    @Override
    public varDefNode visit(varDefNode it) {
        ArrayList<singleVarDefNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.variables.size(); ++i) {
            singleVarDefNode tmp_node = it.variables.get(i).accept(this);
            tmp.add(tmp_node);
            currentScope.defineVariable(tmp_node.id, it.type.type, it.pos, new entity(tmp_node.id));
        }
        it.variables = tmp;
        return it;
    }

    @Override
    public singleVarDefNode visit(singleVarDefNode it) {
        if (it.expr != null) {
            if (it.expr instanceof newExprNode && ((newExprNode) it.expr).type.type.type == type.CLASS){
                ((newExprNode) it.expr).size.add(new constExprNode(1, null));
            }
            it.expr = (ExprNode) it.expr.accept(this);
            entity rhs_ = new entity();
            if (it.expr.val.is_constant) currentBlock.push_back(new assign(new entity(rhs_), new entity(it.expr.val)));
            else rhs_ = new entity(it.expr.val);
            if (currentScope.parentScope() == null){
                currentBlock.push_back(new define(new entity("@" + it.id), new entity(rhs_)));
            } else {
                currentBlock.push_back(new define(new entity(it.id), new entity(rhs_)));
            }
        } else {
            if (currentScope.parentScope() == null){
                currentBlock.push_back(new define(new entity("@" + it.id), null));
            } else {
                currentBlock.push_back(new define(new entity(it.id), null));
            }
        }
        return it;
    }

    @Override
    public typeNode visit(typeNode it) {
        return it;
    }

    @Override
    public blockStmtNode visit(blockStmtNode it) {
        currentScope = new Scope(currentScope);
        ArrayList<StmtNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.stmts.size(); ++i) {
            StmtNode tmp_node = (StmtNode) it.stmts.get(i).accept(this);
            tmp.add(tmp_node);
        }
        it.stmts = tmp;
        currentScope = currentScope.parentScope();
        return it;
    }

    @Override
    public varDefStmtNode visit(varDefStmtNode it) {
        ArrayList<singleVarDefNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.variables.size(); ++i) {
            singleVarDefNode tmp_node = it.variables.get(i).accept(this);
            tmp.add(tmp_node);
            currentScope.defineVariable(tmp_node.id, it.type.type, it.pos, new entity(tmp_node.id));
        }
        it.variables = tmp;
        return it;
    }

    @Override
    public returnStmtNode visit(returnStmtNode it) {
        entity value;
        if (it.value != null) {
            it.value = (ExprNode) it.value.accept(this);
            value = it.value.val;
        } else value = null;
        currentBlock.push_back(new ret(new entity(value), currentRetBlk));
        return it;
    }

    @Override
    public whileStmtNode visit(whileStmtNode it) {
        block cond_blk = new block(), body = new block(), nxt_blk = new block();
        currentBlock.nxtBlock = cond_blk;
        currentBlock = cond_blk;
        it.condition = (ExprNode) it.condition.accept(this);
        currentBlock.push_back(new branch(new entity(it.condition.val), body, nxt_blk));
        currentBlock = body;
        loopDes.push(cond_blk);
        loopFlag.push(0);
        it.body = (StmtNode) it.body.accept(this);
        loopDes.pop();
        loopFlag.pop();
        currentBlock.push_back(new jump(cond_blk));
        currentBlock = nxt_blk;
        return it;
    }

    @Override
    public forStmtNode visit(forStmtNode it) {
        it.init = (ExprNode) it.init.accept(this);
        block cond_blk = new block(), iter_blk = new block(), body = new block(), nxt_blk = new block();
        currentBlock.nxtBlock = cond_blk;
        currentBlock = cond_blk;
        it.condition = (ExprNode) it.condition.accept(this);
        currentBlock.push_back(new branch(new entity(it.condition.val), body, nxt_blk));
        currentBlock = iter_blk;
        it.step = (ExprNode) it.step.accept(this);
        currentBlock.push_back(new jump(cond_blk));
        currentBlock = body;
        loopDes.push(cond_blk);
        loopFlag.push(1);
        it.body = (StmtNode) it.body.accept(this);
        loopDes.pop();
        loopFlag.pop();
        currentBlock.push_back(new jump(iter_blk));
        currentBlock = nxt_blk;
        return it;
    }

    @Override
    public breakStmtNode visit(breakStmtNode it) {
        if (loopFlag.peek() == 1){
            currentBlock.push_back(new jump(loopDes.peek().nxtBlock.successors().get(1)));
        } else {
            currentBlock.push_back(new jump(loopDes.peek().successors().get(1)));
        }
        return it;
    }

    @Override
    public continueStmtNode visit(continueStmtNode it) {
        currentBlock.push_back(new jump(loopDes.peek()));
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
        block trueBranch = new block(), falseBranch = new block(), destination = new block();
        if (it.else_stmt != null){
            currentBlock.push_back(new branch(new entity(it.condition.val), trueBranch, falseBranch));
        } else {
            currentBlock.push_back(new branch(new entity(it.condition.val), trueBranch, destination));
        }
        currentBlock = trueBranch;
        currentScope = new Scope(currentScope);
        if (it.then_stmt != null) it.then_stmt = (StmtNode) it.then_stmt.accept(this);
        currentScope = currentScope.parentScope();
        currentBlock.push_back(new jump(destination));
        currentBlock = falseBranch;
        currentScope = new Scope(currentScope);
        if (it.else_stmt != null) it.else_stmt = (StmtNode) it.else_stmt.accept(this);
        currentScope = currentScope.parentScope();
        currentBlock.push_back(new jump(destination));
        currentBlock = destination;
        return it;
    }

    @Override
    public suffixExprNode visit(suffixExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        it.val = new entity();
        currentBlock.push_back(new assign(new entity(it.val), new entity(it.expr.val)));
//        if (it.expr.val.is_addr && !it.expr.val.id.substring(0, 1).equals("@")){
//            currentBlock.push_back(new load(new entity(it.expr.val), new entity(it.expr.val)));
//            it.expr.val.is_addr = false;
//        }
        if (it.op == suffixExprNode.Op.DECREASE){
            currentBlock.push_back(
                new binary(new entity(it.expr.val), new entity(it.expr.val), new entity(1), binaryExprNode.Op.SUB)
            );
        } else {
            currentBlock.push_back(
                new binary(new entity(it.expr.val), new entity(it.expr.val), new entity(1), binaryExprNode.Op.ADD)
            );
        }
        it.expr_type = new Type(it.expr.expr_type);
        return it;
    }

    @Override
    public prefixExprNode visit(prefixExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
//        if (it.expr.val.is_addr && !it.expr.val.id.substring(0, 1).equals("@")){
//            currentBlock.push_back(
//                new load(new entity(it.expr.val), new entity(it.expr.val))
//            );
//            it.expr.val.is_addr = false;
//        }
        switch(it.op) {
            case NEGATIVE:
                currentBlock.push_back(
                    new binary(new entity(it.expr.val), new entity(0), new entity(it.expr.val), binaryExprNode.Op.SUB)
                );
                break;
            case POSITIVE:
                break;
            case BITWISE_NOT:
                currentBlock.push_back(
                    new binary(new entity(it.expr.val), new entity(it.expr.val), new entity(-1), binaryExprNode.Op.BITWISE_XOR)
                );
                break;
            case DECREASE:
                currentBlock.push_back(
                    new binary(new entity(it.expr.val), new entity(it.expr.val), new entity(1), binaryExprNode.Op.SUB)
                );
                break;
            case INCREASE:
                currentBlock.push_back(
                    new binary(new entity(it.expr.val), new entity(it.expr.val), new entity(1), binaryExprNode.Op.ADD)
                );
                break;
            case NOT:
                currentBlock.push_back(
                    new binary(new entity(it.expr.val), new entity(it.expr.val), new entity(0), binaryExprNode.Op.OR)
                );
                break;
        }
        it.expr_type = new Type(it.expr.expr_type);
        return it;
    }

    @Override
    public thisExprNode visit(thisExprNode it) {
        it.val = new entity("_THIS", true);
        it.expr_type.type = type.CLASS;
        it.expr_type.class_id = currentClass;
        return it;
    }

    @Override
    public newExprNode visit(newExprNode it) {
        // todo: sry dear you are so complicated
        ExprNode tmp_node = (ExprNode) it.size.get(0).accept(this);
        it.val = new entity();
        Integer size_ = 4;
        if (it.type.type.isClass()){
            size_ *= getClass(it.type.type.class_id).members.size();
        }
        entity tmp_mul = new entity();
        currentBlock.push_back(
            new binary(new entity(tmp_mul), new entity(tmp_node.val), new entity(size_), binaryExprNode.Op.MUL)
        );
        currentBlock.push_back(new assign(new entity("_A0"), new entity(tmp_mul)));
        currentBlock.push_back(new call("Mx_malloc"));
        currentBlock.push_back(new define(new entity("_NEW_" + it.size.size()), new entity("_A0")));
        if (it.type.type.type == type.CLASS){
            String classId = it.type.type.class_id;
            currentBlock.push_back(new call(classId + "_memberFn_" + classId));
        }
        if (it.size.size() > 1){
            entity dim = new entity();
            dim.id += "_dim";
            entity i = new entity();
            i.id += "_i";
            entity x = new entity();
            x.id += "_x";
            currentBlock.push_back(
                new binary(new entity(dim), new entity(tmp_node.val), new entity(1), binaryExprNode.Op.SUB)
            );
            currentBlock.push_back(new assign(new entity(i), new entity(tmp_node.val)));
            currentBlock.nxtBlock = new block();
            currentBlock = currentBlock.nxtBlock;
            block retBlk = currentBlock;
            block outBlk = new block();
            currentBlock.push_back(new branch(new entity(i),null, outBlk));
            currentBlock.push_back(
                new binary(new entity(i), new entity(i), new entity(1), binaryExprNode.Op.SUB)
            );
            newExprNode it_ = new newExprNode(null, it.type);
            for (int j = 1; j < it.size.size(); ++j){
                it_.size.add(it.size.get(j));
            }
            it_ = it_.accept(this);
            currentBlock.push_back(
                new binary(new entity(x), new entity(dim), new entity(i), binaryExprNode.Op.SUB)
            );
            currentBlock.push_back(
                new binary(new entity(x), new entity(x), new entity(4), binaryExprNode.Op.MUL)
            );
            currentBlock.push_back(
                new binary(new entity(x), new entity(x), new entity(it.val), binaryExprNode.Op.ADD)
            );
            currentBlock.push_back(new store(new entity(x), new entity(it_.val)));
            currentBlock.push_back(new jump(retBlk));
            currentBlock.nxtBlock = outBlk;
            currentBlock = outBlk;
        }
        currentBlock.push_back(new getPtr("_NEW_" + it.size.size(), new entity(it.val)));
        currentBlock.push_back(new load(new entity(it.val), new entity(it.val)));
        return it;
    }

    @Override
    public assignExprNode visit(assignExprNode it) {
        if (it.rhs instanceof newExprNode && ((newExprNode) it.rhs).type.type.type == type.CLASS){
            ((newExprNode) it.rhs).size.add(new constExprNode(1, null));
        }
        it.rhs = (ExprNode) it.rhs.accept(this);
        entity rhs_ = new entity();
        if (it.rhs.val.is_constant){
            currentBlock.push_back(new assign(new entity(rhs_), new entity(it.rhs.val)));
        } else {
            rhs_ = new entity(it.rhs.val);
        }
        if (it.lhs instanceof varExprNode){
            if (currentScope.isGlobl(((varExprNode) it.lhs).id)) {
                currentBlock.push_back(new store(new entity("@" + ((varExprNode) it.lhs).id), new entity(rhs_)));
            } else {
                entity lhs_ = new entity();
                currentBlock.push_back(new getPtr(((varExprNode) it.lhs).id, new entity(lhs_)));
                currentBlock.push_back(new store(new entity(lhs_), new entity(rhs_)));
            }
        } else {
            it.lhs = (ExprNode) it.lhs.accept(this);
            if (it.lhs instanceof arrayExprNode || it.lhs instanceof memberExprNode) currentBlock.pop();
            else currentBlock.push_back(new getPtr(it.lhs.val.id, new entity(it.lhs.val)));
            currentBlock.push_back(new store(new entity(it.lhs.val), new entity(rhs_)));
        }
        return it;
    }

    @Override
    public binaryExprNode visit(binaryExprNode it) {
        it.lhs = (ExprNode) it.lhs.accept(this);
        it.rhs = (ExprNode) it.rhs.accept(this);
        entity value = new entity();
        it.val = value;
        binaryExprNode.Op op = it.op;
        if (it.rhs.expr_type.type == type.STRING && it.lhs.expr_type.type == type.STRING){
            currentBlock.push_back(new assign(new entity("_A0"), new entity(it.lhs.val)));
            currentBlock.push_back(new assign(new entity("_A1"), new entity(it.rhs.val)));
            if (op == binaryExprNode.Op.ADD){
                currentBlock.push_back(new call("Mx_string_ADD"));
            } else if (op == binaryExprNode.Op.EQ) {
                currentBlock.push_back(new call("Mx_string_EQ"));
            } else if (op == binaryExprNode.Op.NEQ) {
                currentBlock.push_back(new call("Mx_string_NEQ"));
            } else if (op == binaryExprNode.Op.GTH) {
                currentBlock.push_back(new call("Mx_string_GTH"));
            } else if (op == binaryExprNode.Op.LTH) {
                currentBlock.push_back(new call("Mx_string_LTH"));
            } else if (op == binaryExprNode.Op.GEQ) {
                currentBlock.push_back(new call("Mx_string_GEQ"));
            } else if (op == binaryExprNode.Op.LEQ) {
                currentBlock.push_back(new call("Mx_string_LEQ"));
            }
            currentBlock.push_back(new assign(new entity(it.val), new entity("_A0")));
        } else {
            currentBlock.push_back(
                new binary(new entity(value), new entity(it.lhs.val), new entity(it.rhs.val), op)
            );
        }
        return it;
    }

    @Override
    public constExprNode visit(constExprNode it) {
//        System.out.println("in constExprNode");
        if (it.string_value != null){
            entity tmp = new entity(true);
            tmp.id = "%" + tmp.id;
            entity ret = new entity(true);
            currentBlock.push_back(new load(new entity(tmp.id), new entity(ret)));
            blocks.get("_VAR_DEF").push_back(new define(new entity(tmp), new entity(it)));
            it.val = new entity(ret);
        } else {
            it.val = new entity(it);
        }
        return it;
    }

    @Override
    public varExprNode visit(varExprNode it) {
//        System.out.println("var_expr_" + it.id);
        it.expr_type = new Type(currentScope.getType(it.id, true));
        if (currentScope.isGlobl(it.id)){
            it.val = new entity();
            currentBlock.push_back(new load(new entity("@" + it.id), new entity(it.val)));
        } else if (it.expr_type != null && (it.expr_type.dimension > 0 || it.expr_type.isClass() || it.expr_type.type == type.STRING)) {
            it.val = new entity(it.id, true);
        } else {
            it.val = new entity(it.id);
        }
        if (it.expr_type != null && it.expr_type.type == type.STRING){
            it.expr_type.class_id = "string";
        }
        return it;
    }

    @Override
    public funCallExprNode visit(funCallExprNode it) {
        String function_id;
        Integer paraNum = 0;
        if (it.function_id instanceof varExprNode) {
            function_id = ((varExprNode) it.function_id).id;
            it.expr_type = new Type(getFunction(function_id).type.type);
        } else {
            ExprNode expr_ = (ExprNode) ((memberExprNode) it.function_id).expr.accept(this);
//            System.out.println("call: " + ((memberExprNode) it.function_id).member);
            if (expr_.expr_type.type == type.STRING){
                it.expr_type = new Type(getClass("string").getFunction(((memberExprNode) it.function_id).member, null).type.type);
            } else if (expr_.expr_type.dimension > 0){
                it.expr_type = new Type(getClass("*ARRAY").getFunction(((memberExprNode) it.function_id).member, null).type.type);
            } else {
                it.expr_type = new Type(getClass(expr_.expr_type.class_id).getFunction(((memberExprNode) it.function_id).member, null).type.type);
            }
            currentBlock.push_back(new assign(new entity("_A" + paraNum++), new entity(expr_.val)));
            expr_ = (ExprNode) expr_.accept(this);
            if (expr_.expr_type.dimension > 0) {
                function_id = "Mx_array_size";
            } else if (expr_.expr_type.type == type.STRING) {
                function_id = "Mx_string_" + ((memberExprNode) it.function_id).member;
            } else { // this_expr or class_expr
                function_id = expr_.expr_type.class_id + "_memberFn_" + ((memberExprNode) it.function_id).member;
            }
        }
//        System.out.println("call: " + function_id);
        for (int i = 0; i < it.parameters.size(); ++i){
            ExprNode tmp_node = (ExprNode) it.parameters.get(i).accept(this);
            entity tmp = new entity();
            if (paraNum <= 6){
                currentBlock.push_back(new assign(new entity("_A" + paraNum++), new entity(tmp_node.val)));
            } else {
                currentBlock.push_back(
                    new binary(new entity(tmp), new entity("_SP"), new entity((paraNum - 7) * 4), binaryExprNode.Op.ADD)
                );
                entity rhs_ = new entity();
                if (tmp_node.val.is_constant){
                    currentBlock.push_back(new assign(new entity(rhs_), new entity(tmp_node.val)));
                } else {
                    rhs_ = new entity(tmp_node.val);
                }
                currentBlock.push_back(new store(new entity(tmp), new entity(rhs_)));
            }
            paraNum ++;
        }
        if (paraNum - 7 > spillPara.get(currentFun)) spillPara.put(currentFun, paraNum - 7);
        it.val = new entity();
        currentBlock.push_back(new call(function_id));
        currentBlock.push_back(new assign(new entity(it.val), new entity("_A0")));
        return it;
    }

    @Override
    public memberExprNode visit(memberExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        entity ent_ = it.expr.val;
        Integer offset = 0;
        classDefNode node;
        if (it.expr_type.type == type.STRING){
            node = getClass("string");
        } else if (it.expr_type.dimension > 0){
            node = getClass("*ARRAY");
        } else {
            node = getClass(it.expr.expr_type.class_id);
        }
        for (int i = 0; i < node.members.size(); ++i){
            for (int j = 0; j < node.members.get(i).variables.size(); ++j) {
                if (node.members.get(i).variables.get(j).id.equals(it.member)) break;
                offset += 4;
            }
        }
        it.val = new entity(true);
        typeNode t_ = node.getMemberType(it.member);
        if (t_ != null) {
            it.expr_type = new Type(t_.type);
        }
        currentBlock.push_back(
            new binary(new entity(it.val), new entity(ent_), new entity(offset), binaryExprNode.Op.ADD)
        );
        currentBlock.push_back(new load(new entity(it.val), new entity(it.val)));
        return it;
    }

    @Override
    public arrayExprNode visit(arrayExprNode it) {
        it.array = (ExprNode) it.array.accept(this);
        it.index = (ExprNode) it.index.accept(this);
        it.val = new entity(true);
        entity l = new entity(true);
        currentBlock.push_back(
            new binary(new entity(l), new entity(it.index.val), new entity(4), binaryExprNode.Op.MUL)
        );
        currentBlock.push_back(
            new binary(new entity(it.val), new entity(l), new entity(it.array.val), binaryExprNode.Op.ADD)
        );
        it.expr_type = new Type(it.array.expr_type);
        currentBlock.push_back(new load(new entity(it.val), new entity(it.val)));
        it.expr_type.dimension -= 1;
        return it;
    }
}

