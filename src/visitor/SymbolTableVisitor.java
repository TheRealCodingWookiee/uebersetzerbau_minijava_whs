package visitor;

import syntaxtree.*;
import visitor.symboltable.*;

/**
 * Objects of the class SymbolTableVisitor are used to traverse an abstract
 * syntax tree for some Minijava program (in a depth-first manner) in order to
 * fill the corresponding symbol table.
 *
 * Note: the implementation includes no error handling
 */
public class SymbolTableVisitor implements Visitor {

    /**
     * Offset for gloabl variables 
     */
    private static final int OFFSET_GLOBALS = -1;
    /** 
     * symbol table corresponding to the actually visited Minijava program 
     */
    private SymbolTable symbolTable;
    /**
     * offset of a variable declaration in an environment. It gives the number
     * of the storage place of a formal parameter or a local variable
     */
    int offset;

    /**
     * Creates a new instance with empty symbol table. 
     */
    public SymbolTableVisitor() {

        this.symbolTable = new ProgramTable();
        this.offset = OFFSET_GLOBALS;
    }

    /**
     * returns the entire symbol table
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    /* ========================================================================
     * visit methods
     * ===================================================================== */
    
    @Override
    public void visit(Program n) {
        n.mainClass.accept(this);
        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
        }
    }

    @Override
    public void visit(MainClass n) {
        this.enterClassScope(n.classId.name);
        this.enterMethodScope("main", null);
        this.putFormalParameter(n.formalId.name, "java/lang/String;", 1);

        /* 
         * n.classId.accept(this);
         * n.methodId.accept(this);
         * n.statement.accept(this);
         */

        this.leaveMethodScope();
        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclSimple n) {

        /* enter scope of current class */
        this.enterClassScope(n.classId.name);

        /* 
         * n.classId.accept(this);
         */

        /* visit global variable declarations */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /* visit method declarations */
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }

        /* leave current class scope */
        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclExtends n) {

        /* enter scope of current class */
        this.enterClassScope(n.subclassId.name);

        /*
         * n.subclassId.accept(this);
         * n.superclassId.accept(this);
         */

        this.setSuperClassname(n.superclassId.name);

        /* global variable declarations */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /* method declarations */
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }

        this.leaveClassScope();
    }

    @Override
    public void visit(VarDecl n) {
        this.putVariable(n.id.name, this.convertType(n.type), offset);
        offset = offset + 1;

        /*
         * n.type.accept(this);
         * n.id.accept(this);
         */
    }

    @Override
    public void visit(MethodDecl n) {

        /* enter scope of current method */
        this.enterMethodScope(n.methodId.name, n.resultType);

        /*
         * n.resultType.accept(this);
         * n.methodId.accept(this);
         */

        /* visit formal parameters */
        for (int i = 0; i < n.formalList.size(); i++) {
            n.formalList.elementAt(i).accept(this);
        }

        /* visit local variable declarations */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /*
         * for ( int i = 0; i < n.sl.size(); i++ ) {
         *     n.statements.elementAt(i).accept(this);
         * }
         * n.returnExp.accept(this);
         */

        /* leave scope of current method */
        this.leaveMethodScope();
    }

    @Override
    public void visit(Formal n) {
        this.putFormalParameter(n.id.name, this.convertType(n.type), offset);
        offset = offset + 1;

        /*
         * n.t.accept(this);
         * n.i.accept(this);
         */
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
    }

    @Override
    public void visit(If n) {
    }

    @Override
    public void visit(While n) {
    }

    @Override
    public void visit(Print n) {
    }

    @Override
    public void visit(Assign n) {
    }

    @Override
    public void visit(ArrayAssign n) {
    }

    @Override
    public void visit(And n) {
    }

    @Override
    public void visit(LessThan n) {
    }

    @Override
    public void visit(Plus n) {
    }

    @Override
    public void visit(Minus n) {
    }

    @Override
    public void visit(Times n) {
    }

    @Override
    public void visit(ArrayLookup n) {
    }

    @Override
    public void visit(ArrayLength n) {
    }

    @Override
    public void visit(Call n) {
    }

    @Override
    public void visit(IntegerLiteral n) {
    }

    @Override
    public void visit(True n) {
    }

    @Override
    public void visit(False n) {
    }

    @Override
    public void visit(IdentifierExp n) {
    }

    @Override
    public void visit(This n) {
    }

    @Override
    public void visit(NewArray n) {
    }

    @Override
    public void visit(NewObject n) {
    }

    @Override
    public void visit(Not n) {
    }

    @Override
    public void visit(Identifier n) {
    }


    /* ========================================================================
     * helper methods
     * ===================================================================== */
    
    /**
     * enters the scope of the class given by classname and extends the 
     * symbol table with an entry for this class.
     * 
     * @param classname 
     */
    private void enterClassScope(String classname) {

        this.symbolTable = ((ProgramTable) this.symbolTable).putClass(classname);
        offset = OFFSET_GLOBALS;
    }

    /**
     * leaves the current class scope and restores the scope of the surrounding
     * environment
     */
    private void leaveClassScope() {
        this.symbolTable = this.symbolTable.getParent();
        offset = OFFSET_GLOBALS;
    }

    /**
     * enters the scope of the methode given by methodname and extends the 
     * symbol table with an entry for this method.
     * 
     * @param methodname
     * @param type 
     */
    private void enterMethodScope(String methodname, Type type) {

        this.symbolTable = ((ClassTable) this.symbolTable).putMethod(methodname, this.convertType(type));

        /* the first parameter of a method call is always "This" 
         * so the offset starts with 1 */
        offset = 1;
    }

    /**
     * leaves the current method scope and restores the scope of the surrounding
     * environment and the offset.
     */
    private void leaveMethodScope() {
        this.symbolTable = this.symbolTable.getParent();
        offset = OFFSET_GLOBALS;
    }

    /**
     * adds the sub/super class relationship to the current symbol table
     * 
     * @param superClassname 
     */
    private void setSuperClassname(String superClassname) {
        ((ClassTable) this.symbolTable).setSuperClassname(superClassname);
    }

    /**
     * stores the given variable name (and its type and offset) in the symbol 
     * table.
     * If the process is currently within a method scope the variable is stored
     * as a local variable of the current method. If no method scope is entered
     * the variable is stored as a global variable of the current class.
     * 
     * TODO (UG): cast should be omitted
     * 
     * @param varname
     * @param type
     * @param offset 
     */
    private void putVariable(String varname, String type, int offset) {

        if (this.symbolTable instanceof ClassTable) {

            /* defines a global variable */
            ((ClassTable) this.symbolTable).putVariable(varname, type);

        } else {

            /* defines a local variable */
            ((MethodTable) this.symbolTable).putVariable(varname, type, offset);
        }
    }

    /**
     * stores the given formal parameter varname (and its type and offset) 
     * in the symbol table.
     * 
     * @param paramname
     * @param type
     * @param offset 
     */
    private void putFormalParameter(String paramname, String type, int offset) {
        ((MethodTable) this.symbolTable).putFormalParameter(
                paramname, type, offset);
    }

    /**
     * converts a node of the syntax tree of type "Type" into a string. This 
     * string will be stored in the symbol table. 
     * - If type corresponds to a user-defined class, the name of the class 
     *   is returned. 
     * - If the type is a predefined type then either "IntegerType", 
     *   "BooleanType", "IntArrayType" is returned. 
     * - If no type is given "VoidType" is returned 
     * 
     * @param type
     * @return String representation of the given type
     */
    private String convertType(Type type) {

        if (type == null) {
            return "VoidType";

        } else if (type instanceof IdentifierType) {

            return ((IdentifierType) type).getName();

        } else {
            return type.getClass().getSimpleName();
        }
    }
}
