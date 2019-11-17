package syntaxtree;

import visitor.Visitor;

/**
 * Abstract class for all nodes of the syntax tree.
 */
public abstract class Node {

    /* Attribute beginLabel */
    private String beginLabel;
    public void setBeginLabel(String label) {
        this.beginLabel = label;
    }    
    public String getBeginLabel() {
        return this.beginLabel;
    }
    
    /* Attribut nextLabel */
    private String nextLabel;
    public void setNextLabel(String label) {
        this.nextLabel = label;
    }    
    
    public String getNextLabel() {
        return this.nextLabel;
    }
    
    /* Attribut trueLabel */
    private String trueLabel;
    public void setTrueLabel(String label) {
        trueLabel = label;
    }    
    
    public String getTrueLabel() {
        return this.trueLabel;
    }
    
    /* Attribut falseLabel */
    private String falseLabel;
    public void setFalseLabel(String label) {
        falseLabel = label;
    }    
    
    public String getFalseLabel() {
        return this.falseLabel;
    }
    
    /* Attribut Type */
    private String attributeType; 
    public void setType(String type) {
        attributeType = type;
    }    
    
    public String getType() {
        return this.attributeType;
    }
    
    public abstract void accept(Visitor v);
}