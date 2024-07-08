import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import symbol.Cell;
import symbol.GrammarSymbol;
import symbol.NonTerminalSymbol;
import symbol.Production;
import symbol.SymbolType;
import symbol.TerminalSymbol;

public class Problem1 {
    //左递归
    public static NonTerminalSymbol leftRecursion(NonTerminalSymbol nonTerminalSymbol) {
        // A->Aa,A->b  分成含左递归和不含左递归的部分
        // 获取产生式左部的非终结符A
        String nonA = nonTerminalSymbol.getName();
        // 保存A->Aα的链表（含左递归的部分）
        ArrayList<Production> arr1 = new ArrayList<>();
        // 保存A->b的链表（不含左递归的部分）
        ArrayList<Production> arr2 = new ArrayList<>();
        // 遍历A的所有产生式
        for (Production production: nonTerminalSymbol.getpProductionTable()) {
            // 产生式右部的头部文法符名字为A，则将其放入left;否则放入constant
            String name = production.getpBodySymbolTable().get(0).getName();//A
            //递归部分 Aa
            if (name.equals(nonA)) {
                arr1.add(production);
            }//不含递归部分 b
            else {
                arr2.add(production);
            }
        }
        // left为空，说明不含有左递归
        if (arr1.isEmpty()) {
            System.out.println("文法不存在左递归");
            return null;
        }
        System.out.println("文法存在左递归! 消除左递归后的文法G`[A]:");
        // 新建一个非终结符A‘
        NonTerminalSymbol nonAA = new NonTerminalSymbol(nonTerminalSymbol.getName()+'\'', SymbolType.NONTERMINAL);
        // 将A->b变化为A->bA'
        for (Production production: arr2) {
            production.addSymbol(nonAA);
        }
        // 将A->Aα变化为A'->αA' arr1=[A,a]
        for (Production production: arr1) {
            // 非终结符A移除产生式
            nonTerminalSymbol.removeProduction(production);
            // 产生式体中去除头部A
            production.removeSymbol(nonTerminalSymbol);
            // 产生式尾部加上A’ =>aA`
            production.addSymbol(nonAA);
            // 添加到非终结符A‘的产生式集合中
            nonAA.addProduction(production);
        }
        // 添加A`->ε
        Production production = new Production();
        GrammarSymbol epsilon = new GrammarSymbol("ε", SymbolType.NULL);
        production.addSymbol(epsilon);
        // 加入非终结符A’的产生式集合中
        nonAA.addProduction(production);

        return nonAA;
    }

    //左公因子
    public static ArrayList<NonTerminalSymbol> leftCommonFactor(NonTerminalSymbol nonTerminalSymbol) {
        Map<GrammarSymbol,ArrayList<Production>> map = new HashMap<>();
        Boolean flag = false;
        //A->ab, A->ac
        //判断产生式体是否存在相同的终结符（判断个数）
        for(Production production:nonTerminalSymbol.getpProductionTable()) {
            GrammarSymbol head = production.getpBodySymbolTable().get(0);//右边第一个文法符a
            if(map.get(head) == null) {
                ArrayList<Production> p =new ArrayList<>();
                p.add(production);  //ab
                map.put(head,p);    //a---ab
            } else {
                //多次出现 该文法符为公共因子
                map.get(head).add(production);  //a---ac
                flag = true;
            }
        }
        if(flag == false) {
            System.out.println("文法不存在左公因子");
            return null;
        }

        System.out.println("文法存在左公因子! 提取公因子后的文法G`[A]:");
        //A->aA`  A`->b|c
        ArrayList<NonTerminalSymbol> list = new ArrayList<>();
        for(GrammarSymbol grammarSymbol: map.keySet()) {
            //公因子
            if(map.get(grammarSymbol).size() > 1) {
                // 新建一个非终结符A‘
                NonTerminalSymbol nonAA = new NonTerminalSymbol(nonTerminalSymbol.getName()+"\'",SymbolType.NONTERMINAL);
                Production p1 = new Production();
                p1.addSymbol(grammarSymbol);
                p1.addSymbol(nonAA);
                nonTerminalSymbol.addProduction(p1);//A->aA`
                //遍历a在的所有产生式 {ab},{ac}
                for(Production production:map.get(grammarSymbol)) {
                    production.removeSymbol(grammarSymbol);//删掉a,----{b}/{c}
                    // 删除A->……的产生式左部
                    nonTerminalSymbol.removeProduction(production);
                    // 产生式左部为A’->……
                    nonAA.addProduction(production);
                }
                // 添加新的非终结符
                list.add(nonAA);
            }
        }
        return list;
    }

