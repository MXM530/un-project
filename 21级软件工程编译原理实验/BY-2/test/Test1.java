import org.junit.Test;
import symbol.NonTerminalSymbol;
import symbol.Production;
import symbol.SymbolType;
import symbol.TerminalSymbol;

import java.util.ArrayList;
import java.util.Set;


/**
 * Canary Test中文名为金丝雀测试，这个测试主要是在新建测试项目时用，用来测试自己工作的机器环境是否配置正确。
 */
public class Test1 {
    //左递归测试
    @Test
    public void testLeftRecursion() {
        // A->Aa,A->b
        NonTerminalSymbol A = new NonTerminalSymbol("A",SymbolType.NONTERMINAL);
        TerminalSymbol a = new TerminalSymbol("a",SymbolType.TERMINAL);
        TerminalSymbol b = new TerminalSymbol("b",SymbolType.TERMINAL);
        //产生式
        Production p1 = new Production();
        //Aa
        p1.addSymbol(A);
        p1.addSymbol(a);
        //b
        Production p2 = new Production();
        p2.addSymbol(b);
        //产生式头部非终结符添加产生式体
        A.addProduction(p1);
        A.addProduction(p2);

        //判断左递归
        System.out.println("原文法G[A]:");
        System.out.println(A);
        NonTerminalSymbol nonAA = Problem1.leftRecursion(A);
        System.out.println(A);
        System.out.println(nonAA);


    }

    //左公因子测试
    @Test
    public void testLeftommonFactor() {
        //A->ab, A->ac
        NonTerminalSymbol A = new NonTerminalSymbol("A",SymbolType.NONTERMINAL);
        TerminalSymbol a = new TerminalSymbol("a",SymbolType.TERMINAL);
        TerminalSymbol b = new TerminalSymbol("b",SymbolType.TERMINAL);
        TerminalSymbol c = new TerminalSymbol("c",SymbolType.TERMINAL);
        Production p1 = new Production();
        p1.addSymbol(a);
        p1.addSymbol(b);
        Production p2 = new Production();
        p2.addSymbol(a);
        p2.addSymbol(c);
        A.addProduction(p1);
        A.addProduction(p2);
        System.out.println("原文法G[A]:");
        System.out.println(A);
        ArrayList<NonTerminalSymbol> nonAA = Problem1.leftCommonFactor(A);
        System.out.println(A);
        System.out.println(nonAA);


    }

    //产生式的FOIRST--先判断产生式第一个文法符：是终结符则直接返回FIRST，
    // 为非终结符，则判断非终结符中的每一个产生式的第一个文法符，为S则判断下一个，否则FIRST加入s结束
    @Test
    public void TestFirstOfProduction() {
        // 非终结符E,E',T,T',F
        NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
        NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonTT = new NonTerminalSymbol("T'", SymbolType.NONTERMINAL);
        NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
        // 终结符+,*,(,),id,ε
        TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
        TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
        TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
        TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
        TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        // 产生式E->TE'
        Production p1 = new Production();
        p1.addSymbol(T);
        p1.addSymbol(nonEE);
        E.addProduction(p1);
        // 产生式E'->+TE'
        Production p2 = new Production();
        p2.addSymbol(plus);
        p2.addSymbol(T);
        p2.addSymbol(nonEE);
        nonEE.addProduction(p2);
        // 产生式E'->ε
        Production p3 = new Production();
        p3.addSymbol(epsilon);
        nonEE.addProduction(p3);
        // 产生式T->FT'
        Production p4 = new Production();
        p4.addSymbol(F);
        p4.addSymbol(nonTT);
        T.addProduction(p4);
        // 产生式T'->*FT'
        Production p5 = new Production();
        p5.addSymbol(multi);
        p5.addSymbol(F);
        p5.addSymbol(nonTT);
        nonTT.addProduction(p5);
        // 产生式T'->ε
        Production p6 = new Production();
        p6.addSymbol(epsilon);
        nonTT.addProduction(p6);
        // 产生式F->(E)
        Production p7 = new Production();
        p7.addSymbol(left);
        p7.addSymbol(E);
        p7.addSymbol(right);
        F.addProduction(p7);
        // 产生式F->id
        Production p8 = new Production();
        p8.addSymbol(id);
        F.addProduction(p8);

        System.out.println("------------------------------------------------------------------------------");
        System.out.println("产生式的FIRST集：");
        Set<TerminalSymbol> firstP1 = Problem1.getFirstSet(p1);
        Set<TerminalSymbol> firstP2 = Problem1.getFirstSet(p2);
        Set<TerminalSymbol> firstP3 = Problem1.getFirstSet(p3);
        Set<TerminalSymbol> firstP4 = Problem1.getFirstSet(p4);
        Set<TerminalSymbol> firstP5 = Problem1.getFirstSet(p5);
        Set<TerminalSymbol> firstP6 = Problem1.getFirstSet(p6);
        Set<TerminalSymbol> firstP7 = Problem1.getFirstSet(p7);
        Set<TerminalSymbol> firstP8 = Problem1.getFirstSet(p8);
        printFirst(p1,firstP1);
        printFirst(p2,firstP2);
        printFirst(p3,firstP3);
        printFirst(p4,firstP4);
        printFirst(p5,firstP5);
        printFirst(p6,firstP6);
        printFirst(p7,firstP7);
        printFirst(p8,firstP8);
    }

