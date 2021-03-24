package MIR;

public class ret extends terminalStmt{
    public entity value;
    public block retBlk;
    public ret(entity value, block retBlk) {
        this.value = value;
        this.retBlk = retBlk;
    }
}
