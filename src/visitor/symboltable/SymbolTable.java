package visitor.symboltable;

/**
 * Symbol table of a Minijava program.
 *
 * TODO (UG): Implementation includes no error handling
 */
public abstract class SymbolTable {

    /**
     * refers to the surrounding enviroment 
     */
    protected SymbolTable parent;
    
    /**
     * constructs a new symbol table with a reference to the surrounding 
     * symbol table
     */
    SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }
    
    /**
     * @return the surrounding symbol table of this symbol table
     */
    public SymbolTable getParent() {
        return this.parent;
    }
    
    /**
     * @return the symbol table entry for the given variable 
     */
    public VariableType getVariable(String name) {
        return null;
    };
    
    /**
     * @return the symbol table entry for the given class and method
     */
    public ClassTable getClass(String classname) {
        
        SymbolTable table = this;
        while (table.parent != null) {
            table = table.parent;
        }
        return ((ProgramTable) table).getClass(classname);
    }
}