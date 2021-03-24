package MIR;

public class getPtr extends statement{
    public String id;
    public entity ret;
    public getPtr(String id, entity ret){
        this.id = id;
        this.ret = new entity(ret);
    }
}
