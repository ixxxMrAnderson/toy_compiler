package AST;
import Util.position;

public class prefixExprNode extends ExprNode {
    public enum Op {
        INCREASE, DECREASE, POSITIVE, NEGATIVE, NOT, BITWISE_NOT
    };

    public Op op;
    public ExprNode expr;

    public prefixExprNode(position pos, prefixExprNode.Op op, ExprNode expr) {
        super(pos);
        this.op = op;
        this.expr = expr;
    }

    @Override
    public prefixExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
