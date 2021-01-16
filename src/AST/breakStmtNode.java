package AST;
import Util.position;

public class breakStmtNode extends StmtNode {
    public ASTNode loop;

    public breakStmtNode(position pos) {
        super(pos);
    }

    @Override
    public breakStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
