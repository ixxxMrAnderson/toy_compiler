package MIR;

public class call extends statement{
    public String funID;
    public boolean inlined = false;

    public call(String funID) {
        this.funID = funID;
    }
}
