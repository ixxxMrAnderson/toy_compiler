package AST;
import Util.position;

public class arrayExprNode extends ExprNode {
    public ExprNode array, index;
    public String id;

    public arrayExprNode(position pos, ExprNode array, ExprNode index) {
        super(pos);
        this.array = array;
        this.index = index;
        this.isAssignable = true;
    }

    @Override
    public arrayExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
