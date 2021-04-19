package Backend;

import java.util.HashMap;
import java.util.HashSet;
import MIR.*;

public class inline implements Pass{
    public HashSet<Integer> visited = new HashSet<>();
    public HashSet<Integer> detected = new HashSet<>();
    public HashSet<Integer> defined = new HashSet<>();
    public String currentFun;
    public HashMap<String, HashSet<String>> callInFun = new HashMap<>();
    public HashMap<String, block> blocks;
    public block currentRet;
    public HashMap<String, HashSet<String>> definedVar = new HashMap<>();
    public Integer max = 0;
//    public HashMap<String, String> alias = new HashMap<>();

    public inline(HashMap<String, block> blocks){
        this.blocks = blocks;
        for (String name : blocks.keySet()){
            currentFun = name;
            callInFun.put(name, new HashSet<>());
            detectCall(blocks.get(name));
            definedVar.put(name, new HashSet<>());
            defineVar(blocks.get(name));
        }
//        System.out.println(definedVar);
        for (Integer i : detected){
            if (i != null && i > max) max = i;
        }
//        System.out.println("----------------------------------------");
//        System.out.println(detected.size());
//        if (detected.size() < 50 && detected.size() != 11) { // 19 43
            for (String name : blocks.keySet()) {
                currentFun = name;
//        System.out.println("-------------------------------------"+name+"-------------------------------------------");
                visitBlock(blocks.get(name));
            }
//        }
    }

    public void detectCall(block blk){
        if (detected.contains(blk.index)) return;
        else detected.add(blk.index);
        for (statement s : blk.stmts){
            if (s instanceof call){
                callInFun.get(currentFun).add(((call) s).funID);
            }
        }
        for (block b : blk.successors()) detectCall(b);
    }

    public void defineVar(block blk){
        if (defined.contains(blk.index)) return;
        else defined.add(blk.index);
        for (statement s : blk.stmts){
            String def = null;
            if (s instanceof assign){
                if (((assign) s).rhs != null) def = ((assign) s).lhs.id;
            } else if (s instanceof binary){
                def = ((binary) s).lhs.id;
            } else if (s instanceof load){
                def = ((load) s).to.id;
            }
            if (def != null && !def.startsWith("_TMP") && !def.startsWith("@") && !def.startsWith("%")){
                if (!def.equals("_S0") && !def.equals("_SP")){
                    if (!(def.startsWith("_A") && isNumeric(def.substring(2, 3)))){
                        if (!definedVar.get(currentFun).contains(def)) definedVar.get(currentFun).add(def);
                    }
                }
            }
        }
        for (block b : blk.successors()) defineVar(b);
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public entity createAlias(entity e){
        entity e_ = new entity(e);
        if (!e.is_constant) {
            if (!e_.id.startsWith("@") && !e_.id.startsWith("%")){
                if (!e_.id.equals("_S0") && !e_.id.equals("_SP")){
                    if (!(e_.id.startsWith("_A") && isNumeric(e_.id.substring(2, 3)))){
                        e_.id = e_.id + "_alias";
                    }
                }
            }
        }
        return e_;
    }

    public void INLINE(block blk, Integer i){
        statement s = blk.stmts.get(i);
        block toCpy = blocks.get(((call) s).funID);
        if (currentFun.contains("_memberFn_") || ((call) s).funID.contains("_memberFn_")) return;
//        System.out.println("inline " + ((call) s).funID + " in " + currentFun);
        blk.stmts.remove(blk.stmts.get(i)); // remove call
        if (toCpy.successors().size() > 0) {
            currentRet = new block();
            currentRet.index = alloc();
            while (blk.stmts.size() > i) {
                currentRet.stmts.add(blk.stmts.get(i));
                blk.stmts.remove(blk.stmts.get(i));
            }
            if (blk.nxtBlock != null) currentRet.stmts.add(new jump(blk.nxtBlock));
            copied = new HashMap<>();
            blk.stmts.add(new jump(copyBlk(toCpy)));
        } else {
            for (int j = 0; j < toCpy.stmts.size(); ++j) {
//            System.out.println(i);
                statement s_ = toCpy.stmts.get(j);
                if (s_ instanceof binary) {
                    binary b = (binary) s_;
                    blk.stmts.add(i++, new binary(createAlias(b.lhs), createAlias(b.op1), createAlias(b.op2), b.op));
                } else if (s_ instanceof ret) {
                    ret r = (ret) s_;
                    if (r.value != null) {
                        blk.stmts.add(i++, new assign(new entity("_A0"), createAlias(r.value)));
                    }
                    break;
                } else if (s_ instanceof assign) {
                    assign a = (assign) s_;
                    if (a.rhs != null) {
                        blk.stmts.add(i++, new assign(createAlias(a.lhs), createAlias(a.rhs)));
                    } else {
                        blk.stmts.add(i++, new assign(createAlias(a.lhs), null));
                    }
                } else if (s_ instanceof call) {
                    call c = (call) s_;
                    blk.stmts.add(i++, new call(c.funID));
                    if (canInline(c.funID)) INLINE(blk, i - 1);
                } else if (s_ instanceof load) {
                    load l = (load) s_;
                    if (l.id != null) {
                        blk.stmts.add(i++, new load(createAlias(l.id), createAlias(l.to), true));
                    } else if (l.addr != null) {
                        blk.stmts.add(i++, new load(createAlias(l.addr), createAlias(l.to)));
                    } else {
                        blk.stmts.add(i++, new load(l.sp, createAlias(l.to)));
                    }
                } else if (s_ instanceof store) {
                    store st = (store) s_;
                    if (st.id != null) {
                        blk.stmts.add(i++, new store(createAlias(st.id), createAlias(st.value), true));
                    } else if (st.addr != null) {
                        blk.stmts.add(i++, new store(createAlias(st.addr), createAlias(st.value)));
                    } else {
                        blk.stmts.add(i++, new store(st.sp, createAlias(st.value)));
                    }
                }
            }
        }
    }

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
//        System.out.println("visit_"+blk.index);
        for (int i = 0; i < blk.stmts.size(); ++i){
            statement s = blk.stmts.get(i);
            if (s instanceof call && canInline(((call) s).funID)){
                INLINE(blk, i);
            }
        }
//        System.out.println("RNG_SIZE: " + blocks.get("rng").stmts.size());
        for (block b : blk.successors()) visitBlock(b);
    }
    public HashMap<block, block> copied = new HashMap<>();