    /*
        求产生式非终结符的FIRST集
        在使用pBodySymbolTable中元素时，检查其成员变量type的值
        如果为NONTERMINAL，则将其强制类型转换，变回NonTerminalSymbol 类型。
        如果为TERMINAL，则将其强制类型转换，变回TerminalSymbol 类型。
     */

    //产生式的FIRST集
    public static Set<TerminalSymbol> getFirstSet(Production production) {
        //1.是否存在 连续的ε
        boolean flag = true;
        int i = 0;
        Set<TerminalSymbol> FirstList = new HashSet<>();
        ArrayList<GrammarSymbol> grammarSymbols = production.getpBodySymbolTable();
        //新建 ε
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        // 当前面文法符FIRST都包含ε时：从 Y1至 Yj，0<j<n，全为非终结符，且都含虚产生式，那么
        //FIRST=FIRST（Yj)- ε。( ε在Yj中则FIRST加入 ε）
        while(flag && i < production.getBodySize()) {
            // 获取产生式中当前文法符的FIRST
            Set<TerminalSymbol> first = firstOfSymbol(grammarSymbols.get(i));
            // 判断当前文法符FIRST是否包含ε
            if(first.contains(epsilon)) {
                // 跳转到下一个文法符，FIRST去掉ε
                i++;
                first.remove(epsilon);
            } else {
                flag = false;
            }
            // 把当前文法符的FIRST加入结果中
            FirstList.addAll(first);
        }
        // 如果最终能推导出ε，则FIRST集合中包含ε
        if(flag && i == production.getBodySize()) {
            FirstList.add(epsilon);
        }
        production.setpFirstSet(FirstList);
        return FirstList;
    }
    public static Set<TerminalSymbol> firstOfSymbol(GrammarSymbol grammarSymbol) {
        Set<TerminalSymbol> list = new HashSet<>();//first集合
        // 当前文法符为终结符或ε，则直接返回其本身
        if (grammarSymbol.getType() == SymbolType.TERMINAL || grammarSymbol.getType() == SymbolType.NULL) {
            list.add((TerminalSymbol) grammarSymbol);//转换类型！！
            return list;
        }
        // 当前文法符为非终结符，遍历每个产生式
        for (Production production: ((NonTerminalSymbol)grammarSymbol).getpProductionTable()) {
            if (production.getpBodySymbolTable().get(0) == grammarSymbol) {
                continue;
            }
            // 对每个产生式求其FIRST集
            for (TerminalSymbol terminalSymbol: getFirstSet(production)) {
                // 将未加入的终结符加入FIRST集合
                if (!list.contains(terminalSymbol)) {
                    list.add(terminalSymbol);
                }
            }
        }
        // 设置FIRST非终结符的FIRST集合
        ((NonTerminalSymbol) grammarSymbol).setFirstSet(list);
        return list;
    }

