import java.util.ArrayList;
import symbol.DriverType;
import symbol.Graph;
import symbol.LexemeCategory;
public class Problem4 {
    /**
     * 测试(a|b)*abb
     */
    public void test1() {
        //驱动字符集
        int driverIdA = Problem1.range('a','a');
        int driverIdB = Problem1.range('b','b');
        System.out.println("driverId1: "+driverIdA);
        System.out.println("driverId2: "+driverIdB);

        //构建NFA
        System.out.println("构建NFA如下：");
        //a
        Graph graphA = Problem2.generateBasicNFA(DriverType.CHAR,driverIdA,LexemeCategory.EMPTY);
        System.out.println("a");
        System.out.println(graphA);
        //b
        Graph graphB = Problem2.generateBasicNFA(DriverType.CHAR,driverIdB,LexemeCategory.EMPTY);
        System.out.println("b");
        System.out.println(graphB);
        //a|b
        Graph graph3 = Problem2.union(graphA,graphB);
        System.out.println("a|b");
        System.out.println(graph3);
        //(a|b)*
        Graph graph4 = Problem2.closure(graph3);
        System.out.println("(a|b)*");
        System.out.println(graph4);
        //(a|b)*a
        graphA = Problem2.generateBasicNFA(DriverType.CHAR,driverIdA,LexemeCategory.EMPTY);
        Graph graph5 = Problem2.product(graph4,graphA);
        System.out.println("(a|b)*a");
        System.out.println(graph5);
        //(a|b)*ab
        graphB = Problem2.generateBasicNFA(DriverType.CHAR,driverIdB,LexemeCategory.EMPTY);
        Graph graph6 = Problem2.product(graph5,graphB);
        System.out.println("(a|b)*ab");
        System.out.println(graph6);
        //(a|b)*abb
        graphB = Problem2.generateBasicNFA(DriverType.CHAR,driverIdB,LexemeCategory.EMPTY);
        Graph graph7 = Problem2.product(graph6,graphB);
        System.out.println("(a|b)*abb");
        System.out.println(graph7);

        System.out.println("构建DFA如下: ");
        Graph graphDFA = Problem3.NFAToDFA(graph7);
        System.out.println(graphDFA);


    }

