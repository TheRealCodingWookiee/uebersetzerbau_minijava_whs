package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a Minijava program
 */
public class Program {

    public MainClass mainClass;
    public ClassDeclList classes;

    public Program(MainClass mainClass, ClassDeclList classes) {        
        this.mainClass = mainClass;
        this.classes = classes;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
