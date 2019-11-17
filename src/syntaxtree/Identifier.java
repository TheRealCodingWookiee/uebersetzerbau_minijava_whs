package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent an identifier that may occur on the 
 * left-hand-side of an assignment.
 */
public class Identifier extends Node {

    public String name;

    public Identifier(String name) {
        this.name = name;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
