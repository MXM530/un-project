package org.example;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token consume(TokenType type, String errorMessage) {
        if (check(type)) {
            System.out.println("Consuming token: " + peek());
            return advance();
        }
        throw new RuntimeException(errorMessage + " at line " + peek().getLine());
    }

    public Program parse() {
        System.out.println("Starting parse......");
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseStatement());
        }
        System.out.println("Completed parse.");
        return new Program(statements);
    }

    private Statement parseStatement() {
        Token token = peek();
        System.out.println();
        System.out.println("Parsing statement starting with token:  " + token);
        switch (token.getType()) {
            case IF:
                return parseIfStatement();
            case REPEAT:
                return parseRepeatStatement();
            case ID:
                return parseAssignmentStatement();
            case READ:
                return parseReadStatement();
            case WRITE:
                return parseWriteStatement();
            default:
                throw new RuntimeException("Unexpected token: " + token.getType() + " at line " + token.getLine());
        }
    }

    private IfStatement parseIfStatement() {
        System.out.println("Parsing if statement...");
        Token ifToken = consume(TokenType.IF, "Expect 'if'");
        Expression condition = parseExpression();
        consume(TokenType.THEN, "Expect 'then'");
        List<Statement> thenStatements = new ArrayList<>();
        while (!check(TokenType.ELSE) && !check(TokenType.END) && !isAtEnd()) {
            thenStatements.add(parseStatement());
        }
        List<Statement> elseStatements = new ArrayList<>();
        if (check(TokenType.ELSE)) {
            advance();
            while (!check(TokenType.END) && !isAtEnd()) {
                elseStatements.add(parseStatement());
            }
        }
        consume(TokenType.END, "Expect 'end'");
        System.out.println("Completed if statement.");
        return new IfStatement(condition, thenStatements, elseStatements, ifToken.getLine());
    }




    private RepeatStatement parseRepeatStatement() {
        System.out.println("Parsing repeat statement...");
        Token repeatToken = consume(TokenType.REPEAT, "Expect 'repeat'");
        List<Statement> statements = new ArrayList<>();
        while (!check(TokenType.UNTIL) && !isAtEnd()) {
            statements.add(parseStatement());
        }
        consume(TokenType.UNTIL, "Expect 'until'");
        Expression condition = parseExpression();
        consume(TokenType.COLON, "Expect ';'"); // Ensure the ';' after the condition
        System.out.println("Completed repeat statement.");
        return new RepeatStatement(statements, condition, repeatToken.getLine());
    }

    private AssignStatement parseAssignmentStatement() {
        System.out.println("Parsing assignment statement...");
        Token idToken = consume(TokenType.ID, "Expect variable name");
        consume(TokenType.ASSIGN, "Expect ':='");
        Expression expression = parseExpression();
        consume(TokenType.COLON, "Expect ';'");
        System.out.println("Completed assignment statement.");
        return new AssignStatement(idToken.getText(), expression, idToken.getLine());
    }

    private ReadStatement parseReadStatement() {
        System.out.println("Parsing read statement...");
        Token readToken = consume(TokenType.READ, "Expect 'read'");
        Token idToken = consume(TokenType.ID, "Expect variable name");
        consume(TokenType.COLON, "Expect ';'");
        System.out.println("Completed read statement.");
        return new ReadStatement(idToken.getText(), readToken.getLine());
    }

    private WriteStatement parseWriteStatement() {
        System.out.println("Parsing write statement...");
        Token writeToken = consume(TokenType.WRITE, "Expect 'write'");
        Expression expression = parseExpression();
        System.out.println("Completed write statement.");
        return new WriteStatement(expression, writeToken.getLine());
    }

    private Expression parseExpression() {
        System.out.println("Parsing expression...");
        Expression expression = parseSimpleExpression();
        while (check(TokenType.LESS) || check(TokenType.EQ)) {
            Token operator = advance(); // consume operator
            Expression right = parseSimpleExpression();
            expression = new BinaryExpression(expression, operator.getType(), right, operator.getLine());
        }
        System.out.println("Completed expression.");
        return expression;
    }

    private Expression parseSimpleExpression() {
        System.out.println("Parsing simple expression...");
        Expression expression = parseTerm();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            Token operator = advance(); // consume operator
            Expression right = parseTerm();
            expression = new BinaryExpression(expression, operator.getType(), right, operator.getLine());
        }
        System.out.println("Completed simple expression.");
        return expression;
    }

    private Expression parseTerm() {
        System.out.println("Parsing term...");
        Expression expression = parseFactor();
        while (check(TokenType.MULTI) || check(TokenType.DIV)) {
            Token operator = advance(); // consume operator
            Expression right = parseFactor();
            expression = new BinaryExpression(expression, operator.getType(), right, operator.getLine());
        }
        System.out.println("Completed term.");
        return expression;
    }

    private Expression parseFactor() {
        System.out.println("Parsing factor...");
        if (check(TokenType.NUM)) {
            Token number = advance();
            System.out.println("Number: " + number.getText());
            return new NumberExpression(Integer.parseInt(number.getText()), number.getLine());
        } else if (check(TokenType.ID)) {
            Token variable = advance();
            System.out.println("Variable: " + variable.getText());
            return new VariableExpression(variable.getText(), variable.getLine());
        } else if (check(TokenType.LPAR)) {
            advance(); // consume '('
            Expression expression = parseExpression();
            consume(TokenType.RPAR, "Expect ')'");
            return expression;
        } else {
            throw new RuntimeException("Unexpected token in expression: " + peek().getType() + " at line " + peek().getLine());
        }
    }
}
