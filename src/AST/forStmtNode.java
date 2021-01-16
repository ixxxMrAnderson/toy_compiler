package AST;
import Util.position;

public class forStmtNode extends StmtNode {
    public ExprNode init, condition, step;
    public StmtNode body;

    public forStmtNode(position pos, ExprNode init, ExprNode condition, ExprNode step, StmtNode body) {
        super(pos);
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = body;
    }

    @Override
    public forStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}