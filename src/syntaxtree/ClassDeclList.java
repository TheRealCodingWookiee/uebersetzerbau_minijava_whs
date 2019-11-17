package syntaxtree;

import java.util.LinkedList;

/**
 * Objects of this class represent a list of classes
 */
public class ClassDeclList {

    private LinkedList<ClassDecl> list;

    public ClassDeclList() {
        list = new LinkedList<ClassDecl>();
    }

    public void addElement(ClassDecl n) {
        list.add(n);
    }

    public ClassDecl elementAt(int i) {
        return (ClassDecl) list.get(i);
    }

    public int size() {
        return list.size();
    }
}
