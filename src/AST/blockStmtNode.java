package AST;
import Util.position;
import java.util.ArrayList;

public class blockStmtNode extends StmtNode {
    public ArrayList<StmtNode> stmts;

    public blockStmtNode(position pos) {
        super(pos);
        this.stmts = new ArrayList<>();
    }

    public blockStmtNode(blockStmtNode other) {
        super(other.pos);
        stmts = new ArrayList<>();
        for (StmtNode stmt : other.stmts){
            stmts.add(stmt);
        }
    }

    @Override
    public blockStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
