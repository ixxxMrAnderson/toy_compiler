package Util;

import Util.error.semanticError;
import java.util.HashSet;

public class Scope {

    private HashSet<DefinedVariable> members;
    private Scope parent_scope;

    public Scope(Scope parent_scope) {
        members = new HashSet<>();
        this.parent_scope = parent_scope;
    }

    public Scope parentScope() {
        return parent_scope;
    }

    public void defineVariable(String name, Type type, position pos) {
        DefinedVariable tmp = new DefinedVariable(name, type);
        if (members != null){
            for (DefinedVariable tmp_ : members) {
                if (tmp_.id.equals(name))
                    throw new semanticError("Semantic Error: variable redefine", pos);
            }
        }
        members.add(tmp);
    }

    public boolean containsVariable(String name, boolean lookUpon) {
//        System.out.println("find:" + name + ".");
        if (members != null){
            for (DefinedVariable tmp : members) {
//                System.out.println("defined var: " + tmp.id);
                if (tmp.id.equals(name)) return true;
//                System.out.println("defined var:" + tmp.id + ".");
            }
        }
        if (parent_scope != null && lookUpon)
            return parent_scope.containsVariable(name, true);
        else return false;
    }

    public Type getType(String name, boolean lookUpon) {
//        System.out.println("getType: " + name);
        Type return_ = new Type();
        if (members != null){
            for (DefinedVariable tmp : members)
                if (tmp.id.equals(name)) {
//                    System.out.println(name + " dimension: " + tmp.type.dimension);
                    return_.type = tmp.type.type;
                    return_.class_id = tmp.type.class_id;
                    return_.dimension = tmp.type.dimension;
                    return return_;
                }
        }
        if (parent_scope != null && lookUpon)
            return parent_scope.getType(name, true);
        return return_;
    }
}
