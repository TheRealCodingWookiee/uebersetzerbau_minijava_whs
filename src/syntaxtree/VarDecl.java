package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a variable declaration
 * <b>type id</b>
 */
public class VarDecl extends Node {

    public Type type;
    public Identifier id;

    public VarDecl(Type type, Identifier id) {
        this.type = type;
        this.id = id;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
