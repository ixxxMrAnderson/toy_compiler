package AST;
import Util.position;
import Util.Type.type;

public class constExprNode extends ExprNode {
    public int int_value;
    public String string_value;
    public boolean bool_value;

    public constExprNode(int value, position pos) {
        super(pos);
        this.int_value = value;
        this.expr_type.type = type.INT;
    }

    public constExprNode(constExprNode other, position pos) {
        super(pos);
        this.int_value = other.int_value;
        this.string_value = other.string_value;
        this.bool_value = other.bool_value;
        this.expr_type.type = other.expr_type.type;
    }

    public constExprNode(String value, position pos) {
        super(pos);
        this.string_value = value;
        this.expr_type.type = type.STRING;
    }

    public constExprNode(boolean value, position pos) {
        super(pos);
        this.bool_value = value;
        this.expr_type.type = type.BOOL;
    }

    public constExprNode(position pos) {
        super(pos);
        this.expr_type.type = type.NULL;
    }

    @Override
    public constExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
