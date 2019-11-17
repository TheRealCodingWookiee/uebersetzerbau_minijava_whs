/*
 * MethodTable.java
 *
 */

package visitor.symboltable;

import java.util.*;

/**
 * Objects of this class represent a symbol table of a Minijava method.
 *
 * Note: Implementation includes no error handling
 */
public class MethodTable extends SymbolTable {
    
    /**
     * name of the Minijava method
     */
    private String name;
    
    /**
     * list of all types of input parameters in the order of the source file
     */
    private ArrayList<String> formalParameterTypes;
    
    /**
     * result type of the Minijava method
     */
    private String resultType;
    
    /**
     * represents a mapping from the names of the formal parameters defined in 
     * this class to their types.
     */
    private HashMap<String, VariableType> formalParameters;
    
    /**
     * represents a mapping from the names of the local variables defined in 
     * this class to their types.
     */
    private HashMap<String, VariableType> localVariables;
    
    /**
     * create a new instance for a given methodname and a result type of the 
     * Minijava methiod 
     */
    public MethodTable(ClassTable parent, String name, String resultType) {
        
        super(parent);
        
        this.name = name;
        this.formalParameterTypes = new ArrayList<String>();
        this.resultType = resultType;
        
        this.formalParameters = new HashMap<String, VariableType>();
        this.localVariables = new HashMap<String, VariableType>();
    }
    
    /**
     * returns the name of the method
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * returns the types of the formal parameters in the order defined by the 
     * programmer.
     */
    public ArrayList<String> getFormalParameterTypes() {
        
        return formalParameterTypes;
    }
    
    /**
     * returns the result type of the method
     */
    public String getResultType() {
        return this.resultType;
    }
    
    /**
     * adds a new formal parameter (paramname, type)
     */
    public void putFormalParameter(String name, String type, int offset) {
        
        formalParameters.put(name, new VariableType(name, type, offset));
        formalParameterTypes.add(type);
    }
    
    /**
     * adds a new local variable (varname, type)
     */
    public void putVariable(String name, String type, int offset) {
        localVariables.put(name, new VariableType(name, type, offset));
    }
    
    /**
     * returns the type of the identifier named 'varname'. First, it is checked
     * whether it is a local variable, if not, the table of formal parameters 
     * ist checked. The method returns null, if varname is neither a local
     * variable nor a formal parameter.
     */
    public VariableType getVariable(String name) {
        
        VariableType type;
        
        type = localVariables.get(name);
        if (type != null) {
            return type;
        }
        
        type = formalParameters.get(name);
        if (type != null) {
            return type;
        }
        
        return this.parent.getVariable(name);
    }
      
    /**
     * returns a textual representation of the class table
     */
    public String toString() {
        String string = this.getResultType() + " ";

        string += this.getName() + "(";
        string += this.getFormalParameterTypes() + ");";
        
        string += "\n      Formal Parameters\n";
        
        for (VariableType type : this.formalParameters.values()) {
            string += "        " + type + "\n";
        }
        
        string += "      Local Variables\n";
        for (VariableType type : this.localVariables.values()) {
            string += "        " + type + "\n";
        }
        
        return string;
    }    
}
