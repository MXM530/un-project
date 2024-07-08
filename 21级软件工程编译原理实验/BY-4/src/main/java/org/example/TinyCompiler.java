package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinyCompiler {
    private final int ac = 0;
    private final int ac1 = 1;
    private final int gp = 5;
    private final int pc = 7;
    private final int mp = 6;
    private int tmpOffset = 0;
    private final Instruction[] code = new Instruction[100];
    private int emitLoc = 0;
    private final Map<String, Integer> memoryMap = new HashMap<>();

    public void compile(String inputFileName, String outputFileName) throws IOException {
        System.out.printf("开始编译：从文件 %s 到文件 %s\n", inputFileName, outputFileName);
        TinyScanner scanner = new TinyScanner();
        List<Token> tokens = scanner.scanFile(inputFileName);
        System.out.println("扫描完成，生成的tokens数量：" + tokens.size());
        Parser parser = new Parser(tokens);
        Program program = parser.parse();
        System.out.println("解析完成");
        System.out.println();
        generateProgram(program);
        saveOutput(outputFileName);
        System.out.println("编译完成，输出保存至：" + outputFileName);
    }

    private void generateProgram(Program program) {
        System.out.println("开始生成程序代码");
        emitRM("LD", mp, 0, 0, "load max address from location 0");
        emitRM("ST", ac, 0, 0, "clear location 0");

        for (Statement stmt : program.getStatements()) {
            generateStatement(stmt);
        }
        emitRO("HALT", 0, 0, 0, "halt");
        System.out.println("程序代码生成完成");
    }

    private void generateStatement(Statement stmt) {
        System.out.println("生成语句: " + stmt.getClass().getSimpleName());
        if (stmt instanceof IfStatement) {
            generateIfStatement((IfStatement) stmt);
        } else if (stmt instanceof RepeatStatement) {
            generateRepeatStatement((RepeatStatement) stmt);
        } else if (stmt instanceof AssignStatement) {
            generateAssignment((AssignStatement) stmt);
        } else if (stmt instanceof ReadStatement) {
            generateReadStatement((ReadStatement) stmt);
        } else if (stmt instanceof WriteStatement) {
            generateWriteStatement((WriteStatement) stmt);
        }
    }

    private void generateIfStatement(IfStatement stmt) {
        System.out.println("开始生成 If 语句");
        generateExpression(stmt.getCondition());
        int jumpToElse = emitSkip(1); // 占位符，需要后续修补
        System.out.println("If 条件为假时跳转到 Else: 地址 " + jumpToElse);

        for (Statement thenStmt : stmt.getThenStatements()) {
            generateStatement(thenStmt);
        }
        int skipElse = emitSkip(1); // 占位符，需要后续修补
        System.out.println("跳过 Else 的跳转地址: " + skipElse);

        if (!stmt.getElseStatements().isEmpty()) {
            backpatch(jumpToElse, "JEQ", currentLine() - jumpToElse); // 修正到 Else 开始的地址
            for (Statement elseStmt : stmt.getElseStatements()) {
                generateStatement(elseStmt);
            }
        } else {
            backpatch(jumpToElse, "LDA", currentLine() - jumpToElse); // 如果没有 Else，直接跳到 If 语句结束
        }

        backpatch(skipElse, "LDA", currentLine() - skipElse); // 修正到 If 语句结束的地址
        System.out.println("If 语句结束于: " + currentLine());
    }

    private void generateRepeatStatement(RepeatStatement stmt) {
        System.out.println("开始生成 Repeat 语句");
        int loopStart = currentLine();
        System.out.println("循环开始于: " + loopStart);
        for (Statement s : stmt.getStatements()) {
            generateStatement(s);
        }
        generateExpression(stmt.getCondition());
        emitRM("JEQ", ac, loopStart - currentLine() - 1, pc, "repeat until x = 0");
        System.out.println("Repeat 语句生成完成，跳回到: " + loopStart);
    }

    private void generateAssignment(AssignStatement stmt) {
        System.out.println("开始生成赋值语句: " + stmt.getVariable());
        generateExpression(stmt.getExpression());
        int memLoc = getMemoryLocation(stmt.getVariable());
        emitRM("ST", ac, memLoc, gp, "assign: store value");
        System.out.println("赋值完成，变量 " + stmt.getVariable() + " 存储于地址: " + memLoc);
    }

    private void generateReadStatement(ReadStatement stmt) {
        System.out.println("开始生成读取语句: " + stmt.getVariable());
        emitRO("IN", ac, 0, 0, "read integer value");
        int memLoc = getMemoryLocation(stmt.getVariable());
        emitRM("ST", ac, memLoc, gp, "read: store value");
        System.out.println("读取完成，输入值存储于地址: " + memLoc);
    }

    private void generateWriteStatement(WriteStatement stmt) {
        System.out.println("开始生成写入语句");
        generateExpression(stmt.getExpression());
        emitRO("OUT", ac, 0, 0, "write integer value");
        System.out.println("写入完成");
    }
    private void generateExpression(Expression expr) {
        if (expr instanceof BinaryExpression) {
            BinaryExpression binExpr = (BinaryExpression) expr;
            generateExpression(binExpr.getLeft());
            emitRM("ST", ac, tmpOffset--, mp, "save left operand");
            generateExpression(binExpr.getRight());
            emitRM("LD", ac1, ++tmpOffset, mp, "load left operand");
            switch (binExpr.getOperator()) {
                case PLUS:
                    emitRO("ADD", ac, ac1, ac, "op +");
                    break;
                case MINUS:
                    emitRO("SUB", ac, ac1, ac, "op -");
                    break;
                case MULTI:
                    emitRO("MUL", ac, ac1, ac, "op *");
                    break;
                case DIV:
                    emitRO("DIV", ac, ac1, ac, "op /");
                    break;
                case LESS:
                    emitRO("SUB", ac, ac1, ac, "op <");
                    emitRM("JLT", ac, 2, pc, "jump if less");
                    emitRM("LDC", ac, 0, 0, "false case");
                    emitRM("LDA", pc, 1, pc, "skip true case");
                    emitRM("LDC", ac, 1, 0, "true case");
                    break;
                case EQ:
                    emitRO("SUB", ac, ac1, ac, "op ==");
                    emitRM("JEQ", ac, 2, pc, "jump if equal");
                    emitRM("LDC", ac, 0, 0, "false case");
                    emitRM("LDA", pc, 1, pc, "skip true case");
                    emitRM("LDC", ac, 1, 0, "true case");
                    break;
            }
        } else if (expr instanceof NumberExpression) {
            NumberExpression numExpr = (NumberExpression) expr;
            emitRM("LDC", ac, numExpr.getValue(), 0, "load constant");
        } else if (expr instanceof VariableExpression) {
            VariableExpression varExpr = (VariableExpression) expr;
            emitRM("LD", ac, getMemoryLocation(varExpr.getName()), gp, "load variable");
        }
    }

    private int getMemoryLocation(String varName) {
        return memoryMap.computeIfAbsent(varName, k -> memoryMap.size());
    }

    private int emitSkip(int howMany) {
        int i = emitLoc;
        emitLoc += howMany;
        // 确保不会超出指令数组的界限
        for (int j = 0; j < howMany; j++) {
            if (i + j < code.length) {
                code[i + j] = new Instruction("", 0, 0, 0, "skip");
            } else {
                System.err.println("Error: Skipping out of code array bounds.");
                throw new ArrayIndexOutOfBoundsException("Skipping out of code array bounds.");
            }
        }
        return i;
    }


    private void backpatch(int addr, String op, int value) {
        if (addr >= 0 && addr < code.length && code[addr] != null) {
            code[addr].setOp(op);
            code[addr].setD(value);
            System.out.println("Back at " + addr + " with operation " + op + " and value " + value);
        } else {
            System.err.println("Error: Instruction at address " + addr + " is null.");
            throw new IllegalArgumentException("Instruction at address " + addr + " is null.");
        }
    }


    private int currentLine() {
        return emitLoc;
    }

    private void ensureCapacity() {
        if (emitLoc >= code.length) {
            // 扩展数组或抛出异常
            System.err.println("Error: Instruction limit exceeded, consider increasing the code array size.");
            throw new IllegalStateException("Instruction limit exceeded.");
        }
    }

    private void emitRM(String op, int r, int d, int s, String comment) {
        ensureCapacity();  // 确保有足够空间
        code[emitLoc] = new Instruction(op, r, d, s, comment);
        // 在控制台打印完整指令，包括注释
        System.out.printf("%3d:  %5s  %d,%d(%d)  %s\n", emitLoc, op, r, d, s, comment);
        emitLoc++;
    }

    private void emitRO(String op, int r, int s, int t, String comment) {
        ensureCapacity();  // 确保有足够空间
        code[emitLoc] = new Instruction(op, r, s, t, comment);
        // 在控制台打印完整指令，包括注释
        System.out.printf("%3d:  %5s  %d,%d,%d  %s\n", emitLoc, op, r, s, t, comment);
        emitLoc++;
    }


    private void saveOutput(String outputFileName) throws IOException {
        System.out.println("开始保存输出到文件: " + outputFileName);
        try (PrintWriter out = new PrintWriter(new FileWriter(outputFileName))) {
            for (int i = 0; i < emitLoc; i++) {
                Instruction inst = code[i];
                if (inst.getOp().matches("IN|OUT|HALT")) {
                    out.printf("%3d:  %5s  %d,0,0\n", i, inst.getOp(), inst.getR()); // 对于 IN, OUT, HALT 特别处理
                } else if (inst.getOp().matches("SUB|ADD|MUL|DIV|JLT|JEQ")) {
                    out.printf("%3d:  %5s  %d,%d,%d\n", i, inst.getOp(), inst.getR(), inst.getD(), inst.getS()); // 处理三参数指令
                } else {
                    out.printf("%3d:  %5s  %d,%d(%d)\n", i, inst.getOp(), inst.getR(), inst.getD(), inst.getS()); // 常规指令
                }
            }
        }
        System.out.println("输出保存完成");
    }




}
