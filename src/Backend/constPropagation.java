package Backend;

import MIR.*;
import AST.constExprNode;
import Util.Type.type;

import java.util.HashMap;
import java.util.HashSet;

public class constPropagation{
    private HashMap<String, HashSet<defineNode>> definedVar = new HashMap<>();
    private HashSet<defineNode> workList = new HashSet<>();
    private HashMap<Integer, block> index2blk = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> preBlk = new HashMap<>();
    private HashSet<defineNode> Done = new HashSet<>();

    public constPropagation(HashMap<String, block> blocks){
        for (String name : blocks.keySet()){
            index2blk = new HashMap<>();
            definedVar = new HashMap<>();
            Done = new HashSet<>();
            buildList(blocks.get(name));
//            System.out.println(name+" build finished: ");
//            for (defineNode d : workList) printDef(d);
            while (!workList.isEmpty()){
                for (defineNode todo : workList) propagate(todo);
                workList = new HashSet<>();
                updateList();
            }
        }
    }

    private String getEntityString(entity e){
        if (e.is_constant){
            if (e.constant.expr_type.type == type.INT){
                return Integer.toString(e.constant.int_value);
            } else if (e.constant.expr_type.type == type.STRING){
                return e.constant.string_value;
            } else if (e.constant.expr_type.type == type.NULL){
                return "null";
            } else if (e.constant.bool_value == true){
                return "true";
            } else {
                return "false";
            }
        }
        if (e.id != null) {
            if (e.id.startsWith("_A")) return "a" + e.id.substring(2, 3);
            if (e.id.equals("_SP")) return "sp";
            if (e.id.equals("_S0")) return "s0";
        }
        return e.id;
    }

    public void printDef(defineNode def){
        statement s = def.def;
        if (def.def instanceof binary){
            binary b = (binary) s;
            System.out.println("\t" + getEntityString(b.lhs) + " = " +
                    getEntityString(b.op1) + " " + b.op +
                    " " + getEntityString(b.op2) + ";");
        } else if (def.def instanceof assign){
            assign a = (assign) s;
            System.out.println("\t" + getEntityString(a.lhs) + " = " + getEntityString(a.rhs) + ";");
        } else {
            load l = (load) s;
            if (l.addr != null) {
                System.out.println("\tload " + getEntityString(l.addr) + " on " + getEntityString(l.to) + ";");
            } else {
                if (l.id != null) {
                    System.out.println("\tload " + getEntityString(l.id) + " on " + getEntityString(l.to) + ";");
                } else {
                    System.out.println("\tload " + l.sp + " on " + getEntityString(l.to) + ";");
                }
            }
        }
    }

    public void buildList(block blk){
        if (index2blk.containsKey(blk.index)) return;
        else index2blk.put(blk.index, blk);
        for (statement s : blk.stmts){
            if (s instanceof binary){
                if (((binary) s).op1.is_constant && ((binary) s).op2.is_constant) {
                    addList(((binary) s).lhs.id, blk.index, blk.stmts.indexOf(s));
                }
                else addD(((binary) s).lhs.id, blk.index, blk.stmts.indexOf(s));
            } else if (s instanceof assign){
                if (((assign) s).rhs != null) {
                    if (((assign) s).rhs.is_constant) {
                        addList(((assign) s).lhs.id, blk.index, blk.stmts.indexOf(s));
                    } else addD(((assign) s).lhs.id, blk.index, blk.stmts.indexOf(s));
                }
            } else if (s instanceof load){
                addD(((load) s).to.id, blk.index, blk.stmts.indexOf(s));
            }
        }
        for (block b : blk.successors()) {
            if (!preBlk.containsKey(b.index)) preBlk.put(b.index, new HashSet<>());
            preBlk.get(b.index).add(blk.index);
            buildList(b);
        }
    }

    public void addD(String id, Integer blk, Integer index){
        if (!id.startsWith("_A") && !id.equals("_SP") && !id.equals("_S0")) {
            if (!definedVar.containsKey(id)) definedVar.put(id, new HashSet<>());
            definedVar.get(id).add(new defineNode(index2blk.get(blk).stmts.get(index), blk));
        }
    }

    public void addList(String id, Integer blk, Integer index){
        if (!id.startsWith("_A") && !id.equals("_SP") && !id.equals("_S0")) {
            defineNode def = new defineNode(index2blk.get(blk).stmts.get(index), blk);
            workList.add(def);
            if (!definedVar.containsKey(id)) definedVar.put(id, new HashSet<>());
            definedVar.get(id).add(def);
        }
    }

