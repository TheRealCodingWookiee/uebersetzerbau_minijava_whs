/*
 * VariableType.java
 *
 * Created on 14. Dezember 2007, 14:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package visitor.symboltable;

/**
 * Class Type
 */
public class VariableType {
    
    /**
     * Offset für globale Variablen 
     */
    private static final int OFFSET_GLOBALS = -1;
    
    private String name;
    private String type;
    private int offset; 
    
    /** 
     * Creates a new instance of Type (global variable)
     */
    public VariableType(String name, String type) {
        this.name = name;
        this.type = type;
        this.offset = OFFSET_GLOBALS;
    }
    
    /** 
     * Creates a new instance of Type (local variables)
     */
    public VariableType(String name, String type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
    }
        
    /** 
     * Returns the storage needed by this type (in amount of bytes) 
     */
    public String getType() {
        return this.type;
    }
    
    /** 
     * Returns the storage needed by this type (in amount of bytes) 
     */
    public int getOffset() {
        return this.offset;
    }
    
    /** 
     * 
     */
    public Boolean isGlobalVariable() {
        return (this.offset == OFFSET_GLOBALS);
    }
    
    public String toString() {
        return this.name + ":" + this.type + " (" + this.offset + ")";
    }
}

