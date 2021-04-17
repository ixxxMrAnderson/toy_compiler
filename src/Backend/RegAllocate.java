package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class RegAllocate {
    public HashSet<Integer> blklist = new HashSet<>();
    public HashMap<Integer, block> index2blk = new HashMap<>();
    public HashMap<String, block> blocks;
    public HashMap<Integer, HashSet<String>> in;
    public HashMap<Integer, HashSet<String>> out;
    public HashMap<String, HashMap<String, Integer>> stackAlloc;
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
    public HashMap<String, HashMap<String, String>> stackAlias = new HashMap<>();
    public HashMap<String, Integer> currentStack = new HashMap<>();
    public String currentFun;
    public Integer sp = 0;
    public Integer K = 18;

    public RegAllocate(HashMap<String, block> blocks,
                    HashMap<Integer, HashSet<String>> in,
                    HashMap<Integer, HashSet<String>> out,
                    HashMap<String, HashMap<String, Integer>> stackAlloc){
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
        this.in = in;
        this.out = out;
        this.stackAlloc = stackAlloc;
        for (String name : blocks.keySet()){
            currentFun = name;
            buildList(blocks.get(name));
            currentStack = new HashMap<>();
            stackAlias.put(currentFun, new HashMap<>());
            sp = 0;
            Allocate();
            setReg();
            stackAlloc.put(name, currentStack);
            Clear();
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
                    def = a.lhs.id;
                    a.lhs.reg = color.get(a.lhs.id);
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
                } else if (s instanceof call) {
//                    System.out.println(live);
                    for (String id : live){
                        if (!stackAlias.get(currentFun).containsKey(id)) {
                            sp += 4;
                            currentStack.put(id, sp);
                        } else id = stackAlias.get(currentFun).get(id);
                        entity reg = new entity();
                        System.out.println(color);
                        System.out.println(id);
                        reg.reg = color.get(id);
                        index2blk.get(blk).stmts.add(i+1, new load(new entity(id), new entity(reg), true));
                    }
//                    System.out.println();
                    for (String id : live){
                        if (stackAlias.get(currentFun).containsKey(id)) id = stackAlias.get(currentFun).get(id);
                        entity reg = new entity();
                        reg.reg = color.get(id);
                        index2blk.get(blk).stmts.add(i, new store(new entity(id), new entity(reg), true));
                    }
                    // todo LiveAnalysis again
                }
                if (precolored.contains(def)) def = null;
                for (String id : use) {
                    if (!precolored.contains(id)) live.add(id);
                }
                if (def != null) live.remove(def);
                for (String id : precolored) live.remove(id);
            }
        }
    }

    public void Allocate(){
        Build();
        MakeWorkList();
        while (true){
            if (!simplifyList.isEmpty()) Simplify();
            else if (!worklistMoves.isEmpty()) Coalesce();
            else if (!freezeList.isEmpty()) Freeze();
            else if (!spillList.isEmpty()) Spill();
            else break;
//            System.out.println("________________________________________________________________");
//            System.out.println("simplifyList");
//            System.out.println(simplifyList.size());
//            System.out.println("freezelist");
//            System.out.println(freezeList.size());
//            System.out.println("spilllist");
//            System.out.println(spillList.size());
//            System.out.println("wtfffffffffffffffffffffffffffff");
//            System.out.println(adjSet);
        }
        AssignColors();
//        System.out.println("color: ");
//        System.out.println(color);
//        System.out.println("spilledNodes: ");
//        System.out.println(spilledNodes);
//        System.out.println(spilledNodes);
        if (!spilledNodes.isEmpty()){
            RewriteProgram();
            System.out.println("rewritten: ");
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
        for (Integer blk : blklist){
            HashSet<String> live = new HashSet<>();
            for (String id : out.get(blk)) live.add(id);
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
                    def = a.lhs.id;
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
                if (def != null) {
                    for (String id : live) AddEdge(id, def);
                }
                for (String id : use) {
                    if (!precolored.contains(id)) live.add(id);
                }
                if (def != null) live.remove(def);
                for (String id : precolored) live.remove(id);
            }
        }

//        System.out.println("adjSet");
//        System.out.println(adjSet);
//        System.out.println(worklistMoves);
    }

    public void printMoveSet(HashSet<statement> set){
        for (statement s : set){
            System.out.println(((assign) s).lhs.id + " = " + ((assign) s).rhs.id);
        }
    }

    public void AddEdge(String u, String v){
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
            if (id == null) System.out.println("make work list");
            if (precolored.contains(id)) continue;
            if (degree.containsKey(id) && degree.get(id) >= K) spillList.add(id);
            else if (MoveRelated(id)) freezeList.add(id);
            else simplifyList.add(id);
        }
        initial = new HashSet<>();
