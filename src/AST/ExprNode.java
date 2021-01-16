package AST;
import Util.position;
import Util.Type;

public abstract class ExprNode extends ASTNode {
    public Type expr_type;
    public boolean isAssignable;

    public ExprNode(position pos) {
        super(pos);
        this.isAssignable = false;
        this.expr_type = new Type();
    }
}
