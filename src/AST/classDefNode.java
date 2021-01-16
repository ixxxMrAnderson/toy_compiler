package AST;
import Util.Type;
import Util.position;
import java.util.ArrayList;

public class classDefNode extends SectionNode {
    public String id;
    public ArrayList<varDefNode> members;
    public ArrayList<funDefNode> functions;
    public ArrayList<classConstructorNode> constructor;

    public classDefNode(position pos, String id) {
        super(pos);
        this.id = id;
        this.members = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.constructor = new ArrayList<>();
    }

    public classDefNode(classDefNode other) {
        super(other.pos);
        this.id = other.id;
        this.members = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.constructor = new ArrayList<>();
        for (varDefNode var : other.members){
            this.members.add(new varDefNode(var));
        }
        for (funDefNode var : other.functions){
            this.functions.add(new funDefNode(var));
        }
        for (classConstructorNode var : other.constructor){
            this.constructor.add(new classConstructorNode(var));
        }
    }

    public boolean containFunction(String name){
        for (funDefNode fun : functions) {
            if (fun.name.equals(name)) return true;
        }
        return false;
    }

    public boolean containMember(String name){
        for (varDefNode vars : members) {
            for (singleVarDefNode var : vars.variables) {
                if (var.id.equals(name)) return true;
            }
        }
        return false;
    }

    public typeNode getMemberType(String name){
        for (varDefNode vars : members) {
            for (singleVarDefNode var : vars.variables) {
                if (var.id.equals(name)) return vars.type;
            }
        }
        return null;
    }

    public funDefNode getFunction(String name, position pos){
//        System.out.println("getfunction: "+name);
        funDefNode node = new funDefNode(pos);
        for (funDefNode fun : functions) {
//            System.out.println(fun.name + "___" + name);
            if (fun.name.equals(name)){
//                System.out.println("getting_many: "+fun.type.type.dimension);
                return new funDefNode(fun);
            }
        }
        return null;
    }

    public void defineFunction(position pos, String name, Type.type return_type, ArrayList<typeNode> para){
        typeNode return_type_ = new typeNode(pos);
        return_type_.type.type = return_type;
        funDefNode fun_ = new funDefNode(pos);
        fun_.type = return_type_;
        for (typeNode type : para){
            fun_.parameters_type.add(new typeNode(type));
        }
        fun_.name = name;
        functions.add(fun_);
    }

    @Override
    public classDefNode accept(ASTVisitor visitor) {
        return visitor.visit(this);
    }
}