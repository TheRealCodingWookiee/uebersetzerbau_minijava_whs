package syntaxtree;

import visitor.Visitor;

/**
 * Abstract class for all expressions
 */
public abstract class Exp extends Node {

    public abstract void accept(Visitor v);
}
