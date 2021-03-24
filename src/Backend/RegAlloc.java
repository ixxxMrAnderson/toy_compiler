package Backend;

import MIR.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RegAlloc implements Pass{

    public HashMap<String, Integer> id2reg = new HashMap<>();
    public Integer regCnt = 10, sp = 0;
    public HashMap<String, Integer> currentStack = new HashMap<>();
    public ArrayList<String> regIdentifier = new ArrayList<>();
    public HashSet<block> allocated = new HashSet<>();

    public RegAlloc(HashMap<String, block> blocks, HashMap<String, HashMap<String, Integer>> stackAlloc){

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


        for (String i : blocks.keySet()){
            if (i.equals("_VAR_DEF")) continue;
            visitBlock(blocks.get(i));
            stackAlloc.put(i, currentStack);
            currentStack = new HashMap<>();
            sp = 0;
        }
    }

    @Override
    public void visitBlock(block blk) {
        if (allocated.contains(blk)) return;
        else allocated.add(blk);
        if (blk != null) {
            // todo: allocate register while forming stackAlloc
            // todo: how to insert define stmt?
            for (int i = 0; i < blk.stmts().size(); ++i) {
                statement s = blk.stmts().get(i);
                if (s instanceof binary) {
                    binary b = (binary) s;
                    if (!b.op1.is_constant) b.op1.reg = getReg(b.op1.id);
                    if (!b.op2.is_constant) b.op2.reg = getReg(b.op2.id);
                    if (!b.lhs.is_constant) b.lhs.reg = getReg(b.lhs.id);
                } else if (s instanceof jump) {

                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    if (!b.flag.is_constant) b.flag.reg = getReg(b.flag.id);
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null) r.value.reg = getReg(r.value.id);
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (!a.lhs.is_constant) a.lhs.reg = getReg(a.lhs.id);
                } else if (s instanceof call) {

                } else if (s instanceof define) {
                    define d = (define) s;
                    if (!d.var.id.startsWith("@") && !d.var.id.startsWith("%")){
                        sp += 4;
                        currentStack.put(d.var.id, sp);
                    }
                    if (!d.var.is_constant) d.var.reg = getReg(d.var.id);
                    if (!d.assign.is_constant) d.assign.reg = getReg(d.assign.id);
                } else if (s instanceof getPtr) {
                    getPtr g = (getPtr) s;
                    if (!g.ret.is_constant) g.ret.reg = getReg(g.ret.id);
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (!l.addr.is_constant) l.addr.reg = getReg(l.addr.id);
                    if (!l.to.is_constant) l.to.reg = getReg(l.to.id);
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    if (!s_.addr.is_constant) s_.addr.reg = getReg(s_.addr.id);
                    if (!s_.value.is_constant) s_.value.reg = getReg(s_.value.id);
                }
            }
            blk.successors().forEach(this::visitBlock);
        }
    }

    public Integer getReg(String id){ // todo: much more complicated than this...
        if (id2reg.containsKey(id)) return id2reg.get(id);
        id2reg.put(id, regCnt++);
        return regCnt - 1;
        // todo: spill?
        // if spill, insert store, free register
        // load defined variable everytime
    }
}
