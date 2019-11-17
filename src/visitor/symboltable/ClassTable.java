/*
 * ClassTable.java
 */

package visitor.symboltable;

import java.util.*;

/**
 * Objects of this class represent a symbol table of a Minijava class.
 *
 * Note: Implementation includes no error handling
 */
public class ClassTable extends SymbolTable {
    
    /**
     * name of the Minijava class
     */
    private String classname;
    
    /**
     * name of the Minijava super class
     */
    private String superClassname;
    
    /**
     * represents a mapping from the names of the global variables defined in 
     * this class to their types.
     */
    private HashMap<String, VariableType> globalVariables;
    
    /**
     * represents a mapping from the names of the methods defined in this class
     * to their method tables (i.e. all identifiers defined in this method)
     */
    private HashMap<String, MethodTable> methods;
    
    /**
     * creates a new instance for a given class name. Initially the created 
     * instance has no super class, no global variables and methods. 
     */
    public ClassTable(ProgramTable parent, String classname) {
        
        super(parent);
        
        this.classname = classname;
        this.superClassname = null;
        this.globalVariables = new HashMap<String, VariableType>();
        this.methods = new HashMap<String, MethodTable>();
    }
    
    /**
     * sets the super class name 
     */
    public String getName() {
        return this.classname;
    }
    
    /**
     * sets the super class name 
     */
    public void setSuperClassname(String superClassname) {
        this.superClassname = superClassname;
    }
    
    /**
     * returns the super class name
     */
    public String getSuperClassname() {
        return this.superClassname;
    }
    
    /**
     * adds a new global variable (varname, type) 
     */
    public void putVariable(String name, String type) {
        globalVariables.put(name, new VariableType(name, type));
    }
    
    /**
     * adds a new method (methodname, resulttype) 
     */
    public MethodTable putMethod(String methodname, String resulttype) {
        
        MethodTable methodTable = new MethodTable(this, methodname, resulttype);
        methods.put(methodname, methodTable);
        
        return methodTable;
    }

    /**
     * returns the method table for method named methodname 
     */
    public MethodTable getMethod(String methodname) {
        return methods.get(methodname);
    }
    
    /**
     * returns the type of the global variable named varname 
     */
    public VariableType getVariable(String name) {
        
        VariableType type = globalVariables.get(name);
        
        if (type != null) {
            return type;
        }
        
        return this.parent.getClass(this.getSuperClassname()).getVariable(name);
    }

    /**
     * returns a textual representation of the class table
     */
    public String toString() {
        String string = classname + "\n";
        
        if (this.superClassname != null) {
            string += " - extends " + superClassname + "\n";
        }
        
        string += " - Global variables\n";
        for (String varname : globalVariables.keySet()) {
            VariableType type = globalVariables.get(varname);
            string += "      " + type + "\n";
        }
        
        string += " - Methods\n";
        for (String methodname : methods.keySet()) {
            MethodTable methodtable = methods.get(methodname);
            string += "      " + methodtable.toString() + "\n";
        }
        
        return string;
    }    
}
