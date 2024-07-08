import org.junit.Test;
import java.util.ArrayList;
import java.util.Set;
import symbol.DFA;
import symbol.ItemCategoy;
import symbol.ItemSet;
import symbol.LR0Item;
import symbol.NonTerminalSymbol;
import symbol.Production;
import symbol.ProductionInfo;
import symbol.SymbolType;
import symbol.TerminalSymbol;


public class Test2 {
       //求闭包
        @Test
        public void TestGetClosure() {
            // 非终结符E,T,F，E'
            NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
            NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
            NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
            NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
            // 终结符+,*,(,),id,ε
            TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
            TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
            TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
            TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
            TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
            TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
            // 产生式E'->E
            Production p0 = new Production();
            p0.addSymbol(E);
            nonEE.addProduction(p0);
            // 产生式E->E+T
            Production p1 = new Production();
            p1.addSymbol(E);
            p1.addSymbol(plus);
            p1.addSymbol(T);
            E.addProduction(p1);
            // 产生式E->T
            Production p2 = new Production();
            p2.addSymbol(T);
            E.addProduction(p2);
            // 产生式T->T*F
            Production p4 = new Production();
            p4.addSymbol(T);
            p4.addSymbol(multi);
            p4.addSymbol(F);
            T.addProduction(p4);
            // 产生式T->F
            Production p5 = new Production();
            p5.addSymbol(F);
            T.addProduction(p5);
            // 产生式F->(E)
            Production p6 = new Production();
            p6.addSymbol(left);
            p6.addSymbol(E);
            p6.addSymbol(right);
            F.addProduction(p6);
            // 产生式F->id
            Production p7 = new Production();
            p7.addSymbol(id);
            F.addProduction(p7);

            // I0
            ItemSet item0 = new ItemSet();
            LR0Item lr = new LR0Item(nonEE, p0, 0, ItemCategoy.CORE);
            item0.addItem(lr);
            Problem2.getClosure(item0);
            System.out.println(item0);
        }

        //变迁边
        @Test
        public void TestExhaustTransition() {
            // 非终结符E,,T,F，E'
            NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
            NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
            NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
            NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
            // 终结符+,*,(,),id,ε
            TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
            TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
            TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
            TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
            TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
            TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
            // 产生式E'->E
            Production p0 = new Production();
            p0.addSymbol(E);
            nonEE.addProduction(p0);
            // 产生式E->E+T
            Production p1 = new Production();
            p1.addSymbol(E);
            p1.addSymbol(plus);
            p1.addSymbol(T);
            E.addProduction(p1);
            // 产生式E->T
            Production p2 = new Production();
            p2.addSymbol(T);
            E.addProduction(p2);
            // 产生式T->T*F
            Production p4 = new Production();
            p4.addSymbol(T);
            p4.addSymbol(multi);
            p4.addSymbol(F);
            T.addProduction(p4);
            // 产生式T->F
            Production p5 = new Production();
            p5.addSymbol(F);
            T.addProduction(p5);
            // 产生式F->(E)
            Production p6 = new Production();
            p6.addSymbol(left);
            p6.addSymbol(E);
            p6.addSymbol(right);
            F.addProduction(p6);
            // 产生式F->id
            Production p7 = new Production();
            p7.addSymbol(id);
            F.addProduction(p7);

            // I0
            ItemSet item0 = new ItemSet();
            LR0Item lr = new LR0Item(nonEE, p0, 0, ItemCategoy.CORE);
            item0.addItem(lr);
            Problem2.getClosure(item0);
            Problem2.addItemSet(item0);
            Problem2.exhaustTransition(item0);
            System.out.println(Problem2.getAllItemSet());
        }

