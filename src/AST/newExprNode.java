package AST;
import Util.position;
import java.util.ArrayList;

public class newExprNode extends  ExprNode {
    public typeNode type;
    public ArrayList<ExprNode> size;

    public newExprNode(position pos, typeNode type) {
        super(pos);
        this.type = type;
        this.size = new ArrayList<>();
    }

    @Override
    public newExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}