package symbol;

/**
*文法符
 */
public class GrammarSymbol {
    private String name;               //名字
    private SymbolType type;           // 文法符的类别
    public GrammarSymbol(String n,SymbolType t) {
        this.name = n;
        this.type = t;

    }

    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }
    public SymbolType getType() {return type;}
    public void setType(SymbolType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "文法符{"+
                "名字=" + name +'\n' +
                "类别=" + type + "}";
    }
}
