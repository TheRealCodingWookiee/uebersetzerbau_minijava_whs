package visitor;

import java.io.*;

import syntaxtree.*;
import visitor.symboltable.*;

/**
 * Objects of the class JasminVisitor traverse an abstract syntax tree for some
 * Minijava program (in a depth-first manner) in order to create Jasmin files
 * for each Minijava class represented in the abstract syntax tree. The
 * generation of Jasmin files is based on the syntax tree, the symbol table, and
 * the information on types for each expression node of the syntax tree.
 *
 * Note: the implementation includes no error handling
 */
public class JasminVisitor implements Visitor {
    
    /**
     * Enumeration that provides all kinds of Labels to be created
     */
    enum Label {NEXT, BEGIN, TRUE, FALSE};
    
    /**
     * specifies the directory to which the writer writes the Jasmin file
     */
    private final String jasminDir;
    
    /**
     * symbol table corresponding to the actually visited Minijava program
     */
    private SymbolTable symbolTable;
    
    /**
     * holds a unique number for the next label to be generated
     */
    private int nextLabelNo;
    
    /**
     * holds the connection to the file to be written for the current class
     */
    private PrintWriter writer;
    
    /**
     * Creates a new instance of class JasminVisitor with a path of a directory
     * to which the Jasmin file will be generated, a symbol table and type
     * mapping (mapping from nodes of the abstract syntax tree to their
     * corresponding types).
     *
     * The initial state indicates that neither a class nor a method scope
     * has yet been entered and no labels have been generated.
     * 
     * @param jasminDir   path to the Jasmin file
     * @param symbolTable symbol table derived from the correspinding Minijava 
     *                    program
     */
    public JasminVisitor(
            String jasminDir,
            SymbolTable symbolTable) {
        
        this.jasminDir = jasminDir;
        this.symbolTable = symbolTable;
        
        /* initialise labelling */
        this.nextLabelNo = 0;
    }
    
    /* ========================================================================
     * visit methods
     * ===================================================================== */
    
    /**
     * creates Jasmin code for a Minijava program.
     * 
     * @param node 
     */
    @Override
    public void visit(Program node) {
        node.mainClass.accept(this);
        for ( int i = 0; i < node.classes.size(); i++ ) {
            node.classes.elementAt(i).accept(this);
        }
    }
    
    /**
     * creates Jasmin code for the main class declaration.
     * 
     * @param node 
     */
    @Override
    public void visit(MainClass node) {
        
        /* Jasmin-Datei für aktuelle Klasse öffnen */
        writer = openWriter(node.classId.name);
        
        /* enter scope of main class */
        enterClassScope(node.classId.name);
        
        /* emit Jasmin directives */
        node.classId.accept(this);
        emitCode(".class public " + node.classId.name);
        emitCode(".super java/lang/Object");
        
        /* emit standard initialization */
        emitCode(".method public <init>()V");
        emitCode("aload_0");
        emitCode("invokespecial java/lang/Object/<init>()V");
        emitCode("return");
        emitCode(".end method");
        
        /* emit main method declaration */
        node.formalId.accept(this);
        emitCode(".method public static main([Ljava/lang/String;)V");
        emitCode(".limit stack 20"); /* TODO (UG): calc minimal stacksize */
        emitCode(".limit locals 1");
        
        /* emit statement of main method */
        node.statement.accept(this);
        
        emitCode("return");
        emitCode(".end method");
        
        /* enter scope of main class */
        leaveClassScope();
        
        writer.close();
    }
    
    /**
     * creates Jasmin code for a normal class declaration.
     * 
     * @param node 
     */
    @Override
    public void visit(ClassDeclSimple node) {
        
        /* Jasmin-Datei für aktuelle Klasse öffnen */
        writer = openWriter(node.classId.name);
        
        /* enter scope of class */
        enterClassScope(node.classId.name);
        
        /* emit Jasmin directives */
        node.classId.accept(this);
        emitCode(".class public " + node.classId.name);
        emitCode(".super java/lang/Object");
        
        /* emit code for global variable declarations */
        for ( int i = 0; i < node.varDecls.size(); i++ ) {
            node.varDecls.elementAt(i).accept(this);
        }
        
        /* emit code for standard initialization */
        emitCode(".method public <init>()V");
        emitCode("aload_0");
        emitCode("invokespecial java/lang/Object/<init>()V");
        emitCode("return");
        emitCode(".end method");
        
        /* emit code for method declarations */
        for ( int i = 0; i < node.methodDecls.size(); i++ ) {
            node.methodDecls.elementAt(i).accept(this);
        }
        
        /* enter scope of class */
        leaveClassScope();
        
        writer.close();
    }
    
