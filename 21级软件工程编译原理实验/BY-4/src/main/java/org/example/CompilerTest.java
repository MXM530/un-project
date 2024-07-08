package org.example;

import java.io.IOException;

public class CompilerTest {
    public static void main(String[] args) {
        TinyCompiler compiler = new TinyCompiler();
        String inputFileName = "sample.tny";  // 这里填写你的输入文件名
        String outputFileName = "output.tm";  // 这里填写你希望生成的输出文件名

        try {
            compiler.compile(inputFileName, outputFileName);
            System.out.println("TM 源代码生成成功！");
        } catch (IOException e) {
            System.err.println("编译过程中出现错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
