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
    public HashMap<String, HashSet<String>> varInFun = new HashMap<>();
    public Stack<block> breakDes = new Stack<>();
    public Stack<block> continueDes = new Stack<>();


    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public String defVar(String id){
        if (currentScope.parentScope() == null && !id.startsWith("_NEW") && !id.startsWith("_FLAG") ){
            return id;
        }
        Integer index = 0;
        for (String i : varInFun.get(currentFun)){
            if (i.startsWith(id + "_")  && isNumeric(i.substring(id.length() + 1))) index++;
        }
        varInFun.get(currentFun).add(id + "_" + index);
        return id + "_" + index;
    }


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
        for (classDefNode class_ : defined_class){
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
        this.varInFun.put("main", new HashSet<>());
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
        for (int i = 0; i < it.sections.size(); ++i) {
            if (it.sections.get(i) instanceof varDefNode){
                currentBlock = curMainBlk;
                currentFun = "main";
                varDefNode v = (varDefNode) it.sections.get(i);
                for (singleVarDefNode single_v : v.variables){
                    blocks.get("_VAR_DEF").push_back(new define(new entity("@" + single_v.id), null));
                }
            }
            tmp.add((SectionNode) it.sections.get(i).accept(this));
            if (it.sections.get(i) instanceof varDefNode) curMainBlk = currentBlock;
        }
        it.sections = tmp;
        return it;
    }

    @Override
    public classDefNode visit(classDefNode it) {
        currentScope = new Scope(currentScope);
        currentScope.isClassDef = true;
        currentClass = it.id;
        if (it.members != null){
            for (int i = 0; i < it.members.size(); ++i){
                for (int j = 0; j < it.members.get(i).variables.size(); ++j){
                    singleVarDefNode tmp = it.members.get(i).variables.get(j);
                    currentScope.defineVariable(tmp.id, it.members.get(i).type.type, tmp.pos);
                }
            }
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
        currentClass = null;
        return it;
    }

    @Override
    public classConstructorNode visit(classConstructorNode it) {
        currentBlock = new block();
        currentFun = currentClass + "_memberFn_" + it.name;
        blocks.put(currentFun, currentBlock);
        varInFun.put(currentFun, new HashSet<>());
        spillPara.put(currentFun, 0);
        currentBlock.push_back(new define(new entity("_THIS"), new entity("_A0")));
        currentScope = new Scope(currentScope);
        for (int i = 0; i < it.parameters_type.size(); ++i){
            String id = defVar(it.parameters.get(i).id);
            currentScope.defineVariable(id, it.parameters_type.get(i).type, it.pos);
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
            varInFun.put(currentFun, new HashSet<>());
        }
        currentScope = new Scope(currentScope);
        block retBlk = new block();
        retBlk.push_back(new jump(null));
        currentRetBlk = retBlk;
        entity tmp = new entity();
        for (int i = 0; i < it.parameters_type.size(); ++i){
            String para_id = defVar(it.parameters.get(i).id);
            currentScope.defineVariable(para_id, it.parameters_type.get(i).type, null);
            if (paraNum <= 6){
                currentBlock.push_back(new define(new entity(para_id), new entity("_A" + paraNum)));
            } else {
                currentBlock.push_back(
                    new binary(new entity(tmp), new entity("_S0"), new entity((paraNum - 7) * 4), binaryExprNode.Op.ADD)
                );
                currentBlock.push_back(new load(new entity(tmp), new entity(tmp)));
                currentBlock.push_back(new define(new entity(para_id), new entity(tmp)));
            }
            paraNum ++;
        }
        it.suite = it.suite.accept(this);
        if (currentFun.equals("main")){
            currentBlock.push_back(new ret(new entity(0), currentRetBlk));
        } else {
            currentBlock.push_back(new ret(null, currentRetBlk));
        }
        blocks.get(currentFun).tailBlk = retBlk;
        currentFun = null;
        currentScope = currentScope.parentScope();
        return it;
    }

    @Override
    public varDefNode visit(varDefNode it) {
        ArrayList<singleVarDefNode> tmp = new ArrayList<>();
        for (int i = 0; i < it.variables.size(); ++i) {
            String id = defVar(it.variables.get(i).id);
            currentScope.defineVariable(id, it.type.type, it.pos);
            singleVarDefNode tmp_node = it.variables.get(i).accept(this);
            tmp.add(tmp_node);
        }
        it.variables = tmp;
        return it;
    }

    @Override
    public singleVarDefNode visit(singleVarDefNode it) {
        String id = currentScope.getName(it.id);
        if (it.expr != null) {
            it.expr = (ExprNode) it.expr.accept(this);
            entity rhs_ = new entity();
            if (it.expr.val.is_constant) currentBlock.push_back(new assign(new entity(rhs_), new entity(it.expr.val)));
            else rhs_ = new entity(it.expr.val);
            if (currentScope.parentScope() == null){
                currentBlock.push_back(new define(new entity("@" + id), new entity(rhs_)));
            } else {
                currentBlock.push_back(new define(new entity(id), new entity(rhs_)));
            }
        } else {
            if (currentScope.parentScope() == null){
                currentBlock.push_back(new define(new entity("@" + id), null));
            } else {
                currentBlock.push_back(new define(new entity(id), null));
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
            String id = defVar(it.variables.get(i).id);
            currentScope.defineVariable(id, it.type.type, it.pos);
            singleVarDefNode tmp_node = it.variables.get(i).accept(this);
            tmp.add(tmp_node);
        }
        it.variables = tmp;
        return it;
    }

    @Override
    public returnStmtNode visit(returnStmtNode it) {
        if (it.value != null) {
            it.value = (ExprNode) it.value.accept(this);
            currentBlock.push_back(new ret(new entity(it.value.val), currentRetBlk));
        } else {
            currentBlock.push_back(new ret(null, currentRetBlk));
        }
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
        breakDes.push(nxt_blk);
        continueDes.push(cond_blk);
        currentScope = new Scope(currentScope);
        if (it.body != null) it.body = (StmtNode) it.body.accept(this);
        currentScope = currentScope.parentScope();
        breakDes.pop();
        continueDes.pop();
        currentBlock.push_back(new jump(cond_blk));
        currentBlock = nxt_blk;
        return it;
    }

    @Override
    public forStmtNode visit(forStmtNode it) {
        if (it.init != null) it.init = (ExprNode) it.init.accept(this);
        block cond_blk = new block(), iter_blk = new block(), body = new block(), nxt_blk = new block();
        currentBlock.nxtBlock = cond_blk;
        currentBlock = cond_blk;
        if (it.condition != null) {
            it.condition = (ExprNode) it.condition.accept(this);
            currentBlock.push_back(new branch(new entity(it.condition.val), body, nxt_blk));
        } else {
            currentBlock.push_back(new branch(new entity(1), body, nxt_blk));
        }
        currentBlock = iter_blk;
        if (it.step != null) it.step = (ExprNode) it.step.accept(this);
        currentBlock.push_back(new jump(cond_blk));
        currentBlock = body;
        breakDes.push(nxt_blk);
        continueDes.push(iter_blk);
        currentScope = new Scope(currentScope);
        if (it.body != null) it.body = (StmtNode) it.body.accept(this);
        currentScope = currentScope.parentScope();
        breakDes.pop();
        continueDes.pop();
        currentBlock.push_back(new jump(iter_blk));
        currentBlock = nxt_blk;
        return it;
    }

    @Override
    public breakStmtNode visit(breakStmtNode it) {
        currentBlock.push_back(new jump(breakDes.peek()));
        return it;
    }

    @Override
    public continueStmtNode visit(continueStmtNode it) {
        currentBlock.push_back(new jump(continueDes.peek()));
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
        binaryExprNode tmp_b;
        if (it.op != null) {
            if (it.op == suffixExprNode.Op.DECREASE) {
                tmp_b = new binaryExprNode(it.pos, binaryExprNode.Op.SUB, it.expr, new constExprNode(1, it.pos));
            } else {
                tmp_b = new binaryExprNode(it.pos, binaryExprNode.Op.ADD, it.expr, new constExprNode(1, it.pos));
            }
            assignExprNode tmp_a = new assignExprNode(it.expr, tmp_b, it.pos);
            visit(tmp_a);
        }
        it.op = null;
        it.expr_type = new Type(it.expr.expr_type);
        return it;
    }


    @Override
    public prefixExprNode visit(prefixExprNode it) {
        it.expr = (ExprNode) it.expr.accept(this);
        it.val = new entity(it.expr.val);
        entity tmp_lhs = new entity();
        binaryExprNode tmp_b;
        assignExprNode tmp_a;
        if (it.op != null) {
            switch (it.op) {
                case NEGATIVE:
                    if (it.expr.val.is_constant) {
                        it.val = new entity(-it.expr.val.constant.int_value);
                        return it;
                    }
                    currentBlock.push_back(
                            new binary(new entity(tmp_lhs), new entity(0), new entity(it.expr.val), binaryExprNode.Op.SUB)
                    );
                    it.val = new entity(tmp_lhs);
                    break;
                case POSITIVE:
                    break;
                case BITWISE_NOT:
                    currentBlock.push_back(
                            new binary(new entity(tmp_lhs), new entity(it.expr.val), new entity(-1), binaryExprNode.Op.BITWISE_XOR)
                    );
                    it.val = new entity(tmp_lhs);
                    break;
                case DECREASE:
                    tmp_b = new binaryExprNode(it.pos, binaryExprNode.Op.SUB, it.expr, new constExprNode(1, it.pos));
                    tmp_a = new assignExprNode(it.expr, tmp_b, it.pos);
                    visit(tmp_a);
                    if (!(it.expr instanceof varExprNode)) it.val = tmp_b.val;
                    break;
                case INCREASE:
                    tmp_b = new binaryExprNode(it.pos, binaryExprNode.Op.ADD, it.expr, new constExprNode(1, it.pos));
                    tmp_a = new assignExprNode(it.expr, tmp_b, it.pos);
                    visit(tmp_a);
                    if (!(it.expr instanceof varExprNode)) it.val = tmp_b.val;
                    break;
                case NOT:
                    currentBlock.push_back(
                            new binary(new entity(tmp_lhs), new entity(it.expr.val), new entity(1), binaryExprNode.Op.BITWISE_XOR)
                    );
                    it.val = new entity(tmp_lhs);
                    break;
                default:
            }
        }
        it.op = null;
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
    public newExprNode visit(newExprNode it){
        if (it.type.type.isClass()){
            it.size.add(new constExprNode(1, null));
        }
        return visit(it, true);
    }

    public newExprNode visit(newExprNode it, boolean recursion) {
        ExprNode tmp_node = (ExprNode) it.size.get(0).accept(this);
        it.val = new entity();
        if (it.type.type.isClass() && it.size.size() == 1){
            Integer size_ = 4 * getClass(it.type.type.class_id).members.size();
            currentBlock.push_back(new assign(new entity("_A0"), new entity(size_)));
            currentBlock.push_back(new call("malloc"));
            currentBlock.push_back(new assign(new entity(it.val), new entity("_A0")));
            String classId = it.type.type.class_id;
            if (getClass(classId).constructor != null && getClass(classId).constructor.size() > 0) {
                currentBlock.push_back(new call(classId + "_memberFn_" + classId));
            }
            return it;
        } else {
            currentBlock.push_back(new assign(new entity("_A0"), new entity(tmp_node.val)));
            currentBlock.push_back(new call("Mx_malloc"));
        }
        if (it.size.size() > 1){
            String new_id = defVar("_NEW_" + it.size.size());
            currentBlock.push_back(new define(new entity(new_id), new entity("_A0")));
            String flag_id = defVar("_FLAG_" + it.size.size());
            currentBlock.push_back(new define(new entity(flag_id), new entity(tmp_node.val)));
            currentBlock.nxtBlock = new block();
            currentBlock = currentBlock.nxtBlock;
            block retBlk = currentBlock;
            block outBlk = new block();
            currentBlock.push_back(new branch(new entity(flag_id),null, outBlk));
            entity tmp_addr = new entity();
            entity tmp_val = new entity();
            newExprNode it_ = new newExprNode(null, it.type);
            for (int j = 1; j < it.size.size(); ++j){
                it_.size.add(it.size.get(j));
            }
            it_ = visit(it_, true);
            entity x = new entity(), y = new entity();
            currentBlock.push_back(
                new binary(new entity(x), new entity(flag_id), new entity(2), binaryExprNode.Op.SLA)
            );
            currentBlock.push_back(new load(new entity(new_id), new entity(y), true));
            currentBlock.push_back(
                new binary(new entity(x), new entity(x), new entity(y), binaryExprNode.Op.ADD)
            );
            currentBlock.push_back(new store(new entity(x), new entity(it_.val)));
            currentBlock.push_back(new binary(new entity(tmp_val), new entity(flag_id), new entity(1), binaryExprNode.Op.SUB));
            currentBlock.push_back(new store(new entity(flag_id), new entity(tmp_val), true));
            currentBlock.push_back(new jump(retBlk));
            currentBlock.nxtBlock = outBlk;
            currentBlock = outBlk;
            currentBlock.push_back(new load(new entity(new_id), new entity(it.val), true));
        } else {
            currentBlock.push_back(new assign(new entity(it.val), new entity("_A0")));
        }
        return it;
    }

    @Override
    public assignExprNode visit(assignExprNode it) {
        it.rhs = (ExprNode) it.rhs.accept(this);
        entity rhs_ = new entity();
        if (it.rhs.val.is_constant){
            currentBlock.push_back(new assign(new entity(rhs_), new entity(it.rhs.val)));
        } else {
            rhs_ = new entity(it.rhs.val);
        }
        if (it.lhs instanceof varExprNode){
            String id = currentScope.getName(((varExprNode) it.lhs).id);
            if (currentScope.isGlobl(id)) {
                currentBlock.push_back(new store(new entity("@" + id), new entity(rhs_)));
            } else if (currentScope.isMember(((varExprNode) it.lhs).id)){
                memberExprNode tmp_mem = new memberExprNode(it.pos, new thisExprNode(it.pos), ((varExprNode) it.lhs).id);
                tmp_mem = tmp_mem.accept(this);
                currentBlock.pop();
                currentBlock.push_back(new store(new entity(tmp_mem.val), new entity(rhs_)));
            } else {
                currentBlock.push_back(new store(new entity(id), new entity(rhs_), true));
            }
        } else {
            if (!(it.lhs instanceof prefixExprNode)) {
                it.lhs = (ExprNode) it.lhs.accept(this);
                if (it.lhs instanceof arrayExprNode || it.lhs instanceof memberExprNode) {
                    currentBlock.pop();
                    currentBlock.push_back(new store(new entity(it.lhs.val), new entity(rhs_)));
                } else {
                    currentBlock.push_back(new store(new entity(it.lhs.val.id), new entity(rhs_), true));
                }
            } else {
                currentBlock.push_back(new store(new entity(it.lhs.val), new entity(rhs_)));
            }
        }
        return it;
    }

    public boolean containsAnd(binaryExprNode it){
        if (it.op == binaryExprNode.Op.AND) return true;
        if (it.lhs instanceof binaryExprNode && it.rhs instanceof binaryExprNode){
            return containsAnd((binaryExprNode) it.lhs) || containsAnd((binaryExprNode) it.rhs);
        } else if (it.lhs instanceof binaryExprNode){
            return containsAnd((binaryExprNode) it.lhs);
        } else if (it.rhs instanceof binaryExprNode){
            return containsAnd((binaryExprNode) it.rhs);
        } else {
            return false;
        }
    }

    public boolean containsOr(binaryExprNode it){
        if (it.op == binaryExprNode.Op.OR) return true;
        if (it.lhs instanceof binaryExprNode && it.rhs instanceof binaryExprNode){
            return containsOr((binaryExprNode) it.lhs) || containsOr((binaryExprNode) it.rhs);
        } else if (it.lhs instanceof binaryExprNode){
            return containsOr((binaryExprNode) it.lhs);
        } else if (it.rhs instanceof binaryExprNode){
            return containsOr((binaryExprNode) it.rhs);
        } else {
            return false;
        }
    }

    @Override
    public binaryExprNode visit(binaryExprNode it) {
        if (!containsAnd(it) && !containsOr(it)) return visit(it, true);
        binaryExprNode ret;
        block nxt = new block();
        if (containsAnd(it)){
            currentBlock.optAndBlk = new block();
            ret = visit(it, true);
            currentBlock.push_back(new define(new entity(ret.val), new entity(ret.val)));
            entity val = new entity();
            currentBlock.optAndBlk.push_back(new assign(new entity(val), new entity(0)));
            currentBlock.optAndBlk.push_back(new store(new entity(ret.val.id), new entity(val), true));
            currentBlock.optAndBlk.push_back(new jump(nxt));
            currentBlock.push_back(new jump(nxt));
            currentBlock = nxt;
            return ret;
        }
        if (containsOr(it)){
            currentBlock.optOrBlk = new block();
            ret = visit(it, true);
            currentBlock.push_back(new define(new entity(ret.val), new entity(ret.val)));
            entity val = new entity();
            currentBlock.optOrBlk.push_back(new assign(new entity(val), new entity(1)));
            currentBlock.optOrBlk.push_back(new store(new entity(ret.val.id), new entity(val), true));
            currentBlock.optOrBlk.push_back(new jump(nxt));
            currentBlock.push_back(new jump(nxt));
            currentBlock = nxt;
            return ret;
        }
        return null;
    }

    public binaryExprNode visit(binaryExprNode it, boolean recursion) {
        if (it.lhs instanceof binaryExprNode) it.lhs = visit((binaryExprNode) it.lhs, true);
        else it.lhs = (ExprNode) it.lhs.accept(this);
        entity value = new entity();
        if (it.op == binaryExprNode.Op.AND){
            currentBlock.push_back(new branch(it.lhs.val, null, currentBlock.optAndBlk));
        } else if (it.op == binaryExprNode.Op.OR){
            currentBlock.push_back(new branch(it.lhs.val, currentBlock.optOrBlk, null));
        }
        if (it.rhs instanceof binaryExprNode) it.rhs = visit((binaryExprNode) it.rhs, true);
        else it.rhs = (ExprNode) it.rhs.accept(this);
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
        String id = currentScope.getName(it.id);
        if (currentScope.isMember(it.id)){
            memberExprNode this_ = new memberExprNode(it.pos, new thisExprNode(it.pos), it.id);
            this_ = this_.accept(this);
            it.val = new entity(this_.val);
            return it;
        }
        it.expr_type = new Type(currentScope.getType(id, true));
        if (currentScope.isGlobl(id)){
            it.val = new entity("@" + id);
        } else {
            it.val = new entity(id);
        }
        return it;
    }

    @Override
    public funCallExprNode visit(funCallExprNode it) {
        String function_id;
        Integer paraNum = 0;
        ExprNode expr_class = new varExprNode(null, null);
        if (it.function_id instanceof varExprNode) {
            function_id = ((varExprNode) it.function_id).id;
            if (currentClass != null && getClass(currentClass).containFunction(function_id)) {
                memberExprNode tmp_mem = new memberExprNode(it.pos, new thisExprNode(it.pos), function_id);
                funCallExprNode tmp = new funCallExprNode(it.pos, tmp_mem);
                for (ExprNode i : it.parameters){
                    tmp.parameters.add(i);
                }
                tmp = tmp.accept(this);
                it.val = new entity(tmp.val);
                return it;
            }
            it.expr_type = new Type(getFunction(function_id).type.type);
        } else {
            expr_class = (ExprNode) ((memberExprNode) it.function_id).expr.accept(this);
            if (expr_class.expr_type.type == type.STRING){
                function_id = "Mx_string_" + ((memberExprNode) it.function_id).member;
                it.expr_type = new Type(getClass("string").getFunction(((memberExprNode) it.function_id).member, null).type.type);
            } else if (expr_class.expr_type.dimension > 0){
                function_id = "Mx_array_size";
                it.expr_type = new Type(getClass("*ARRAY").getFunction(((memberExprNode) it.function_id).member, null).type.type);
            } else {
                function_id = expr_class.expr_type.class_id + "_memberFn_" + ((memberExprNode) it.function_id).member;
                it.expr_type = new Type(getClass(expr_class.expr_type.class_id).getFunction(((memberExprNode) it.function_id).member, null).type.type);
            }
            paraNum++;
        }
        ArrayList<entity> para_ent = new ArrayList<>();
        for (int i = 0; i < it.parameters.size(); ++i) {
            ExprNode tmp_node = (ExprNode) it.parameters.get(i).accept(this);
            para_ent.add(tmp_node.val);
        }
        if (paraNum == 1){
            currentBlock.push_back(new assign(new entity("_A0"), new entity(expr_class.val)));
        }
        for (int i = 0; i < it.parameters.size(); ++i){
            entity tmp = new entity();
            if (paraNum <= 6){
                currentBlock.push_back(new assign(new entity("_A" + paraNum++), new entity(para_ent.get(i))));
            } else {
                currentBlock.push_back(
                    new binary(new entity(tmp), new entity("_SP"), new entity((paraNum - 7) * 4), binaryExprNode.Op.ADD)
                );
                entity rhs_ = new entity();
                if (para_ent.get(i).is_constant){
                    currentBlock.push_back(new assign(new entity(rhs_), new entity(para_ent.get(i))));
                } else {
                    rhs_ = new entity(para_ent.get(i));
                }
                currentBlock.push_back(new store(new entity(tmp), new entity(rhs_)));
                paraNum ++;
            }
        }
        if (currentFun == null ? paraNum - 7 > spillPara.get("main") : paraNum - 7 > spillPara.get(currentFun)) spillPara.put(currentFun, paraNum - 7);
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
        if (it.expr.expr_type.type == type.STRING){
            node = getClass("string");
        } else if (it.expr.expr_type.dimension > 0){
            node = getClass("*ARRAY");
        } else {
            node = getClass(it.expr.expr_type.class_id);
        }
        for (int i = 0; i < node.members.size(); ++i){
            if (node.members.get(i).variables.get(0).id.equals(it.member)) break;
            offset += 4;
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
                new binary(new entity(l), new entity(it.index.val), new entity(1), binaryExprNode.Op.ADD)
        );
        currentBlock.push_back(
            new binary(new entity(l), new entity(l), new entity(2), binaryExprNode.Op.SLA)
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

