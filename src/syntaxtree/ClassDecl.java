package syntaxtree;

import visitor.Visitor;

/**
 * Abstract class for all class declarations
 */
public abstract class ClassDecl {

    public abstract void accept(Visitor v);
}
