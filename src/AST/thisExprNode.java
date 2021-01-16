package AST;
import Util.position;

public class thisExprNode extends ExprNode {

    public String class_id;

    public thisExprNode(position pos) {
        super(pos);
    }

    @Override
    public thisExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}