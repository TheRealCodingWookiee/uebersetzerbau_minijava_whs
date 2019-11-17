package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a print statment
 * <b>System.out.println(exp)</b>
 */
public class Print extends Statement {

    public Exp exp;

    public Print(Exp exp) {        
        this.exp = exp;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
