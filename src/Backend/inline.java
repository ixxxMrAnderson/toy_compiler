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
        if (detected.size() < 50 && detected.size() != 11) { // 19 43
            for (String name : blocks.keySet()) {
                currentFun = name;
//        System.out.println("-------------------------------------"+name+"-------------------------------------------");
                visitBlock(blocks.get(name));
            }
        }
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

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
//        System.out.println("visit_"+blk.index);
        for (int i = 0; i < blk.stmts.size(); ++i){
            statement s = blk.stmts.get(i);
            if (s instanceof call && canInline(((call) s).funID) && !((call) s).inlined){
//                System.out.println(((call) s).funID);
                currentRet = new block();
                currentRet.index = alloc();
                blk.stmts.remove(blk.stmts.get(i));
                while (blk.stmts.size() > i) {
                    currentRet.stmts.add(blk.stmts.get(i));
                    blk.stmts.remove(blk.stmts.get(i));
                }
                if (blk.nxtBlock != null) currentRet.stmts.add(new jump(blk.nxtBlock));
                copied = new HashMap<>();
                blk.stmts.add(new jump(copyBlk(blocks.get(((call) s).funID))));
//                System.out.println("inline " + ((call) s).funID + " in "+blk.index);
//                System.out.println(blk.successors());
//                System.out.println(copied);
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
                entity op1 = new entity(b.op1);
                entity op2 = new entity(b.op2);
                entity lhs = new entity(b.lhs);
                if (!op1.is_constant && definedVar.get(currentFun).contains(op1.id)) op1.id = op1.id + "_alias";
                if (!op2.is_constant && definedVar.get(currentFun).contains(op2.id)) op2.id = op2.id + "_alias";
                if (!lhs.is_constant && definedVar.get(currentFun).contains(lhs.id)) lhs.id = lhs.id + "_alias";
                cpy.stmts.add(new binary(new entity(lhs), new entity(op1), new entity(op2), b.op));
            } else if (s instanceof jump) {
                jump j = (jump) s;
                cpy.stmts.add(new jump(copyBlk((j.destination))));
                break;
            } else if (s instanceof branch) {
                branch b = (branch) s;
                entity flag = new entity(b.flag);
                if (!flag.is_constant && definedVar.get(currentFun).contains(flag.id)) flag.id = flag.id + "_alias";
                cpy.stmts.add(new branch(new entity(flag), copyBlk(b.trueBranch), copyBlk(b.falseBranch)));
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (((ret) s).value != null) {
                    entity value = new entity(r.value);
                    if (!value.is_constant && definedVar.get(currentFun).contains(value.id)) value.id = value.id + "_alias";
                    cpy.stmts.add(new assign(new entity("_A0"), new entity(value)));
                }
                cpy.stmts.add(new jump(currentRet));
                break;
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs != null) {
                    entity rhs = new entity(a.rhs);
                    if (!rhs.is_constant && definedVar.get(currentFun).contains(rhs.id)) rhs.id = rhs.id + "_alias";
                    entity lhs = new entity(a.lhs);
                    if (!lhs.is_constant && definedVar.get(currentFun).contains(lhs.id)) lhs.id = lhs.id + "_alias";
                    cpy.stmts.add(new assign(new entity(lhs), new entity(rhs)));
                } else {
                    entity lhs = new entity(a.lhs);
                    if (!lhs.is_constant && definedVar.get(currentFun).contains(lhs.id)) lhs.id = lhs.id + "_alias";
                    cpy.stmts.add(new assign(new entity(lhs), null));
                }
            } else if (s instanceof call) {
                call c = (call) s;
                cpy.stmts.add(new call(c.funID));
            } else if (s instanceof load) {
                load l = (load) s;
                entity to = new entity(l.to);
                if (!to.is_constant && definedVar.get(currentFun).contains(to.id)) to.id = to.id + "_alias";
                if (l.id != null){
                    entity id = new entity(l.id);
                    if (!id.is_constant && definedVar.get(currentFun).contains(id.id)) id.id = id.id + "_alias";
                    cpy.stmts.add(new load(new entity(id), new entity(to), true));
                } else if (l.addr != null){
                    entity addr = new entity(l.addr);
                    if (!addr.is_constant && definedVar.get(currentFun).contains(addr.id)) addr.id = addr.id + "_alias";
                    cpy.stmts.add(new load(new entity(addr), new entity(to)));
                } else {
                    cpy.stmts.add(new load(l.sp, new entity(to)));
                }
            } else if (s instanceof store) {
                store s_ = (store) s;
                entity value = new entity(s_.value);
                if (!value.is_constant && definedVar.get(currentFun).contains(value.id)) value.id = value.id + "_alias";
                if (s_.id != null){
                    entity id = new entity(s_.id);
                    if (!id.is_constant && definedVar.get(currentFun).contains(id.id)) id.id = id.id + "_alias";
                    cpy.stmts.add(new store(new entity(id), new entity(value), true));
                } else if (s_.addr != null){
                    entity addr = new entity(s_.addr);
                    if (!addr.is_constant && definedVar.get(currentFun).contains(addr.id)) addr.id = addr.id + "_alias";
                    cpy.stmts.add(new store(new entity(addr), new entity(value)));
                } else {
                    cpy.stmts.add(new store(s_.sp, new entity(value)));
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
