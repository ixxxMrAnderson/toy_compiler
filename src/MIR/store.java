package MIR;

public class store extends statement{
    public entity addr;
    public entity id;
    public entity value;

    public store(entity addr, entity value){
        this.addr = new entity(addr);
        this.id = null;
        this.value = new entity(value);
    }

    public store(entity id, entity value, boolean id_flag){
        this.addr = null;
        this.id = new entity(id);
        this.value = new entity(value);
    }

    public store(entity id, boolean id_flag){
        if (id_flag) {
            this.addr = null;
            this.id = new entity(id);
            this.value = null;
        } else {
            this.addr = null;
            this.id = null;
            this.value = new entity(value);
        }
    }
}
