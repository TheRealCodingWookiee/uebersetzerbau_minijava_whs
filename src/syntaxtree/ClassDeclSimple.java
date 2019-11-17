package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a simple class (without superclass)
 */
public class ClassDeclSimple extends ClassDecl {

    public Identifier classId;
    public VarDeclList varDecls;
    public MethodDeclList methodDecls;

    public ClassDeclSimple(Identifier classId, VarDeclList varDecls, 
            MethodDeclList methodDecls) {
        this.classId = classId;
        this.varDecls = varDecls;
        this.methodDecls = methodDecls;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