    /**
     * creates Jasmin code for a subclass declaration.
     * 
     * @param node 
     */
    @Override
    public void visit(ClassDeclExtends node) {
        
        /* Jasmin-Datei für aktuelle Unterklasse öffnen */
        writer = openWriter(node.subclassId.name);
        
        /* enter scope of class */
        enterClassScope(node.subclassId.name);
        
        /* emit Jasmin directives */
        node.subclassId.accept(this);
        emitCode(".class public " + node.subclassId.name);
        node.superclassId.accept(this);
        emitCode(".super " + node.superclassId.name);
        
        /* emit code for global variable declarations */
        for ( int i = 0; i < node.varDecls.size(); i++ ) {
            node.varDecls.elementAt(i).accept(this);
        }
        
        /* emit code for standard initialization */
        emitCode(".method public <init>()V");
        emitCode("aload_0");
        emitCode("invokespecial " + node.superclassId.name + "/<init>()V");
        emitCode("return");
        emitCode(".end method");
        
        /* emit code for method declarations */
        for ( int i = 0; i < node.methodDecls.size(); i++ ) {
            node.methodDecls.elementAt(i).accept(this);
        }
        
        /* enter scope of class */
        leaveClassScope();
        
        writer.close();
    }
    
    /**
     * creates Jasmin code for a variable declaration.
     * 
     * @param node 
     */
    @Override
    public void visit(VarDecl node) {
        node.type.accept(this);
        node.id.accept(this);
       
        if (this.isGlobalVariable(node.id.name)) {
            
            emitCode(".field public " + node.id.name + " " 
                        + this.getJasminType(node.getType()));
        }
    }
  
    /**
     * creates Jasmin code for a method declaration.
     * 
     * @param node 
     */
    @Override
    public void visit(MethodDecl node) {
        // TODO: Minijava-Projekt (Blatt 4)
        enterMethodScope(node.methodId.name);
        
        /* emit Jasmin directives */
        String classname = this.getCurrentClassname();
        emitCode(".method public " + this.getJasminMethod(classname, node.methodId.name));
        emitCode(".limit stack 20"); // TODO (UG): calc minimal stack size
        emitCode(".limit locals " + (node.formalList.size() + node.varDecls.size() + 1));
        
        node.resultType.accept(this);
        node.methodId.accept(this);
         
         for ( int i = 0; i < node.formalList.size(); i++ ) {
             node.formalList.elementAt(i).accept(this);
         }        

         for ( int i = 0; i < node.varDecls.size(); i++ ) {
            node.varDecls.elementAt(i).accept(this);
         }
         
        for (int i = 0; i < node.statements.size(); i++) {
            
            node.statements.elementAt(i).accept(this);
        }
        
        node.returnExp.accept(this);
        
        emitCode(this.getJasminInstructionType(node.returnExp.getType()) + "return");
        
        emitCode(".end method");
        
        /* leave current method scope */
        leaveMethodScope();
    }
    
    /**
     * creates Jasmin code for a formal method parameter.
     * 
     * @param node 
     */
    @Override
    public void visit(Formal node) {
        node.type.accept(this);
        node.id.accept(this);
    }
    
    /**
     * empty function.
     * 
     * @param node 
     */
    @Override
    public void visit(IntArrayType node) {
    }
    
    /**
     * empty function.
     * 
     * @param node 
     */
    @Override
    public void visit(BooleanType node) {
    }
    
    /**
     * empty function.
     * 
     * @param node 
     */
    @Override
    public void visit(IntegerType node) {
    }
    
    /**
     * empty function.
     * 
     * @param node 
     */
    @Override
    public void visit(IdentifierType node) {
    }
    
    /**
     * creates Jasmin code for a block of statements.
     * 
     * @param node 
     */
    @Override
    public void visit(Block node) {
        // TODO: Minijava-Projekt (Blatt 4)
        for (int i = 0; i < node.statements.size(); i++) {
            node.statements.elementAt(i).accept(this);            
        }
    }

    /**
     * creates Jasmin code for an if statement.
     * 
     * @param node 
     */
    @Override
    public void visit(If node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.exp.accept(this);
        node.thenStatement.accept(this);
        node.elseStatement.accept(this);
    }
    
    /**
     * creates Jasmin code for a while statement.
     * 
     * @param node 
     */
    @Override
    public void visit(While node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.exp.accept(this);
        node.statement.accept(this);
    }
    