    public void printFirst(Production production,Set<TerminalSymbol> first) {
        System.out.print(production + "  FIRST：{");
        for (TerminalSymbol symbol: first) {
            System.out.print(symbol.getName()+ ", ");
        }
        System.out.print("}");
        System.out.println("\n");
    }

    //非终结符的FIRST集
    @Test
    public void TestFirstOfNonSymbol() {
        //--------------定义符号
        // 非终结符E,E',T,T',F
        NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
        NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonTT = new NonTerminalSymbol("T'", SymbolType.NONTERMINAL);
        NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
        // 终结符+,*,(,),id,ε
        TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
        TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
        TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
        TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
        TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        //-----------构建产生式
        // 产生式E->TE'
        Production p1 = new Production();
        p1.addSymbol(T);
        p1.addSymbol(nonEE);
        E.addProduction(p1);
        // 产生式E'->+TE'
        Production p2 = new Production();
        p2.addSymbol(plus);
        p2.addSymbol(T);
        p2.addSymbol(nonEE);
        nonEE.addProduction(p2);
        // 产生式E'->ε
        Production p3 = new Production();
        p3.addSymbol(epsilon);
        nonEE.addProduction(p3);
        // 产生式T->FT'
        Production p4 = new Production();
        p4.addSymbol(F);
        p4.addSymbol(nonTT);
        T.addProduction(p4);
        // 产生式T'->*FT'
        Production p5 = new Production();
        p5.addSymbol(multi);
        p5.addSymbol(F);
        p5.addSymbol(nonTT);
        nonTT.addProduction(p5);
        // 产生式T'->ε
        Production p6 = new Production();
        p6.addSymbol(epsilon);
        nonTT.addProduction(p6);
        // 产生式F->(E)
        Production p7 = new Production();
        p7.addSymbol(left);
        p7.addSymbol(E);
        p7.addSymbol(right);
        F.addProduction(p7);
        // 产生式F->id
        Production p8 = new Production();
        p8.addSymbol(id);
        F.addProduction(p8);

        Set<TerminalSymbol> firstE = Problem1.firstOfSymbol(E);
        Set<TerminalSymbol> firstnonEE = Problem1.firstOfSymbol(nonEE);
        Set<TerminalSymbol> firstT = Problem1.firstOfSymbol(T);
        Set<TerminalSymbol> firstnonTT = Problem1.firstOfSymbol(nonTT);
        Set<TerminalSymbol> firstF = Problem1.firstOfSymbol(F);
        System.out.println("原文法G[E]:");
        System.out.println(E);
        System.out.println(nonEE);
        System.out.println(T);
        System.out.println(nonTT);
        System.out.println(F);
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("非终结符的的FIRST集：");
        printFirstOfSymbol(E, firstE);
        printFirstOfSymbol(nonEE, firstnonEE);
        printFirstOfSymbol(T, firstT);
        printFirstOfSymbol(nonTT, firstnonTT);
        printFirstOfSymbol(F, firstF);
    }

    public void printFirstOfSymbol(NonTerminalSymbol nonTerminalSymbol, Set<TerminalSymbol> first) {
        System.out.print(nonTerminalSymbol.getName() + " FIRST: {");
        for (TerminalSymbol symbol: first) {
            System.out.print(symbol.getName()+ ", ");
        }
        System.out.print("}\n");
    }


