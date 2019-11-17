/*
 * ProgramTable.java
 *
 */

package visitor.symboltable;

import java.util.*;

/**
 * Objects of this class represent a symbol table of a Minijava program.
 *
 * TODO (UG): Implementation includes no error handling
 */
public class ProgramTable extends SymbolTable {
    
    /**
     * represents a mapping from class names to their class tables (i.e. all
     * identifiers defined within this class)
     */
    private HashMap<String, ClassTable> classes;
    
    /**
     * Create a new instance with an empty mapping from classes to claa tables
     */
    public ProgramTable() {
        
        /* Create symbol table without surrounding environment (symbol table) */
        super(null);
        classes = new HashMap<String, ClassTable>();
    }
    
    /**
     * adds a new class with an empty class table
     */
    public ClassTable putClass(String classname) {
        ClassTable classTable = new ClassTable(this, classname);
        classes.put(classname, classTable);
        
        return classTable;
    }
    
    /**
     * returns the class table for a given class name, returns null if the 
     * mapping does not include classname
     */
    public ClassTable getClass(String classname) {
        return classes.get(classname);
    }
      
    /**
     * returns a textual representation of the symbol table
     */
    public String toString() {
        String string = "";
        
        for (String classname : classes.keySet()) {
            
            ClassTable classTable = classes.get(classname);
            string += classTable.toString();
        }
        
        return string;
    }
}
