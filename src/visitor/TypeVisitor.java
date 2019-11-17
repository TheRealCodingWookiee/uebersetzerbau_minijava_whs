package visitor;

import java.util.HashMap;

import syntaxtree.*;
import visitor.symboltable.*;

/**
 * Objects of the class TypeVisitor are used to traverse an abstract
 * syntax tree for some Minijava program (in a depth-first manner) in order
 * to provide a mapping from the nodes of the abstract syntax tree to their
 * corresponding types. Types of identifiers (i.e. leaf nodes of the tree) are
 * retrieved from the symbol table provided by the SymbolTableVisitor. The types
 * of internal nodes are derived by means of the type system of m´MiniJava.
 *
 * The mapping is restricted to nodes for expressions (i.e. it includes no 
 * statement or declarations nodes).
 *
 * TODO (UG): the implementation includes no error handling
 */
public class TypeVisitor implements Visitor {

    /** 
     * symbol table of the currently visited MiniJava program 
     */
    private SymbolTable symbolTable;
    /** 
     * mapping from nodes of the abstract syntax tree to their type 
     *
     * TODO (UG): types should not be Strings
     */
    private HashMap<Node, String> types;

    /**
     * Creates a new instance with the given symbol table and an empty mapping
     * from nodes of the abstract syntax tree to their types. 
     */
    public TypeVisitor(SymbolTable symbolTable) {

        this.symbolTable = symbolTable;
        this.types = new HashMap<Node, String>();
    }

    /**
     * returns the entire mapping from nodes to types
     */
    public HashMap<Node, String> getTypes() {
        return types;
    }

    /* ========================================================================
     * visit methods
     * ===================================================================== */
    
