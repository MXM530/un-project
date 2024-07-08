package org.example;

import java.io.IOException;
import java.util.List;

public class LexerTest {

    public static void main(String[] args) {
        try {
            List<Token> tokens = TinyScanner.scanFile("sample.tny");
            printTokens(tokens);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void printTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.printf("Token Type: %-10s Token: %-10s Is Reserved: %b%n",
                    token.getType(),
                    token.getText(),
                    isReservedWord(token.getText())
            );
        }
    }

    private static boolean isReservedWord(String token) {
        switch (token) {
            case "if":
            case "else":
            case "then":
            case "end":
            case "repeat":
            case "until":
            case "read":
            case "write":
                return true;
            default:
                return false;
        }
    }
}