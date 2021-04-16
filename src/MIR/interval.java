package MIR;

public class interval {
    public Integer startBlk;
    public Integer startLine;
    public Integer endBlk;
    public Integer endLine;
    public String id;
    public interval (String id, Integer startBlk, Integer startLine, Integer endBlk, Integer endLine){
        this.id = id;
        this.startBlk = startBlk;
        this.startLine = startLine;
        this.endBlk = endBlk;
        this.endLine = endLine;
    }
}
