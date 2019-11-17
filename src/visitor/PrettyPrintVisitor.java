package visitor;

import syntaxtree.*;

/**
 * This visitor displays the syntax tree of a Minijava program as a Minijava
 * program
 */
public class PrettyPrintVisitor implements Visitor {

    private int indent;
    
    @Override
    public void visit(Program n) {
        indent = 0;
        
        n.mainClass.accept(this);
        writelnCode();
            
        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
            writelnCode();
        }
    }

    @Override
    public void visit(MainClass n) {
        writePrefix(); 
        writeCode("class ");
        n.classId.accept(this);
        writeCode(" ");
        writelnCode("{");
        enterScope(); 
        
        writePrefix();
        writeCode("public static void main (String [] ");
        n.formalId.accept(this);
        writeCode(") ");
        
        writelnCode("{");
        enterScope();
        
        writePrefix();
        n.statement.accept(this);
        writelnCode();
        
        leaveScope();
        writePrefix();
        writelnCode("}");
        
        leaveScope();
        writePrefix();
        writelnCode("}");
    }

    @Override
    public void visit(ClassDeclSimple n) {
        writePrefix();
        writeCode("class ");
        n.classId.accept(this);
        writeCode(" ");
        writelnCode("{");
        enterScope();
        
        for (int i = 0; i < n.varDecls.size(); i++) {
            writePrefix();
            n.varDecls.elementAt(i).accept(this);
            writelnCode();
            if (i == n.varDecls.size() - 1) {
                writelnCode();
            }
        }
        
        for (int i = 0; i < n.methodDecls.size(); i++) {
            writePrefix();
            n.methodDecls.elementAt(i).accept(this);
            writelnCode();
            if (i < n.methodDecls.size() - 1) {
                writelnCode();
            }
        }
        
        leaveScope(); 
        writePrefix();
        writelnCode("}");
    }

    @Override
    public void visit(ClassDeclExtends n) {
        writeCode("class ");
        n.subclassId.accept(this);
        writeCode(" extends ");
        n.superclassId.accept(this);
        writeCode(" ");
        writelnCode("{");
        enterScope();
        
        for (int i = 0; i < n.varDecls.size(); i++) {
            writePrefix();
            n.varDecls.elementAt(i).accept(this);
            writelnCode();
            if (i == n.varDecls.size() - 1) {
                writelnCode();
            }
        }
        
        for (int i = 0; i < n.methodDecls.size(); i++) {
            writePrefix();
            n.methodDecls.elementAt(i).accept(this);
            writelnCode();
            if (i < n.methodDecls.size() - 1) {
                writelnCode();
            }
        }
        
        leaveScope(); 
        writePrefix();
        writelnCode("}");
    }

    @Override
    public void visit(VarDecl n) {
        n.type.accept(this);
        writeCode(" ");
        n.id.accept(this);
        writeCode(";");
    }

    @Override
    public void visit(MethodDecl n) {
        writeCode("public ");
        n.resultType.accept(this);
        writeCode(" ");
        n.methodId.accept(this);
        
        writeCode("(");
        for (int i = 0; i < n.formalList.size(); i++) {
            n.formalList.elementAt(i).accept(this);
            if (i + 1 < n.formalList.size()) {
                writeCode(", ");
            }
        }
        writeCode(") ");
        writelnCode("{");
        
        enterScope();
        
        for (int i = 0; i < n.varDecls.size(); i++) {
            writePrefix();
            n.varDecls.elementAt(i).accept(this);
            writelnCode();
            if (i == n.varDecls.size() - 1) {
                writelnCode();
            }
        }
        
        for (int i = 0; i < n.statements.size(); i++) {
            writePrefix();
            n.statements.elementAt(i).accept(this);
            writelnCode();
        }
        
        writePrefix();
        writeCode("return ");
        n.returnExp.accept(this);
        writelnCode(";");
        
        leaveScope(); 
        writePrefix();
        writeCode("}");
    }

    @Override
    public void visit(Formal n) {
        n.type.accept(this);
        writeCode(" ");
        n.id.accept(this);
    }

    @Override
    public void visit(IntArrayType n) {
        writeCode("int []");
    }

    @Override
    public void visit(BooleanType n) {
        writeCode("boolean");
    }

    @Override
    public void visit(IntegerType n) {
        writeCode("int");
    }

    @Override
    public void visit(IdentifierType n) {
        writeCode(n.name);
    }

    @Override
    public void visit(Block n) {
        writelnCode("{");
        enterScope();
        
        for (int i = 0; i < n.statements.size(); i++) {
            writePrefix();
            n.statements.elementAt(i).accept(this);
            writelnCode();
        }
        
        leaveScope(); 
        writePrefix();
        writeCode("}");
    }

    @Override
    public void visit(If n) {
        writeCode("if (");
        n.exp.accept(this);
        writeCode(") ");
        
        n.thenStatement.accept(this);
        
        writeCode(" else ");
        n.elseStatement.accept(this);
    }

    @Override
    public void visit(While n) {
        writeCode("while (");
        n.exp.accept(this);
        writeCode(") ");
        
        n.statement.accept(this);
    }

    @Override
    public void visit(Print n) {
        writeCode("System.out.println(");
        n.exp.accept(this);
        writeCode(");");
    }

    @Override
    public void visit(Assign n) {
        n.id.accept(this);
        writeCode(" = ");
        n.exp.accept(this);
        writeCode(";");
    }

    @Override
    public void visit(ArrayAssign n) {
        n.arrayId.accept(this);
        writeCode("[");
        n.index.accept(this);
        writeCode("] = ");
        n.exp.accept(this);
        writeCode(";");
    }

    @Override
    public void visit(And n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" && ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(LessThan n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" < ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Plus n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" + ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Minus n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" - ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Times n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" * ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(ArrayLookup n) {
        n.arrayId.accept(this);
        writeCode("[");
        n.index.accept(this);
        writeCode("]");
    }

    @Override
    public void visit(ArrayLength n) {
        n.arrayId.accept(this);
        writeCode(".length");
    }

    @Override
    public void visit(Call n) {
        n.exp.accept(this);
        writeCode(".");
        n.methodId.accept(this);
        writeCode("(");
        for (int i = 0; i < n.expList.size(); i++) {
            n.expList.elementAt(i).accept(this);
            if (i + 1 < n.expList.size()) {
                writeCode(", ");
            }
        }
        writeCode(")");
    }

    @Override
    public void visit(IntegerLiteral n) {
        writeCode(n.value);
    }

    @Override
    public void visit(True n) {
        writeCode("true");
    }

    @Override
    public void visit(False n) {
        writeCode("false");
    }

    @Override
    public void visit(IdentifierExp n) {
        writeCode(n.name);
    }

    @Override
    public void visit(This n) {
        writeCode("this");
    }

    @Override
    public void visit(NewArray n) {
        writeCode("new int [");
        n.exp.accept(this);
        writeCode("]");
    }

    @Override
    public void visit(NewObject n) {
        writeCode("new ");
        writeCode(n.classId.name);
        writeCode("()");
    }

    @Override
    public void visit(Not n) {
        writeCode("(!");
        n.exp.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Identifier n) {
        writeCode(n.name);
    }
    
    
/* ==========================================================================
 * helper methods
 * ======================================================================= */
    
    public void writelnCode() {
        System.out.println();
    }
    
    public void writelnCode(String code) {
        System.out.println(code);
    }
    
    public void writeCode(String code) {
        System.out.print(code);
    }
    
    public void writeCode(int num) {
        System.out.print(num + "");
    }
    
    public void writePrefix() {
        for (int i = 0; i < indent; i++) {
            writeCode("    ");
        }
    }
    
    private void enterScope() {
        this.indent++;
    }

    private void leaveScope() {
        this.indent--;
    }
}
