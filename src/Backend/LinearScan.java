package Backend;

import MIR.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LinearScan implements Pass{

    public Integer sp = 0, currentIndex = 0;
    public block currentBlk;
    public HashMap<String, Integer> currentStack = new HashMap<>();
    public HashSet<block> allocated = new HashSet<>();
    public HashMap<String, ArrayList<interval>> liveIntervals = new HashMap();
    public HashMap<Integer, HashSet<Integer>> dom2sub;
    public ArrayList<interval> active = new ArrayList<>();
    public HashMap<interval, Integer> register = new HashMap<>();
    public HashSet<Integer> regPool = new HashSet<>();
    private Integer MAX = 18;


    public LinearScan(HashMap<String, block> blocks,
                      HashMap<String, HashMap<String, Integer>> stackAlloc,
                      HashMap<Integer, HashSet<Integer>> dom2sub){
        this.dom2sub = dom2sub;
        for (int i = 5; i < 31; ++i) {
            if ((i <= 17 && i >= 10) || i == 8){

            } else {
                regPool.add(i);
            }
        }
        for (String i : blocks.keySet()){
            if (i.equals("_VAR_DEF")) continue;
            visitBlock(blocks.get(i));
        }
        for (String i : blocks.keySet()){
            currentStack = new HashMap<>();
            sp = 0;
            for (interval j : liveIntervals.get(i)){
                expireOldInterval(j);
                if (active.size() == MAX) spillInterval(j);
                else {
                    Integer toAllocate = -1;
                    for (Integer k : regPool){
                        toAllocate = k;
                        break;
                    }
                    register.put(j, toAllocate);
                    regPool.remove(toAllocate);
                    addActive(j);
                }
            }
            stackAlloc.put(i, currentStack);
        }
    }

    public void expireOldInterval(interval i){
        for (int j = 0; j < active.size(); ++j){
            if (cmpStartEnd(i, active.get(j))) return;
            active.remove(j);
            regPool.add(register.get(j));
        }
    }

    public void spillInterval(interval i){
        interval spill = active.get(active.size() - 1);
        if (cmpEnd(i, spill)){
            register.put(i, register.get(spill));
            active.remove(spill);
            addActive(i);
            sp += 4;
            currentStack.put(returnID(spill.id), sp);
        } else {
            sp += 4;
            currentStack.put(returnID(i.id), sp);
        }
    }

    public void addActive(interval i){
        for (int j = 0; j < active.size(); ++j){
            if (cmpEnd(i, active.get(j))){
                active.add(j, i);
                return;
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

    @Override
    public void visitBlock(block blk) {
        if (allocated.contains(blk)) return;
        else allocated.add(blk);
        currentBlk = blk;
        if (blk != null) {
            for (currentIndex = 0; currentIndex < blk.stmts.size(); ++currentIndex) {
                statement s = blk.stmts.get(currentIndex);
                if (s instanceof binary) {
                    binary b = (binary) s;
                    updateInterval(b.op1, blk.index, currentIndex);
                    updateInterval(b.op2, blk.index, currentIndex);
                    createInterval(b.lhs, blk.index, currentIndex);
                } else if (s instanceof jump) {

                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    updateInterval(b.flag, blk.index, currentIndex);
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null) updateInterval(r.value, blk.index, currentIndex);
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    createInterval(a.lhs, blk.index, currentIndex);
                    updateInterval(a.rhs, blk.index, currentIndex);
                } else if (s instanceof call) { // clear the table (no caller safe)

//                } else if (s instanceof define) {
//                    define d = (define) s;
////                    String id = returnID(d.var.id);
////                    System.out.println("REG----------define----------" + d.var.id + "--------" + currentStack);
////                    if (!id.startsWith("@") && !id.startsWith("%") && !currentStack.containsKey(returnID(id))) {
////                        sp += 4;
////                        currentStack.put(returnID(id), sp);
////                    }
//                    if (d.assign != null) {
////                        defined.add(d.var.id);
//                        updateInterval(d.assign, blk.index, currentIndex);
//                        createInterval(d.var, blk.index, currentIndex);
//                        d.toAssign = true;
//                    }
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (l.addr != null) updateInterval(l.addr, blk.index, currentIndex);
                    updateInterval(l.to, blk.index, currentIndex);
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    if (s_.addr != null) {
                        if (!s_.addr.id.startsWith("@")) {
//                            updateInterval(b.lhs, blk.index, currentIndex);
                            System.out.println("noooooooooooooooooooooooo_way");
                        }
                        else updateInterval(s_.addr, blk.index, currentIndex);
                    }
                    updateInterval(s_.value, blk.index, currentIndex);
                }
            }
            blk.successors().forEach(this::visitBlock);
            if (blk.optAndBlk != null) visitBlock(blk.optAndBlk);
            if (blk.optOrBlk != null) visitBlock(blk.optOrBlk);
        }
    }

    public String returnID(String id){
        for (int i = 1; i <= id.length(); ++i){
            if (id.substring(0, i).contains("#")) {
                return id.substring(0, i - 1);
            }
        }
        return id;
    }

    public boolean cmpStart(interval a, interval b){ // return a < b
        return true;
    }

    public boolean cmpEnd(interval a, interval b){
        return true;
    }

    public boolean cmpStartEnd(interval a, interval b){
        return true;
    }

    public void updateInterval(entity e, Integer blk, Integer index){
        if (e.is_constant){

        } else {

        }
    }

    public void createInterval(entity e, Integer blk, Integer index){
        if (e.is_constant){

        } else {

        }
    }
}
