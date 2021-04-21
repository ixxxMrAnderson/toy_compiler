package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;

public class SSA implements Pass{

    private Integer blockCnt = 0;
    private HashSet<Integer> visited = new HashSet<>();
    private HashMap<block, Integer> block2index = new HashMap<>();
    private HashMap<Integer, block> index2blk = new HashMap<>();
    private HashMap<String, HashSet<Integer>> blks = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> preBlk = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> DT = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> DF = new HashMap<>();
    private HashMap<String, Integer> fun2entry = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> dom2sub;
    private String currentFun;
    private HashMap<String, HashMap<String, HashSet<Integer>>> definedVar = new HashMap<>();

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
        }
//        generateDT();
//        generateDF();
//        for (Integer index : index2blk.keySet()){ // building dom tree
//            if (fun2entry.containsValue(index)) continue;
//            Integer dom = idom(index);
//            if (!dom2sub.containsKey(dom)) dom2sub.put(dom, new HashSet<>());
//            dom2sub.get(dom).add(index);
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
            for (statement s : blk.stmts) {
                if (s instanceof binary) {
                    binary b = (binary) s;
                    add_D(blk, b.lhs.id);
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (a.rhs != null) add_D(blk, a.lhs.id);
                } else if (s instanceof load) {
                    load l = (load) s;
                    add_D(blk, l.to.id);
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

    private void add_D(block blk, String id){
        if (!id.equals("_SP")  && !id.equals("_S0") ){
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

    private Integer getBlockName(block b) {
        if (block2index.containsKey(b)) return block2index.get(b);
        else {
            block2index.put(b, blockCnt++);
            return blockCnt - 1;
        }
    }
}