    //测试非终结符的FOLLOW集
    @Test
    public void TestFollowOfSymbol() {
        // 非终结符E,E',T,T',F
        NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
        NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonTT = new NonTerminalSymbol("T'", SymbolType.NONTERMINAL);
        NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
        // 终结符+,*,(,),id,ε
        TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
        TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
        TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
        TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
        TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        // 产生式E->TE'
        Production p1 = new Production();
        p1.addSymbol(T);
        p1.addSymbol(nonEE);
        E.addProduction(p1);
        // 产生式E'->+TE'
        Production p2 = new Production();
        p2.addSymbol(plus);
        p2.addSymbol(T);
        p2.addSymbol(nonEE);
        nonEE.addProduction(p2);
        // 产生式E'->ε
        Production p3 = new Production();
        p3.addSymbol(epsilon);
        nonEE.addProduction(p3);
        // 产生式T->FT'
        Production p4 = new Production();
        p4.addSymbol(F);
        p4.addSymbol(nonTT);
        T.addProduction(p4);
        // 产生式T'->*FT'
        Production p5 = new Production();
        p5.addSymbol(multi);
        p5.addSymbol(F);
        p5.addSymbol(nonTT);
        nonTT.addProduction(p5);
        // 产生式T'->ε
        Production p6 = new Production();
        p6.addSymbol(epsilon);
        nonTT.addProduction(p6);
        // 产生式F->(E)
        Production p7 = new Production();
        p7.addSymbol(left);
        p7.addSymbol(E);
        p7.addSymbol(right);
        F.addProduction(p7);
        // 产生式F->id
        Production p8 = new Production();
        p8.addSymbol(id);
        F.addProduction(p8);


        System.out.println("------------------------------------------------------------------------------");
        System.out.println("非终结符的的FOLLOW集：");
        // 求FIRST集合
        Set<TerminalSymbol> firstE = Problem1.firstOfSymbol(E);
        Set<TerminalSymbol> firstnonEE = Problem1.firstOfSymbol(nonEE);
        Set<TerminalSymbol> firstT = Problem1.firstOfSymbol(T);
        Set<TerminalSymbol> firstnonTT = Problem1.firstOfSymbol(nonTT);
        Set<TerminalSymbol> firstF = Problem1.firstOfSymbol(F);
        // 求FOLLOW集合
        // 结束符#
        TerminalSymbol end = new TerminalSymbol("$", SymbolType.TERMINAL);
        E.addFollow(end);
        // 求FOLLOW集合
        Problem1.followOfSymbol(E);
        Problem1.followOfSymbol(nonEE);
        Problem1.followOfSymbol(T);
        Problem1.followOfSymbol(nonTT);
        Problem1.followOfSymbol(F);
//        System.out.println(E);
//        System.out.println(nonEE);
//        System.out.println(T);
//        System.out.println(nonTT);
//        System.out.println(F);

        // 添加依赖
//        System.out.println("添加依赖非终结符后的FOLLOW集：");
        E.addFollowDependent();
        nonEE.addFollowDependent();
        T.addFollowDependent();
        nonTT.addFollowDependent();
        F.addFollowDependent();
        System.out.println(E);
        System.out.println(nonEE);
        System.out.println(T);
        System.out.println(nonTT);
        System.out.println(F);
    }

    @Test
    public void TestIsLL1() {
        // 非终结符E,E',T,T',F
        NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
        NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonTT = new NonTerminalSymbol("T'", SymbolType.NONTERMINAL);
        NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
        // 终结符+,*,(,),id,ε
        TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
        TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
        TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
        TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
        TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        // 产生式E->TE'
        Production p1 = new Production();
        p1.addSymbol(T);
        p1.addSymbol(nonEE);
        E.addProduction(p1);
        // 产生式E'->+TE'
        Production p2 = new Production();
        p2.addSymbol(plus);
        p2.addSymbol(T);
        p2.addSymbol(nonEE);
        nonEE.addProduction(p2);
        // 产生式E'->ε
        Production p3 = new Production();
        p3.addSymbol(epsilon);
        nonEE.addProduction(p3);
        // 产生式T->FT'
        Production p4 = new Production();
        p4.addSymbol(F);
        p4.addSymbol(nonTT);
        T.addProduction(p4);
        // 产生式T'->*FT'
        Production p5 = new Production();
        p5.addSymbol(multi);
        p5.addSymbol(F);
        p5.addSymbol(nonTT);
        nonTT.addProduction(p5);
        // 产生式T'->ε
        Production p6 = new Production();
        p6.addSymbol(epsilon);
        nonTT.addProduction(p6);
        // 产生式F->(E)
        Production p7 = new Production();
        p7.addSymbol(left);
        p7.addSymbol(E);
        p7.addSymbol(right);
        F.addProduction(p7);
        // 产生式F->id
        Production p8 = new Production();
        p8.addSymbol(id);
        F.addProduction(p8);

        // 求FIRST集合
        Set<TerminalSymbol> firstE = Problem1.firstOfSymbol(E);
        Set<TerminalSymbol> firstnonEE = Problem1.firstOfSymbol(nonEE);
        Set<TerminalSymbol> firstT = Problem1.firstOfSymbol(T);
        Set<TerminalSymbol> firstnonTT = Problem1.firstOfSymbol(nonTT);
        Set<TerminalSymbol> firstF = Problem1.firstOfSymbol(F);
        // 求FOLLOW集合
        // 结束符$
        TerminalSymbol end = new TerminalSymbol("$", SymbolType.TERMINAL);
        E.addFollow(end);
        // 求FOLLOW集合
        Problem1.followOfSymbol(E);
        Problem1.followOfSymbol(nonEE);
        Problem1.followOfSymbol(T);
        Problem1.followOfSymbol(nonTT);
        Problem1.followOfSymbol(F);

        // 添加依赖
        E.addFollowDependent();
        nonEE.addFollowDependent();
        T.addFollowDependent();
        nonTT.addFollowDependent();
        F.addFollowDependent();

        System.out.println("LL1分析表信息");
        System.out.println("E:" + Problem1.isLL1(E));
        System.out.println("nonEE:" + Problem1.isLL1(nonEE));
        System.out.println("T:" + Problem1.isLL1(T));
        System.out.println("nonTT:" + Problem1.isLL1(nonTT));
        System.out.println("F:" + Problem1.isLL1(F));
    }

