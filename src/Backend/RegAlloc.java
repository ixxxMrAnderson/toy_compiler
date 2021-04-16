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
    public HashSet<String> defined = new HashSet<>();

    public RegAlloc(HashMap<String, block> blocks, HashMap<String, HashMap<String, Integer>> stackAlloc){
        for (int i = 0; i < 32; ++i) reg2id.put(i, null);
        for (String i : blocks.keySet()){
            if (i.equals("_VAR_DEF")) continue;
            visitBlock(blocks.get(i));
            stackAlloc.put(i, currentStack);
            currentStack = new HashMap<>();
            sp = 0;
        }
//        System.out.println(stackAlloc);
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
            defined = new HashSet<>();
            for (String id : id2reg.keySet()){
                reg2id.put(id2reg.get(id), null);
//                if (id.startsWith("_TMP")) reg2id.put(id2reg.get(id), null);
            }
            id2reg = new HashMap<>();
//            for (Integer reg : reg2id.keySet()){
//                if (reg2id.get(reg) != null) {
//                    id2reg.put(reg2id.get(reg), reg);
//                }
//            }
//            System.out.println("index: " + blk.index);
            for (currentIndex = 0; currentIndex < blk.stmts.size(); ++currentIndex) {
                if (blk.stmts.size() > 10000){
                    break;
                } else {
//                    System.out.println(blk.stmts.size());
                }
//                System.out.println(blk.stmts.get(currentIndex));
                upload();
                statement s = blk.stmts.get(currentIndex);
                if (s instanceof binary) {
                    binary b = (binary) s;
                    allocReg(b.op1);
                    allocReg(b.op2);
                    allocReg(b.lhs, true);
                } else if (s instanceof jump) {
                    clear();
                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    allocReg(b.flag);
                    if (currentIndex == blk.stmts.size() - 1) {
                        reg2id.put(id2reg.get(b.flag.id), null);
                        id2reg.remove(b.flag.id);
                        clear();
                    } else {
                        for (String id : id2reg.keySet()){
                            entity assign = new entity();
                            assign.reg = id2reg.get(id);
                            if (id.startsWith("@") && defined.contains(id)) {
                                entity addr = new entity(returnID(id));
                                addr.reg = 17;
                                currentBlk.stmts.add(currentIndex++, new store(new entity(addr), new entity(assign)));
                            } else if (currentStack.containsKey(returnID(id)) && defined.contains(id)) {
                                currentBlk.stmts.add(currentIndex++, new store(new entity(returnID(id)), new entity(assign), true));
                            }
                        }
                    }
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null && !r.value.is_constant) allocReg(r.value);
                    if (r.value != null && id2reg.containsKey(r.value.id)){
                        reg2id.put(id2reg.get(r.value.id), null);
                        id2reg.remove(r.value.id);
                    }
                    for (String id : id2reg.keySet()){
                        if (isNumeric(id)) {
                            reg2id.put(id2reg.get(id), null);
                            continue;
                        }
                        entity assign = new entity();
                        assign.reg = id2reg.get(id);
                        if (id.startsWith("@") && defined.contains(id)) {
                            entity addr = new entity(returnID(id));
                            addr.reg = 17;
                            currentBlk.stmts.add(currentIndex++, new store(new entity(addr), new entity(assign)));
                        }
                        reg2id.put(id2reg.get(id), null);
                    }
                    defined = new HashSet<>();
                    id2reg = new HashMap<>();
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (!a.lhs.is_constant) allocReg(a.lhs, true);
                    if (!a.rhs.is_constant) allocReg(a.rhs);
                } else if (s instanceof call) { // clear the table (no caller safe)
                    clear(true);
                } else if (s instanceof define) {
                    define d = (define) s;
                    String id = returnID(d.var.id);
//                    System.out.println("REG----------define----------" + d.var.id + "--------" + currentStack);
                    if (!id.startsWith("@") && !id.startsWith("%") && !currentStack.containsKey(returnID(id))){
                        sp += 4;
                        currentStack.put(returnID(id), sp);
                    }
                    if (d.assign != null) {
                        defined.add(d.var.id);
                        allocReg(d.assign);
                        allocReg(d.var, true);
                        d.toAssign = true;
                        if (returnID(d.var.id).equals(returnID(d.assign.id))) {
                            reg2id.put(id2reg.get(d.assign.id), null);
                            id2reg.remove(d.assign.id);
                        }
//                        id2reg.put(d.var.id, d.assign.reg);
                    }
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (l.addr != null && !l.addr.is_constant) allocReg(l.addr);
                    if (!l.to.is_constant) allocReg(l.to, true);
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    if (s_.addr != null && !s_.addr.is_constant){
                        if (!s_.addr.id.startsWith("@")) allocReg(s_.addr);
                        else allocReg(s_.addr, true);
                    }
                    if (!s_.value.is_constant) {
                        allocReg(s_.value);
                    }
                }
            }
            clear();
            blk.successors().forEach(this::visitBlock);
            if (blk.optAndBlk != null) visitBlock(blk.optAndBlk);
            if (blk.optOrBlk != null) visitBlock(blk.optOrBlk);
        }
    }

    public void clear(){
        clear(false);
    }

    public void clear(boolean call_flag){
        for (String id : id2reg.keySet()){
            if (isNumeric(id)) {
                reg2id.put(id2reg.get(id), null);
                continue;
            }
            entity assign = new entity();
            assign.reg = id2reg.get(id);
            if (!currentStack.containsKey(returnID(id)) && !id.startsWith("@")  && !id.startsWith("%") && call_flag) {
                sp += 4;
                currentStack.put(returnID(id), sp);
                currentBlk.stmts.add(currentIndex++, new define(new entity(id), new entity(assign)));
            } else if (id.startsWith("@") && defined.contains(id)) {
                entity addr = new entity(returnID(id));
                addr.reg = 17;
                currentBlk.stmts.add(currentIndex++, new store(new entity(addr), new entity(assign)));
            } else if (currentStack.containsKey(returnID(id)) && defined.contains(id)) {
                currentBlk.stmts.add(currentIndex++, new store(new entity(returnID(id)), new entity(assign), true));
            }
            reg2id.put(id2reg.get(id), null);
        }
        defined = new HashSet<>();
        id2reg = new HashMap<>();
    }

    public String returnID(String id){
        for (int i = 1; i <= id.length(); ++i){
            if (id.substring(0, i).contains("#")) {
                return id.substring(0, i - 1);
            }
        }
        return id;
    }

    public void allocReg(entity var){
        allocReg(var, false);
    }

    // a0-a7 (10-17);
    // s0-s1 (8-9), s2-s11 (18-27);
    // t0-t2 (5-7), t3-t6 (28-31)
    public void allocReg(entity var, boolean flag){ // todo: much more complicated than this...
        // flag: to load on, no need to pre-load
//        System.out.println("allocate: " + var.id);
//        System.out.println(id2reg);

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

        String ret_id = null;
        for (String id : id2reg.keySet()){
            if (returnID(id).equals(returnID(var.id))){
                ret_id = id;
                break;
            }
        }
        if (ret_id != null){
            id2reg.put(var.id, id2reg.get(ret_id));
            reg2id.put(id2reg.get(ret_id), var.id);
            id2reg.remove(ret_id);
            var.reg = id2reg.get(var.id);
            return;
        }

        for (int i = 5; i < 32; ++i){
            if (i >= 8 && i <= 17) continue;
            if (reg2id.get(i) == null){
                var.reg = i;
                id2reg.put(var.id, i);
                reg2id.put(i, var.id);
                if ((currentStack.containsKey(returnID(var.id)) || var.id.startsWith("@")) && !flag){
                    entity to = new entity();
                    to.reg = i;
                    if (var.id.startsWith("@")) {
                        currentBlk.stmts.add(currentIndex++, new load(new entity(var.id), new entity(to)));
                    } else {
                        currentBlk.stmts.add(currentIndex++, new load(new entity(var.id), new entity(to), true));
                    }
                }
                return;
            }
        }

        // spill t0
        entity assign = new entity();
        assign.reg = 5;
        if (!currentStack.containsKey(returnID(reg2id.get(5)))){
            sp += 4;
            currentStack.put(returnID(reg2id.get(5)), sp);
        }
        currentBlk.stmts.add(currentIndex++, new define(new entity(reg2id.get(5)), new entity(assign)));
        var.reg = 5;
        id2reg.remove(reg2id.get(5));
        id2reg.put(var.id, 5);
        reg2id.put(5, var.id);
        if ((currentStack.containsKey(returnID(var.id)) || var.id.startsWith("@")) && !flag){
            entity to = new entity();
            to.reg = 5;
            if (var.id.startsWith("@")) {
                currentBlk.stmts.add(currentIndex++, new load(new entity(var.id), new entity(to)));
            } else {
                currentBlk.stmts.add(currentIndex++, new load(new entity(var.id), new entity(to), true));
            }
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
                    } else if (s instanceof jump) {

                    } else if (s instanceof branch) {
                        branch b = (branch) s;
                        if (b.flag.id != null && b.flag.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof ret) {
                        ret r = (ret) s;
                        if (r.value != null && r.value.id != null && r.value.id.equals(reg2id.get(i))) flag = true;
                    } else if (s instanceof assign) {
                        assign a = (assign) s;
                        if (a.rhs.id != null && a.rhs.id.equals(reg2id.get(i))) flag = true;
                        if (a.lhs.id != null && a.lhs.id.equals(reg2id.get(i))){
                            flag = false;
                            break;
                        }
                    } else if (s instanceof call) {

                    } else if (s instanceof define) {
                        define d = (define) s;
                        if (d.assign != null && d.assign.id != null && d.assign.id.equals(reg2id.get(i))) flag = true;
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
                if (!flag && (reg2id.get(i).startsWith("_TMP") || isNumeric(reg2id.get(i)))){ // @var store its addr instead of value
//                    System.out.println("remove: "+reg2id.get(i));
                    id2reg.remove(reg2id.get(i));
                    reg2id.put(i, null);
                }
            }
        }
    }
}
