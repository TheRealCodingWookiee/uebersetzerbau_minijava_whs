package syntaxtree;

import java.util.Vector;

/**
 * Objects of this class represent a list of statements
 */
public class StatementList {

    private Vector list;

    public StatementList() {
        list = new Vector();
    }

    public void addElement(Statement n) {
        list.addElement(n);
    }

    public Statement elementAt(int i) {
        return (Statement) list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}
