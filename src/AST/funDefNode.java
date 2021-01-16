package AST;
import Util.Type;
import Util.position;

import java.util.ArrayList;

public class funDefNode extends SectionNode {
    public typeNode type;
    public String name;
    public ArrayList<singleVarDefNode> parameters;
    public ArrayList<typeNode> parameters_type;
    public blockStmtNode suite;

    public funDefNode(position pos, String name, blockStmtNode body, typeNode type) {
        super(pos);
        this.name = name;
        this.suite = body;
        this.parameters = new ArrayList<>();
        this.parameters_type = new ArrayList<>();
        this.type = new typeNode(pos);
        this.type.type.type = type.type.type;
        this.type.type.dimension = type.type.dimension;
        this.type.type.class_id = type.type.class_id;
    }

    public funDefNode(position pos) {
        super(pos);
        this.suite = new blockStmtNode(pos);
        this.parameters = new ArrayList<>();
        this.parameters_type = new ArrayList<>();
        this.type = new typeNode(pos);
    }

    public funDefNode(funDefNode other) {
        super(other.pos);
        this.name =other.name;
        this.suite = new blockStmtNode (other.suite);
        this.type = new typeNode(other.type);
        this.parameters = new ArrayList<>();
        this.parameters_type = new ArrayList<>();
        for (typeNode type : other.parameters_type){
            this.parameters_type.add(new typeNode(type));
        }
        for (singleVarDefNode var : other.parameters){
            this.parameters.add(new singleVarDefNode(var));
        }
    }

    @Override
    public funDefNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
