package MIR;

import java.util.ArrayList;

public class block {
    public ArrayList<statement> stmts = new ArrayList<>();
    private terminalStmt tailStmt = null;
    public block nxtBlock = null;
    public block tailBlk = null;

    public block() {}
    public void push_back(statement stmt) {
        stmts.add(stmt);
        if (stmt instanceof terminalStmt) {
            tailStmt = (terminalStmt)stmt;
        }
    }
    public void pop(){
        stmts.remove(stmts.size() - 1);
    }
    public ArrayList<block> successors() {
        ArrayList<block> ret = new ArrayList<>();
        ret.add(nxtBlock);
        if (tailStmt instanceof branch) {
            ret.add(((branch) tailStmt).trueBranch);
            ret.add(((branch) tailStmt).falseBranch);
        } else if (tailStmt instanceof jump) {
            if (((jump) tailStmt).destination == this) {
                ret.add(nxtBlock);
            } else {
                ret.add(((jump) tailStmt).destination);
            }
        }
        return ret;
    }
}
