package Backend;

import MIR.*;
import Util.Type.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AsmPrinter implements Pass{
    private int blockCnt = 0, sbssCnt = 0;
    private HashMap<block, Integer> blockIndex = new HashMap<>();
    private HashSet<String> printed = new HashSet<>();
    private ArrayList<define> roList = new ArrayList<>();
    private HashMap<String, Integer> sbssIndex = new HashMap<>();
    public String currentFun;
    public HashMap<String, HashMap<String, Integer>> stackAlloc;
    public HashMap<String, Integer> spillPara;

    public AsmPrinter(HashMap<String, block> b, HashMap<String
            , HashMap<String, Integer>> stackAlloc, HashMap<String, Integer> spillPara){
        System.out.println("----------------------------ASM----------------------------\n");
        this.stackAlloc = stackAlloc;
        this.spillPara = spillPara;
        handleGlobl(b.get("_VAR_DEF"));
        for (String name : b.keySet()){
            if (name.equals("_VAR_DEF")) continue;
            System.out.println("\t.text");
            System.out.println("\t.align\t2");
            System.out.println("\t.globl\t" + name);
            System.out.println("\t.type\t" + name + ", @function");
            System.out.println(name + ":");
            currentFun = name;
            visitBlock(b.get(name));
            visitBlock(b.get(name).tailBlk);
        }
    }

    public void visitBlock(block blk) {
        if (printed.contains(getBlockName(blk))) return;
        else printed.add(getBlockName(blk));
        if (blk != null) {
            if (blk.tailBlk == null){
                System.out.println(getBlockName(blk) + ":");
            } else {;
                Integer sp = spillPara.get(currentFun) * 4 + stackAlloc.size() * 4 + 8;
                System.out.println("\taddi\tsp,sp,-" + sp);
                System.out.println("\tsw\ts0," + (sp - 4) + "(sp)");
                System.out.println("\tsw\tra," + (sp - 8) + "(sp)");
                System.out.println("\taddi\ts0,sp," + sp);
            }
            for (statement s : blk.stmts()) {
                if (s instanceof binary) {
                    printBinaryIns((binary) s);
                } else if (s instanceof jump) {
                    jump j = (jump) s;
                    if (j.destination == null){
                        Integer sp = spillPara.get(currentFun) * 4 + stackAlloc.size() * 4 + 8;
                        System.out.println("\tlw\ts0," + (sp - 4) + "(sp)");
                        System.out.println("\tlw\tra," + (sp - 8) + "(sp)");
                        System.out.println("\taddi\tsp,sp," + sp);
                        System.out.println("\tjr\tra");
                        System.out.println("\t.size\t" + currentFun + ", .-" + currentFun);
                    } else {
                        System.out.println("\tj\t" + getBlockName(j.destination));
                    }
                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    if (b.trueBranch == null){
                        System.out.println("\tbeq\t" + getEntityString(b.flag) + ",zero,"
                                + getBlockName(b.falseBranch));
                    } else {
                        System.out.println("\tbeq\t" + getEntityString(b.flag) + ",zero,"
                                + getBlockName(b.falseBranch));
                        System.out.println("\tj\t" + getBlockName(b.trueBranch));
                    }
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null) {
                        if (r.value.is_constant){
                            System.out.println("\tli\ta0," + getEntityString(r.value));
                        } else {
                            System.out.println("\tmv\ta0," + getEntityString(r.value));
                        }
                    }
                    System.out.println("\tj\t" + getBlockName(r.retBlk));
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (a.rhs.is_constant){
                        System.out.println("\tli\t" + getEntityString(a.lhs) + ","
                                + getEntityString(a.rhs));
                    } else {
                        System.out.println("\tmv\t" + getEntityString(a.lhs) + ","
                                + getEntityString(a.rhs));
                    }
                } else if (s instanceof call) {
                    call c = (call) s;
                    System.out.println("\tcall\t" + c.funID);
                } else if (s instanceof define) {
                    define d = (define) s;
                    if (d.assign != null) {
                        if (getEntityString(d.var).startsWith("@")) {
                            System.out.println("\tlui\t" + getEntityString(d.var) + ",%hi(.G"
                                    + sbssIndex.get(getEntityString(d.var)) + ")");
                            System.out.println("\tsw\t" + getEntityString(d.assign) + ",%lo(.G"
                                    + sbssIndex.get(getEntityString(d.var)) + ")(" + getEntityString(d.var) + ")");
                        } else {
                            System.out.println("\tsw\t" + getEntityString(d.assign) + ",-"
                                    + (8 + stackAlloc.get(currentFun).get(getEntityString(d.var))) + "(s0)");
                        }
                    }
                } else if (s instanceof getPtr) {
                    getPtr g = (getPtr) s;
                    if (g.id.startsWith("@")) continue;
                    System.out.println("\taddi\t" + getEntityString(g.ret) + ",s0,-"
                            + (8 + stackAlloc.get(currentFun).get(g.id)));
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (getEntityString(l.addr).startsWith("@")){
                        System.out.println("\tlui\t" + getEntityString(l.to) + ",%hi(.G"
                                + sbssIndex.get(getEntityString(l.addr)) + ")");
                        System.out.println("\tlw\t" + getEntityString(l.to) + ",%lo(.G"
                                + sbssIndex.get(getEntityString(l.addr)) + ")(" + getEntityString(l.to) + ")");
                    } else if (getEntityString(l.addr).startsWith("%")){
                        Integer index = 0;
                        for (int i = 0; i < roList.size(); ++i){
                            if (roList.get(i).equals(getEntityString(l.addr))){
                                index = i;
                                break;
                            }
                        }
                        System.out.println("\tlui\t" + getEntityString(l.to) + ",%hi(.S" + index + ")");
                        System.out.println("\taddi\t" + getEntityString(l.to) + "," + getEntityString(l.to)
                                + ".%lo(.S" + index + ")");
                    } else {
                        System.out.println("\tlw\t" + getEntityString(l.to) + ",0(" + getEntityString(l.addr) + ")");
                    }
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    // todo : handle store constant
                    if (getEntityString(s_.addr).startsWith("@")){
                        System.out.println("\tlui\t" + getEntityString(s_.addr) + ",%hi(.G"
                                + sbssIndex.get(getEntityString(s_.addr)) + ")");
                        System.out.println("\tsw\t" + getEntityString(s_.value) + ",%lo(.G"
                                + sbssIndex.get(getEntityString(s_.addr)) + ")(" + getEntityString(s_.addr) + ")");
                    } else {
                        System.out.println("\tsw\t" + getEntityString(s_.value) + ",0("
                                + getEntityString(s_.addr) + ")");
                    }

                }
            }
            blk.successors().forEach(this::visitBlock);
        }
    }

    private void handleGlobl(block globlDef){
        for (statement i : globlDef.stmts()){
            if (((define) i).assign != null){
                roList.add((define) i);
            } else {
                sbssIndex.put(((define) i).var.id, sbssCnt++);
            }
        }
        System.out.println("\t.text");
        System.out.println("\t.section\t.rodata");
        for (int i = 0; i < roList.size(); ++i){
            System.out.println("\t.align\t2");
            System.out.println(".S" + i + ":");
            System.out.println("\t.string\t" + getEntityString(roList.get(i).assign));
        }
        System.out.println("\t.text");
        System.out.println("\t.section\t.sbss,\"aw\",@nobits");
        for (String i : sbssIndex.keySet()) {
            String name = ".G" + sbssIndex.get(i);
            System.out.println("\t.globl\t" + name);
            System.out.println("\t.align\t2");
            System.out.println("\t.type\t" + name + ", @object");
            System.out.println("\t.size\t" + name + ", 4");
            System.out.println(name + ":");
            System.out.println("\t.zero\t4");
        }
    }

    private void printBinaryIns(binary binaryIns){
        entity lop = new entity();
        entity rop = new entity();
        if (binaryIns.op1.is_constant){
            System.out.println("\tli\t" + getEntityString(lop) + "," + getEntityString(binaryIns.op1));
        } else {
            lop = new entity(binaryIns.op1);
        }
        if (binaryIns.op2.is_constant){
            System.out.println("\tli\t" + getEntityString(rop) + "," + getEntityString(binaryIns.op2));
        } else {
            rop = new entity(binaryIns.op2);
        }
        switch (binaryIns.op) {
            case NEQ:
                System.out.println(
                        "\tsub\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                System.out.println(
                        "\tsnez\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop)
                );
                break;
            case EQ:
                System.out.println(
                        "\tsub\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                System.out.println(
                        "\tsqez\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop)
                );
                break;
            case GTH:
                System.out.println(
                        "\tsgt\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case LTH:
                System.out.println(
                        "\tslt\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case GEQ:
                System.out.println(
                        "\tslt\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                System.out.println(
                        "\txori\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(binaryIns.lhs) + "," + getEntityString(lop)
                );
                break;
            case LEQ:
                System.out.println(
                        "\tsgt\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                System.out.println(
                        "\txori\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(binaryIns.lhs) + "," + getEntityString(lop)
                );
                break;
            case ADD:
                System.out.println(
                        "\tadd\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case SUB:
                System.out.println(
                        "\tsub\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case MUL:
                System.out.println(
                        "\tmul\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case DIV:
                System.out.println(
                        "\tdiv\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case MOD:
                System.out.println(
                        "\trem\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case OR: case BITWISE_OR:
                System.out.println(
                        "\tor\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case AND: case BITWISE_AND:
                System.out.println(
                        "\tand\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case BITWISE_XOR:
                System.out.println(
                        "\txor\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case SLA:
                System.out.println(
                        "\tsll\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
            case SRA:
                System.out.println(
                        "\tsra\t" + getEntityString(binaryIns.lhs) + "," + getEntityString(lop) + "," + getEntityString(rop)
                );
                break;
        }
    }

    private String getBlockName(block b) {
        if (blockIndex.containsKey(b)) return ".B" + blockIndex.get(b);
        else {
            blockIndex.put(b, blockCnt++);
            return ".B" + (blockCnt - 1);
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
        if (e.id.startsWith("_A")) return "a" + e.id.substring(2, 3);
        if (e.id.equals("_SP")) return "sp";
        if (e.id.equals("_S0")) return "s0";
        return e.id;
    }
}
