package AST;
import java.util.ArrayList;
import Util.position;

public class funCallExprNode extends ExprNode {
    public ExprNode function_id;
    public ArrayList<ExprNode> parameters;

    public funCallExprNode(position pos, ExprNode function_id) {
        super(pos);
        this.function_id = function_id;
        this.parameters = new ArrayList<>();
    }

    @Override
    public funCallExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
