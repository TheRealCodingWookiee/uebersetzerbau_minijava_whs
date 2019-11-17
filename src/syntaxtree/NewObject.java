package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent the construction of a new object
 * <b>new classId()</b>
 */
public class NewObject extends Exp {

    public Identifier classId;

    public NewObject(Identifier classId) {
        this.classId = classId;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
