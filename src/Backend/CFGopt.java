package Backend;

import MIR.*;
import Util.Type.type;

import java.util.HashMap;
import java.util.HashSet;

public class CFGopt implements Pass{
    public HashMap<block, block> alias = new HashMap<>();
    public HashSet<Integer> detected = new HashSet<>();
    public HashSet<Integer> visited = new HashSet<>();
    public HashMap<Integer, HashSet<Integer>> preBlk = new HashMap<>();
    public HashMap<Integer, block> index2blk = new HashMap<>();

    public CFGopt(HashMap<String, block> blocks){
        for (String name : blocks.keySet()){
            detectAlias(blocks.get(name));
            visitBlock(blocks.get(name));
        }
        for (Integer b : index2blk.keySet()){
            if (preBlk.containsKey(b) && preBlk.get(b).size() == 1){
                block pre = null;
                for (Integer i : preBlk.get(b)) pre = index2blk.get(i);
                Integer index = -1;
                for (int i = 0; i < pre.stmts.size(); ++i){
                    if (pre.stmts.get(i) instanceof jump && ((jump) pre.stmts.get(i)).destination == index2blk.get(b)){
                        index = i;
                        break;
                    }
                }
                if (index != -1 || pre.nxtBlock == index2blk.get(b)){
                    if (index != -1) {
                        while (pre.stmts.size() > index) {
                            pre.stmts.remove(pre.stmts.get(index));
                        }
                    }
                    pre.nxtBlock = index2blk.get(b).nxtBlock;
                    for (statement s : index2blk.get(b).stmts) pre.stmts.add(s);
                }
            }
        }
    }

    public void detectAlias(block blk){
        if (detected.contains(blk.index)) return;
        else detected.add(blk.index);
        index2blk.put(blk.index, blk);
        if (blk.stmts.size() > 0){
            if (blk.stmts.get(0) instanceof jump) {
                if (((jump) blk.stmts.get(0)).destination != null) {
                    alias.put(blk, ((jump) blk.stmts.get(0)).destination);
                }
            } else if (blk.stmts.get(0) instanceof branch) {
                branch b = (branch) blk.stmts.get(0);
                if (b.flag.is_constant){
                    if (b.flag.constant.expr_type.type == type.INT){
                        if (b.flag.constant.int_value == 0) alias.put(blk, b.falseBranch);
                        else alias.put(blk, b.trueBranch);
                    } else {
                        if (b.flag.constant.bool_value) alias.put(blk, b.trueBranch);
                        else alias.put(blk, b.falseBranch);
                    }
                }
            }
        }
        for (block nxt : blk.successors()) {
            if (!preBlk.containsKey(nxt.index)) preBlk.put(nxt.index, new HashSet<>());
            preBlk.get(nxt.index).add(blk.index);
            detectAlias(nxt);
        }
    }

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
        for (statement s : blk.stmts){
            if (s instanceof jump){
                jump j = (jump) s;
                if (alias.containsKey(j.destination)) j.destination = alias.get(j.destination);
            } else if (s instanceof branch){
                branch b = (branch) s;
                if (b.flag.is_constant){
                    if (b.flag.constant.expr_type.type == type.INT){
                        if (b.flag.constant.int_value == 0) b.trueBranch = null;
                        else b.falseBranch = null;
                    } else {
                        if (b.flag.constant.bool_value) b.falseBranch = null;
                        else b.trueBranch = null;
                    }
                }
                if (alias.containsKey(b.trueBranch)) b.trueBranch = alias.get(b.trueBranch);
                if (alias.containsKey(b.falseBranch)) b.falseBranch = alias.get(b.falseBranch);
            }
        }
        for (block nxt : blk.successors()) visitBlock(nxt);
    }
}
