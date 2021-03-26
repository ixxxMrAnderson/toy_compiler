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
    public boolean isClassDef = false;

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

    public boolean isMember(String name){
//        System.out.println("isMember??????????????????????" + name);
//        System.out.println(members_name);
//        System.out.println(isClassDef);
        if (members_name != null){
            for (String tmp : members_name) {
                if (tmp.equals(name) && !isClassDef) return false;
                if (tmp.equals(name) && isClassDef) return true;
            }
        }
        if (parent_scope != null)
            return parent_scope.isMember(name);
        return false;
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public String getName(String name) {
        if (members != null){
            for (DefinedVariable tmp : members) {
//                System.out.println("id: " + tmp.id);
//                System.out.println("name: " + name);
                if (tmp.id.startsWith(name)){
                    if (tmp.id.equals(name)) return name;
                    if (tmp.id.length() > name.length() + 1 && isNumeric(tmp.id.substring(name.length() + 1))) return tmp.id;
                }
            }
        }
        if (parent_scope != null)
            return parent_scope.getName(name);
        else return null;
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
}
