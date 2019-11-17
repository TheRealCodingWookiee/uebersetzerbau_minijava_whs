package syntaxtree;

import visitor.Visitor;

/**
 * Objects of this class represent a block, i.e. a list of statements
 */
public class Block extends Statement {

    public StatementList statements;

    public Block(StatementList statements) {
        this.statements = statements;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

