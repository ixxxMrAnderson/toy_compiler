package MIR;

public class branch extends terminalStmt {
    public entity flag;
    public block trueBranch, falseBranch;
    public branch(entity flag, block trueBranch, block falseBranch) {
        super();
        this.flag = flag;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }
}
