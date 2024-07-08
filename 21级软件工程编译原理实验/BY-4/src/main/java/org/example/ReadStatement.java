package org.example;

class ReadStatement extends Statement {
    String variable;

    ReadStatement(String variable, int line) {
        this.variable = variable;
        this.line = line;
    }

    public String getVariable() {
        return variable;
    }
}
