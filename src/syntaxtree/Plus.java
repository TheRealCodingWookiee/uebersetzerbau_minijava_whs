package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent an addition
 * <b>left + right</b>
 */
public class Plus extends Exp {

    public Exp left;
    public Exp right;

    public Plus(Exp left, Exp right) {        
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