        //3)文法的LR(0)型DFA求解
        @Test
        public void TestGetDFA() {
            // 非终结符E,,T,F，E'
            NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
            NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
            NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
            NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
            // 终结符+,*,(,),id,ε
            TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
            TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
            TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
            TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
            TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
            TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
            // 产生式E'->E
            Production p0 = new Production();
            p0.addSymbol(E);
            nonEE.addProduction(p0);
            // 产生式E->E+T
            Production p1 = new Production();
            p1.addSymbol(E);
            p1.addSymbol(plus);
            p1.addSymbol(T);
            E.addProduction(p1);
            // 产生式E->T
            Production p2 = new Production();
            p2.addSymbol(T);
            E.addProduction(p2);
            // 产生式T->T*F
            Production p4 = new Production();
            p4.addSymbol(T);
            p4.addSymbol(multi);
            p4.addSymbol(F);
            T.addProduction(p4);
            // 产生式T->F
            Production p5 = new Production();
            p5.addSymbol(F);
            T.addProduction(p5);
            // 产生式F->(E)
            Production p6 = new Production();
            p6.addSymbol(left);
            p6.addSymbol(E);
            p6.addSymbol(right);
            F.addProduction(p6);
            // 产生式F->id
            Production p7 = new Production();
            p7.addSymbol(id);
            F.addProduction(p7);

            // I0
            ItemSet item0 = new ItemSet();
            LR0Item lr = new LR0Item(nonEE, p0, 0, ItemCategoy.CORE);
            item0.addItem(lr);
            Problem2.getClosure(item0);
            Problem2.addItemSet(item0);

            DFA dfa = Problem2.getDFA(item0);
            System.out.println("所有状态： ");
            System.out.println(Problem2.getAllItemSet());
            System.out.println("----------------------------------------------------------------");
            System.out.println(dfa);
        }

        @Test
        public void TestIsSLR1() {
            // 非终结符Z', Z
            NonTerminalSymbol nonzz = new NonTerminalSymbol("Z'", SymbolType.NONTERMINAL);
            NonTerminalSymbol Z = new NonTerminalSymbol("Z", SymbolType.NONTERMINAL);
            // 终结符a,c,d
            TerminalSymbol a = new TerminalSymbol("a", SymbolType.TERMINAL);
            TerminalSymbol c = new TerminalSymbol("c", SymbolType.TERMINAL);
            TerminalSymbol d = new TerminalSymbol("d", SymbolType.TERMINAL);

            // 产生式Z'->Z
            Production p0 = new Production();
            p0.addSymbol(Z);
            nonzz.addProduction(p0);
            // 产生式Z->d
            Production p1 = new Production();
            p1.addSymbol(d);
            Z.addProduction(p1);
            // 产生式Z->cZa
            Production p2 = new Production();
            p2.addSymbol(c);
            p2.addSymbol(Z);
            p2.addSymbol(a);
            Z.addProduction(p2);
            // 产生式Z->Za
            Production p3 = new Production();
            p3.addSymbol(Z);
            p3.addSymbol(a);
            Z.addProduction(p3);

            // 求FIRST集合
            Set<TerminalSymbol> firstnonzz = Problem1.firstOfSymbol(nonzz);
            Set<TerminalSymbol> firstZ = Problem1.firstOfSymbol(Z);
            // 求FOLLOW集合
            // 结束符$
            TerminalSymbol end = new TerminalSymbol("$", SymbolType.TERMINAL);
            nonzz.addFollow(end);
            // 求FOLLOW集合
            Problem1.followOfSymbol(nonzz);
            Problem1.followOfSymbol(Z);
            // 添加依赖
            nonzz.addFollowDependent();
            Z.addFollowDependent();

            // I0
            ItemSet item0 = new ItemSet();
            LR0Item lr = new LR0Item(nonzz, p0, 0, ItemCategoy.CORE);
            item0.addItem(lr);
            Problem2.getClosure(item0);
            Problem2.addItemSet(item0);
            DFA dfa = Problem2.getDFA(item0);
            //DFA
            System.out.println("所有状态： ");
            System.out.println(Problem2.getAllItemSet());
            System.out.println("----------------------------------------------------------------");
            System.out.println(dfa);
            boolean flag = Problem2.isSLR1();
            if(flag == false) {
                System.out.println("不是SLR(1)文法");
            } else  {
                System.out.println("是SLR(1)文法！");
            }
        }

