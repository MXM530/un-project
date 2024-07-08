package org.example;

class VariableExpression extends Expression {
    String name;

    VariableExpression(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }
}