    public Integer alloc(){
        return ++max;
    }

    public block copyBlk(block blk){
        if (blk == null) return null;
//        System.out.println("in_cpy");
        if (copied.containsKey(blk)) return copied.get(blk);
        block cpy = new block();
        copied.put(blk, cpy);
        cpy.index = alloc();
        for (int i = 0; i < blk.stmts.size(); ++i){
//            System.out.println(i);
            statement s = blk.stmts.get(i);
            if (s instanceof binary) {
                binary b = (binary) s;
                cpy.stmts.add(new binary(createAlias(b.lhs), createAlias(b.op1), createAlias(b.op2), b.op));
            } else if (s instanceof jump) {
                jump j = (jump) s;
                cpy.stmts.add(new jump(copyBlk((j.destination))));
                break;
            } else if (s instanceof branch) {
                branch b = (branch) s;
                cpy.stmts.add(new branch(createAlias(b.flag), copyBlk(b.trueBranch), copyBlk(b.falseBranch)));
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (((ret) s).value != null) {
                    cpy.stmts.add(new assign(new entity("_A0"), createAlias(r.value)));
                }
                cpy.stmts.add(new jump(currentRet));
                break;
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs != null) {
                    cpy.stmts.add(new assign(createAlias(a.lhs), createAlias(a.rhs)));
                } else {
                    cpy.stmts.add(new assign(createAlias(a.lhs), null));
                }
            } else if (s instanceof call) {
                call c = (call) s;
                cpy.stmts.add(new call(c.funID));
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.id != null){
                    cpy.stmts.add(new load(createAlias(l.id), createAlias(l.to), true));
                } else if (l.addr != null){
                    cpy.stmts.add(new load(createAlias(l.addr), createAlias(l.to)));
                } else {
                    cpy.stmts.add(new load(l.sp, createAlias(l.to)));
                }
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.id != null){
                    cpy.stmts.add(new store(createAlias(s_.id), createAlias(s_.value), true));
                } else if (s_.addr != null){
                    cpy.stmts.add(new store(createAlias(s_.addr), createAlias(s_.value)));
                } else {
                    cpy.stmts.add(new store(s_.sp, createAlias(s_.value)));
                }
            }
        }
        if (blk.nxtBlock != null) cpy.nxtBlock = copyBlk(blk.nxtBlock);
        if (blk.optOrBlk != null) cpy.optOrBlk = copyBlk(blk.optOrBlk);
        if (blk.optAndBlk != null) cpy.optOrBlk = copyBlk(blk.optAndBlk);
        return cpy;
    }

    public HashSet<String> path = new HashSet<>();
    public boolean canInline(String id) {
        if (!blocks.containsKey(id)) return false;
        path = new HashSet<>();
        return !walk(id, id);
    }

    public boolean walk(String from, String to){
        path.add(from);
        if (callInFun.containsKey(from)) {
            for (String des : callInFun.get(from)) {
                if (des.equals(to)) return true;
                else if (!path.contains(des)) {
                    if (walk(des, to)) return true;
                }
            }
        }
        return false;
    }
}
