package MIR;

public class phi extends statement{
    public entity lhs, rhs, born;

    public phi(entity lhs, entity rhs, entity born){
        this.lhs = new entity(lhs);
        this.rhs = new entity(rhs);
        this.born = new entity(born);
    }
}