    @Test
    public void TestParseTable() {
        // 非终结符E,E',T,T',F
        NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
        NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
        NonTerminalSymbol nonTT = new NonTerminalSymbol("T'", SymbolType.NONTERMINAL);
        NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
        // 终结符+,*,(,),id,ε
        TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
        TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
        TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
        TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
        TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        // 产生式E->TE'
        Production p1 = new Production();
        p1.addSymbol(T);
        p1.addSymbol(nonEE);
        E.addProduction(p1);
        // 产生式E'->+TE'
        Production p2 = new Production();
        p2.addSymbol(plus);
        p2.addSymbol(T);
        p2.addSymbol(nonEE);
        nonEE.addProduction(p2);
        // 产生式E'->ε
        Production p3 = new Production();
        p3.addSymbol(epsilon);
        nonEE.addProduction(p3);
        // 产生式T->FT'
        Production p4 = new Production();
        p4.addSymbol(F);
        p4.addSymbol(nonTT);
        T.addProduction(p4);
        // 产生式T'->*FT'
        Production p5 = new Production();
        p5.addSymbol(multi);
        p5.addSymbol(F);
        p5.addSymbol(nonTT);
        nonTT.addProduction(p5);
        // 产生式T'->ε
        Production p6 = new Production();
        p6.addSymbol(epsilon);
        nonTT.addProduction(p6);
        // 产生式F->(E)
        Production p7 = new Production();
        p7.addSymbol(left);
        p7.addSymbol(E);
        p7.addSymbol(right);
        F.addProduction(p7);
        // 产生式F->id
        Production p8 = new Production();
        p8.addSymbol(id);
        F.addProduction(p8);

        // 求FIRST集合
        Set<TerminalSymbol> firstE = Problem1.firstOfSymbol(E);
        Set<TerminalSymbol> firstnonEE = Problem1.firstOfSymbol(nonEE);
        Set<TerminalSymbol> firstT = Problem1.firstOfSymbol(T);
        Set<TerminalSymbol> firstnonTT = Problem1.firstOfSymbol(nonTT);
        Set<TerminalSymbol> firstF = Problem1.firstOfSymbol(F);
        // 求FOLLOW集合
        // 结束符$
        TerminalSymbol end = new TerminalSymbol("$", SymbolType.TERMINAL);
        E.addFollow(end);
        // 求FOLLOW集合
        Problem1.followOfSymbol(E);
        Problem1.followOfSymbol(nonEE);
        Problem1.followOfSymbol(T);
        Problem1.followOfSymbol(nonTT);
        Problem1.followOfSymbol(F);

        // 添加依赖
        E.addFollowDependent();
        nonEE.addFollowDependent();
        T.addFollowDependent();
        nonTT.addFollowDependent();
        F.addFollowDependent();

        ArrayList<NonTerminalSymbol> pNonTerminalSymbolTable = new ArrayList<>();
        pNonTerminalSymbolTable.add(E);
        pNonTerminalSymbolTable.add(nonEE);
        pNonTerminalSymbolTable.add(T);
        pNonTerminalSymbolTable.add(nonTT);
        pNonTerminalSymbolTable.add(F);

        System.out.println(Problem1.parseTable(pNonTerminalSymbolTable));
    }




}

