package AST;
import Util.position;

public class whileStmtNode extends StmtNode{
    public ExprNode condition;
    public StmtNode body;

    public whileStmtNode(position pos, ExprNode condition, StmtNode body) {
        super(pos);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public whileStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
