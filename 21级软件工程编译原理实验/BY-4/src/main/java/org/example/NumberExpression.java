package org.example;

class NumberExpression extends Expression {
        int value;

        NumberExpression(int value, int line) {
            this.value = value;
            this.line = line;
        }

    public int getValue() {
            return value;
    }
}
