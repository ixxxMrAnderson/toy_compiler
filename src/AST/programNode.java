package AST;
import Util.Type;
import Util.error.semanticError;
import Util.position;
import java.util.ArrayList;
import java.util.HashSet;

public class programNode extends ASTNode {
    public ArrayList<SectionNode> sections;
    public HashSet<classDefNode> defined_class;
    public HashSet<funDefNode> defined_function;
    public HashSet<String> defined_function_name;
    public HashSet<String> defined_class_name;

    public programNode(position pos) {
        super(pos);
        this.sections = new ArrayList<>();
        this.defined_class = new HashSet<>();
        this.defined_function = new HashSet<>();
        this.defined_class_name = new HashSet<>();
        this.defined_function_name = new HashSet<>();
    }

    public void defineFunction(position pos, String name, Type.type return_type, ArrayList<typeNode> para){
        defined_function_name.add(name);
        typeNode return_type_ = new typeNode(pos);
        return_type_.type.type = return_type;
        funDefNode fun_ = new funDefNode(pos);
        fun_.type = return_type_;
        for (typeNode type : para){
            fun_.parameters_type.add(type);
        }
        fun_.name = name;
        defined_function.add(fun_);
    }

    public funDefNode getFunction(String name, position pos){
        for (funDefNode fun : defined_function) {
//            System.out.println(fun.name + "___" + name);
            if (fun.name.equals(name)){
//                System.out.println("getting_many: "+fun.type.type.dimension);
                return new funDefNode(fun);
            }
        }
        return null;
    }

    public classDefNode getClass(String name, position pos){
        classDefNode node = new classDefNode(pos, name);
        if (!defined_class_name.contains(name)) throw new semanticError("Semantic Error: class_defined", pos);
        for (classDefNode class_ : defined_class){
            if (class_.id.equals(name)){
                node.members = class_.members;
                node.functions = class_.functions;
                node.constructor = class_.constructor;
            }
        }
        return node;
    }

    @Override
    public programNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}