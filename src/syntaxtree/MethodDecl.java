package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a method declaration
 */
public class MethodDecl {

    public Type resultType;
    public Identifier methodId;
    public FormalList formalList;
    public VarDeclList varDecls;
    public StatementList statements;
    public Exp returnExp;

    public MethodDecl(Type resultType, Identifier methodId, 
            FormalList formalList, VarDeclList varDecls, 
            StatementList statements, Exp returnExp) {
        
        this.resultType = resultType;
        this.methodId = methodId;
        this.formalList = formalList;
        this.varDecls = varDecls;
        this.statements = statements;
        this.returnExp = returnExp;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
