package symbol;
import java.util.*;
/**
 * 产生式
 */
public class Production {
    //产生式序号，起标识作用
    private int productionId;
    //产生式体中包含的文法符个数
    private int bodySize;
    //产生式体中包含的文法符
    private ArrayList <GrammarSymbol>  pBodySymbolTable;
    //产生式的FIRST函数值
    private Set <TerminalSymbol>  pFirstSet;

    public static int idNumber = 0;//产生式序号

    public Production() {
        productionId = idNumber++;
        bodySize = 0;
        pBodySymbolTable = new ArrayList<>();
        pFirstSet = new HashSet<>();
    }

    public int getProductionId() {
        return productionId;
    }

    public void setProductionId(int productionId) {
        this.productionId = productionId;
    }

    public void setBodySize(int bodySize) {
        this.bodySize = bodySize;
    }

    public int getBodySize() {
        return bodySize;
    }

    public ArrayList<GrammarSymbol> getpBodySymbolTable() {
        return pBodySymbolTable;
    }

    public void setpBodySymbolTable(ArrayList<GrammarSymbol> pBodySymbolTable) {
        this.pBodySymbolTable = pBodySymbolTable;
    }

    public Set<TerminalSymbol> getpFirstSet() {
        return pFirstSet;
    }

    public void setpFirstSet(Set<TerminalSymbol> pFirstSet) {
        this.pFirstSet = pFirstSet;
    }
//    添加产生式
    public void addSymbol(GrammarSymbol g) {
        pBodySymbolTable.add(g);
        bodySize++;
    }
    public void removeSymbol(GrammarSymbol g) {
        pBodySymbolTable.remove(g);
        bodySize--;
    }
    //判断是否有ε产生式
    public Boolean isEpsilon() {
        if(bodySize == 1 && pBodySymbolTable.get(0).getName().equals("ε")) {
            return true;
        }else return false;
    }
    @Override
    public String toString() {
        String s = new String();
        for (GrammarSymbol symbol: pBodySymbolTable) {
            s += symbol.getName();
        }
        return "{" +
                " 产生式序号: " + productionId +
                ", 文法符个数: " + bodySize +
                ", 产生式体文法符: " + s  +
//              ", pFirstSet=" + pFirstSet +
                '}' ;
    }
}
/**
 * 注：产生式体中，文法符之间都是连接运算，因此也就可省去连接运算符。
 * 把产生式中的某个文法符放入pBodySymbolTable前，要强制类型转换，变成GrammarSymbol *类型。这种类型转换没有问题。
 * 因为TerminalSymbol和NonTerminalSymbol都是GrammarSymbol的子类。在使用pBodySymbolTable中元素时，检查其成员变量type的值，
 * 如果为NONTERMINAL，则将其强制类型转换，变回NonTerminalSymbol *类型。
 * 如果为TERMINAL，则将其强制类型转换，变回TerminalSymbol *类型。
 */
