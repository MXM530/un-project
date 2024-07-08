package org.example;

import java.util.List;

class RepeatStatement extends Statement {
    List<Statement> statements;
    Expression condition;

    RepeatStatement(List<Statement> statements, Expression condition, int line) {
        this.statements = statements;
        this.condition = condition;
        this.line = line;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Expression getCondition() {
        return condition;
    }
}
