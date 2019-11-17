package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent the boolean value
 * <b>true</b>
 */
public class True extends Exp {

    public void accept(Visitor v) {
        v.visit(this);
    }
}
