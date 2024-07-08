package org.example;

class AssignStatement extends Statement {
    String variable;
    Expression expression;

    AssignStatement(String variable, Expression expression, int line) {
        this.variable = variable;
        this.expression = expression;
        this.line = line;
    }

    public String getVariable() {
        return variable;
    }

    public Expression getExpression() {
        return expression;
    }
}
