package AST;
import Util.position;

public class continueStmtNode extends StmtNode {
    public ASTNode loop;

    public continueStmtNode(position pos) {
        super(pos);
    }

    @Override
    public continueStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
