package Backend;

import MIR.*;
import Util.Type.type;

import java.util.HashMap;
import java.util.HashSet;

public class CFGopt implements Pass{
    public HashMap<block, block> alias = new HashMap<>();
    public HashSet<Integer> detected = new HashSet<>();
    public HashSet<Integer> visited = new HashSet<>();

    public CFGopt(HashMap<String, block> blocks){
        for (String name : blocks.keySet()){
            detectAlias(blocks.get(name));
            visitBlock(blocks.get(name));
        }
    }

    public void detectAlias(block blk){
        if (detected.contains(blk.index)) return;
        else detected.add(blk.index);
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
        for (block nxt : blk.successors()) detectAlias(nxt);
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
