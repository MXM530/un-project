package symbol;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class NonTerminalSymbol extends GrammarSymbol{
    //有关非终结符构成的产生式
   private ArrayList <Production>  pProductionTable;
    //产生式的个数
    private int numOfProduction;
    //非终结符的FIRST函数值
    private Set <TerminalSymbol>  pFirstSet;
    //非终结符的FOLLOW函数值
    private Set <TerminalSymbol>  pFollowSet;
    //求非终结符的FOLLOW函数值时存放所依赖的非终结符。
    private Set <NonTerminalSymbol> pDependentSetInFollow;
    public NonTerminalSymbol(String name,SymbolType type) {
        super(name,type);
        pProductionTable = new ArrayList<>();
        numOfProduction = 0;
        pFirstSet = new HashSet<>();
        pFollowSet = new HashSet<>();
        pDependentSetInFollow = new HashSet<>();
    }

    //获取非终结符的所有产生式
    public ArrayList<Production> getpProductionTable() {
        return pProductionTable;
    }
    public void setpProductionTable(ArrayList<Production> pProductionTable) {this.pProductionTable = pProductionTable;}
    public int getNumOfProduction() {return numOfProduction;}

    public void setNumOfProduction(int numOfProduction) {
        this.numOfProduction = numOfProduction;
    }
    public Set<TerminalSymbol> getpFirstSet() {return pFirstSet;}
    public void setFirstSet(Set<TerminalSymbol> pFirstSet) {this.pFirstSet = pFirstSet;}

    public Set<TerminalSymbol> getpFollowSet() {
        return pFollowSet;
    }

    public void setpFollowSet(Set<TerminalSymbol> pFollowSet) {
        this.pFollowSet = pFollowSet;
    }

    public Set<NonTerminalSymbol> getpDependentSetInFollow() {
        return pDependentSetInFollow;
    }

    public void setpDependentSetInFollow(Set<NonTerminalSymbol> pDependentSetInFollow) {
        this.pDependentSetInFollow = pDependentSetInFollow;
    }
    //删除产生式
    public void removeProduction(Production production) {
        pProductionTable.remove(production);
        numOfProduction--;
    }
    public void addProduction(Production production) {
        pProductionTable.add(production);
        numOfProduction++;
    }
    //FOLLOW集依赖的非终结符是否存在
    public void addDependentSetInFollow(NonTerminalSymbol non) {
        if(non.getName() != getName()) {
            pDependentSetInFollow.add(non);
        }
    }
    //非终结符的FOLLOW集添加FIRST集
    public void addFollowSet(Set<TerminalSymbol> ter) {pFollowSet.addAll(ter);}
    public void addFollow(TerminalSymbol ter) {pFollowSet.add(ter);}
    public void addFollowDependent() {
        for (NonTerminalSymbol non:pDependentSetInFollow) {
            addFollowSet(non.pFollowSet);
        }
    }
    //产生式是否含有ε变迁
    public Boolean isEpsilon() {
        for (TerminalSymbol ter:pFirstSet) {
            if(ter.getName().equals("ε")) {
                return true;
            }
        }
        return false;
    }
    public Boolean containsEpsilon(){
        for (TerminalSymbol symbol: pFirstSet) {
            if (symbol.getName().equals("ε")) {
                return true;
            }
        }
        return false;
    }
    //FIRST去除空ε
    public Set<TerminalSymbol> removeREpsilon() {
        Set<TerminalSymbol> first = new HashSet<>(pFirstSet);
        for(TerminalSymbol ter:first) {
            if(ter.getName().equals("ε")) {
                first.remove(ter);
                break;
            }
        }
        return first;
    }
    @Override
    public String toString() {
        String first = "{";
        for (TerminalSymbol s: pFirstSet) {
            first += s.getName() + ", ";
        }
        first += "}";
        // follow集合
        String follow = "{";
        for (TerminalSymbol s: pFollowSet) {
            follow += s.getName() + ", ";
        }
        follow += "}";
        // follow dependent集合依赖的非终结符
        String dependent = "{";
        for (NonTerminalSymbol s: pDependentSetInFollow) {
            dependent += s.getName() + " ";
        }
        dependent += "}";
        return this.getName() + " {" +
                " \n产生式个数: " + numOfProduction +
                ", \n产生式集:" + pProductionTable +
                ", \nFIRST集:" + first +
                ", \nFOLLOW集；" + follow +
                ", \nFOLLOW集依赖的非终结符:" + dependent +"\n" +
                '}';
    }

}
