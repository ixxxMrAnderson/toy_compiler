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
    public HashSet<String> deadVars = new HashSet<>();
    public HashSet<Integer> collected = new HashSet<>();
    public HashSet<Integer> cleaned = new HashSet<>();

    public LivenessAnalysis(HashMap<String, block> blocks,
    HashMap<Integer, HashSet<String>> in,
    HashMap<Integer, HashSet<String>> out){
        this.in = in;
        this.out = out;
        for (String name : blocks.keySet()){
            deadVars = new HashSet<>();
            buildList(blocks.get(name));
            if (name.equals("_VAR_DEF")) continue;
//            System.out.println(name+": "+deadVars);
            collectDeadVar(blocks.get(name));
//            System.out.println(name+": "+deadVars);
            cleanDeadVar(blocks.get(name));
        }
//        System.out.println("def");
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
                for (String name : use.get(i)) in.get(i).add(name);
                out.put(i, new HashSet<>());
                for (block b : index2blk.get(i).successors()){
                    for (String name : in.get(b.index)) out.get(i).add(name);
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
            if (ret) {
//                System.out.println("ret");
//                System.out.println(in);
//                System.out.println(out);
                return;
            }
        }
    }

    public void collectDeadVar(block blk){
        if (collected.contains(blk.index)) return;
        else collected.add(blk.index);
        for (statement s : blk.stmts){
            if (s instanceof binary) {
                binary b = (binary) s;
                if (!b.op1.is_constant) {
                    if (!b.lhs.id.equals(b.op1.id)) deadVars.remove(b.op1.id);
                }
                if (!b.op2.is_constant) {
                    if (!b.lhs.id.equals(b.op2.id)) deadVars.remove(b.op2.id);
                }
            } else if (s instanceof branch) {
                branch b = (branch) s;
                if (!b.flag.is_constant) deadVars.remove(b.flag.id);
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (r.value != null && !r.value.is_constant) deadVars.remove(r.value.id);
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs != null && !a.rhs.is_constant) {
                    if (!a.lhs.id.equals(a.rhs.id)) deadVars.remove(a.rhs.id);
                }
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.addr != null) deadVars.remove(l.addr.id);
                deadVars.remove(l.to.id);
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.addr != null) deadVars.remove(s_.addr.id);
                if (!s_.value.is_constant) deadVars.remove(s_.value.id);
            }
        }
        for (block b : blk.successors()) collectDeadVar(b);
    }

    public void cleanDeadVar(block blk){
        if (cleaned.contains(blk.index)) return;
        else cleaned.add(blk.index);
        for (String id : deadVars){
            def.get(blk.index).remove(id);
            use.get(blk.index).remove(id);
        }
        for (block b : blk.successors()) cleanDeadVar(b);
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
                if (!b.op1.is_constant) addU(blk.index, b.op1.id);
                if (!b.op2.is_constant) addU(blk.index, b.op2.id);
                addD(blk.index, b.lhs.id);
            } else if (s instanceof branch) {
                branch b = (branch) s;
                if (!b.flag.is_constant) addU(blk.index, b.flag.id);
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (r.value != null && !r.value.is_constant) addU(blk.index, r.value.id);
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs != null && !a.rhs.is_constant) addU(blk.index, a.rhs.id);
                if (a.rhs != null) addD(blk.index, a.lhs.id);
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.addr != null) addU(blk.index, l.addr.id);
                addD(blk.index, l.to.id);
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.addr != null) addU(blk.index, s_.addr.id);
                if (!s_.value.is_constant) addU(blk.index, s_.value.id);
            }
        }
        for (block b : blk.successors()){
            if (b != null && !blklist.contains(b.index)) buildList(b);
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

    public void addU(Integer key, String val){
        if (!def.get(key).contains(val) && !(val.startsWith("_A") && isNumeric(val.substring(2, 3)))){
            if (!val.equals("_SP") && !val.equals("_S0")) {
                use.get(key).add(val);
                deadVars.add(val);
            }
        }
    }

    public void addD(Integer key, String val){
        if (!use.get(key).contains(val) && !(val.startsWith("_A") && isNumeric(val.substring(2, 3)))){
            if (!val.equals("_SP") && !val.equals("_S0")) {
                def.get(key).add(val);
                deadVars.add(val);
            }
        }
    }
}
