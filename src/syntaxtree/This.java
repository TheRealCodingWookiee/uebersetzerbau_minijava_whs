package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent the object 
 * <b>this</b>
 */
public class This extends Exp {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
