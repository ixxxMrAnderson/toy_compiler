package AST;
import Util.Type;
import Util.position;

public class typeNode extends ASTNode {
    public Type type;

    public typeNode(position pos) {
        super(pos);
        this.type = new Type();
    }

    public typeNode(position pos, Type type) {
        super(pos);
        this.type = type;
    }

    public typeNode(typeNode other) {
        super(other.pos);
        this.type = new Type(other.type);
    }

    public boolean isArray() {
        return type.dimension > 0;
    }

    @Override
    public typeNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}