package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;

public class ADCE implements Pass{
    HashMap<String, block> blocks;
    HashSet<Integer> visited = new HashSet<>();

    public ADCE(HashMap<String, block> blocks){
        this.blocks = blocks;
        for (String name : blocks.keySet()){
            visitBlock(blocks.get(name));
            for (block nxt : blocks.get(name).successors()) visitBlock(nxt);
        }
    }

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
        HashMap<Integer, HashSet<String>> in = new HashMap<>();
        HashMap<Integer, HashSet<String>> out = new HashMap<>();
//        System.out.println(blk.index);
        new LivenessAnalysis(blocks, in, out);
//        System.out.println("live analysis done: "+blk.stmts.size());
        HashSet<String> live = new HashSet<>();
        for (String id : out.get(blk.index)) live.add(id);
        for (int i = blk.stmts.size() - 1; i >= 0; --i) {
            statement s = blk.stmts.get(i);
            HashSet<String> use = new HashSet<>();
            String def = null;
            if (s instanceof binary) {
                binary b = (binary) s;
                if (!b.op1.is_constant) use.add(b.op1.id);
                if (!b.op2.is_constant) use.add(b.op2.id);
                def = b.lhs.id;
            } else if (s instanceof branch) {
                branch b = (branch) s;
                if (!b.flag.is_constant) use.add(b.flag.id);
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (r.value != null && !r.value.is_constant) use.add(r.value.id);
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs != null && !a.rhs.is_constant) use.add(a.rhs.id);
                if (a.rhs != null) def = a.lhs.id;
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.addr != null) use.add(l.addr.id);
                def = l.to.id;
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.addr != null) use.add(s_.addr.id);
                if (!s_.value.is_constant) use.add(s_.value.id);
            }
            if (def != null && !live.contains(def)) {
                if (!def.startsWith("_A") && !def.equals("_S0") && !def.equals("_SP")) {
                    blk.stmts.remove(s);
                    if (i > 0 && blk.stmts.size() > i && blk.stmts.get(i - 1) instanceof call){
                        blk.stmts.remove(blk.stmts.get(i - 1));
                    }
                    live.remove(def);
                    continue;
                }
            }
            for (String id : use) {
                if (!id.startsWith("_A") && !id.equals("_S0") && !id.equals("_SP")) live.add(id);
            }
        }
//        System.out.println("done: " + blk.stmts.size());
//        System.out.println("nxt: " + blk.successors().size());
        for (block b : blk.successors()) visitBlock(b);
    }
}