//        System.out.println("_______________makeWorkList_____________________");
//        System.out.println("simplifyList");
//        System.out.println(simplifyList);
//        System.out.println("freezelist");
//        System.out.println(freezeList);
//        System.out.println("spilllist");
//        System.out.println(spillList);
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
//        System.out.println("______________before_simplify_____________________");
//        System.out.println("simplifyList");
//        System.out.println(simplifyList);
//        System.out.println("freezelist");
//        System.out.println(freezeList);
//        System.out.println("spilllist");
//        System.out.println(spillList);
//        System.out.println(selectStack);
        for (String id : simplifyList){
            n = id;
            break;
        }
        simplifyList.remove(n);
        if (n == null) System.out.println("______________________________SIM");
        selectStack.push(n);
        for (String m : Adjacent(n)) DecrementDegree(m);
//        System.out.println("______________after_simplify_____________________");
//        System.out.println("simplifyList");
//        System.out.println(simplifyList);
//        System.out.println("freezelist");
//        System.out.println(freezeList);
//        System.out.println("spilllist");
//        System.out.println(spillList);
//        System.out.println(selectStack);
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
        for (String n : nodes){
            for (statement m : NodeMoves(n)){
                if (activeMoves.contains(m)){
                    activeMoves.remove(m);
                    worklistMoves.add(m);
                }
            }
        }
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
        x = a.lhs.id;
        y = a.rhs.id;
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
//        System.out.println("___________________coalesced__________________");
//        printMoveSet(coalescedMoves);
//        System.out.println("___________________active__________________");
//        printMoveSet(activeMoves);
//        System.out.println("___________________frozen__________________");
//        printMoveSet(frozenMoves);
//        System.out.println("___________________constraint__________________");
//        printMoveSet(constrainedMoves);
//        System.out.println("___________________todo__________________");
//        printMoveSet(worklistMoves);
//        System.out.println("___________________coalesced nodes__________________");
//        System.out.println(coalescedNodes);
    }

    public void AddWorkList(String u){

        if (u == null) System.out.println("addWork list");
        if (!precolored.contains(u) && !MoveRelated(u) && (!degree.containsKey(u) || degree.get(u) < K)){
            freezeList.remove(u);
            simplifyList.add(u);
        }
    }

//    public boolean OK(String t, String r){
//        return (!degree.containsKey(t) || degree.get(t) < K) || precolored.contains(t) || adjSet.contains("("+t+","+r+")");
//    }

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
        if (freezeList.contains(v)) freezeList.remove(v);
        else spillList.remove(v);
        if (v == null) System.out.println("_________________________________________________-");
        coalescedNodes.add(v);
        alias.put(v, u);
//        for (String id : worklistMoves.get(v).keyset()) nodeMoves.get(u).add(id);
        for (String t : Adjacent(v)){
            AddEdge(t, u);
            DecrementDegree(t);
        }
        if (u == null) System.out.println("combine");
        if (degree.containsKey(u) && degree.get(u) >= K && freezeList.contains(u)){
            freezeList.remove(u);
            simplifyList.add(u);
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
        if (u == null) System.out.println("freeze");
        simplifyList.add(u);
        FreezeMoves(u);
//        System.out.println("___________________Freeze_end___________________");
    }

    public void FreezeMoves(String u) {
        for (statement m : NodeMoves(u)){
            String x = null, y = null, v = null;
            // todo: copy(x, y) = mv x, y
            assign a = (assign) m;
            x = a.lhs.id;
            y = a.rhs.id;
            if (GetAlias(y).equals(GetAlias(u))) v = GetAlias(x);
            else v = GetAlias(y);
            activeMoves.remove(m);
            frozenMoves.add(m);
            if (v == null) System.out.println("freeze move");
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
        if (m == null) System.out.println("spill");
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
            for (int i = 5; i < 31; ++i) {
                if ((i <= 17 && i >= 10) || i == 8){

                } else {
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
                color.put(n, c);
            }
        }
//        System.out.println(color);
//        System.out.println(alias);
//        System.out.println(coalescedNodes);
        for (String n : coalescedNodes) {
//            while (!color.containsKey(n)) n = GetAlias(n);
            color.put(n, color.get(GetAlias(n)));
        }
    }

    public void RewriteProgram(){
        initial = new HashSet<>();
        for (String v : spilledNodes){
            if (!currentStack.containsKey(v)) {
                sp += 4;
                currentStack.put(v, sp);
            }
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
                        if (a.lhs.id.equals(v)) {
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
                            if (!precolored.contains(id)) {
                                stackAlias.get(currentFun).put(id, v);
                                initial.add(id);
                                index2blk.get(blk).stmts.add(i++, new load(new entity(v), new entity(id), true));
                            }
                        }
                    }
                    if (def != null){
                        if (!precolored.contains(def)) {
                            stackAlias.get(currentFun).put(def, v);
                            initial.add(def);
                            index2blk.get(blk).stmts.add(i++, new load(new entity(v), new entity(def), true));
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
                if (!precolored.contains(a.lhs.id)) initial.add(a.lhs.id);
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
