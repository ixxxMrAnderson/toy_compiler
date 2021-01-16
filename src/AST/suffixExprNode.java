package AST;
import Util.position;

public class suffixExprNode extends ExprNode {
    public enum Op {
        INCREASE, DECREASE
    };

    public Op op;
    public ExprNode expr;

    public suffixExprNode(position pos, suffixExprNode.Op op, ExprNode expr) {
        super(pos);
        this.op = op;
        this.expr = expr;
    }

    @Override
    public suffixExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
