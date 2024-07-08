package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TinyScanner {

    static int lineno = 0;
    static int linepos = 0;
    static int linesize = 0;
    static boolean saveflag = true;
    static String res = "";
    static StateType state = StateType.START;
    static TokenType token;

    static boolean isID(char c) {
        return Character.isLetter(c);
    }

    static boolean isNUM(char c) {
        return Character.isDigit(c);
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=' || c == '<' || c == '(' || c == ')' || c == ';';
    }

    static boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t';
    }

    static TokenType identifyReserved(String s) {
        switch (s) {
            case "if":
                return TokenType.IF;
            case "else":
                return TokenType.ELSE;
            case "then":
                return TokenType.THEN;
            case "end":
                return TokenType.END;
            case "repeat":
                return TokenType.REPEAT;
            case "until":
                return TokenType.UNTIL;
            case "read":
                return TokenType.READ;
            case "write":
                return TokenType.WRITE;
            default:
                return TokenType.ID;
        }
    }

    static List<Token> scanToken(String line) {
        List<Token> tokens = new ArrayList<>();
        res = "";
        linepos = 0;
        linesize = line.length();
        System.out.printf("Scanning line: %s%n", line);
        while (linepos < linesize) {
            saveflag = true;
            char ch = line.charAt(linepos);
            System.out.printf("State: %s, Char: '%c', LinePos: %d%n", state, ch, linepos);
            switch (state) {
                case START:
                    if (isWhiteSpace(ch)) {
                        saveflag = false;
                    } else if (isID(ch)) {
                        state = StateType.INID;
                    } else if (isNUM(ch)) {
                        state = StateType.INNUM;
                    } else if (ch == ':') {
                        state = StateType.INASSIGN;
                    } else if (ch == '{') {
                        saveflag = false;
                        state = StateType.INCOMMENT;
                    } else if (isOperator(ch)) {
                        state = StateType.DONE;
                        token = identifyOperator(ch);
                    } else {
                        state = StateType.ERROR;
                    }
                    break;
                case INID:
                    if (!isID(ch)) {
                        state = StateType.DONE;
                        linepos--;
                        saveflag = false;
                        token = identifyReserved(res); // 检查是否是保留字
                    }
                    break;
                case INNUM:
                    if (!isNUM(ch)) {
                        state = StateType.DONE;
                        linepos--;
                        saveflag = false;
                        token = TokenType.NUM;
                    }
                    break;
                case INASSIGN:
                    if (ch == '=') {
                        state = StateType.DONE;
                        token = TokenType.ASSIGN;
                    } else {
                        state = StateType.ERROR;
                        linepos--;
                        saveflag = false;
                    }
                    break;
                case INCOMMENT:
                    if (ch == '}') {
                        state = StateType.START;
                        saveflag = false;
                    } else {
                        saveflag = false;
                    }
                    break;
                case ERROR:
                    state = StateType.DONE;
                    linepos--;
                    saveflag = false;
                    token = TokenType.ERR;
                    break;
                default:
                    break;
            }
            linepos++;
            if (saveflag) {
                res += ch;
            }
            if (state == StateType.DONE) {
                if (!res.trim().isEmpty()) {
                    System.out.printf("Token identified: Type=%s, Text='%s'%n", token, res.trim());
                    tokens.add(new Token(token, res.trim(), lineno));
                }
                res = "";
                state = StateType.START;
            }
        }
        // 处理行末的token
        if (state == StateType.INID) {
            token = identifyReserved(res);
            tokens.add(new Token(token, res.trim(), lineno));
            System.out.printf("End of line token: Type=%s, Text='%s'%n", token, res.trim());
        } else if (state == StateType.INNUM) {
            token = TokenType.NUM;
            tokens.add(new Token(token, res.trim(), lineno));
            System.out.printf("End of line token: Type=%s, Text='%s'%n", token, res.trim());
        } else if (state == StateType.INASSIGN) {
            token = TokenType.ERR;
            tokens.add(new Token(token, res.trim(), lineno));
            System.out.printf("End of line token: Type=%s, Text='%s'%n", token, res.trim());
        }
        return tokens;
    }

    private static TokenType identifyOperator(char ch) {
        switch (ch) {
            case '+': return TokenType.PLUS;
            case '-': return TokenType.MINUS;
            case '*': return TokenType.MULTI;
            case '/': return TokenType.DIV;
            case '=': return TokenType.EQ;
            case '<': return TokenType.LESS;
            case '(': return TokenType.LPAR;
            case ')': return TokenType.RPAR;
            case ';': return TokenType.COLON;
            default: return TokenType.ERR;
        }
    }

    public static List<Token> scanFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<Token> tokens = new ArrayList<>();
        String line;
        lineno = 0;
        while ((line = reader.readLine()) != null) {
            lineno++;
            tokens.addAll(scanToken(line));
        }
        reader.close();
        return tokens;
    }
}
