package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent an assignment: 
 * <b>id = exp</b>
 */
public class Assign extends Statement {

    public Identifier id;
    public Exp exp;

    public Assign(Identifier id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

