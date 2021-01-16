package AST;
import Util.position;

public class binaryExprNode extends ExprNode {
    public ExprNode lhs, rhs;
    public enum Op {
        MUL, DIV, MOD, ADD, SUB, SLA, SRA, GTH, LTH, GEQ, LEQ, EQ, NEQ,
        BITWISE_AND, BITWISE_XOR, BITWISE_OR, AND, OR
    }
    public Op op;

    public binaryExprNode(position pos, Op op, ExprNode lhs, ExprNode rhs) {
        super(pos);
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public binaryExprNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
