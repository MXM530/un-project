package org.example;

import java.util.List;
import java.util.Map;

class IfStatement extends Statement {
    Expression condition;
    List<Statement> thenStatements;
    List<Statement> elseStatements;

    IfStatement(Expression condition, List<Statement> thenStatements, List<Statement> elseStatements, int line) {
        this.condition = condition;
        this.thenStatements = thenStatements;
        this.elseStatements = elseStatements;
        this.line = line;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getThenStatements() {
        return thenStatements;
    }

    public List<Statement> getElseStatements() {
        return elseStatements;
    }


}
