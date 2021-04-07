package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;

public class SSA implements Pass{

    private Integer blockCnt = 0;
    private HashSet<Integer> visited = new HashSet<>();
    private HashMap<String, Integer> counter = new HashMap<>();
    private HashMap<block, Integer> blockIndex = new HashMap<>();
    private HashMap<Integer, HashSet<String>> D_chain = new HashMap<>();
    private HashMap<Integer, HashSet<String>> U_chain = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> preBlk = new HashMap<>();

    public SSA(HashMap<String, block> b){
        for (String name : b.keySet()){
            if (name.equals("_VAR_DEF")) continue;
            visitBlock(b.get(name));
            visitBlock(b.get(name).tailBlk);
        }
        System.out.println(U_chain);
        System.out.println(preBlk);
    }

    @Override
    public void visitBlock(block blk) {
        if (visited.contains(getBlockName(blk))) return;
        else visited.add(getBlockName(blk));
        if (blk != null) {
            U_chain.put(getBlockName(blk), new HashSet<>());
            D_chain.put(getBlockName(blk), new HashSet<>());
            for (statement s : blk.stmts) {
                if (s instanceof binary) {
                    binary b = (binary) s;
                    if (!b.op1.is_constant) add_U(blk, b.op1.id);
                    if (!b.op2.is_constant) add_U(blk, b.op2.id);
                } else if (s instanceof jump) {

                } else if (s instanceof branch) {
                    branch b = (branch) s;
                    if (!b.flag.is_constant) add_U(blk, b.flag.id);
                } else if (s instanceof ret) {
                    ret r = (ret) s;
                    if (r.value != null) {
                        if (!r.value.is_constant) add_U(blk, r.value.id);
                    }
                    break;
                } else if (s instanceof assign) {
                    assign a = (assign) s;
                    if (!a.rhs.is_constant) add_U(blk, a.rhs.id);
                } else if (s instanceof call) {

                } else if (s instanceof define) {
                    define d = (define) s;
                    if (d.assign != null) {
                        if (!d.assign.is_constant) add_U(blk, d.assign.id);
                    }
//                    if (!d.var.id.startsWith("_TMP")) D_chain.get(blk).add(d.var.id);
                } else if (s instanceof load) {
                    load l = (load) s;
                    if (l.addr == null) add_U(blk, l.id.id);
                } else if (s instanceof store) {
                    store s_ = (store) s;
                    add_U(blk, s_.value.id);
                    if (s_.addr == null) add_U(blk, s_.id.id);
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

    private Integer getBlockName(block b) {
        if (blockIndex.containsKey(b)) return blockIndex.get(b);
        else {
            blockIndex.put(b, blockCnt++);
            return blockCnt - 1;
        }
    }
}
