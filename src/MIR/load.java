package MIR;

public class load extends statement{
    public entity addr;
    public entity id;
    public entity to;
    public load(entity id, entity to, boolean id_true){
        this.addr = null;
        this.id = new entity(id);
        this.to = new entity(to);
    }

    public load(entity addr, entity to){
        this.addr = new entity(addr);
        this.id = null;
        this.to = new entity(to);
    }
}
