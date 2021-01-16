package AST;
import Util.position;

public class memberExprNode extends ExprNode {
    public ExprNode expr;
    public String member;

    public memberExprNode(position pos, ExprNode expr, String member) {
        super(pos);
        this.expr = expr;
        this.member = member;
        this.isAssignable = true;
    }

    @Override
    public memberExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
