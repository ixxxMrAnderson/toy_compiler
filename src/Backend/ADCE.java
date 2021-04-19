package Backend;

import MIR.block;

import java.util.HashMap;

public class ADCE implements Pass{
    public ADCE(HashMap<String, block> blocks){
        for (String name : blocks.keySet()){
            visitBlock(blocks.get(name));
            for (block nxt : blocks.get(name).successors()) visitBlock(nxt);
        }
    }

    @Override
    public void visitBlock(block b) {

    }
}
