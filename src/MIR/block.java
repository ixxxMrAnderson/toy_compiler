package MIR;

import java.util.ArrayList;

public class block {
    public ArrayList<statement> stmts = new ArrayList<>();
    public block nxtBlock = null;
    public block tailBlk = null;
    public block optAndBlk = null;
    public block optOrBlk = null;
    public Integer index;

    public block() {}
    public void push_back(statement stmt) {
        stmts.add(stmt);
    }
    public statement tail(){return stmts.get(stmts.size() - 1);}
    public void pop(){
        stmts.remove(stmts.size() - 1);
    }
    public ArrayList<block> successors() {
        ArrayList<block> ret = new ArrayList<>();
        if (nxtBlock != null) ret.add(nxtBlock);
        if (optOrBlk != null) ret.add(optOrBlk);
        if (optAndBlk != null) ret.add(optAndBlk);
        for (int i = 0; i < stmts.size(); ++i){
            statement s = stmts.get(i);
            if (s instanceof jump){
                jump j = (jump) s;
                if (j.destination != null && j.destination != this && !ret.contains(j.destination)){
                    ret.add(j.destination);
                }
                break;
            } else if (s instanceof branch){
                branch b = (branch) s;
                if (b.trueBranch != null && !ret.contains(b.trueBranch)) {
                    ret.add(b.trueBranch);
                }
                if (b.falseBranch != null && !ret.contains(b.falseBranch)) {
                    ret.add(b.falseBranch);
                }
            } else if (s instanceof ret) break;
        }
        return ret;
    }
}
