package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a negation 
 * <b>! exp</b>
 */
public class Not extends Exp {

    public Exp exp;

    public Not(Exp exp) {
        this.exp = exp;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