        //LR语法分析表的填写
        @Test
        public void TestGetCell() {
            // 非终结符E,T,F，E'
            NonTerminalSymbol nonEE = new NonTerminalSymbol("E'", SymbolType.NONTERMINAL);
            NonTerminalSymbol E = new NonTerminalSymbol("E", SymbolType.NONTERMINAL);
            NonTerminalSymbol T = new NonTerminalSymbol("T", SymbolType.NONTERMINAL);
            NonTerminalSymbol F = new NonTerminalSymbol("F", SymbolType.NONTERMINAL);
            // 终结符+,*,(,),id,ε
            TerminalSymbol plus = new TerminalSymbol("+", SymbolType.TERMINAL);
            TerminalSymbol multi = new TerminalSymbol("*", SymbolType.TERMINAL);
            TerminalSymbol left = new TerminalSymbol("(", SymbolType.TERMINAL);
            TerminalSymbol right = new TerminalSymbol(")", SymbolType.TERMINAL);
            TerminalSymbol id = new TerminalSymbol("id", SymbolType.TERMINAL);
            TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
            // 产生式E'->E
            Production p0 = new Production();
            p0.addSymbol(E);
            nonEE.addProduction(p0);
            // 产生式E->E+T
            Production p1 = new Production();
            p1.addSymbol(E);
            p1.addSymbol(plus);
            p1.addSymbol(T);
            E.addProduction(p1);
            // 产生式E->T
            Production p2 = new Production();
            p2.addSymbol(T);
            E.addProduction(p2);
            // 产生式T->T*F
            Production p4 = new Production();
            p4.addSymbol(T);
            p4.addSymbol(multi);
            p4.addSymbol(F);
            T.addProduction(p4);
            // 产生式T->F
            Production p5 = new Production();
            p5.addSymbol(F);
            T.addProduction(p5);
            // 产生式F->(E)
            Production p6 = new Production();
            p6.addSymbol(left);
            p6.addSymbol(E);
            p6.addSymbol(right);
            F.addProduction(p6);
            // 产生式F->id
            Production p7 = new Production();
            p7.addSymbol(id);
            F.addProduction(p7);

            //所有非终结符
            ArrayList<NonTerminalSymbol> nonTerminalSymbols = new ArrayList<>();
            nonTerminalSymbols.add(nonEE);
            nonTerminalSymbols.add(E);
            nonTerminalSymbols.add(T);
            nonTerminalSymbols.add(F);
            // 求FIRST集合
            for (NonTerminalSymbol symbol: nonTerminalSymbols) {
                Problem1.firstOfSymbol(symbol);
            }
            // 求FOLLOW集合
            // 结束符$
            TerminalSymbol end = new TerminalSymbol("$", SymbolType.TERMINAL);
            E.addFollow(end);
            // 求FOLLOW集合
            for (NonTerminalSymbol symbol: nonTerminalSymbols) {
                Problem1.followOfSymbol(symbol);
            }
            // 添加依赖
            for (NonTerminalSymbol symbol: nonTerminalSymbols) {
                symbol.addFollowDependent();
                System.out.println(symbol);
            }

            // I0
            ItemSet item0 = new ItemSet();
            LR0Item lr = new LR0Item(nonEE, p0, 0, ItemCategoy.CORE);
            item0.addItem(lr);
            Problem2.getClosure(item0);
            Problem2.addItemSet(item0);
            // DFA
            DFA dfa = Problem2.getDFA(item0);
            Problem2.isSLR1();

            // 产生式概述表
            ArrayList<ProductionInfo> productionInfoTable = new ArrayList<>();
            for (NonTerminalSymbol symbol: nonTerminalSymbols) {
                productionInfoTable.addAll(Problem2.createInfo(symbol));
            }
            productionInfoTable.remove(0);

            // 语法分析表
            Problem2.getCell(dfa);
            System.out.println("--------------------------------------------------------------");
            System.out.println(productionInfoTable);
            System.out.println("--------------------------------------------------------------");
            System.out.println(Problem2.getpActionCellTable());
            System.out.println("--------------------------------------------------------------");
            System.out.println(Problem2.getpGotoCellTable());
        }

}
