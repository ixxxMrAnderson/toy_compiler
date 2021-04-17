package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;

public class LivenessAnalysis {
    public HashSet<Integer> blklist = new HashSet<>();
    public HashMap<Integer, block> index2blk = new HashMap<>();
    public HashMap<Integer, HashSet<String>> in_ = new HashMap<>();
    public HashMap<Integer, HashSet<String>> in;
    public HashMap<Integer, HashSet<String>> out_ = new HashMap<>();
    public HashMap<Integer, HashSet<String>> out;
    public HashMap<Integer, HashSet<String>> use = new HashMap<>();
    public HashMap<Integer, HashSet<String>> def = new HashMap<>();

    public LivenessAnalysis(HashMap<String, block> blocks,
    HashMap<Integer, HashSet<String>> in,
    HashMap<Integer, HashSet<String>> out){
        this.in = in;
        this.out = out;
        for (String name : blocks.keySet()){
            buildList(blocks.get(name));
        }
//        System.out.println(def);
//        System.out.println(use);
        while (true){
            for (Integer i : blklist) {
                in_.put(i, new HashSet<>());
                for (String name : in.get(i)) in_.get(i).add(name);
                out_.put(i, new HashSet<>());
                for (String name : out.get(i)) out_.get(i).add(name);
                in.put(i, new HashSet<>());
                for (String name : out.get(i)) {
                    if (!def.get(i).contains(name)) in.get(i).add(name);
                }
//                for (String name : def.get(i)) in.get(i).remove(name);
                for (String name : use.get(i)) in.get(i).add(name);
                out.put(i, new HashSet<>());
                for (block b : index2blk.get(i).successors()){
                    for (String name : in.get(b.index)) out.get(i).add(name);
                }
                if (index2blk.get(i).optAndBlk != null){
                    for (String name : in.get(index2blk.get(i).optAndBlk.index)) out.get(i).add(name);
                }
                if (index2blk.get(i).optOrBlk != null){
                    for (String name : in.get(index2blk.get(i).optOrBlk.index)) out.get(i).add(name);
                }
            }

            boolean ret = true;
            for (Integer i : blklist){
                if (in.get(i).size() != in_.get(i).size() || out.get(i).size() != out_.get(i).size()) ret = false;
                for (String id : in.get(i)){
                    if (!in_.get(i).contains(id)){
                        ret = false;
                        break;
                    }
                }
                for (String id : out.get(i)){
                    if (!out_.get(i).contains(id)){
                        ret = false;
                        break;
                    }
                }
                if (!ret) break;
            }
            if (ret) return;
        }
    }

    public void buildList(block blk){
        blklist.add(blk.index);
        index2blk.put(blk.index, blk);
        in_.put(blk.index, new HashSet<>());
        in.put(blk.index, new HashSet<>());
        out_.put(blk.index, new HashSet<>());
        out.put(blk.index, new HashSet<>());
        use.put(blk.index, new HashSet<>());
        def.put(blk.index, new HashSet<>());
        for (statement s : blk.stmts){
            if (s instanceof binary) {
                binary b = (binary) s;
                if (!b.op1.is_constant && !def.get(blk.index).contains(b.op1.id)) use.get(blk.index).add(b.op1.id);
                if (!b.op2.is_constant && !def.get(blk.index).contains(b.op2.id)) use.get(blk.index).add(b.op2.id);
                if (!use.get(blk.index).contains(b.lhs.id)) def.get(blk.index).add(b.lhs.id);
            } else if (s instanceof branch) {
                branch b = (branch) s;
                if (!def.get(blk.index).contains(b.flag.id)) use.get(blk.index).add(b.flag.id);
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (r.value != null && !r.value.is_constant && !def.get(blk.index).contains(r.value.id)) use.get(blk.index).add(r.value.id);
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (!a.rhs.is_constant && !def.get(blk.index).contains(a.rhs.id)) use.get(blk.index).add(a.rhs.id);
                if (!use.get(blk.index).contains(a.lhs.id)) def.get(blk.index).add(a.lhs.id);
            } else if (s instanceof define) {
                define d = (define) s;
                if (d.assign != null) {
                    if (!d.assign.is_constant && !def.get(blk.index).contains(d.assign.id)) use.get(blk.index).add(d.assign.id);
                    if (!def.get(blk.index).contains(d.var.id)) def.get(blk.index).add(d.var.id);
                }
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.addr != null && !def.get(blk.index).contains(l.addr.id)) {
                    if (!def.get(blk.index).contains(l.to.id)) use.get(blk.index).add(l.to.id);
                    use.get(blk.index).add(l.addr.id);
                } else {
                    if (!use.get(blk.index).contains(l.to.id)) def.get(blk.index).add(l.to.id);
                }
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.addr != null && !s_.addr.is_constant && !def.get(blk.index).contains(s_.addr.id)) use.get(blk.index).add(s_.addr.id);
                if (!s_.value.is_constant && !def.get(blk.index).contains(s_.value.id)) use.get(blk.index).add(s_.value.id);
            }
        }
        for (block b : blk.successors()){
            if (b != null && !blklist.contains(b.index)) buildList(b);
        }
        if (blk.optAndBlk != null && !blklist.contains(blk.optAndBlk)) buildList(blk.optAndBlk);
        if (blk.optOrBlk != null && !blklist.contains(blk.optOrBlk)) buildList(blk.optOrBlk);
    }
}