    @Override
    public void visit(Program n) {

        /* main class declaration */
        n.mainClass.accept(this);

        /* list of class declarations */
        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
        }
    }

    @Override
    public void visit(MainClass n) {

        this.enterClassScope(n.classId.name);

        /*
         * n.i1.accept(this);
         */

        this.enterMethodScope("main");

        /*
         * n.i2.accept(this);
         */

        /* statement of main method */
        n.statement.accept(this);

        this.leaveMethodScope();
        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclSimple n) {

        this.enterClassScope(n.classId.name);

        /*
         * n.i.accept(this);
         */

        /* global variable declaration */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /* list of method declarations */
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }

        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclExtends n) {

        this.enterClassScope(n.subclassId.name);

        /*
         * n.i.accept(this);
         * n.j.accept(this);
         */

        /* global variable declaration */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /* list of method declarations */
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }

        this.leaveClassScope();
    }

    @Override
    public void visit(VarDecl n) {
        n.setType(this.getVariableType(n.id.name));
    }

    @Override
    public void visit(MethodDecl n) {

        this.enterMethodScope(n.methodId.name);

        /*
         * n.resultType.accept(this);
         * n.methodId.accept(this);
         *
         * for ( int i = 0; i < n.fl.size(); i++ ) {
         *     n.formalList.elementAt(i).accept(this);
         * }
         *
         * for ( int i = 0; i < n.vl.size(); i++ ) {
         *     n.varDecls.elementAt(i).accept(this);
         * }
         */

        /* statement list of method */
        for (int i = 0; i < n.statements.size(); i++) {
            n.statements.elementAt(i).accept(this);
        }

        /* expression to be returned */
        n.returnExp.accept(this);

        this.leaveMethodScope();
    }

    @Override
    public void visit(Formal n) {
    }

    @Override
    public void visit(IntArrayType n) {
    }

    @Override
    public void visit(BooleanType n) {
    }

    @Override
    public void visit(IntegerType n) {
    }

    @Override
    public void visit(IdentifierType n) {
    }

    @Override
    public void visit(Block n) {
        for (int i = 0; i < n.statements.size(); i++) {
            n.statements.elementAt(i).accept(this);
        }
    }

    @Override
    public void visit(If n) {
        n.exp.accept(this);
        n.thenStatement.accept(this);
        n.elseStatement.accept(this);
    }

    @Override
    public void visit(While n) {
        n.exp.accept(this);
        n.statement.accept(this);
    }

    @Override
    public void visit(Print n) {
        n.exp.accept(this);
    }

    @Override
    public void visit(Assign n) {
        n.id.accept(this);
        n.exp.accept(this);
    }

    @Override
    public void visit(ArrayAssign n) {
        n.arrayId.accept(this);
        n.index.accept(this);
        n.exp.accept(this);
    }

    @Override
    public void visit(And n) {
        n.left.accept(this);
        n.right.accept(this);

        n.setType("BooleanType");
    }

    @Override
    public void visit(LessThan n) {
        n.left.accept(this);
        n.right.accept(this);

        n.setType("BooleanType");
    }

    @Override
    public void visit(Plus n) {
        n.left.accept(this);
        n.right.accept(this);

        n.setType("IntegerType");
    }

    @Override
    public void visit(Minus n) {
        n.left.accept(this);
        n.right.accept(this);

        n.setType("IntegerType");
    }

    @Override
    public void visit(Times n) {
        n.left.accept(this);
        n.right.accept(this);

        n.setType("IntegerType");
    }

    @Override
    public void visit(ArrayLookup n) {
        n.arrayId.accept(this);
        n.index.accept(this);

        n.setType("IntegerType");
    }

    @Override
    public void visit(ArrayLength n) {
        n.arrayId.accept(this);
        n.setType("IntegerType");
    }

    @Override
    public void visit(Call n) {

        n.exp.accept(this);

        /*
         * n.methodId.accept(this);
         */

        /* visit actual parameters */
        for (int i = 0; i < n.expList.size(); i++) {
            n.expList.elementAt(i).accept(this);
        }

        /* derive result type of method call */
        String classname = n.exp.getType();
        n.setType(this.getResultType(classname, n.methodId.name));
    }

    @Override
    public void visit(IntegerLiteral n) {
        n.setType("IntegerType");
    }

    @Override
    public void visit(True n) {
        n.setType("BooleanType");
    }

    @Override
    public void visit(False n) {
        n.setType("BooleanType");
    }

    @Override
    public void visit(IdentifierExp n) {
        n.setType(this.getVariableType(n.name));
    }

    @Override
    public void visit(This n) {

        /* derive the name of the current class */
        n.setType(this.getThisClassname());
    }

    @Override
    public void visit(NewArray n) {
        n.exp.accept(this);
        n.setType("IntArrayType");
    }

    @Override
    public void visit(NewObject n) {
        n.setType(n.classId.name);
    }

    @Override
    public void visit(Not n) {
        n.exp.accept(this);
        n.setType("BooleanType");
    }

    @Override
    public void visit(Identifier n) {

        n.setType(this.getVariableType(n.name));
    }

    /* ========================================================================
     * helper methods
     * ===================================================================== */
    
    /* ========================================================================
     * handling scopes
     * ===================================================================== */
    
    /**
     * enters the scope of the class given by classname.
     * @param classname 
     */
    private void enterClassScope(String classname) {
        this.symbolTable =
                ((ProgramTable) this.symbolTable).getClass(classname);
    }

    /**
     * leaves the current class scope
     */
    private void leaveClassScope() {
        this.symbolTable = this.symbolTable.getParent();
    }

    /**
     * enters the scope of the class given by methodname.
     * 
     * @param methodname 
     */
    private void enterMethodScope(String methodname) {
        this.symbolTable =
                ((ClassTable) this.symbolTable).getMethod(methodname);
    }

    /**
     * leaves the current method scope
     */
    private void leaveMethodScope() {
        this.symbolTable = this.symbolTable.getParent();
    }

    /* ========================================================================
     * handling types
     * ===================================================================== */
    
    /**
     * returns the the class name of the current class
     */
    private String getThisClassname() {
        /* 
         * the current scope is method scope, so the parent is the current
         * class scope
         */
        return ((ClassTable) this.symbolTable.getParent()).getName();
    }

    /**
     * returns the type of the given variable name from the symbol table.
     * 
     * @param varname
     * @return type of the given variable
     */
    private String getVariableType(String varname) {

        return this.symbolTable.getVariable(varname).getType();
    }

    /**
     * returns the result type of the method specified by classname and 
     * methodname.
     * 
     * @param classname
     * @param methodname
     * @return result type of the method as string
     */
    private String getResultType(String classname, String methodname) {

        return this.symbolTable.getClass(classname).getMethod(methodname)
                .getResultType();
    }
}
