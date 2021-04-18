package Backend;

import MIR.*;
import Util.Type.type;

import java.util.HashMap;
import java.util.HashSet;

public class RegAlloc implements Pass{

    public HashMap<String, Integer> id2reg = new HashMap<>();
    public HashMap<Integer, String> reg2id = new HashMap<>();
    public Integer sp = 0, currentIndex = 0;
    public block currentBlk;
    public HashMap<String, Integer> currentStack = new HashMap<>();
    public HashSet<block> allocated = new HashSet<>();

    public RegAlloc(HashMap<String, block> blocks, HashMap<String, Integer> stackAlloc){
        for (int i = 0; i < 32; ++i) reg2id.put(i, null);
        for (String i : blocks.keySet()){
            if (i.equals("_VAR_DEF")) continue;
            visitBlock(blocks.get(i));
            stackAlloc.put(i, sp);
            currentStack = new HashMap<>();
            sp = 0;
        }
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    @Override
    public void visitBlock(block blk) {
        if (allocated.contains(blk)) return;
        else allocated.add(blk);
        currentBlk = blk;
//        System.out.println("------------------------------");
//        System.out.println(id2reg);
        if (blk != null) {
            for (String id : id2reg.keySet()){
                reg2id.put(id2reg.get(id), null);
            }
            id2reg = new HashMap<>();
            for (currentIndex = 0; currentIndex < blk.stmts.size(); ++currentIndex) {
                upload();
                statement s = blk.stmts.get(currentIndex);
//                System.out.println("----------" + s + "----------");
//                System.out.println(id2reg);
                if (s instanceof binary) {
                    binary b = (binary) s;
                    allocReg(b.op1);
                    allocReg(b.op2);
                    allocReg(b.lhs, true);
                } else if (s instanceof jump) {

                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    allocReg(b.flag);
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null && !r.value.is_constant) allocReg(r.value);
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (a.rhs != null && !a.rhs.is_constant) allocReg(a.rhs);
                    if (!a.lhs.is_constant && a.rhs != null) {
                        if (a.lhs.id.startsWith("_TMP") || a.lhs.id.startsWith("_A")) {
                            allocReg(a.lhs, true);
                        } else {
                            if (!currentStack.containsKey(a.lhs.id)) {
                                sp += 4;
                                currentStack.put(a.lhs.id, sp);
                            }
                            a.lhs.reg = a.rhs.reg;
                            currentBlk.stmts.add(currentIndex++, new store(currentStack.get(a.lhs.id), new entity(a.rhs)));
                        }
                    }
                } else if (s instanceof call) { // clear the table (no caller safe)
                    for (String id : id2reg.keySet()){
                        if (isNumeric(id)) continue;
                        entity assign = new entity();
                        assign.reg = id2reg.get(id);
                        if (!currentStack.containsKey(id)){
                            sp += 4;
                            currentStack.put(id, sp);
                            currentBlk.stmts.add(currentIndex++, new store(sp, new entity(assign)));
                        }
                        reg2id.put(id2reg.get(id), null);
                    }
                    id2reg = new HashMap<>();
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (l.addr != null) allocReg(l.addr);
                    allocReg(l.to, true);
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    if (s_.addr != null) allocReg(s_.addr, true);
                    if (!s_.value.is_constant) allocReg(s_.value);
                }
            }
            blk.successors().forEach(this::visitBlock);
        }
    }

    public void allocReg(entity var){
        allocReg(var, false);
    }

    // a0-a7 (10-17);
    // s0-s1 (8-9), s2-s11 (18-27);
    // t0-t2 (5-7), t3-t6 (28-31)
    public void allocReg(entity var, boolean flag){ // todo: much more complicated than this...
        if (var.id.startsWith("_A")) {
            var.reg = 10 + Integer.parseInt(var.id.substring(2, 3));
            return;
        }
        if (var.id.equals("_SP")){
            var.reg = 2;
            return;
        }
        if (var.id.equals("_S0")){
            var.reg = 8;
            return;
        }
        if (var.is_constant) {
            if (var.constant.expr_type.type == type.INT){
                var.id = String.valueOf(var.constant.int_value);
            } else if (var.constant.expr_type.type == type.NULL){
                var.id = "0";
            } else if (var.constant.bool_value == true){
                var.id = "1";
            } else {
                var.id = "0";
            }
        }
        if (id2reg.containsKey(var.id)) {
            var.reg = id2reg.get(var.id);
            return;
        }
        for (int i = 5; i < 32; ++i){
            if (i >= 8 && i <= 17) continue;
            if (reg2id.get(i) == null){
                var.reg = i;
                id2reg.put(var.id, i);
                reg2id.put(i, var.id);
                if (currentStack.containsKey(var.id)){
                    entity to = new entity();
                    to.reg = i;
                    currentBlk.stmts.add(currentIndex, new load(currentStack.get(var.id), new entity(to)));
                    currentIndex += 1;
                }
                return;
            }
        }
        // spill t0
        entity assign = new entity();
        assign.reg = 5;
        if (!currentStack.containsKey(reg2id.get(5))){
            sp += 4;
            currentStack.put(reg2id.get(5), sp);
        }
        currentBlk.stmts.add(currentIndex++, new store(currentStack.get(reg2id.get(5)), new entity(assign)));
        var.reg = 5;
        id2reg.remove(reg2id.get(5));
        id2reg.put(var.id, 5);
        reg2id.put(5, var.id);
        if (currentStack.containsKey(var.id) && !flag){
            entity to = new entity();
            to.reg = 5;
            currentBlk.stmts.add(currentIndex, new load(currentStack.get(var.id), new entity(to)));
            currentIndex += 1;
        }
        return;
    }

    public void upload(){
        for (int i = 5; i < 32; ++i){
            if (i >= 8 && i <= 17) continue;
            if (reg2id.get(i) != null){
                boolean flag = false;
                for (int j = currentIndex; j < currentBlk.stmts.size(); ++j){
                    statement s = currentBlk.stmts.get(j);
                    if (s instanceof binary) {
                        binary b = (binary) s;
                        if (b.op1.id != null && b.op1.id.equals(reg2id.get(i))) flag = true;
                        if (b.op2.id != null && b.op2.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof branch) {
                        branch b = (branch) s;
                        if (b.flag.id != null && b.flag.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof ret) {
                        ret r = (ret) s;
                        if (r.value != null && r.value.id != null && r.value.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof assign) {
                        assign a = (assign) s;
                        if (a.rhs.id != null && a.rhs.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof load) {
                        load l = (load) s;
                        if (l.addr != null && l.addr.id != null && l.addr.id.equals(reg2id.get(i))) flag = true;
                        if (l.id != null && l.id.id != null && l.id.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof store) {
                        store s_ = (store) s;
                        if (s_.addr != null && s_.addr.id != null && s_.addr.id.equals(reg2id.get(i))) flag = true;
                        if (s_.id != null && s_.id.id != null && s_.id.id.equals(reg2id.get(i))) flag = true;
                        if (s_.value.id != null && s_.value.id.equals(reg2id.get(i))) flag = true;
                    }
                    if (flag) break;
                }
                if (!flag){
                    id2reg.remove(reg2id.get(i));
                    reg2id.put(i, null);
                }
            }
        }
    }
}
