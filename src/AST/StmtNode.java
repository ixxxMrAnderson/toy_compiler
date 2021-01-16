package AST;
import Util.position;
import Util.Type;

public abstract class StmtNode extends ASTNode {
    public Type stmt_type;
    public StmtNode(position pos) {
        super(pos);
        this.stmt_type = new Type();
    }
}
