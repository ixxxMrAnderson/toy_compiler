package Util;

public class Type {
    public enum type{
        INT, STRING, BOOL, VOID, CLASS, NULL
    }
    public type type;
    public String class_id;
    public int dimension;
    public Type(){
        this.type = type.NULL;
        dimension = 0;
    }

    public Type(type type){
        this.type = type;
        dimension = 0;
    }

    public Type(Type other){
        this.type = other.type;
        this.dimension = other.dimension;
        this.class_id = other.class_id;
    }

    public boolean cmp(Type other){
//        System.out.println("this: " + this.type);
//        System.out.println("other: " + other.type);
        if (this.dimension != other.dimension) return false;
        if (this.type != type.CLASS && this.type.equals(other.type)) return true;
//        System.out.println("this: " + this.type);
//        System.out.println("other: " + other.type);
        if (this.type == type.CLASS && other.type == type.CLASS && this.class_id.equals(other.class_id)) return true;
        return false;
    }
}
