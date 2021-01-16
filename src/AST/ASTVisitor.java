package AST;

public interface ASTVisitor {
    programNode visit(programNode it);

    classDefNode visit(classDefNode it);
    classConstructorNode visit(classConstructorNode it);
    funDefNode visit(funDefNode it);
    varDefNode visit(varDefNode it);
    singleVarDefNode visit(singleVarDefNode it);
    typeNode visit(typeNode it);

    blockStmtNode visit(blockStmtNode it);
    varDefStmtNode visit(varDefStmtNode it);
    returnStmtNode visit(returnStmtNode it);
    whileStmtNode visit(whileStmtNode it);
    forStmtNode visit(forStmtNode it);
    breakStmtNode visit(breakStmtNode it);
    continueStmtNode visit(continueStmtNode it);
    exprStmtNode visit(exprStmtNode it);
    ifStmtNode visit(ifStmtNode it);

    suffixExprNode visit(suffixExprNode it);
    prefixExprNode visit(prefixExprNode it);
    thisExprNode visit(thisExprNode it);
    newExprNode visit(newExprNode it);
    assignExprNode visit(assignExprNode it);
    binaryExprNode visit(binaryExprNode it);
    constExprNode visit(constExprNode it);
    varExprNode visit(varExprNode it);
    funCallExprNode visit(funCallExprNode it);
    memberExprNode visit(memberExprNode it);
    arrayExprNode visit(arrayExprNode it);
}
