package MIR;

import java.util.ArrayList;

public class block {
    public ArrayList<statement> stmts = new ArrayList<>();
    private terminalStmt tailStmt = null;
    public block nxtBlock = null;
    public block tailBlk = null;
    public block optAndBlk = null;
    public block optOrBlk = null;
    public Integer index;

    public block() {}
    public void push_back(statement stmt) {
        stmts.add(stmt);
        if (stmt instanceof terminalStmt) {
            tailStmt = (terminalStmt)stmt;
        }
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
        if (tailStmt instanceof branch) {
            if (((branch) tailStmt).trueBranch != null && !ret.contains(((branch) tailStmt).trueBranch)) {
                ret.add(((branch) tailStmt).trueBranch);
            }
            if (((branch) tailStmt).falseBranch != null && !ret.contains(((branch) tailStmt).falseBranch)) {
                ret.add(((branch) tailStmt).falseBranch);
            }
        } else if (tailStmt instanceof jump) {
            if (((jump) tailStmt).destination != this && !ret.contains(((jump) tailStmt).destination)) {
                ret.add(((jump) tailStmt).destination);
            }
        }
        return ret;
    }
}
