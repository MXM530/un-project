package org.example;

import java.util.List;

class Program extends ASTNode {
    List<Statement> statements;

    Program(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}

