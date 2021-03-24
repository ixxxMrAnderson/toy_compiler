package AST;
import Util.position;
import Util.Type;
import MIR.entity;

public abstract class ExprNode extends ASTNode {
    public Type expr_type;
    public boolean isAssignable;
    public entity val;

    public ExprNode(position pos) {
        super(pos);
        this.isAssignable = false;
        this.expr_type = new Type();
        this.val = new entity();
    }
}
