package Util;

import Util.error.semanticError;
import java.util.HashSet;

public class Scope {

    private HashSet<DefinedVariable> members;
    private HashSet<String> members_name;
    private Scope parent_scope;

    public Scope(Scope parent_scope) {
        members = new HashSet<>();
        members_name = new HashSet<>();
        this.parent_scope = parent_scope;
    }

    public Scope parentScope() {
        return parent_scope;
    }

    public void defineVariable(String name, Type type, position pos) {
        DefinedVariable tmp = new DefinedVariable(name, type);
        if (members_name != null && members_name.contains(name))
            throw new semanticError("Semantic Error: variable redefine", pos);
        members.add(tmp);
        members_name.add(name);
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
        if (members_name != null && members_name.contains(name)) {
//            System.out.println("get type: "+name);
            for (DefinedVariable tmp : members)
                if (tmp.id.equals(name))
                    return new Type(tmp.type);
        } else if (parent_scope != null && lookUpon) {
            return parent_scope.getType(name, true);
        }
        return null;
    }
}
