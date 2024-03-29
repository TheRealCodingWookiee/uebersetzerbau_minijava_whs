package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a multiplication 
 * <b>left * right</b>
 */
public class Times extends Exp {

    public Exp left;
    public Exp right;

    public Times(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
