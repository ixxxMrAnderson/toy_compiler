package Util;

import Util.error.semanticError;
import MIR.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Scope {

    private HashSet<DefinedVariable> members;
    private HashSet<String> members_name;
    public HashMap<String, entity> entities = new HashMap<>();
    public HashMap<String, ArrayList<entity>> array_size = new HashMap<>();
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

    public void defineVariable(String name, Type type, position pos, entity ent) {
        if (this.parent_scope == null) System.out.println("is global");
        System.out.println("defining " + name);
        DefinedVariable tmp = new DefinedVariable(name, type);
        if (members_name != null && members_name.contains(name))
            throw new semanticError("Semantic Error: variable redefine", pos);
        members.add(tmp);
        members_name.add(name);
        entities.put(name, ent);
    }

    public boolean containsVariable(String name, boolean lookUpon) {
        if (members != null){
            for (DefinedVariable tmp : members) {
                if (tmp.id.equals(name)) return true;
            }
        }
        if (parent_scope != null && lookUpon)
            return parent_scope.containsVariable(name, true);
        else return false;
    }

    public boolean isGlobl(String name) {
        if (members != null){
            for (DefinedVariable tmp : members) {
                if (tmp.id.equals(name) && parent_scope != null) return false;
                if (tmp.id.equals(name) && parent_scope == null) return true;
            }
        }
        if (parent_scope != null)
            return parent_scope.isGlobl(name);
        return false;
    }

    public Type getType(String name, boolean lookUpon) {
        if (members_name != null && members_name.contains(name)) {
            for (DefinedVariable tmp : members)
                if (tmp.id.equals(name))
                    return new Type(tmp.type);
        } else if (parent_scope != null && lookUpon) {
            return parent_scope.getType(name, true);
        }
        return null;
    }

    public entity getArraySize(String name, Integer index){
        if (members_name != null && members_name.contains(name)) {
            return array_size.get(name).get(index);
        } else if (parent_scope != null) {
            return parent_scope.getArraySize(name, index);
        }
        return null;
    }

    public entity getEntity(String name, boolean lookUpon) {
        if (members_name != null && members_name.contains(name)) {
            return entities.get(name);
        } else if (parent_scope != null && lookUpon) {
            return parent_scope.getEntity(name, true);
        }
        return null;
    }

    public void setEntity(String name, entity ent, boolean lookUpon) {
        if (members_name != null && members_name.contains(name)) {
            entities.put(name, ent);
        } else if (parent_scope != null && lookUpon) {
            parent_scope.setEntity(name, ent,true);
        }
    }
}
