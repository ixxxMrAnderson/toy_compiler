package AST;
import Util.position;
import java.util.ArrayList;

public class varDefNode extends SectionNode {
    public typeNode type;
    public ArrayList<singleVarDefNode> variables;

    public varDefNode(position pos, typeNode type) {
        super(pos);
        this.type = type;
        this.variables = new ArrayList<>();
    }

    public varDefNode(varDefNode other){
        super(other.pos);
        this.type = new typeNode(other.type);
        this.variables = new ArrayList<>();
        for (singleVarDefNode var : other.variables){
            this.variables.add(new singleVarDefNode(var));
        }
    }

    @Override
    public varDefNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}
