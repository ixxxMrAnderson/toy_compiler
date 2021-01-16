package AST;
import Util.position;

public class assignExprNode extends ExprNode{
    public ExprNode lhs, rhs;
    public typeNode type;

    public assignExprNode(ExprNode lhs, ExprNode rhs, position pos) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public assignExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
