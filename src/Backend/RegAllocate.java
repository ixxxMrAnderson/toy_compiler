package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class RegAllocate {
    public HashSet<Integer> blklist = new HashSet<>();
    public HashMap<Integer, block> index2blk = new HashMap<>();
    public HashMap<String, block> blocks;
    public HashMap<String, Integer> stackAlloc;
    public HashMap<String, HashSet<statement>> moveList = new HashMap<>();
    public HashSet<String> spillList = new HashSet<>();
    public HashSet<String> freezeList = new HashSet<>();
    public HashSet<String> simplifyList = new HashSet<>();
    public HashSet<String> precolored = new HashSet<>();
    public HashMap<String, Integer> degree = new HashMap<>();
    public HashMap<String, HashSet<String>> adjList = new HashMap<>();
    public HashSet<String> adjSet = new HashSet<>();
    public HashSet<String> initial = new HashSet<>();
    public Stack<String> selectStack = new Stack<>();
    public HashSet<String> coalescedNodes = new HashSet<>();
    public HashSet<statement> worklistMoves = new HashSet<>();
    public HashSet<statement> activeMoves = new HashSet<>();
    public HashSet<statement> coalescedMoves = new HashSet<>();
    public HashSet<statement> constrainedMoves = new HashSet<>();
    public HashSet<statement> frozenMoves = new HashSet<>();
//    public HashMap<String, HashSet<String>> nodeMoves = new HashMap<>();
    public HashSet<String> spilledNodes = new HashSet<>();
    public HashSet<String> coloredNodes = new HashSet<>();
    public HashMap<String, String> alias = new HashMap<>();
    public HashMap<String, Integer> color = new HashMap<>();
    public String currentFun;
    public Integer sp = 0;
    public Integer K = 18;
//    public Integer K = 3;

    public RegAllocate(HashMap<String, block> blocks,
                    HashMap<String, Integer> stackAlloc){
        Integer cnt = 0;
        for (statement s : blocks.get("main").stmts){
            if (s instanceof assign && ((assign) s).rhs == null) cnt++;
            else break;
        }
        if (cnt > 200){
//            System.out.println("55");
            new RegAlloc(blocks, stackAlloc);
        } else {
            precolored.add("_A0");
            precolored.add("_A1");
            precolored.add("_A2");
            precolored.add("_A3");
            precolored.add("_A4");
            precolored.add("_A5");
            precolored.add("_A6");
            precolored.add("_A7");
            precolored.add("_SP");
            precolored.add("_S0");
            this.blocks = blocks;
            this.stackAlloc = stackAlloc;
            for (String name : blocks.keySet()) {
                currentFun = name;
                buildList(blocks.get(name));
                sp = 0;
                Allocate();
                setReg();
                stackAlloc.put(name, sp);
                Clear();
            }
        }
    }

    public void setReg(){
        for (Integer blk : blklist){
            HashSet<String> live = new HashSet<>();
            HashMap<Integer, HashSet<String>> tmp_in = new HashMap<>();
            HashMap<Integer, HashSet<String>> tmp_out = new HashMap<>();
            new LivenessAnalysis(blocks, tmp_in, tmp_out);
            for (String id : tmp_out.get(blk)) live.add(id);
            for (int i = index2blk.get(blk).stmts.size() - 1; i >= 0; --i) {
                statement s = index2blk.get(blk).stmts.get(i);
                HashSet<String> use = new HashSet<>();
                String def = null;
                if (s instanceof binary) {
                    binary b = (binary) s;
                    if (!b.op1.is_constant) {
                        use.add(b.op1.id);
                        b.op1.reg = color.get(b.op1.id);
                    }
                    if (!b.op2.is_constant) {
                        use.add(b.op2.id);
                        b.op2.reg = color.get(b.op2.id);
                    }
                    def = b.lhs.id;
                    b.lhs.reg = color.get(b.lhs.id);
                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    if (!b.flag.is_constant) {
                        use.add(b.flag.id);
                        b.flag.reg = color.get(b.flag.id);
                    }
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null && !r.value.is_constant) {
                        use.add(r.value.id);
                        r.value.reg = color.get(r.value.id);
                    }
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (a.rhs != null &&!a.rhs.is_constant) {
                        use.add(a.rhs.id);
                        a.rhs.reg = color.get(a.rhs.id);
                    }
                    if (a.rhs != null) {
                        def = a.lhs.id;
                        a.lhs.reg = color.get(a.lhs.id);
                    }
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (l.addr != null) {
                        use.add(l.addr.id);
                        l.addr.reg = color.get(l.addr.id);
                    }
                    def = l.to.id;
                    l.to.reg = color.get(l.to.id);
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    if (s_.addr != null) {
                        use.add(s_.addr.id);
                        s_.addr.reg = color.get(s_.addr.id);
                    }
                    if (!s_.value.is_constant) {
                        use.add(s_.value.id);
                        s_.value.reg = color.get(s_.value.id);
                    }
                } else if (s instanceof call && !((call) s).inlined) {
                    HashMap<String, Integer> tmp_stack = new HashMap<>();
                    HashSet<String> live_ = new HashSet<>();
                    HashSet<Integer> reg_ = new HashSet<>();
                    for (String id : live) {
                        if (reg_.contains(color.get(id))) continue;
                        else reg_.add(color.get(id));
                        live_.add(id);
                    }
                    for (String id : live_){
                        entity reg = new entity("_S0");
                        reg.reg = color.get(id);
                        sp += 4;
                        tmp_stack.put(id, sp);
//                        System.out.println("SPILL: "+id+"____"+sp);
                        index2blk.get(blk).stmts.add(i+1, new load(sp, new entity(reg)));
                    }
                    for (String id : live_){
                        entity reg = new entity("_S0");
                        reg.reg = color.get(id);
                        index2blk.get(blk).stmts.add(i, new store(tmp_stack.get(id), new entity(reg)));
                    }
                }
                if (precolored.contains(def)) def = null;
                if (def != null) live.remove(def);
                for (String id : use) {
                    if (!precolored.contains(id)) live.add(id);
                }
                for (String id : precolored) live.remove(id);
            }
        }
    }

    public void Allocate(){
        Build();
        MakeWorkList();
        while (true){
            Integer cnt = 0;
            if (!simplifyList.isEmpty()) Simplify();
            else if (!worklistMoves.isEmpty()) Coalesce();
            else if (!freezeList.isEmpty()) Freeze();
            else if (!spillList.isEmpty()) Spill();
            else break;
            cnt++;
            if (cnt > 2000000) {
                System.out.println("time out");
                break;
            }
        }
        AssignColors();
        if (!spilledNodes.isEmpty()){
            RewriteProgram();
            Allocate();
        }
    }

    public void Clear(){
        blklist = new HashSet<>();
        index2blk = new HashMap<>();
        moveList = new HashMap<>();
        spillList = new HashSet<>();
        freezeList = new HashSet<>();
        simplifyList = new HashSet<>();
        degree = new HashMap<>();
        adjList = new HashMap<>();
        adjSet = new HashSet<>();
        initial = new HashSet<>();
        selectStack = new Stack<>();
        coalescedNodes = new HashSet<>();
        worklistMoves = new HashSet<>();
        activeMoves = new HashSet<>();
        coalescedMoves = new HashSet<>();
        constrainedMoves = new HashSet<>();
        frozenMoves = new HashSet<>();
        spilledNodes = new HashSet<>();
        coloredNodes = new HashSet<>();
        alias = new HashMap<>();
        color = new HashMap<>();
    }

    public void Build(){
        HashMap<Integer, HashSet<String>> tmp_in = new HashMap<>();
        HashMap<Integer, HashSet<String>> tmp_out = new HashMap<>();
        new LivenessAnalysis(blocks, tmp_in, tmp_out);
        for (Integer blk : blklist){
            HashSet<String> live = new HashSet<>();
            for (String id : tmp_out.get(blk)) live.add(id);
            for (int i = index2blk.get(blk).stmts.size() - 1; i >= 0; --i){
                statement s = index2blk.get(blk).stmts.get(i);
                boolean moveFlag = false;
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
                } else if (s instanceof assign){
                    assign a = (assign) s;
                    if (a.rhs != null && !a.rhs.is_constant) {
                        use.add(a.rhs.id);
                        if (!precolored.contains(a.rhs.id) && !precolored.contains(a.lhs.id)) moveFlag = true;
                    }
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
                if (precolored.contains(def)) def = null;
                if (moveFlag){
                    String use_ = null;
                    for (String id : use) use_ = id;
                    if (live.contains(use_)) live.remove(use_);
                    if (!moveList.containsKey(use_)) moveList.put(use_, new HashSet<>());
                    moveList.get(use_).add(s);
                    if (!moveList.containsKey(def)) moveList.put(def, new HashSet<>());
                    moveList.get(def).add(s);
                    worklistMoves.add(s);
                }
//                if (blk == 24){
////                    System.out.println(i);
////                    System.out.println(def);
////                    System.out.println(live);
//                }
                if (def != null) {
                    for (String id : live) AddEdge(id, def);
                    live.remove(def);
                }
                for (String id : use) {
                    if (!precolored.contains(id)) live.add(id);
                }
                for (String id : precolored) live.remove(id);
            }
        }
    }

    public void printMoveSet(HashSet<statement> set){
        for (statement s : set){
            System.out.println(((assign) s).lhs.id + " = " + ((assign) s).rhs.id);
        }
    }

    public void AddEdge(String u, String v){
//        System.out.println("Add edge: "+u+"____"+v);
        if (!adjSet.contains("("+u+","+v+")") && !u.equals(v)){
            adjSet.add("("+u+","+v+")");
            adjSet.add("("+v+","+u+")");
            if (!precolored.contains(u)){
                if (!adjList.containsKey(u)) adjList.put(u, new HashSet<>());
                adjList.get(u).add(v);
                if (degree.containsKey(u)){
                    Integer toPut = degree.get(u) + 1;
                    degree.put(u, toPut);
                } else {
                    degree.put(u, 1);
                }
            }
            if (!precolored.contains(v)){
                if (!adjList.containsKey(v)) adjList.put(v, new HashSet<>());
                adjList.get(v).add(u);
                if (degree.containsKey(v)){
                    Integer toPut = degree.get(v) + 1;
                    degree.put(v, toPut);
                } else {
                    degree.put(v, 1);
                }
            }
        }
    }

    public void MakeWorkList(){
        for (String id : initial){
//            if (id == null) System.out.println("make work list");
            if (precolored.contains(id)) continue;
            if (degree.containsKey(id) && degree.get(id) >= K) spillList.add(id);
            else if (MoveRelated(id)) freezeList.add(id);
            else simplifyList.add(id);
        }
        initial = new HashSet<>();
    }

    public HashSet<String> Adjacent(String n){
        HashSet<String> ret = new HashSet<>();
        if (adjList.containsKey(n)) {
            for (String id : adjList.get(n)) ret.add(id);
        }
        for (String id : selectStack){
            if (ret.contains(id)) ret.remove(id);
        }
        for (String id : coalescedNodes){
            if (ret.contains(id)) ret.remove(id);
        }
        return ret;
    }

    public HashSet<statement> NodeMoves(String n){
        HashSet<statement> ret = new HashSet<>();
        for (statement id : activeMoves) {
            if (moveList.containsKey(n) && moveList.get(n).contains(id)) ret.add(id);
        }
        for (statement id : worklistMoves) {
            if (moveList.containsKey(n) && moveList.get(n).contains(id)) ret.add(id);
        }
        return ret;
    }

    public boolean MoveRelated(String n){
        return NodeMoves(n).size() != 0;
    }

    public void Simplify(){
        String n = null;
        for (String id : simplifyList){
            n = id;
            break;
        }
        simplifyList.remove(n);
//        if (n == null) System.out.println("______________________________SIM");
        selectStack.push(n);
        for (String m : Adjacent(n)) DecrementDegree(m);
    }

    public void DecrementDegree(String m){
        if (!degree.containsKey(m)) return;
        Integer d = degree.get(m);
        degree.put(m, d - 1);
        if (d == K){
            HashSet<String> tmp = Adjacent(m);
            tmp.add(m);
            EnableMoves(tmp);
            spillList.remove(m);
            if (MoveRelated(m)) freezeList.add(m);
            else simplifyList.add(m);
        }
    }

    public void EnableMoves(HashSet<String> nodes){
//        printMoveSet(workli        stMoves);
        for (String n : nodes){
            for (statement m : NodeMoves(n)){
                if (activeMoves.contains(m)){
                    activeMoves.remove(m);
                    worklistMoves.add(m);
                }
            }
        }
//        printMoveSet(worklistMoves);
    }

    public void Coalesce(){
//        System.out.println("___________________Coalesce_________________");
        statement m = null;
        for (statement s : worklistMoves){
            m = s;
            break;
        }
        String x = null, y = null, u = null, v = null;
        // todo: copy(x, y) = mv x, y
        assign a = (assign) m;
        y = a.lhs.id;
        x = a.rhs.id;
//        System.out.println("___________________Coalesce_______"+x+"____"+y+"_______");
        x = GetAlias(x);
        y = GetAlias(y);
        if (precolored.contains(y)){
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        worklistMoves.remove(m);
        if (u.equals(v)){
            coalescedMoves.add(m);
            AddWorkList(u);
        } else if (adjSet.contains("("+u+","+v+")")){
            constrainedMoves.add(m);
            AddWorkList(u);
            AddWorkList(v);
        } else {
            boolean active = false;
            HashSet<String> tmp = new HashSet<>();
            for (String id : Adjacent(u)) tmp.add(id);
            for (String id : Adjacent(v)) tmp.add(id);
            if (!Conservatve(tmp)) active = true;
            if (active) activeMoves.add(m);
            else {
                coalescedMoves.add(m);
                Combine(u, v);
                AddWorkList(u);
            }
        }
    }

    public void AddWorkList(String u){

//        if (u == null) System.out.println("addWork list");
        if (!precolored.contains(u) && !MoveRelated(u) && (!degree.containsKey(u) || degree.get(u) < K)){
            freezeList.remove(u);
            simplifyList.add(u);
        }
    }

    public boolean Conservatve(HashSet<String> nodes){
        Integer k = 0;
        for (String n : nodes){
            if (degree.containsKey(n) && degree.get(n) >= K) k++;
        }
        return k < K;
    }

    public String GetAlias(String n){
        if (coalescedNodes.contains(n)) return GetAlias(alias.get(n));
        else return n;
    }

    public void Combine(String u, String v){
//        System.out.println("___________________COMBINE__"+u+" and " +v+"_________________");
//        if (v.equals("_TMP_205") || v.equals("u_0"))
//            System.out.println(selectStack);
        if (freezeList.contains(v)) freezeList.remove(v);
        else spillList.remove(v);
//        if (v == null) System.out.println("_________________________________________________-");
        coalescedNodes.add(v);
        alias.put(v, u);
        if (moveList.get(v) != null) {
            for (statement mv : moveList.get(v)) moveList.get(u).add(mv);
        }
        for (String t : Adjacent(v)){
            AddEdge(t, u);
            DecrementDegree(t);
        }
//        if (u == null) System.out.println("combine");
        if (degree.containsKey(u) && degree.get(u) >= K && freezeList.contains(u)){
            freezeList.remove(u);
            spillList.add(u);
        }
    }

    public void Freeze(){
//        System.out.println("___________________Freeze_________________");
        String u = null;
        for (String id : freezeList){
            u = id;
            break;
        }
        freezeList.remove(u);
//        if (u == null) System.out.println("freeze");
        simplifyList.add(u);
        FreezeMoves(u);
//        System.out.println("___________________Freeze_end___________________");
    }

    public void FreezeMoves(String u) {
        for (statement m : NodeMoves(u)){
            String x = null, y = null, v = null;
            // todo: copy(x, y) = mv x, y
            assign a = (assign) m;
            y = a.lhs.id;
            x = a.rhs.id;
            if (GetAlias(y).equals(GetAlias(u))) v = GetAlias(x);
            else v = GetAlias(y);
            activeMoves.remove(m);
            frozenMoves.add(m);
//            if (v == null) System.out.println("freeze move");
            if (NodeMoves(v).isEmpty() && (!degree.containsKey(v) || degree.get(v) < K)){
                freezeList.remove(v);
                simplifyList.add(v);
            }
        }
    }

    public void Spill(){
        String m = null;
        for (String id : spillList){
            m = id;
            break;
        }
        spillList.remove(m);
//        if (m == null) System.out.println("spill");
        simplifyList.add(m);
        FreezeMoves(m);
    }

    public void AssignColors(){
        for (String id : precolored){
            if (id.startsWith("_A")) color.put(id, 10+Integer.parseInt(id.substring(2, 3)));
            else if (id.startsWith("_SP")) color.put(id, 2);
            else color.put(id, 8);
        }
        while (!selectStack.isEmpty()){
            String n = selectStack.pop();
            HashSet<Integer> okColors = new HashSet<>();
            // a0-a7 (10-17);
            // s0-s1 (8-9), s2-s11 (18-27);
            // t0-t2 (5-7), t3-t6 (28-31)
            for (int i = 5; i <= 31; ++i) {
                if ((i >= 5 && i <= 7) || i == 9 || (i >= 18 && i <= 31)){
                    okColors.add(i);
                }
            }
            if (adjList.containsKey(n)) {
                for (String w : adjList.get(n)) {
                    if (coloredNodes.contains(GetAlias(w))) {
                        okColors.remove(color.get(GetAlias(w)));
                    }
                }
            }
            if (okColors.isEmpty()) spilledNodes.add(n);
            else {
                coloredNodes.add(n);
                Integer c = 0;
                for (Integer i : okColors){
                    c = i;
                    break;
                }
//                if (n.equals("u_0") || n.equals("v_0")) System.out.println("put "+n+", "+c);
                color.put(n, c);
            }
        }
//        System.out.println(coalescedNodes);
        for (String n : coalescedNodes) {
//            if (n.equals("u_0") || n.equals("v_0")) System.out.println("alias : put "+n+"("+GetAlias(n)+")"+", "+color.get(GetAlias(n)));
            color.put(n, color.get(GetAlias(n)));
        }
    }

    public void RewriteProgram(){
        initial = new HashSet<>();
//        System.out.println("rewrite: ");
        for (String v : spilledNodes){
            sp += 4;
            System.out.println("SPILL: "+v+"____"+sp);
            for (Integer blk : blklist){
                for (int i = 0; i < index2blk.get(blk).stmts.size(); ++i) {
                    statement s = index2blk.get(blk).stmts.get(i);
                    String def = null;
                    HashSet<String> use = new HashSet<>();
                    if (s instanceof binary) {
                        binary b = (binary) s;
                        if (!b.op1.is_constant && b.op1.id.equals(v)) {
                            b.op1 = new entity();
                            use.add(b.op1.id);
                        }
                        if (!b.op2.is_constant && b.op2.id.equals(v)) {
                            b.op2 = new entity();
                            use.add(b.op2.id);
                        }
                        if (b.lhs.id.equals(v)) {
                            b.lhs = new entity();
                            def = b.lhs.id;
                        }
                    } else if (s instanceof branch) {
                        branch b = (branch) s;
                        if (!b.flag.is_constant && b.flag.id.equals(v)) {
                            b.flag = new entity();
                            use.add(b.flag.id);
                        }
                    } else if (s instanceof ret) {
                        ret r = (ret) s;
                        if (r.value != null && !r.value.is_constant && r.value.id.equals(v)) {
                            r.value = new entity();
                            use.add(r.value.id);
                        }
                    } else if (s instanceof assign) {
                        assign a = (assign) s;
                        if (a.rhs != null && !a.rhs.is_constant && a.rhs.id.equals(v)){
                            a.rhs = new entity();
                            use.add(a.rhs.id);
                        }
                        if (a.lhs.id.equals(v) && a.rhs != null) {
                            a.lhs = new entity();
                            def = a.lhs.id;
                        }
                    } else if (s instanceof load) {
                        load l = (load) s;
                        if (l.addr != null && l.addr.id.equals(v)){
                            l.addr = new entity();
                            use.add(l.addr.id);
                        }
                        if (l.to.id.equals(v)){
                            l.to = new entity();
                            def = l.to.id;
                        }
                    } else if (s instanceof store) {
                        store s_ = (store) s;
                        if (s_.addr != null && s_.addr.id.equals(v)){
                            s_.addr = new entity();
                            use.add(s_.addr.id);
                        }
                        if (!s_.value.is_constant && s_.value.id.equals(v)){
                            s_.value = new entity();
                            use.add(s_.value.id);
                        }
                    }
                    if (!use.isEmpty()){
                        for (String id : use) {
//                            System.out.println("\tNEW_USE_" + id);
                            if (!precolored.contains(id)) {
                                initial.add(id);
                                index2blk.get(blk).stmts.add(i++, new load(sp, new entity(id)));
                            }
                        }
                    }
                    if (def != null){
                        if (!precolored.contains(def)) {
                            initial.add(def);
                            index2blk.get(blk).stmts.add(++i, new store(sp, new entity(def)));
                        }
                    }
                }
            }
        }
        spilledNodes = new HashSet<>();
        for (String id : coloredNodes) {
            if (!precolored.contains(id)) initial.add(id);
        }
        for (String id : coalescedNodes) {
            if (!precolored.contains(id)) initial.add(id);
        }
        coloredNodes = new HashSet<>();
        coalescedNodes = new HashSet<>();
        moveList = new HashMap<>();
        spillList = new HashSet<>();
        freezeList = new HashSet<>();
        simplifyList = new HashSet<>();
        degree = new HashMap<>();
        adjList = new HashMap<>();
        adjSet = new HashSet<>();
        selectStack = new Stack<>();
        worklistMoves = new HashSet<>();
        activeMoves = new HashSet<>();
        coalescedMoves = new HashSet<>();
        constrainedMoves = new HashSet<>();
        frozenMoves = new HashSet<>();
        spilledNodes = new HashSet<>();
        alias = new HashMap<>();
        color = new HashMap<>();
    }

    public void buildList(block blk){
        blklist.add(blk.index);
        index2blk.put(blk.index, blk);
        for (statement s : blk.stmts){
            if (s instanceof binary) {
                binary b = (binary) s;
                if (!b.op1.is_constant && !precolored.contains(b.op1.id)) initial.add(b.op1.id);
                if (!b.op2.is_constant && !precolored.contains(b.op2.id)) initial.add(b.op2.id);
                initial.add(b.lhs.id);
            } else if (s instanceof branch) {
                branch b = (branch) s;
                if (!b.flag.is_constant) initial.add(b.flag.id);
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (r.value != null && !r.value.is_constant && !precolored.contains(r.value.id)) initial.add(r.value.id);
            } else if (s instanceof assign){
                assign a = (assign) s;
                if (a.rhs != null && !a.rhs.is_constant && !precolored.contains(a.rhs.id)) initial.add(a.rhs.id);
                if (a.rhs != null && !precolored.contains(a.lhs.id)) initial.add(a.lhs.id);
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.addr != null) initial.add(l.addr.id);
                initial.add(l.to.id);
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.addr != null) initial.add(s_.addr.id);
                if (!s_.value.is_constant) initial.add(s_.value.id);
            }
        }
        for (block b : blk.successors()){
            if (b != null && !blklist.contains(b.index)) buildList(b);
        }
    }
}
