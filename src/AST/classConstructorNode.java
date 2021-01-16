package AST;
import Util.position;
import java.util.ArrayList;

public class classConstructorNode extends SectionNode {
    public String name;
    public ArrayList<singleVarDefNode> parameters;
    public ArrayList<typeNode> parameters_type;
    public blockStmtNode suite;

    public classConstructorNode(position pos, String name, blockStmtNode suite) {
        super(pos);
        this.name = name;
        this.suite = suite;
        this.parameters = new ArrayList<>();
        this.parameters_type = new ArrayList<>();
    }

    public classConstructorNode(classConstructorNode other){
        super(other.pos);
        this.name =other.name;
        this.suite = new blockStmtNode (other.suite);
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
    public classConstructorNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}