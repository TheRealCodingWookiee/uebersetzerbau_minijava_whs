package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent an object class type.
 */
public class IdentifierType extends Type {

    public String name;

    public IdentifierType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
