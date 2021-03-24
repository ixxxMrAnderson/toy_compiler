package MIR;

import AST.constExprNode;
import Util.Type.type;

public class entity {
    static int cnt;
    public String id;
    public int reg;
    public boolean is_constant;
    public boolean is_addr = false;
    public constExprNode constant;

    public entity(){
        id = "_TMP_" + (cnt++);
    }

    public entity(String id){
        this.id = id;
    }

    public entity(String id, boolean flag){
        this.id = id;
        this.is_addr = flag;
    }

    public entity(boolean flag){
        this.is_addr = flag;
        id = "_TMP_" + (cnt++);
    }

    public entity(entity other){
        this.id = other.id;
        this.reg = other.reg;
        this.is_addr = other.is_addr;
        this.is_constant = other.is_constant;
        if (this.is_constant == true) {
            this.constant = new constExprNode(other.constant, null);
        }
    }

    public entity(int value){
        this.id = Integer.toString(value);
        this.is_constant = true;
        this.constant = new constExprNode(value, null);
    }

    public entity(constExprNode e){
        this.is_constant = true;
        this.constant = new constExprNode(e, null);
        if (this.constant.expr_type.type == type.INT){
            this.id = Integer.toString(this.constant.int_value);
        } else if (this.constant.expr_type.type == type.STRING){
            this.id = this.constant.string_value;
        } else if (this.constant.expr_type.type == type.NULL){
            this.id = "null";
        } else if (this.constant.bool_value == true){
            this.id = "true";
        } else {
            this.id = "false";
        }
    }
}
