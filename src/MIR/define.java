package MIR;

public class define extends statement{
    public entity var;
    public entity assign;
    public boolean toAssign = false;

    public define(entity var, entity assign){
        this.var = var;
        this.assign = assign;
    }
}