    public void propagate(defineNode todo){
        entity const_;
        String id;
        if (todo.def instanceof assign) {
            assign a = (assign) todo.def;
            const_ = a.rhs;
            id = a.lhs.id;
        } else {
            binary b = (binary) todo.def;
            constExprNode cNode = null;
            switch (b.op) {
                case NEQ:
                    cNode = new constExprNode(b.op1.constant.int_value != b.op2.constant.int_value, null);
                    break;
                case EQ:
                    cNode = new constExprNode(b.op1.constant.int_value == b.op2.constant.int_value, null);
                    break;
                case GTH:
                    cNode = new constExprNode(b.op1.constant.int_value > b.op2.constant.int_value, null);
                    break;
                case LTH:
                    cNode = new constExprNode(b.op1.constant.int_value < b.op2.constant.int_value, null);
                    break;
                case GEQ:
                    cNode = new constExprNode(b.op1.constant.int_value >= b.op2.constant.int_value, null);
                    break;
                case LEQ:
                    cNode = new constExprNode(b.op1.constant.int_value <= b.op2.constant.int_value, null);
                    break;
                case ADD:
                    cNode = new constExprNode(b.op1.constant.int_value + b.op2.constant.int_value, null);
                    break;
                case SUB:
                    cNode = new constExprNode(b.op1.constant.int_value - b.op2.constant.int_value, null);
                    break;
                case MUL:
                    cNode = new constExprNode(b.op1.constant.int_value * b.op2.constant.int_value, null);
                    break;
                case DIV:
                    if (b.op2.constant.int_value == 0) cNode = new constExprNode(0, null);
                    else cNode = new constExprNode(b.op1.constant.int_value / b.op2.constant.int_value, null);
                    break;
                case MOD:
                    cNode = new constExprNode(b.op1.constant.int_value % b.op2.constant.int_value, null);
                    break;
                case OR:
                    cNode = new constExprNode(b.op1.constant.bool_value || b.op2.constant.bool_value, null);
                    break;
                case BITWISE_OR:
                    cNode = new constExprNode(b.op1.constant.int_value | b.op2.constant.int_value, null);
                    break;
                case AND:
                    cNode = new constExprNode(b.op1.constant.bool_value && b.op2.constant.bool_value, null);
                    break;
                case BITWISE_AND:
                    cNode = new constExprNode(b.op1.constant.int_value & b.op2.constant.int_value, null);
                    break;
                case BITWISE_XOR:
                    cNode = new constExprNode(b.op1.constant.int_value ^ b.op2.constant.int_value, null);
                    break;
                case SLA:
                    cNode = new constExprNode(b.op1.constant.int_value << b.op2.constant.int_value, null);
                    break;
                case SRA:
                    cNode = new constExprNode(b.op1.constant.int_value >> b.op2.constant.int_value, null);
                    break;
            }
            const_ = new entity(cNode);
            id = b.lhs.id;
        }
        if (definedVar.get(id).size() == 1){
//            System.out.println("propagate: " + id + "_" + getEntityString(const_));
            for (Integer blk : index2blk.keySet()){
                for (statement s : index2blk.get(blk).stmts){
                    if (s instanceof binary) {
                        binary b = (binary) s;
                        if (!b.op1.is_constant && b.op1.id.equals(id)) b.op1 = new entity(const_);
                        if (!b.op2.is_constant && b.op2.id.equals(id)) b.op2 = new entity(const_);
                    } else if (s instanceof branch) {
                        branch b = (branch) s;
                        if (!b.flag.is_constant && b.flag.id.equals(id)) b.flag = new entity(const_);
                    } else if (s instanceof ret) {
                        ret r = (ret) s;
                        if (r.value != null && !r.value.is_constant && r.value.id.equals(id)) r.value = new entity(const_);
                    } else if (s instanceof assign) {
                        assign a_ = (assign) s;
                        if (a_.rhs != null && !a_.rhs.is_constant && a_.rhs.id.equals(id)) a_.rhs = new entity(const_);
                    } else if (s instanceof load) {

                    } else if (s instanceof store) {
                        store s_ = (store) s;
                        if (!s_.value.is_constant && s_.value.id.equals(id)) s_.value = new entity(const_);
                    }
                }
            }
            index2blk.get(todo.blk).stmts.remove(todo.def);
            definedVar.get(id).remove(todo);
        }
        Done.add(todo);
        // todo : if propagation is not thorough (most cases in this implementation), statement shouldn't be deleted
    }

    public void updateList(){
        for (String var : definedVar.keySet()){
            for (defineNode d : definedVar.get(var)){
                if (d.def instanceof binary){
                    if (((binary) d.def).op1.is_constant && ((binary) d.def).op2.is_constant) workList.add(d);
                } else if (d.def instanceof assign){
                    if (((assign) d.def).rhs.is_constant) workList.add(d);
                }
            }
            for (defineNode d : Done) workList.remove(d);
        }
    }
}
