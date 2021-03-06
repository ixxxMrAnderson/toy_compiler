package Backend;

import AST.binaryExprNode;
import MIR.*;
import Util.Type.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AsmPrinter implements Pass{
    private int blockCnt = 0, sbssCnt = 0;
    private HashMap<block, Integer> blockIndex = new HashMap<>();
    private HashSet<String> printed = new HashSet<>();
    private ArrayList<assign> roList = new ArrayList<>();
    private HashMap<String, Integer> sbssIndex = new HashMap<>();
    public ArrayList<String> regIdentifier = new ArrayList<>();
    public String currentFun;
    public HashMap<String, Integer> stackAlloc;
    public HashMap<String, Integer> spillPara;
    public HashMap<String, block> blocks;
    public HashMap<block, Boolean> blk2call = new HashMap<>();

    public AsmPrinter(HashMap<String, block> b, HashMap<String
            , Integer> stackAlloc, HashMap<String, Integer> spillPara){
        blocks = b;
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

//        System.out.println("------------------stack---------------" + stackAlloc);
//        System.out.println("----------------------------ASM----------------------------\n");
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
            } else {
                Integer sp = spillPara.get(currentFun) * 4 + stackAlloc.get(currentFun) + 12;
                    System.out.println("\taddi\tsp,sp,-" + sp);
                    System.out.println("\tsw\ts0," + (sp - 4) + "(sp)");
                    if (callIn(blocks.get(currentFun))) {
                        System.out.println("\tsw\tra," + (sp - 8) + "(sp)");
                    }
                    System.out.println("\taddi\ts0,sp," + sp);
            }
            for (int i = 0; i < blk.stmts.size(); ++i) {
                statement s = blk.stmts.get(i);
                if (s instanceof binary) {
                    printBinaryIns((binary) s);
                } else if (s instanceof jump) {
                    jump j = (jump) s;
                    if (j.destination == null){
                        Integer sp = spillPara.get(currentFun) * 4 + stackAlloc.get(currentFun) + 12;
                        System.out.println("\tlw\ts0," + (sp - 4) + "(sp)");
                        if (callIn(blocks.get(currentFun))) {
                            System.out.println("\tlw\tra," + (sp - 8) + "(sp)");
                        }
                        System.out.println("\taddi\tsp,sp," + sp);
                        System.out.println("\tjr\tra");
                        System.out.println("\t.size\t" + currentFun + ", .-" + currentFun);
                    } else {
                        System.out.println("\tj\t" + getBlockName(j.destination));
                    }
                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    if (b.flag.is_constant){
                        System.out.println("\tli\ta7," + getReg(b.flag));
                        if (b.trueBranch == null){
                            System.out.println("\tbeq\ta7,zero,"
                                    + getBlockName(b.falseBranch));
                        } else if (b.falseBranch == null){
                            System.out.println("\tbne\ta7,zero,"
                                    + getBlockName(b.trueBranch));
                        } else {
                            System.out.println("\tbeq\ta7,zero,"
                                    + getBlockName(b.falseBranch));
                            System.out.println("\tj\t" + getBlockName(b.trueBranch));
                        }
                    } else {
                        if (b.trueBranch == null){
                            System.out.println("\tbeq\t" + getReg(b.flag) + ",zero,"
                                    + getBlockName(b.falseBranch));
                        } else if (b.falseBranch == null){
                            System.out.println("\tbne\t" + getReg(b.flag) + ",zero,"
                                    + getBlockName(b.trueBranch));
                        } else {
                            System.out.println("\tbeq\t" + getReg(b.flag) + ",zero,"
                                    + getBlockName(b.falseBranch));
                            System.out.println("\tj\t" + getBlockName(b.trueBranch));
                        }
                    }
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null) {
                        if (r.value.is_constant){
                            System.out.println("\tli\ta0," + getReg(r.value));
                        } else {
                            System.out.println("\tmv\ta0," + getReg(r.value));
                        }
                    }
                    System.out.println("\tj\t" + getBlockName(r.retBlk));
                    break;
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (a.rhs != null) {
                        if (a.rhs.is_constant) {
                            System.out.println("\tli\t" + getReg(a.lhs) + ","
                                    + getReg(a.rhs));
                        } else if (a.lhs.reg != a.rhs.reg) {
                            if (detectMv(blk, i)) continue;
                            System.out.println("\tmv\t" + getReg(a.lhs) + ","
                                    + getReg(a.rhs));
                        }
                    }
                } else if (s instanceof call) {
                    call c = (call) s;
                    if (!c.inlined) {
                        System.out.println("\tcall\t" + c.funID);
                    }
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (l.addr != null) {
                        System.out.println("\tlw\t" + getReg(l.to) + ",0(" + getReg(l.addr) + ")");
                    } else {
                        if (l.id != null && getEntityString(l.id).startsWith("%")) {
                            Integer index = 0;
                            for (int j = 0; j < roList.size(); ++j) {
                                if (getEntityString(roList.get(j).lhs).equals(getEntityString(l.id))) {
                                    index = j;
                                    break;
                                }
                            }
                            System.out.println("\tlui\t" + getReg(l.to) + ",%hi(.S" + index + ")");
                            System.out.println("\taddi\t" + getReg(l.to) + "," + getReg(l.to)
                                    + ",%lo(.S" + index + ")");
                        } else if (l.id != null && getEntityString(l.id).startsWith("@")) {
                            System.out.println("\tlui\t" + getReg(l.to) + ",%hi(.G"
                                    + sbssIndex.get(getEntityString(l.id)) + ")");
                            System.out.println("\tlw\t" + getReg(l.to) + ",%lo(.G"
                                    + sbssIndex.get(getEntityString(l.id)) + ")(" + getReg(l.to) + ")");
                        } else {
                            System.out.println("\tlw\t" + getReg(l.to) + ",-" + (l.sp + 8) + "(s0)");
                        }
                    }
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    if (s_.value.is_constant) {
                        s_.value.reg = regIdentifier.indexOf("a6");
                        System.out.println("\tli\ta6," + getReg(s_.value));
                    }
                    if (s_.addr != null) {
                            System.out.println("\tsw\t" + regIdentifier.get(s_.value.reg) + ",0("
                                    + getReg(s_.addr) + ")");
                    } else {
                          if (s_.id != null && getEntityString(s_.id).startsWith("@")) {
                                System.out.println("\tlui\t" + "a7,%hi(.G"
                                        + sbssIndex.get(getEntityString(s_.id)) + ")");
                                System.out.println("\tsw\t" + regIdentifier.get(s_.value.reg) + ",%lo(.G"
                                        + sbssIndex.get(getEntityString(s_.id)) + ")(a7)");
                          } else {
                              System.out.println("\tsw\t" + regIdentifier.get(s_.value.reg) + ",-" + (s_.sp + 8) + "(s0)");
                          }
                    }
                }
            }
            blk.successors().forEach(this::visitBlock);
        }
    }

    private boolean detectMv(block blk, int index){
        Integer r = ((assign) blk.stmts.get(index)).rhs.reg;
        Integer l = ((assign) blk.stmts.get(index)).lhs.reg;
        for (int i = index - 1; i >= 0; --i){
            if (blk.stmts.get(i) instanceof assign){
                assign a = (assign) blk.stmts.get(i);
                if (a.rhs == null) continue;
                if (a.rhs.reg == a.lhs.reg) continue;
                if (a.rhs.reg == r && a.lhs.reg == l) return true;
                else break;
            } else break;
        }
        return false;
    }

    public boolean callIn(block blk){
        if (blk2call.containsKey(blk)) return blk2call.get(blk);
        boolean ret = false;
        for (statement s : blk.stmts){
            if (s instanceof call && !((call) s).inlined) ret = true;
        }
        blk2call.put(blk, ret);
        for (block b : blk.successors()){
            if (callIn(b)) ret = true;
        }
        if (ret) blk2call.put(blk, ret);
        return ret;
    }

    private void handleGlobl(block globlDef){
        for (statement i : globlDef.stmts){
            if (((assign) i).rhs != null){
                roList.add((assign) i);
            } else {
                sbssIndex.put(((assign) i).lhs.id, sbssCnt++);
            }
        }
        System.out.println("\t.text");
        System.out.println("\t.section\t.rodata");
        for (int i = 0; i < roList.size(); ++i){
            System.out.println("\t.align\t2");
            System.out.println(".S" + i + ":");
            System.out.println("\t.string\t" + getEntityString(roList.get(i).rhs));
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
        lop.reg = binaryIns.op1.reg;
        entity rop = new entity();
        rop.reg = binaryIns.op2.reg;
        if (!binaryIns.op1.is_constant && binaryIns.op2.is_constant){
            if (binaryIns.op2.constant.int_value < 20) {
//                System.out.println(binaryIns.op);
                if (binaryIns.op == binaryExprNode.Op.ADD) {
                    System.out.println("\taddi\t" + getReg(binaryIns.lhs) + "," + getReg(binaryIns.op1) + "," + getReg(binaryIns.op2));
                    return;
                } else if (binaryIns.op == binaryExprNode.Op.SUB){
                    System.out.println("\taddi\t" + getReg(binaryIns.lhs) + "," + getReg(binaryIns.op1) + ",-" + getReg(binaryIns.op2));
                    return;
                }
            }
        }
        if (binaryIns.op1.is_constant){
            lop.reg = 16;
            System.out.println("\tli\t" + getReg(lop) + "," + getReg(binaryIns.op1));
        } else {
            lop = new entity(binaryIns.op1);
        }
        if (binaryIns.op2.is_constant){
            rop.reg = 17;
            System.out.println("\tli\t" + getReg(rop) + "," + getReg(binaryIns.op2));
        } else {
            rop = new entity(binaryIns.op2);
        }
        switch (binaryIns.op) {
            case NEQ:
                System.out.println(
                        "\tsub\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                System.out.println(
                        "\tsnez\t" + getReg(binaryIns.lhs) + "," + getReg(binaryIns.lhs)
                );
                break;
            case EQ:
                System.out.println(
                        "\tsub\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                System.out.println(
                        "\tseqz\t" + getReg(binaryIns.lhs) + "," + getReg(binaryIns.lhs)
                );
                break;
            case GTH:
                System.out.println(
                        "\tsgt\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case LTH:
                System.out.println(
                        "\tslt\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case GEQ:
                System.out.println(
                        "\tslt\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                System.out.println(
                        "\txori\t" + getReg(binaryIns.lhs) + "," + getReg(binaryIns.lhs) + ",1"
                );
                break;
            case LEQ:
                System.out.println(
                        "\tsgt\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                System.out.println(
                        "\txori\t" + getReg(binaryIns.lhs) + "," + getReg(binaryIns.lhs) + ",1"
                );
                break;
            case ADD:
                System.out.println(
                        "\tadd\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case SUB:
                System.out.println(
                        "\tsub\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case MUL:
                System.out.println(
                        "\tmul\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case DIV:
                System.out.println(
                        "\tdiv\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case MOD:
                System.out.println(
                        "\trem\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case OR: case BITWISE_OR:
                System.out.println(
                        "\tor\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case AND: case BITWISE_AND:
                System.out.println(
                        "\tand\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case BITWISE_XOR:
                System.out.println(
                        "\txor\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case SLA:
                System.out.println(
                        "\tsll\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
            case SRA:
                System.out.println(
                        "\tsra\t" + getReg(binaryIns.lhs) + "," + getReg(lop) + "," + getReg(rop)
                );
                break;
        }
    }

    private String getBlockName(block b) {
//        return ".B" + b.index;
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
        if (e.id != null) {
            if (e.id.startsWith("_A")) return "a" + e.id.substring(2, 3);
            if (e.id.equals("_SP")) return "sp";
            if (e.id.equals("_S0")) return "s0";
        }
        return e.id;
    }

    private String getReg(entity e){
        if (e.is_constant){
            if (e.constant.expr_type.type == type.INT){
                return Integer.toString(e.constant.int_value);
            } else if (e.constant.expr_type.type == type.STRING){
                return e.constant.string_value;
            } else if (e.constant.expr_type.type == type.NULL){
                return "0";
            } else if (e.constant.bool_value == true){
                return "1";
            } else {
                return "0";
            }
        }
        return regIdentifier.get(e.reg);
    }
}
