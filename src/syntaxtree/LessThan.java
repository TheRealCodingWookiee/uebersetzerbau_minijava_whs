package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a comparison: 
 * {@code left < right}
 */
public class LessThan extends Exp {

    public Exp left;
    public Exp right;

    public LessThan(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
