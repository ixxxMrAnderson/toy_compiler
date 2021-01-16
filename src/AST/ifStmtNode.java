package AST;
import Util.position;

public class ifStmtNode extends StmtNode {
    public ExprNode condition;
    public StmtNode then_stmt, else_stmt;

    public ifStmtNode(ExprNode condition, StmtNode then_stmt, StmtNode else_stmt, position pos) {
        super(pos);
        this.condition = condition;
        this.then_stmt = then_stmt;
        this.else_stmt = else_stmt;
    }

    @Override
    public ifStmtNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
