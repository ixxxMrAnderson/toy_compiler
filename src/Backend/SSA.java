package Backend;

import MIR.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SSA implements Pass{

    private Integer blockCnt = 0;
    private HashSet<Integer> visited = new HashSet<>();
    private HashMap<String, Integer> counter = new HashMap<>();
    private HashMap<block, Integer> block2index = new HashMap<>();
    private HashMap<Integer, block> index2blk = new HashMap<>();
    private HashMap<String, HashSet<Integer>> blks = new HashMap<>();
    private HashMap<Integer, HashSet<String>> U_chain = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> preBlk = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> DT = new HashMap<>();
    private HashMap<String, Integer> fun2entry = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> dom2sub;
    private HashMap<Integer, HashSet<Integer>> DF = new HashMap<>();
    private String currentFun;
    private HashMap<String, HashMap<String, HashSet<Integer>>> definedVar = new HashMap<>();
    private HashMap<String, HashSet<varNode>> varNodes = new HashMap<>();
    private HashSet<Integer> inserted = new HashSet<>();
    private HashSet<String> glblVars = new HashSet<>();

    public SSA(HashMap<String, block> b, HashMap<Integer, HashSet<Integer>> dom2sub){
        currentFun = "main";
        this.dom2sub = dom2sub;
        definedVar.put(currentFun, new HashMap<>());
        blks.put(currentFun, new HashSet<>());
        visitBlock(b.get("main"));
        for (String name : b.keySet()){
            if (name.equals("_VAR_DEF") || name.equals("main")) continue;
            currentFun = name;
            definedVar.put(currentFun, new HashMap<>());
            blks.put(currentFun, new HashSet<>());
            visitBlock(b.get(name));
            for (String var : glblVars){
                if (!definedVar.get(name).containsKey(var)) {
                    definedVar.get(name).put(var, new HashSet<>());
                    definedVar.get(name).get(var).add(b.get(name).index);
                }
            }
//            visitBlock(b.get(name).tailBlk);
        }
//        System.out.println("definedVar:");
//        System.out.println(definedVar);
//        System.out.println("preBlk:");
//        System.out.println(preBlk);
        generateDT();
//        System.out.println("DT:");
//        System.out.println(DT);
        generateDF();
//        System.out.println("DF:");
//        System.out.println(DF);
        for (String name : b.keySet()){ // insertingPhi
            if (name.equals("_VAR_DEF")) continue;
            for (String var : definedVar.get(name).keySet()){
                HashSet<Integer> F = new HashSet<>();
                HashSet<Integer> W = new HashSet<>();
                for (Integer d : definedVar.get(name).get(var)){
                    if (!W.contains(d)) W.add(d);
                }
                while (!W.isEmpty()){
                    Integer x = 0;
                    for (Integer w : W){
                        x = w;
                        W.remove(x);
                        if (x == w) break;
                    }
                    if (DF.get(x) != null) {
                        for (Integer y : DF.get(x)) {
                            if (!F.contains(y)) {
                                index2blk.get(y).stmts.add(0, new phi(new entity(var)));
                                F.add(y);
                                if (!definedVar.get(name).get(var).contains(y) && !W.contains(y)) W.add(y);
                            }
                        }
                    }
                }
            }
        }
        for (Integer index : index2blk.keySet()){ // building dom tree
            if (fun2entry.containsValue(index)) continue;
            Integer dom = idom(index);
            if (!dom2sub.containsKey(dom)) dom2sub.put(dom, new HashSet<>());
            dom2sub.get(dom).add(index);
        }
//        System.out.println("dom2sub:");
//        System.out.println(dom2sub);
        for (String name : b.keySet()){
            if (name.equals("_VAR_DEF")) continue;
            currentFun = name;
            varNodes.put(name, new HashSet<>());
            for (String id : definedVar.get(name).keySet()){
                varNodes.get(name).add(new varNode(id));
            }
            rename(fun2entry.get(name));
        }
//        for (String name : b.keySet()){
//            if (name.equals("_VAR_DEF")) continue;
//            insertCopy(b.get(name).index);
//        }
    }

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(getBlockName(blk))) return;
        else visited.add(getBlockName(blk));
        if (blk != null) {
            blk.index = getBlockName(blk);
            index2blk.put(blk.index, blk);
            blks.get(currentFun).add(blk.index);
            U_chain.put(getBlockName(blk), new HashSet<>());
//            D_chain.put(getBlockName(blk), new HashSet<>());
            for (statement s : blk.stmts) {
                if (s instanceof binary) {
                    binary b = (binary) s;
                    if (!b.lhs.is_constant) add_D(blk, b.lhs.id);
                } else if (s instanceof define) {
                    define d = (define) s;
//                    System.out.println("def: " + d.var.id);
                    add_D(blk, d.var.id);
                } else if (s instanceof store) {
                    store s_ = (store) s;
//                    System.out.println("store");
                    if (s_.addr == null) add_D(blk, s_.id.id);
                    if (s_.addr != null && s_.addr.id.startsWith("@")) add_D(blk, s_.addr.id);
                }
            }
            for (block b_ : blk.successors()){
                if (b_ != null) {
                    if (preBlk.get(getBlockName(b_)) == null)
                        preBlk.put(getBlockName(b_), new HashSet<>());
                    preBlk.get(getBlockName(b_)).add(getBlockName(blk));
                    visitBlock(b_);
                }
            }
            if (blk.optAndBlk != null) {
                if (preBlk.get(getBlockName(blk.optAndBlk)) == null)
                    preBlk.put(getBlockName(blk.optAndBlk), new HashSet<>());
                preBlk.get(getBlockName(blk.optAndBlk)).add(getBlockName(blk));
                visitBlock(blk.optAndBlk);
            }
            if (blk.optOrBlk != null) {
                if (preBlk.get(getBlockName(blk.optOrBlk)) == null)
                    preBlk.put(getBlockName(blk.optOrBlk), new HashSet<>());
                preBlk.get(getBlockName(blk.optOrBlk)).add(getBlockName(blk));
                visitBlock(blk.optOrBlk);
            }
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

    private void add_U(block blk, String id){
        if (!id.startsWith("_TMP") && !id.equals("_SP")  && !id.equals("_S0") ){
            if (!(id.startsWith("_A") && isNumeric(id.substring(2, 3)))){
                U_chain.get(getBlockName(blk)).add(id);
            }
        }
    }

    private void add_D(block blk, String id){
//        System.out.println(currentFun + "_add_D: "+id);
        if (id.startsWith("@")) glblVars.add(id);
        if (!id.startsWith("_TMP") && !id.equals("_SP")  && !id.equals("_S0") ){
            if (!(id.startsWith("_A") && isNumeric(id.substring(2, 3)))){
                if (!definedVar.get(currentFun).containsKey(id)) definedVar.get(currentFun).put(id, new HashSet<>());
                definedVar.get(currentFun).get(id).add(getBlockName(blk));
            }
        }
    }

    private HashSet<Integer> passed = new HashSet<>();
    private boolean walk(Integer from, Integer to, Integer without){
        passed.add(from);
        if (preBlk.get(from) == null && from != to) return false;
        for (Integer des : preBlk.get(from)){
            if (passed.contains(des) || des == without) continue;
            if (des == to || walk(des, to, without)) return true;
        }
        return false;
    }

    private void generateDT(){
        for (String name : blks.keySet()){
            Integer entry = -1;
            for (Integer i : blks.get(name)){
                if (!preBlk.containsKey(i)) entry = i;
            }
            fun2entry.put(name, entry);
            for (Integer from : blks.get(name)){
                if (from == entry) continue;
                DT.put(from, new HashSet<>());
                for (Integer without : blks.get(name)){
                    if (without == from || without == entry) continue;
                    passed = new HashSet<>();
                    if (!walk(from, entry, without)) DT.get(from).add(without);
                }
                DT.get(from).add(entry);
            }
        }
    }

    private Integer idom(Integer x){
        for (Integer dom : DT.get(x)){
            if (DT.get(x).size() == 1) return dom;
            if (DT.get(dom) == null) continue;
            boolean isIdom = true;
            for (Integer i : DT.get(x)){
                if (dom == i) continue;
                if (!DT.get(dom).contains(i)) isIdom = false;
            }
            if (isIdom) return dom;
        }
        return -1;
    }

    private void generateDF(){
        for (Integer b : preBlk.keySet()){
            for (Integer x : preBlk.get(b)){
                while (DT.get(b) != null && !DT.get(b).contains(x)){
                    if (!DF.containsKey(x)) DF.put(x, new HashSet<>());
                    if (!DF.get(x).contains(b)) DF.get(x).add(b);
                    x = idom(x);
                }
            }
        }
    }

    public void rename(Integer blk){
        for (int i = 0; i < index2blk.get(blk).stmts.size(); ++i){
            statement s = index2blk.get(blk).stmts.get(i);
            if (s instanceof binary) {
                binary b = (binary) s;
                if (!b.op1.is_constant) {
                    updateReachingDef_(b.op1.id, blk);
                    if (getVarDef(b.op1.id) != null) b.op1.id = getVarDef(b.op1.id).id;
                }
                if (!b.op2.is_constant) {
                    updateReachingDef_(b.op2.id, blk);
                    if (getVarDef(b.op2.id) != null) b.op2.id = getVarDef(b.op2.id).id;
                }
            } else if (s instanceof branch) {
                branch b = (branch) s;
                if (!b.flag.is_constant) {
                    updateReachingDef_(b.flag.id, blk);
                    if (getVarDef(b.flag.id) != null) b.flag.id = getVarDef(b.flag.id).id;
                }
            } else if (s instanceof ret) {
                ret r = (ret) s;
                if (r.value != null && !r.value.is_constant) {
                    updateReachingDef_(r.value.id, blk);
                    if (getVarDef(r.value.id) != null) r.value.id = getVarDef(r.value.id).id;
                }
            } else if (s instanceof assign) {
                assign a = (assign) s;
                if (a.rhs.id != null && !a.rhs.is_constant) {
                    updateReachingDef_(a.rhs.id, blk);
                    if (getVarDef(a.rhs.id) != null) a.rhs.id = getVarDef(a.rhs.id).id;
                }
            } else if (s instanceof define) {
                define d = (define) s;
                if (d.assign != null && !d.assign.is_constant) {
                    updateReachingDef_(d.assign.id, blk);
                    if (getVarDef(d.assign.id) != null) d.assign.id = getVarDef(d.assign.id).id;
                }
                    updateReachingDef_(d.var.id, blk);
                    String v_ = createCopy(d.var.id);
                    String pre_id = d.var.id;
                    d.var.id = v_;
                    varNode new_v = new varNode(v_, blk, getVarDef(pre_id));
                    varNodes.get(currentFun).add(new_v);
                    for (varNode x : varNodes.get(currentFun)){
                        if (x.id.equals(pre_id)) x.def = new_v;
                    }
            } else if (s instanceof load) {
                load l = (load) s;
                if (l.addr != null && l.addr.id != null && !l.addr.id.startsWith("%")) {
                    updateReachingDef_(l.addr.id, blk);
                    if (getVarDef(l.addr.id) != null) l.addr.id = getVarDef(l.addr.id).id;
                }
                if (l.to != null && l.to.id != null) {
                    updateReachingDef_(l.to.id, blk);
                    if (getVarDef(l.to.id) != null) l.to.id = getVarDef(l.to.id).id;
                }
                if (l.id != null && l.id.id != null) {
                    updateReachingDef_(l.id.id, blk);
                    if (getVarDef(l.id.id) != null) l.id.id = getVarDef(l.id.id).id;
                }
            } else if (s instanceof store) {
                store s_ = (store) s;
                if (s_.value != null && s_.value.id != null){
                    updateReachingDef_(s_.value.id, blk);
                    if (getVarDef(s_.value.id) != null) s_.value.id = getVarDef(s_.value.id).id;
                }
                if (s_.id != null && s_.id.id != null){
                    updateReachingDef_(s_.id.id, blk);
                    String v_ = createCopy(s_.id.id);
                    String pre_id = s_.id.id;
                    s_.id.id = v_;
                    varNode new_v = new varNode(v_, blk, getVarDef(pre_id));
                    varNodes.get(currentFun).add(new_v);
                    for (varNode x : varNodes.get(currentFun)){
                        if (x.id.equals(pre_id)) x.def = new_v;
                    }
                    index2blk.get(blk).stmts.remove(i);
                    index2blk.get(blk).stmts.add(i, new define(new entity(s_.id), new entity(s_.value)));
                }
                if (s_.addr != null && s_.addr.id.startsWith("@")){
                    updateReachingDef_(s_.addr.id, blk);
                    String v_ = createCopy(s_.addr.id);
                    String pre_id = s_.addr.id;
                    s_.addr.id = v_;
                    varNode new_v = new varNode(v_, blk, getVarDef(pre_id));
                    varNodes.get(currentFun).add(new_v);
                    for (varNode x : varNodes.get(currentFun)){
                        if (x.id.equals(pre_id)) {
                            x.def = new_v;
//                            System.out.println("update def of " + x.id );
                        }
                    }
                    index2blk.get(blk).stmts.remove(i);
                    index2blk.get(blk).stmts.add(i, new define(new entity(s_.addr), new entity(s_.value)));
                }
            } else if (s instanceof phi){
                phi p = (phi) s;
                updateReachingDef_(p.born.id, blk);
                String v_ = createCopy(p.born.id);
                String pre_id = p.born.id;
                p.born.id = v_;
                varNode new_v = new varNode(v_, blk, getVarDef(pre_id));
                varNodes.get(currentFun).add(new_v);
                for (varNode x : varNodes.get(currentFun)){
                    if (x.id.equals(pre_id)) x.def = new_v;
                }
            }
        }
        for (block b_ : index2blk.get(blk).successors()){
            if (b_ != null) {
                for (statement s : b_.stmts){
                    if (s instanceof phi){
                        phi p = (phi) s;
                        String phi_id = returnID(p.born.id);
                        updateReachingDef_(phi_id, blk);
                        p.varList.add(getVarDef(phi_id) == null? null: new entity(getVarDef(phi_id).id));
                        p.blkList.add(blk);
                    }
                }
            }
        }
        if (dom2sub.containsKey(blk)) {
            for (Integer sub : dom2sub.get(blk)) {
                rename(sub);
            }
        }
    }

    public void insertCopy(Integer blk){
        if (inserted.contains(blk)) return;
        else inserted.add(blk);
        for (statement s : index2blk.get(blk).stmts){
            if (s instanceof phi){
                phi p = (phi) s;
                for (entity var : p.varList){
                    if (var != null){
                        define deToInsert = new define(new entity(p.born), new entity(var));
                        block blkToInsert = index2blk.get(p.blkList.get(p.varList.indexOf(var)));
                        Integer index = blkToInsert.stmts.size();
                        for (int i = index - 1; i >= 0; --i){
                            if (blkToInsert.stmts.get(i) instanceof jump || blkToInsert.stmts.get(i) instanceof branch){
                                index --;
                            } else {
                                break;
                            }
                        }
                        if (index == blkToInsert.stmts.size()){
                            blkToInsert.stmts.add(deToInsert);
                        } else {
                            blkToInsert.stmts.add(index, deToInsert);
                        }
                    }
                }
            }
        }
        for (block b : index2blk.get(blk).successors()){
            insertCopy(b.index);
        }
    }

    public void updateReachingDef_(String id, Integer i){
        if (!id.startsWith("_TMP") && !id.equals("_SP")  && !id.equals("_S0") && !isNumeric(id)){
            if (!(id.startsWith("_A") && isNumeric(id.substring(2, 3)))){
                updateReachingDef(id, i);
            }
        }
    }

    public varNode getVarDef(String id){
        for (varNode x : varNodes.get(currentFun)){
            if (x.id.equals(id)) return x.def;
        }
        return null;
    }

    public String returnID(String id){
        for (int i = 1; i <= id.length(); ++i){
            if (id.substring(0, i).contains("#")) {
                return id.substring(0, i - 1);
            }
        }
        return id;
    }

    public String createCopy(String id){
        id = returnID(id);
        if (!counter.containsKey(id)){
            counter.put(id, 0);
            return id + "#0";
        } else {
            Integer cnt = counter.get(id);
            counter.remove(id);
            counter.put(id, ++cnt);
            return id + "#" + cnt;
        }
    }

    public void updateReachingDef(String v, Integer i){
//        System.out.println("update: " + v + ", " + i);
        varNode v_ = null;
        for (varNode x : varNodes.get(currentFun)){
//            System.out.println(x.id + "____" + (x.def == null ? "null":x.def.id));
            if (x.id.equals(v)) v_ = x;
        }
        varNode r = v_.def;
        while (r != null && (!DT.containsKey(i) || !DT.get(i).contains(r.blk)) && r.blk != i) {
            r = r.def;
        }
        v_.def = r;
    }

    private Integer getBlockName(block b) {
        if (block2index.containsKey(b)) return block2index.get(b);
        else {
            block2index.put(b, blockCnt++);
            return blockCnt - 1;
        }
    }
}
