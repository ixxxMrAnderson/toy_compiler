package MIR;

public class store extends statement{
    public entity addr;
    public entity value;

    public store(entity addr, entity value){
        this.addr = new entity(addr);
        this.value = new entity(value);
    }
}
