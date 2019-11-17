package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent identifiers that may occur in some
 * expression.
 */
public class IdentifierExp extends Exp {

    public String name;

    public IdentifierExp(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
