package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent an integer constant.
 */
public class IntegerLiteral extends Exp {

    public int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
