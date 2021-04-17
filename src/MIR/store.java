package MIR;

public class store extends statement{
    public entity addr;
    public Integer sp;
    public entity value;
    public entity id;

    public store(entity addr, entity value){
        this.addr = new entity(addr);
        this.value = new entity(value);
        this.id = null;
    }

    public store(entity id, entity value, boolean flag){
        this.addr = null;
        this.id = new entity(id);
        this.value = new entity(value);
    }

    public store(Integer sp, entity value){
        this.addr = null;
        this.id = null;
        this.sp = sp;
        this.value = new entity(value);
    }
}
