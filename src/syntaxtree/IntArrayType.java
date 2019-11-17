package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent the type 
 * <b>int[]</b>
 */
public class IntArrayType extends Type {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
