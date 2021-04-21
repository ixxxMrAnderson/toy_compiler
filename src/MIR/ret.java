package MIR;

public class ret extends statement {
    public entity value;
    public block retBlk;
    public ret(entity value, block retBlk) {
        this.value = value;
        this.retBlk = retBlk;
    }
}
