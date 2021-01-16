package AST;
import Util.position;

public class varExprNode extends ExprNode {
    public String id;
    public typeNode type;

    public varExprNode(String id, position pos) {
        super(pos);
        this.id = id;
        this.isAssignable = true;
    }

    @Override
    public varExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
