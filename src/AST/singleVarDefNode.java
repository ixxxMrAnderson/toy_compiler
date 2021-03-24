package AST;
import Util.position;
import MIR.*;

public class singleVarDefNode extends SectionNode {
    public String id;
    public ExprNode expr;

    public singleVarDefNode(position pos, String id, ExprNode expr) {
        super(pos);
        this.id = id;
        this.expr = expr;
    }

    public singleVarDefNode(singleVarDefNode other) {
        super(other.pos);
        this.id = other.id;
        this.expr = other.expr;
    }

    @Override
    public singleVarDefNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}