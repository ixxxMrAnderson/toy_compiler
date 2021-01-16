package AST;
import Util.position;
import java.util.ArrayList;

public class varDefStmtNode extends StmtNode {
    public typeNode type;
    public ArrayList<singleVarDefNode> variables;

    public varDefStmtNode(position pos, typeNode type) {
        super(pos);
        this.type = type;
        this.variables = new ArrayList<>();
    }

    @Override
    public varDefStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
