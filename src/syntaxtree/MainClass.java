package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a class that includes the method main
 */
public class MainClass {

    public Identifier classId;
    public Identifier formalId;
    public Statement statement;

    public MainClass(Identifier classId, Identifier formalId, 
            Statement statement) {
        
        this.classId = classId;
        this.formalId = formalId;
        this.statement = statement;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}

