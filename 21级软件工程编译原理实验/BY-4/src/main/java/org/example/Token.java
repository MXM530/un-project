package org.example;

public class Token {
    private TokenType type;   // Token的类型，由TokenType枚举定义
    private String text;      // Token的文本内容
    private int line;         // Token出现在源代码中的行号，用于错误报告

    public Token(TokenType type, String text, int line) {
        this.type = type;
        this.text = text;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s', line %d)", type, text, line);
    }
}
