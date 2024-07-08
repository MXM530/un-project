package org.example;

class WriteStatement extends Statement {
    Expression expression;

    WriteStatement(Expression expression, int line) {
        this.expression = expression;
        this.line = line;
    }

    public Expression getExpression() {
        return expression;
    }
}
