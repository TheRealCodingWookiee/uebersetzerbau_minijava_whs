package syntaxtree;

import visitor.Visitor;

/**
 * Abstract class for all statements
 */
public abstract class Statement extends Node {

    @Override
    public abstract void accept(Visitor v);
}
