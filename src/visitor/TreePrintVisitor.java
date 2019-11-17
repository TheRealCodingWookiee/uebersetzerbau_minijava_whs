package visitor;

import syntaxtree.*;

/**
 * This visitor displays the syntax tree of a Minijava program as a tree
 */
public class TreePrintVisitor implements Visitor {

    private int indent = 0;

    @Override
    public void visit(Program n) {
        indent = 1;
        System.out.println("Program");
        n.mainClass.accept(this);
        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(MainClass n) {
        write_prefix(indent);
        indent++;
        System.out.println("MainClass ");
        n.classId.accept(this);
        n.formalId.accept(this);
        n.statement.accept(this);
        indent--;
    }

    @Override
    public void visit(ClassDeclSimple n) {
        write_prefix(indent);
        indent++;
        System.out.println("ClassDeclSimple ");
        n.classId.accept(this);
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
            if (i + 1 < n.varDecls.size()) {
            }
        }
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(ClassDeclExtends n) {
        write_prefix(indent);
        indent++;
        System.out.println("ClassDeclExtends ");
        n.subclassId.accept(this);
        n.superclassId.accept(this);
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
            if (i + 1 < n.varDecls.size()) {
                System.out.println();
            }
        }
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(VarDecl n) {
        write_prefix(indent);
        indent++;
        System.out.println("VarDecl");
        n.type.accept(this);
        n.id.accept(this);
        indent--;
    }

    @Override
    public void visit(MethodDecl n) {
        write_prefix(indent);
        indent++;
        System.out.println("MethodDecl");
        n.resultType.accept(this);
        n.methodId.accept(this);
        for (int i = 0; i < n.formalList.size(); i++) {
            n.formalList.elementAt(i).accept(this);
            if (i + 1 < n.formalList.size()) {
            }
        }
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.statements.size(); i++) {
            n.statements.elementAt(i).accept(this);
            if (i < n.statements.size()) {
            }
        }
        n.returnExp.accept(this);
        indent--;
    }

    @Override
    public void visit(Formal n) {
        write_prefix(indent);
        indent++;
        System.out.println("Formal");
        n.type.accept(this);
        n.id.accept(this);
        indent--;
    }

    @Override
    public void visit(IntArrayType n) {
        write_prefix(indent);
        indent++;
        System.out.println("IntArrayType");
        indent--;
    }

    @Override
    public void visit(BooleanType n) {
        write_prefix(indent);
        indent++;
        System.out.println("BooleanType");
        indent--;
    }

    @Override
    public void visit(IntegerType n) {
        write_prefix(indent);
        indent++;
        System.out.println("IntegerType");
        indent--;
    }

    @Override
    public void visit(IdentifierType n) {
        write_prefix(indent);
        indent++;
        System.out.println("IdentifierType");
        write_prefix(indent);
        System.out.println(n.name);
        indent--;
    }

    @Override
    public void visit(Block n) {
        write_prefix(indent);
        indent++;
        System.out.println("Block");
        for (int i = 0; i < n.statements.size(); i++) {
            n.statements.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(If n) {
        write_prefix(indent);
        indent++;
        System.out.println("If");
        n.exp.accept(this);
        n.thenStatement.accept(this);
        n.elseStatement.accept(this);
        indent--;
    }

    @Override
    public void visit(While n) {
        write_prefix(indent);
        indent++;
        System.out.println("While");
        n.exp.accept(this);
        n.statement.accept(this);
        indent--;
    }

    @Override
    public void visit(Print n) {
        write_prefix(indent);
        indent++;
        System.out.println("Print");
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(Assign n) {
        write_prefix(indent);
        indent++;
        System.out.println("Assign");
        n.id.accept(this);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(ArrayAssign n) {
        write_prefix(indent);
        indent++;
        System.out.println("ArrayAssign");
        n.arrayId.accept(this);
        n.index.accept(this);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(And n) {
        write_prefix(indent);
        indent++;
        System.out.println("And");
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(LessThan n) {
        write_prefix(indent);
        indent++;
        System.out.println("LessThan");
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(Plus n) {
        write_prefix(indent);
        indent++;
        System.out.println("Plus");
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(Minus n) {
        write_prefix(indent);
        indent++;
        System.out.println("Minus");
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(Times n) {
        write_prefix(indent);
        indent++;
        System.out.println("Times");
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(ArrayLookup n) {
        write_prefix(indent);
        indent++;
        System.out.println("ArrayLookup");
        n.arrayId.accept(this);
        n.index.accept(this);
        indent--;
    }

    @Override
    public void visit(ArrayLength n) {
        write_prefix(indent);
        indent++;
        System.out.println("ArrayLength");
        n.arrayId.accept(this);
        indent--;
    }

    @Override
    public void visit(Call n) {
        write_prefix(indent);
        indent++;
        System.out.println("Call");
        n.exp.accept(this);
        n.methodId.accept(this);
        for (int i = 0; i < n.expList.size(); i++) {
            n.expList.elementAt(i).accept(this);
            if (i + 1 < n.expList.size()) {
            }
        }
        indent--;
    }

    @Override
    public void visit(IntegerLiteral n) {
        write_prefix(indent);
        indent++;
        System.out.println("IntegerLiteral");
        write_prefix(indent);
        System.out.println(n.value);
        indent--;
    }

    @Override
    public void visit(True n) {
        write_prefix(indent);
        indent++;
        System.out.println("True");
        indent--;
    }

    @Override
    public void visit(False n) {
        write_prefix(indent);
        indent++;
        System.out.println("False");
        indent--;
    }

    @Override
    public void visit(IdentifierExp n) {
        write_prefix(indent);
        indent++;
        System.out.println("IdentifierExp");
        write_prefix(indent);
        System.out.println(n.name);
        indent--;
    }

    @Override
    public void visit(This n) {
        write_prefix(indent);
        indent++;
        System.out.println("This");
        indent--;
    }

    @Override
    public void visit(NewArray n) {
        write_prefix(indent);
        indent++;
        System.out.println("NewArray");
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(NewObject n) {
        write_prefix(indent);
        indent++;
        System.out.println("NewObject");
        write_prefix(indent);
        System.out.println(n.classId.name);
        indent--;
    }

    @Override
    public void visit(Not n) {
        write_prefix(indent);
        indent++;
        System.out.println("Not");
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(Identifier n) {
        write_prefix(indent);
        indent++;
        System.out.println("Identifier");
        write_prefix(indent);
        System.out.println(n.name);
        indent--;
    }

    public void write_prefix(int indent) {
        for (int i = 0; i < indent - 1; i++) {
            System.out.print("    ");
        }
        System.out.print("|-- ");
    }
}
