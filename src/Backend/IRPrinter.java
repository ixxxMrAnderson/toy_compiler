package Backend;

import MIR.*;
import AST.*;
import Util.Type.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class IRPrinter implements Pass {
    private int blockCnt = 0;
    private HashMap<block, Integer> blockIndex = new HashMap<>();
    private HashSet<String> printed = new HashSet<>();
    public ArrayList<String> regIdentifier = new ArrayList<>();

    public IRPrinter(HashMap<String, block> b) {
        regIdentifier.add("zero"); // 0
        regIdentifier.add("ra");   // 1
        regIdentifier.add("sp");   // 2
        regIdentifier.add("gp");   // 3
        regIdentifier.add("tp");   // 4
        regIdentifier.add("t0");   // 5
        regIdentifier.add("t1");   // 6
        regIdentifier.add("t2");   // 7
        regIdentifier.add("s0");   // 8
        regIdentifier.add("s1");   // 9
        regIdentifier.add("a0");   // 10
        regIdentifier.add("a1");   // 11
        regIdentifier.add("a2");   // 12
        regIdentifier.add("a3");   // 13
        regIdentifier.add("a4");   // 14
        regIdentifier.add("a5");   // 15
        regIdentifier.add("a6");   // 16
        regIdentifier.add("a7");   // 17
        regIdentifier.add("s2");   // 18
        regIdentifier.add("s3");   // 19
        regIdentifier.add("s4");   // 20
        regIdentifier.add("s5");   // 21
        regIdentifier.add("s6");   // 22
        regIdentifier.add("s7");   // 23
        regIdentifier.add("s8");   // 24
        regIdentifier.add("s9");   // 25
        regIdentifier.add("s10");  // 26
        regIdentifier.add("s11");  // 27
        regIdentifier.add("t3");   // 28
        regIdentifier.add("t4");   // 29
        regIdentifier.add("t5");   // 30
        regIdentifier.add("t6");   // 31
        System.out.println("----------------------------IR----------------------------\n");
        for (String name : b.keySet()){
            System.out.println("\n" + name + "_");
            visitBlock(b.get(name));
        }
    }

    public void visitBlock(block blk) {
        if (blk == null || printed.contains(getBlkName(blk))) return;
        else printed.add(getBlkName(blk));
        if (blk != null) {
            System.out.println(getBlkName(blk) + ":");
            for (statement s : blk.stmts){
                System.out.println(blk.stmts.indexOf(s));
                print(s);
            }
            blk.successors().forEach(this::visitBlock);
            if (blk.optAndBlk != null) visitBlock(blk.optAndBlk);
            if (blk.optOrBlk != null) {
//                System.out.println("optOr------------------------------------" + blk.optOrBlk);
                visitBlock(blk.optOrBlk);
            }
        }
    }

    private String getBlkName(block b) {
        return ".B" + (b==null?"null":b.index);
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
        if (e == null) return "null";
        if (e.is_constant){
            if (e.constant.expr_type.type == type.INT){
                return Integer.toString(e.constant.int_value) + "(" + regIdentifier.get(e.reg) + ")";
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
        return e.id + "(" + regIdentifier.get(e.reg) + ")";
    }

    private void print(statement s) {
        if (s instanceof binary) {
            binary b = (binary) s;
            System.out.println("\t" + getEntityString(b.lhs) + " = " +
                    getEntityString(b.op1) + " " + getOpString(b.op) +
                    " " + getEntityString(b.op2) + ";");
        } else if (s instanceof jump) {
            jump j = (jump) s;
            System.out.println("\tj " + getBlkName(j.destination) + ";");
        } else if (s instanceof branch) {
            branch b = (branch) s;
            System.out.println("\tbr " + getEntityString(b.flag) + " " +
                    getBlkName(b.trueBranch) + ", " + getBlkName(b.falseBranch) + ";");
        } else if (s instanceof ret) {
            ret r = (ret) s;
            System.out.println("\tret" + (r.value == null ? "" :
                    (" " + getEntityString(r.value))) + ";");
        } else if (s instanceof assign) {
            assign a = (assign) s;
//            if (a.rhs != null) {
                System.out.println("\t" + getEntityString(a.lhs) +
                        " = " + getEntityString(a.rhs) + ";");
//            }
        } else if (s instanceof call) {
            call c = (call) s;
            System.out.println("\tcall " + c.funID + ";");
//        } else if (s instanceof define) {
//            define d = (define) s;
//            if (d.assign != null) {
//                if (d.toAssign) {
//                    System.out.println("\t" + getEntityString(d.var) +
//                            " = " + getEntityString(d.assign) + ";");
//                } else {
//                    System.out.println("\tdefine " + getEntityString(d.var) + (d.assign == null ? "" :
//                            (", assign " + getEntityString(d.assign))) + ";");
//                }
//            }
        } else if (s instanceof load) {
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
        } else if (s instanceof store) {
            store s_ = (store) s;
            if (s_.addr != null) {
                System.out.println("\tstore " + getEntityString(s_.value) + " in "
                        + getEntityString(s_.addr) + ";");
            } else {
                if (s_.id != null) {
                    System.out.println("\tstore " + getEntityString(s_.value) + " in "
                            + getEntityString(s_.id) + ";");
                } else {
                    System.out.println("\tstore " + getEntityString(s_.value) + " in "
                            + s_.sp + ";");
                }
            }
        } else if (s instanceof phi){
//            phi p = (phi) s;
//            System.out.println("\tphi " + getEntityString(p.born));
//            if (p.varList != null && p.varList.size() > 0) {
//                System.out.println("[");
//                for (entity i : p.varList) {
//                    System.out.println(getEntityString(i) + ", ");
//                }
//                System.out.println("];");
//            }
        }
    }
}
