package symbol;
public class TerminalSymbol extends GrammarSymbol{
    LexemeCategory category;
    public TerminalSymbol(String name,SymbolType type)  {
        super(name,type);
    }
    public LexemeCategory getCategory() {return category;}
    public void setCategory(LexemeCategory category) {
        this.category = category;
    }
    @Override
    public String toString() {
        return this.getName() + "{" +
                "\n类型=" + getType() +
                ",\n词类=" + category + "\n}";
    }
}
