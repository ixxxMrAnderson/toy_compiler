package Backend;

import java.util.HashMap;
import java.util.HashSet;
import MIR.*;

public class inline implements Pass{
    public HashSet<Integer> visited = new HashSet<>();
    public HashSet<Integer> detected = new HashSet<>();
    public String currentFun;
    public HashMap<String, HashSet<String>> callInFun = new HashMap<>();
    public HashMap<String, block> blocks;
    public block currentRet;
    public Integer max = 0;

    public inline(HashMap<String, block> blocks){
        this.blocks = blocks;
        for (String name : blocks.keySet()){
            currentFun = name;
            callInFun.put(name, new HashSet<>());
            detectCall(blocks.get(name));
        }
//        System.out.println(detected);
        for (Integer i : detected){
            if (i != null && i > max) max = i;
        }
        for (String name : blocks.keySet()){
            currentFun = name;
//        System.out.println("-------------------------------------"+name+"-------------------------------------------");
            visitBlock(blocks.get(name));
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
                cpy.stmts.add(new binary(new entity(b.lhs), new entity(b.op1), new entity(b.op2), b.op));
            } else if (s instanceof jump) {
                jump j = (jump) s;
                cpy.stmts.add(new jump(copyBlk((j.destination))));
                break;
            } else if (s instanceof branch) {
                branch b = (branch) s;
                cpy.stmts.add(new branch(new entity(b.flag), copyBlk(b.trueBranch), copyBlk(b.falseBranch)));
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (((ret) s).value != null) {
                    cpy.stmts.add(new assign(new entity("_A0"), new entity(r.value)));
                }
                cpy.stmts.add(new jump(currentRet));
                break;
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs != null) {
                    cpy.stmts.add(new assign(new entity(a.lhs), new entity(a.rhs)));
                } else {
                    cpy.stmts.add(new assign(new entity(a.lhs), null));
                }
            } else if (s instanceof call) {
                call c = (call) s;
                cpy.stmts.add(new call(c.funID));
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.id != null){
                    cpy.stmts.add(new load(new entity(l.id), new entity(l.to), true));
                } else if (l.addr != null){
                    cpy.stmts.add(new load(new entity(l.addr), new entity(l.to)));
                } else {
                    cpy.stmts.add(new load(l.sp, new entity(l.to)));
                }
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.id != null){
                    cpy.stmts.add(new store(new entity(s_.id), new entity(s_.value), true));
                } else if (s_.addr != null){
                    cpy.stmts.add(new store(new entity(s_.addr), new entity(s_.value)));
                } else {
                    cpy.stmts.add(new store(s_.sp, new entity(s_.value)));
                }
            }
        }
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
