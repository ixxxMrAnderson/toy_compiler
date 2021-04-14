package MIR;

public class varNode {
    public String id;
    public Integer blk;
    public varNode def;

    public varNode(){
        def = null;
    }

    public varNode(String id){
        this.id = id;
        def = null;
    }

    public varNode(String id, Integer blk, varNode def){
        this.id = id;
        this.blk = blk;
        this.def = def;
    }
}
