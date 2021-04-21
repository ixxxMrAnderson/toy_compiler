package MIR;

public class defineNode {
    public statement def;
    public Integer blk;

    public defineNode(statement def, Integer blk){
        this.def = def;
        this.blk = blk;
    }

}
