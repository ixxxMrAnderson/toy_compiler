package MIR;

public class load extends statement{
    public entity addr;
    public Integer sp;
    public entity to;
    public entity id;
    public load(Integer sp, entity to){
        this.addr = null;
        this.id = null;
        this.sp = sp;
        this.to = new entity(to);
    }

    public load(entity addr, entity to){
        this.id = null;
        this.addr = new entity(addr);
        this.to = new entity(to);
    }

    public load(entity id, entity to, boolean flag){
        this.addr = null;
        this.id = new entity(id);
        this.to = new entity(to);
    }
}
