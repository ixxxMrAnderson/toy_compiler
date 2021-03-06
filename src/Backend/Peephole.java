package Backend;

import MIR.*;

import java.util.HashMap;
import java.util.HashSet;

public class Peephole {
    public HashSet<Integer> visited = new HashSet<>();

    public Peephole(HashMap<String, block> blocks) {
        for (String name : blocks.keySet()) {
//                System.out.println(name);
            visitBlock(blocks.get(name));
        }
    }

    public void visitBlock(block blk){
        if (visited.contains(blk.index)) return;
        else visited.add(blk.index);
//        System.out.println("________visit______________________"+blk.index);
        for (int j = 0; j < blk.stmts.size(); ++j){
            statement s = blk.stmts.get(j);
            if (s instanceof assign && ((assign) s).lhs.id.startsWith("_A")){
                for (int k = j; k < blk.stmts.size(); ++k) {
//                    System.out.println("______________________________________"+((assign) s).lhs.id);
                    statement t = blk.stmts.get(k);
                    if (t instanceof assign){
                        if (((assign) t).lhs.id.startsWith("_A")) {
//                            System.out.println("continue1");
                            continue;
                        }
                        if (((assign) t).rhs == null || ((assign) t).rhs.is_constant) {
//                            System.out.println("break1");
                            break;
                        }
                        if (!((assign) t).rhs.id.startsWith("_A")) {
//                            System.out.println("break2");
                            break;
                        }
                        if (!((assign) t).rhs.id.equals(((assign) s).lhs.id)) {
//                            System.out.println("continue2: ");
                            continue;
                        }
//                        System.out.println("remove " + ((assign) t).lhs.id+" "+k+" -> "+j);
                        ((assign) s).lhs.id = ((assign) t).lhs.id;
                        blk.stmts.remove(t);
                        break;
                    } else break;
                }
            }
        }
        if (blk.successors().size() == 0){
//            Integer mvCnt = 0;
//            while (true){
//                mvCnt = 0;
//                entity lhs = null, rhs = null;
//                for (int j = 0; j < blk.stmts.size(); ++j){
//                    statement s = blk.stmts.get(j);
//                    if (s instanceof assign) {
//                        mvCnt++;
//
//                    }
//                }
//            }
        } else {
            for (block b : blk.successors()) visitBlock(b);
        }
    }
}
