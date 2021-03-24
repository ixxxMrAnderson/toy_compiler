package MIR;

public class load extends statement{
    public entity addr;
    public entity to;
    public load(entity addr, entity to){
        this.addr = new entity(addr);
        this.to = new entity(to);
    }
}