    /**
     * 测试TINY语言
     */
    public void test2() {
        //构建字符集
        int charSetLetterLower = Problem1.range('a','z');   //indexId = 0 segmentId = 0
        int charSetLetterUpper = Problem1.range('A','Z');   //indexId = 1 segmentId = 1
        int charSetDigit = Problem1.range('0','9');         //indexId = 2 segmentId = 2
        //子母集
        int charSetLetter = Problem1.union(charSetLetterLower,charSetLetterUpper);  //indexId = 3, ['a','Z'],segmentId = 3

        ArrayList<Integer> charSetList = new ArrayList<>();
        for(char ch = 'a'; ch <= 'z'; ch++) {
            charSetList.add(Problem1.range(ch,ch));         //给每一个小写字母添加一个字符集，添加成段
        }
        //System.out.println(charSetList);  4-29

        //定义运算符
        int charSetPlus = Problem1.range('+', '+');
        int charSetSub = Problem1.range('-', '-');
        int charSetMul = Problem1.range('*', '*');
        int charSetDiv = Problem1.range('/', '/');
        int charSetEqual = Problem1.range('=', '=');
        int charSetLt = Problem1.range('<', '<');
        int charSetLeftBracket = Problem1.range('(', '(');
        int charSetRightBracket = Problem1.range(')', ')');
        int charSetSemicolon = Problem1.range(';', ';');
        int charSetColon = Problem1.range(':', ':');
        int charSetSpace = Problem1.range(' ', ' ');
        int charSetLeftK = Problem1.range('{', '{');
        int charSetRightK = Problem1.range('}', '}');

        //关键字if
        Graph graphI = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(8),LexemeCategory.EMPTY);
        Graph graphF = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(5),LexemeCategory.KEYWORD);
        Graph graphIF = Problem2.product(graphI,graphF);

        //关键字then
        Graph graphT = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(19),LexemeCategory.EMPTY);
        Graph graphH = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(7),LexemeCategory.EMPTY);
        Graph graphE = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.EMPTY);
        Graph graphN = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(13),LexemeCategory.KEYWORD);
        Graph graphTH = Problem2.product(graphT,graphH);
        Graph graphTHE = Problem2.product(graphTH,graphE);
        Graph graphTHEN= Problem2.product(graphTHE,graphN);

        //关键字else
        graphE = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.EMPTY);
        Graph graphL = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(11),LexemeCategory.EMPTY);
        Graph graphS = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(18),LexemeCategory.EMPTY);
        Graph graphE1 = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.KEYWORD);
        Graph graphEL = Problem2.product(graphE,graphL);
        Graph graphELS = Problem2.product(graphEL,graphS);
        Graph graphELSE = Problem2.product(graphELS,graphE1);

        //end
        graphE = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.EMPTY);
        graphN = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(13),LexemeCategory.EMPTY);
        Graph graphD = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(3),LexemeCategory.KEYWORD);
        Graph graphEN = Problem2.product(graphE,graphN);
        Graph graphEND = Problem2.product(graphEN,graphD);

        //repeat
        Graph graphR = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(17),LexemeCategory.EMPTY);
        graphE = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.EMPTY);
        Graph graphP = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(15),LexemeCategory.EMPTY);
        graphE1 =Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(17),LexemeCategory.EMPTY);
        Graph graphA = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(0),LexemeCategory.EMPTY);
        graphT = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(19),LexemeCategory.KEYWORD);
        Graph graphRE = Problem2.product(graphR,graphE);
        Graph graphREP = Problem2.product(graphRE,graphP);
        Graph graphREPE = Problem2.product(graphREP,graphE1);
        Graph graphREPEA = Problem2.product(graphREPE,graphA);
        Graph graphREPEAT = Problem2.product(graphREPEA,graphT);

        //until
        Graph graphU = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(20),LexemeCategory.EMPTY);
        graphN = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(13),LexemeCategory.EMPTY);
        graphT = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(19),LexemeCategory.EMPTY);
        graphI =Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(8),LexemeCategory.EMPTY);
        graphL = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(11),LexemeCategory.KEYWORD);
        Graph graphUN = Problem2.product(graphU,graphN);
        Graph graphUNT = Problem2.product(graphUN,graphT);
        Graph graphUNTI = Problem2.product(graphUNT,graphI);
        Graph graphUNTIL = Problem2.product(graphUNTI,graphL);

        //read
        graphR = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(17),LexemeCategory.EMPTY);
        graphE = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.EMPTY);
        graphA = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(0),LexemeCategory.EMPTY);
        graphD = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(3),LexemeCategory.KEYWORD);
        graphRE = Problem2.product(graphR,graphE);
        Graph graphREA = Problem2.product(graphRE,graphA);
        Graph graphREAD = Problem2.product(graphREA,graphD);

        //write
        Graph graphW = Problem2.generateBasicNFA(DriverType.CHAR, charSetList.get(22), LexemeCategory.EMPTY);
        graphR = Problem2.generateBasicNFA(DriverType.CHAR, charSetList.get(17), LexemeCategory.EMPTY);
        graphI = Problem2.generateBasicNFA(DriverType.CHAR, charSetList.get(8), LexemeCategory.EMPTY);
        graphT = Problem2.generateBasicNFA(DriverType.CHAR, charSetList.get(19), LexemeCategory.EMPTY);
        graphE = Problem2.generateBasicNFA(DriverType.CHAR, charSetList.get(4), LexemeCategory.KEYWORD);
        Graph graphWR = Problem2.product(graphW, graphR);
        Graph graphWRI = Problem2.product(graphWR, graphI);
        Graph graphWRIT = Problem2.product(graphWRI, graphT);
        Graph graphWRITE = Problem2.product(graphWRIT, graphE);

        //运算符
        //+
        Graph graphPlus = Problem2.generateBasicNFA(DriverType.CHAR,charSetPlus,LexemeCategory.NUMERIC_OPERATOR);
        //-
        Graph graphSub = Problem2.generateBasicNFA(DriverType.CHAR,charSetSub,LexemeCategory.NUMERIC_OPERATOR);
        //*
        Graph graphMul = Problem2.generateBasicNFA(DriverType.CHAR,charSetMul,LexemeCategory.NUMERIC_OPERATOR);
        // /
        Graph graphDiv = Problem2.generateBasicNFA(DriverType.CHAR,charSetDiv,LexemeCategory.NUMERIC_OPERATOR);
        //<
        Graph graphLt = Problem2.generateBasicNFA(DriverType.CHAR,charSetLt,LexemeCategory.COMPARE_CONST);
        // =
        Graph graphEQ = Problem2.generateBasicNFA(DriverType.CHAR,charSetEqual,LexemeCategory.LOGIC_OPERATOR);
        // (
        Graph graphLeftBracket = Problem2.generateBasicNFA(DriverType.CHAR,charSetLeftBracket,LexemeCategory.LOGIC_OPERATOR);
        // )
        Graph graphRightBracket = Problem2.generateBasicNFA(DriverType.CHAR,charSetRightBracket,LexemeCategory.LOGIC_OPERATOR);
        // ;
        Graph graphSemicolon = Problem2.generateBasicNFA(DriverType.CHAR,charSetSemicolon,LexemeCategory.LOGIC_OPERATOR);
        // :=
        Graph graphColon = Problem2.generateBasicNFA(DriverType.CHAR,charSetColon,LexemeCategory.LOGIC_OPERATOR);
        // ID =letter+
        Graph graphLetter = Problem2.generateBasicNFA(DriverType.CHARSET,charSetLetter,LexemeCategory.ID);
        Graph graphID = Problem2.plusClosure(graphLetter);
        //Num = digit+
        Graph graphDigit = Problem2.generateBasicNFA(DriverType.CHARSET,charSetDigit,LexemeCategory.INTEGER_CONST);
        Graph graphNum = Problem2.plusClosure(graphDigit);
        // 空格
        Graph graphSpace = Problem2.generateBasicNFA(DriverType.CHARSET,charSetSpace,LexemeCategory.SPACE_CONST);
        //注释用{...}围起来，且不能嵌套。
        Graph graphLeftK = Problem2.generateBasicNFA(DriverType.CHAR,charSetLeftK,LexemeCategory.EMPTY);
        Graph graphLetter1 = Problem2.generateBasicNFA(DriverType.CHARSET,charSetLetter,LexemeCategory.EMPTY);
        Graph graphContent = Problem2.closure(graphLetter1);  //闭包运算求内容
        Graph graphRightK = Problem2.generateBasicNFA(DriverType.CHARSET,charSetRightK,LexemeCategory.NOTE);
        Graph graphCOMMENT = Problem2.product(Problem2.product(graphLeftK,graphContent),graphRightK);
        //error 错误信息
        graphE = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(4),LexemeCategory.EMPTY);
        graphR = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(17),LexemeCategory.EMPTY);
        Graph graphR1 = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(17),LexemeCategory.EMPTY);
        Graph graphO = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(14),LexemeCategory.EMPTY);
        Graph graphR2 = Problem2.generateBasicNFA(DriverType.CHAR,charSetList.get(17),LexemeCategory.ERROR);
        Graph graphER = Problem2.union(graphE,graphR);
        Graph graphERR = Problem2.union(graphER,graphR1);
        Graph graphERRO = Problem2.union(graphERR,graphO);
        Graph graphERROR = Problem2.union(graphERRO,graphR2);


        //构建NFA
        Graph graph1 = Problem2.union(graphIF,graphTHEN);
        Graph graph2 = Problem2.union(graph1,graphELSE);
        Graph graph3 = Problem2.union(graph2,graphEND);
        Graph graph4 = Problem2.union(graph3,graphREPEAT);
        Graph graph5 = Problem2.union(graph4,graphUNTIL);
        Graph graph6 = Problem2.union(graph5,graphREAD);
        Graph graph7 = Problem2.union(graph6,graphWRITE);
        Graph graph8 = Problem2.union(graph7,graphPlus);
        Graph graph9 = Problem2.union(graph8,graphSub);
        Graph graph10 = Problem2.union(graph9,graphMul);
        Graph graph11 = Problem2.union(graph10,graphDiv);
        Graph graph12 = Problem2.union(graph11,graphEQ);
        Graph graph13 = Problem2.union(graph12, graphLt);
        Graph graph14 = Problem2.union(graph13, graphLeftBracket);
        Graph graph15 = Problem2.union(graph14, graphRightBracket);
        Graph graph16 = Problem2.union(graph15, graphSemicolon);
        Graph graph17 = Problem2.union(graph16, graphColon);
        Graph graph18 = Problem2.union(graph17, graphID);
        Graph graph19 = Problem2.union(graph18, graphNum);
        Graph graph20 = Problem2.union(graph19, graphSpace);
        Graph graph21 = Problem2.union(graph20,graphERROR);
        Graph graphNFA = Problem2.union(graph21, graphCOMMENT);

        //构建DFA
        Graph graphDFA = Problem3.NFAToDFA(graphNFA);
        System.out.println(graphDFA);


    }

    public static void main(String[] args) {
        Problem4 problem4 = new Problem4();
        problem4.test1();
//        problem4.test2();
    }
}
