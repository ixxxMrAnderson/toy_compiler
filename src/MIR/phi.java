package MIR;

import java.util.ArrayList;

public class phi extends statement{
    public entity born;
    public ArrayList<entity> varList = new ArrayList<>();
    public ArrayList<Integer> blkList = new ArrayList<>();

    public phi(entity born){
        this.born = new entity(born);
    }
}