    /**
     * creates Jasmin code for a println statement.
     * 
     * @param node 
     */
    @Override
    public void visit(Print node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.exp.accept(this);
    }
    
    /**
     * creates Jasmin code for an assignment.
     * 
     * @param node 
     */
    @Override
    public void visit(Assign node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.id.accept(this);
        node.exp.accept(this);
    }
    
    /**
     * creates Jasmin code for an assignment to an array element.
     * 
     * @param node 
     */
    @Override
    public void visit(ArrayAssign node) {
        // TODO: Minijava-Projekt (Blatt 4)      
        node.arrayId.accept(this);
        node.index.accept(this);
        node.exp.accept(this);
    }
    
    /**
     * creates Jasmin code for a boolean conjunction.
     * 
     * @param node 
     */
    @Override
    public void visit(And node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.left.accept(this);
        node.right.accept(this);
    }
    
    /**
     * creates Jasmin code for a less than relation.
     * 
     * @param node 
     */
    @Override
    public void visit(LessThan node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.left.accept(this);
        node.right.accept(this);
    }
    
    /**
     * creates Jasmin code for an addition.
     * 
     * @param node 
     */
    @Override
    public void visit(Plus node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.left.accept(this);
        node.right.accept(this);
    }
    
    /**
     * creates Jasmin code for a subtraction.
     * 
     * @param node 
     */
    @Override
    public void visit(Minus node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.left.accept(this);
        node.right.accept(this);
    }
    
    /**
     * creates Jasmin code for a multiplication.
     * 
     * @param node 
     */
    @Override
    public void visit(Times node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.left.accept(this);
        node.right.accept(this);
    }
    
    /**
     * creates Jasmin code for accessing an elements of an array.
     * 
     * @param node 
     */
    @Override
    public void visit(ArrayLookup node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.arrayId.accept(this);
        node.index.accept(this);
    }
    
    /**
     * creates Jasmin code for retrieving the length of an array.
     * 
     * @param node 
     */
    @Override
    public void visit(ArrayLength node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.arrayId.accept(this);
    }
    
    /**
     * creates Jasmin code for a method call.
     * 
     * @param node 
     */
    @Override
    public void visit(Call node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.exp.accept(this);
        node.methodId.accept(this);
        for ( int i = 0; i < node.expList.size(); i++ ) {
            node.expList.elementAt(i).accept(this);
        }
    }
    
