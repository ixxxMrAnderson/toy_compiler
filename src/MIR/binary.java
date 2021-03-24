package MIR;

import AST.binaryExprNode.Op;

public class binary extends statement {
    public entity lhs;
    public entity op1, op2;
    public Op op;
    public binary(entity lhs, entity op1, entity op2, Op op) {
        super();
        this.lhs = lhs;
        this.op1 = op1;
        this.op2 = op2;
        this.op = op;
    }
}