    //非终结符的FOLLOE集
    public static void followOfSymbol(NonTerminalSymbol nonTerminalSymbol) {
        // 新建ε
        TerminalSymbol epsilon = new TerminalSymbol("ε", SymbolType.NULL);
        // 遍历nonTerminalSymbol产生式
        for (Production production: nonTerminalSymbol.getpProductionTable()) {
            // 产生式文法符个数
            int size = production.getBodySize();
            // 获取最后一个文法符
            GrammarSymbol last = production.getpBodySymbolTable().get(size - 1);
            // nonTerminalSymbol->ε，表示为空，跳过该产生式
            if (last.getName().equals("ε")) {
                continue;
            }
            // 最后一个文法符为非终结符，其FOLLOW集合依赖于nonTerminalSymbol的FOLLOW集合
            if (last.getType() == SymbolType.NONTERMINAL) {
                ((NonTerminalSymbol) last).addDependentSetInFollow(nonTerminalSymbol);
            }
            // ε是否持续（从最后一个开始往前找）
            Boolean flag = true;
            // 从倒数第二个开始
            int i = size-2, j = size-1;
            while (i >= 0) {
                GrammarSymbol lasti = production.getpBodySymbolTable().get(i);
                // 当前文法符为非终结符
                if (lasti.getType() == SymbolType.NONTERMINAL) {
                    // 遍历其后FIRST连续包含ε的非终结符
                    for (int k = i+1; k <= j; k ++) {
                        // 若第k个文法符为终结符，将其自身加入FOLLOW集合即可
                        if (production.getpBodySymbolTable().get(k).getType() == SymbolType.TERMINAL) {
                            ((NonTerminalSymbol)lasti).addFollow((TerminalSymbol) production.getpBodySymbolTable().get(k));
                            flag = false;
                        }
                        else {
                            NonTerminalSymbol Yk = (NonTerminalSymbol)production.getpBodySymbolTable().get(k);
                            Set<TerminalSymbol> firstYk = Yk.removeREpsilon();//first(Yk)-ε
                            // 若当前终结符的后续非终结符的FIRST集合不包含ε，说明无法持续到最后，flag置0
                            if (!Yk.containsEpsilon()) {
                                flag = false;
                            }
                            // 将其FIRST集合-{ε}加入非终结符的FOLLOW集合
                            ((NonTerminalSymbol)lasti).addFollowSet(firstYk);
                        }
                    }
                    // 如果flag仍为true，表示当前文法符仍能到达最后，则其FOLLOW集合依赖于symbol
                    if (flag) {
                        ((NonTerminalSymbol)lasti).addDependentSetInFollow(nonTerminalSymbol);
                    }

                    // 如果当前文法符的FIRST集合不包含ε，说明FOLLOW集合无法到达后续，将j修改为当前i
                    if (!((NonTerminalSymbol)lasti).getpFirstSet().contains(epsilon)) {
                        j = i;
                    }

                }
                else {
                    j = i;
                    if (flag) {
                        flag = false;
                    }
                }
                i --;
            }
        }
    }

    public static Boolean isLL1 (NonTerminalSymbol nonTerminalSymbol) {
        Map<TerminalSymbol, Integer> map = new HashMap<>();
        // 判断是否有X->ε,若存在需将FOLLOW保存进map
        if (nonTerminalSymbol.containsEpsilon()) {
            for (TerminalSymbol s: nonTerminalSymbol.getpFollowSet()) {
                map.put(s, -1);
            }
        }
        // 遍历产生式
        for (Production production: nonTerminalSymbol.getpProductionTable()) {
            // 遍历产生式的FIRST集合，若其未出现过，说明无交集，否则有交集（不为LL（1）文法）
            for (TerminalSymbol s: production.getpFirstSet()) {
                if (map.get(s) == null) {
                    map.put(s,production.getProductionId());
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Cell> parseTable(ArrayList<NonTerminalSymbol> pNonTerminalSymbolTable) {
        ArrayList<Cell> pParseTableOfLL = new ArrayList<>();
        // 遍历每个非终结符
        for (NonTerminalSymbol nonTerminalSymbol: pNonTerminalSymbolTable) {
            // 遍历每个产生式
            for (Production production: nonTerminalSymbol.getpProductionTable()) {
                // 若该产生式为X->ε，则选择FOLLOW集合中的终结符填入该产生式
                if (production.isEpsilon()) {
                    for (TerminalSymbol t: nonTerminalSymbol.getpFollowSet()) {
                        Cell cell = new Cell(nonTerminalSymbol, t, production);
                        pParseTableOfLL.add(cell);
                    }
                }
                // 否则选择FIRST集合中的终结符填入该产生式
                else {
                    for (TerminalSymbol t: production.getpFirstSet()) {
                        Cell cell = new Cell(nonTerminalSymbol, t, production);
                        pParseTableOfLL.add(cell);
                    }
                }
            }
        }
        return pParseTableOfLL;
    }
}