    /**
     * creates Jasmin code for an integer literal.
     * 
     * @param node 
     */
    @Override
    public void visit(IntegerLiteral node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for the boolean value true.
     * 
     * @param node 
     */
    @Override
    public void visit(True node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for the boolean value false.
     * 
     * @param node 
     */
    @Override
    public void visit(False node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for the access to an identifier.
     * 
     * @param node 
     */
    @Override
    public void visit(IdentifierExp node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for an access to 'this' object.
     * 
     * @param node 
     */
    @Override
    public void visit(This node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for an array creation.
     * 
     * @param node 
     */
    @Override
    public void visit(NewArray node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for an object creation.
     * 
     * @param node 
     */
    @Override
    public void visit(NewObject node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
    
    /**
     * creates Jasmin code for a negation.
     * 
     * @param node 
     */    
    @Override
    public void visit(Not node) {
        // TODO: Minijava-Projekt (Blatt 4)
        node.exp.accept(this);
    }
    
    /**
     * creates Jasmin code for an identifier.
     * 
     * @param node node of an identifier
     */
    @Override
    public void visit(Identifier node) {
        // TODO: Minijava-Projekt (Blatt 4)
    }
 
    
    /* ========================================================================
     * helpers
     * ===================================================================== */
    
    /**
     * Opens the Jasmin output file for a given class.
     * 
     * @param classname name of a Minijava class
     * @return PrintWriter, if the file could be opened successfully, 
     *                      null otherwise
     */
    private PrintWriter openWriter(String classname) {
        
        PrintWriter newWriter = null;
        
        try {
            String full_filename = jasminDir + "/" + classname + ".j";
            newWriter = new PrintWriter(new FileOutputStream(full_filename));
            
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        return newWriter;
    }
    
    /* ========================================================================
     * handling scope
     * ===================================================================== */

    /**
     * enters the scope of the class given by classname.
     * 
     * @param classname name of a Minijava class
     */
    private void enterClassScope(String classname) {
        this.symbolTable = 
                ((ProgramTable) this.symbolTable).getClass(classname);
    }
    
    /**
     * leaves the current class scope.
     */
    private void leaveClassScope() {
        this.symbolTable = this.symbolTable.getParent();
    }
    
    /**
     * enters the scope of the class given by methodname.
     * 
     * @param methodname name of a Minjava method 
     */
    private void enterMethodScope(String methodname) {
        this.symbolTable = 
                ((ClassTable) this.symbolTable).getMethod(methodname);
    }
    
    /**
     * leaves the current method scope.
     */
    private void leaveMethodScope() {
        this.symbolTable = this.symbolTable.getParent();
    }
    
    /* ========================================================================
     * methods for code generation
     * ===================================================================== */

    /**
     * Writes the given code string to the current Jasmin file and to standard
     * output. Jasmin directives start at column 1, instructions at column 5
     * 
     * @param code 
     */
    private void emitCode(String code) {
        
        if (code.charAt(0) != '.') {
            writer.print("    ");
            // System.out.print("    ");
        }
        writer.println(code);
        // System.out.println(code);
    }
    
    /**
     * Writes the given string as a label to the current Jasmin file and to
     * standard output. Labels start at column 3.
     * 
     * @param label     Label to be written
     */
    private void emitLabel(String label) {
        writer.println("  " + label + ":");
        // System.out.println("  " + label + ":");
    }
    
    
    /* ========================================================================
     * methods for label handling
     * ===================================================================== */
    
    /**
     * Generates a new label composed from a unique label number and the String
     * "kind", e.g. L1_NEXT or L2_FALSE.
     * 
     * @param kind  String from enumeration Label
     * @return      Label
     */
    private String newLabel(Label kind) {
        
        nextLabelNo++;
        
        return "L" + nextLabelNo + "_" + kind;
    }
    
    /* ========================================================================
     * methods for type handling
     * ===================================================================== */

    /**
     * For a given Minijava type this method computes the prefix 'i' or 'a' 
     * used in Jasmin load and store instructions, i.e. for IntegerType and
     * BooleanType the prefix "i" and for all other types the prefix "a" is 
     * delivered.
     * 
     * @param type Minijava type
     * @return Jasmin prefix
     */
    private String getJasminInstructionType(String type) {
        
        if(type.equals("IntegerType")) {
            return "i";
        } else if (type.equals("BooleanType")) {
            return "i";
        } else {
            return "a";
        }
    }
    
    /**
     * translates a Minijava type to a Jasmin type.
     * 
     * @param type name of a Minijava type
     * @return Jasmin type
     */
    private String getJasminType(String type) {
        
        if (type.equals("IntegerType")) {
            return "I";
            
        } else if (type.equals("BooleanType")) {
            return "Z";
            
        } else if (type.equals("IntArrayType")) {
            return "[I";
            
        } else {
            return "L" + type + ";";    /* class name */
        }
    }
    
    /**
     * retrieves the method signature in Jasmin syntax of Minijava class and
     * method.
     * 
     * @param classname name of Minijava class
     * @param methodname name of Minijava method 
     * @return Jasmin method signature
     */
    private String getJasminMethod(String classname, String methodname) {
        MethodTable methodTable = this.getMethodTable(classname, methodname);
        
        String string = methodTable.getName() + "(";
        for (String type : methodTable.getFormalParameterTypes()) {
            string += this.getJasminType(type);
        }
        string +=  ")" + this.getJasminType(methodTable.getResultType());
        
        return string;
    }
    
   
    /* ========================================================================
     * accessing symbol table
     * ===================================================================== */

    /**
     * retrieves the class name of the current Minijava class.
     */
    private String getCurrentClassname() {
        /* 
         * the current scope is method scope, so the parent is the current
         * class scope
         */
        return ((ClassTable) this.symbolTable.getParent()).getName();
    }
    
    /**
     * retrieves the method table specified by class and method name.
     * 
     * @param classname name of a Minijava class
     * @param methodname name of a Minijava method
     * 
     * @return method table specified by class and method name
     */
    private MethodTable getMethodTable(String classname, String methodname) {
        
        return this.symbolTable.getClass(classname).getMethod(methodname);
    }
    
    /**
     * returns the number of the Minijava variable named varname that 
     * corresponds to the Jasmin variable for varname.
     * 
     * @param varname name of a Minijava variable
     * @return number of corresponding Jasmin variable
     */
    private int getVariableNumber(String varname) {
        
        return this.symbolTable.getVariable(varname).getOffset();
    }
    
    /**
     * checks if varname is a global variable or not.
     * 
     * @param varname name of a Minijava variable
     * @return true if varname is a global variable and false otherwise
     */
    private boolean isGlobalVariable(String varname) {
        
        return this.symbolTable.getVariable(varname).isGlobalVariable();
    }
}
