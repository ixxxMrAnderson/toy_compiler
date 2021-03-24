package Backend;

import MIR.*;
import AST.*;
import Util.Type.type;

import java.util.HashMap;
import java.util.HashSet;

public class IRPrinter implements Pass {
    private int blockCnt = 0;
    private HashMap<block, Integer> blockIndex = new HashMap<>();
    private HashSet<String> printed = new HashSet<>();

    public IRPrinter(HashMap<String, block> b) {
        System.out.println("----------------------------IR----------------------------\n");
        for (String name : b.keySet()){
            System.out.println("\n" + name + "_");
            visitBlock(b.get(name));
        }
    }

    public void visitBlock(block blk) {
        if (printed.contains(getBlockName(blk))) return;
        else printed.add(getBlockName(blk));
        if (blk != null) {
            System.out.println(getBlockName(blk) + ":");
            blk.stmts().forEach(this::print);
            blk.successors().forEach(this::visitBlock);
        }
    }

    private String getBlockName(block b) {
        if (blockIndex.containsKey(b)) return ".B" + blockIndex.get(b);
        else {
            blockIndex.put(b, blockCnt++);
            return ".B" + (blockCnt - 1);
        }
    }

    private String getOpString(binaryExprNode.Op op) {
        if (op == binaryExprNode.Op.ADD) return "+";
        else if (op == binaryExprNode.Op.SUB) return "-";
        else if (op == binaryExprNode.Op.EQ) return "==";
        else if (op == binaryExprNode.Op.AND) return "&&";
        else if (op == binaryExprNode.Op.OR) return "||";
        else if (op == binaryExprNode.Op.BITWISE_AND) return "&";
        else if (op == binaryExprNode.Op.BITWISE_OR) return "|";
        else if (op == binaryExprNode.Op.BITWISE_XOR) return "^";
        else if (op == binaryExprNode.Op.MUL) return "*";
        else if (op == binaryExprNode.Op.DIV) return "/";
        else if (op == binaryExprNode.Op.MOD) return "%";
        else if (op == binaryExprNode.Op.SLA) return "<<";
        else if (op == binaryExprNode.Op.SRA) return ">>";
        else if (op == binaryExprNode.Op.GTH) return ">";
        else if (op == binaryExprNode.Op.LTH) return "<";
        else if (op == binaryExprNode.Op.GEQ) return ">=";
        else if (op == binaryExprNode.Op.LEQ) return "<=";
        else return "!=";
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
        return e.id;
    }

    private void print(statement s) {
        if (s instanceof binary) {
            binary b = (binary) s;
            System.out.println("\t" + getEntityString(b.lhs) + " = " +
                    getEntityString(b.op1) + " " + getOpString(b.op) +
                    " " + getEntityString(b.op2) + ";");
        } else if (s instanceof jump) {
            jump j = (jump) s;
            System.out.println("\tj " + getBlockName(j.destination) + ";");
        } else if (s instanceof branch) {
            branch b = (branch) s;
            System.out.println("\tbr " + getEntityString(b.flag) + " " +
                    getBlockName(b.trueBranch) + ", " + getBlockName(b.falseBranch) + ";");
        } else if (s instanceof ret) {
            ret r = (ret) s;
            System.out.println("\tret" + (r.value == null ? "" :
                    (" " + getEntityString(r.value))) + ";");
        } else if (s instanceof assign) {
            assign a = (assign) s;
            System.out.println("\t" + getEntityString(a.lhs) +
                    " = " + getEntityString(a.rhs) + ";");
        } else if (s instanceof call) {
            call c = (call) s;
            System.out.println("\tcall " + c.funID + ";");
        } else if (s instanceof define) {
            define d = (define) s;
            System.out.println("\tdefine " + getEntityString(d.var) + (d.assign == null ? "" :
                    (", assign " + getEntityString(d.assign)))+ ";");
        } else if (s instanceof getPtr) {
            getPtr g = (getPtr) s;
            System.out.println("\tgetPtr " + g.id + getEntityString(g.ret) + ";");
        } else if (s instanceof load) {
            load l = (load) s;
            System.out.println("\tload " + getEntityString(l.addr) + " on " + getEntityString(l.to) + ";");
        } else if (s instanceof store) {
            store s_ = (store) s;
            System.out.println("\tstore " + getEntityString(s_.value) + " in "
                    + getEntityString(s_.addr) + ";");
        }
    }
}
