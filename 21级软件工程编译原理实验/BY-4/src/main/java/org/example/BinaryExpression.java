package org.example;

class BinaryExpression extends Expression {
    Expression left;
    TokenType operator;
    Expression right;

    BinaryExpression(Expression left, TokenType operator, Expression right, int line) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.line = line;
    }

    public Expression getLeft() {
        return left;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }
}
