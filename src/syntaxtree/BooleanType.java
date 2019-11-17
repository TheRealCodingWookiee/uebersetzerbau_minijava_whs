package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent the type <b>boolean</b>
 */
public class BooleanType extends Type {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
