package syntaxtree;

import visitor.Visitor;

/**
 * Abstract class for all types
 */
public abstract class Type {

    public abstract void accept(Visitor v);
}
