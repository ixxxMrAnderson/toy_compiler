package Backend;

import java.util.ArrayList;
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
    public String currentInline;
    public Integer max = 0;
    public boolean kill_flag = false;

    public inline(HashMap<String, block> blocks){
        this.blocks = blocks;
        HashSet<String> name_set = new HashSet<>();
        for (String name : blocks.keySet()){
            currentFun = name;
            name_set.add(name);
            callInFun.put(name, new HashSet<>());
            detectCall(blocks.get(name));
        }
        for (Integer i : detected){
            if (i != null && i > max) max = i;
        }
//        System.out.println("----------------------------------------");
//        System.out.println(detected.size());
//        if (detected.size() < 50 && detected.size() != 11) { // 19 43
//            for (String name : blocks.keySet()) {
//                currentFun = name;
//                visitBlock(blocks.get(name));
//            }
//        }
        for (String name : name_set) {
            if (!name.equals("main") && !name.equals("_VAR_DEF")) {
//                System.out.println("inline: "+name);
                currentInline = name;
                visited = new HashSet<>();
                for (String blk : blocks.keySet()) {
                    currentFun = blk;
                    visitBlock(blocks.get(blk));
                }
                new Peephole(blocks);
                if (kill_flag){
                    kill();
                    return;
                }
                if (canInline(name)) blocks.remove(name);
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
                        e_.id = e_.id + "_alias" + cntInline;
                    }
                }
            }
        }
        return e_;
    }


    public HashMap<String, Integer> inlineCnt = new HashMap<>();
    public Integer cntInline = 0;
    public void INLINE(block blk, Integer i){
        cntInline++;
        statement s = blk.stmts.get(i);
        block toCpy = blocks.get(((call) s).funID);
//        System.out.println("wrong inline " + ((call) s).funID + " in " + currentFun);
        if (!currentInline.equals(((call) s).funID)) return;
//        if (!canInline(currentInline) && callInFun.get(currentInline).size() > 1) return;
        if (!inlineCnt.containsKey(currentInline)) inlineCnt.put(currentInline, 1);
        else if (inlineCnt.get(currentInline) > 0 && !canInline(currentInline)) return;
        else inlineCnt.put(currentInline, inlineCnt.get(currentInline) + 1);
        if (inlineCnt.get(currentInline) > 50) kill_flag = true;
        if (kill_flag) return;
//        System.out.println("inline " + ((call) s).funID + " in " + currentFun);
        blk.stmts.remove(blk.stmts.get(i)); // remove call
        if (toCpy.successors().size() > 0) {
//            System.out.println("inline 1 " + ((call) s).funID + " in " + currentFun);
            currentRet = new block();
            currentRet.index = alloc();
            while (blk.stmts.size() > i) {
                currentRet.stmts.add(blk.stmts.get(i));
                blk.stmts.remove(blk.stmts.get(i));
            }
            if (blk.nxtBlock != null) currentRet.stmts.add(new jump(blk.nxtBlock));
            copied = new HashMap<>();
            blk.stmts.add(new jump(copyBlk(toCpy)));
        } else if (toCpy != blk) {
//            System.out.println("inline 2 " + ((call) s).funID + " in " + currentFun);
            for (int j = 0; j < toCpy.stmts.size(); ++j) {
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
//            System.out.println("inline 2 " + ((call) s).funID + " in " + currentFun);
            for (int j = 0; j < blk.stmts.size(); ++j){
                statement s_ = blk.stmts.get(j);
                if (s_ instanceof call && blocks.containsKey(((call) s_).funID)){
                    INLINE(blk, j);
                }
            }
//            System.out.println("inline 2" + ((call) s).funID + " in " + currentFun);
        }
    }

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
        for (int i = 0; i < blk.stmts.size(); ++i){
            statement s = blk.stmts.get(i);
            if (s instanceof call && blocks.containsKey(((call) s).funID)){
                INLINE(blk, i);
            }
        }
        for (block b : blk.successors()) visitBlock(b);
    }
    public HashMap<block, block> copied = new HashMap<>();

    public Integer alloc(){
        return ++max;
    }

    public block copyBlk(block blk){
        if (blk == null) return null;
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

    public void kill(){
//        System.out.println("kill");
        visited = new HashSet<>();
        for (String name: blocks.keySet()){
//            System.out.println(name);
            visit_(blocks.get(name));
        }
    }

    public void visit_(block blk) {
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
        for (int i = 0; i < blk.stmts.size(); ++i){
            statement s = blk.stmts.get(i);
            if (s instanceof ret){
                ((ret) s).value = new entity(0);
            } else if (s instanceof call) {
//                System.out.println(((call) s).funID);
                if (!blocks.containsKey(((call) s).funID) || ((call) s).funID.startsWith("a")) blk.stmts.remove(s);
            }
        }
        for (block b : blk.successors()) visitBlock(b);
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
