package symbol;
public class Cell {
    private NonTerminalSymbol nonTerminalSymbol;
    private TerminalSymbol terminalSymbol;
    private Production production;

    public Cell(NonTerminalSymbol nonTerminalSymbol, TerminalSymbol terminalSymbol, Production production) {
        this.nonTerminalSymbol = nonTerminalSymbol;
        this.terminalSymbol = terminalSymbol;
        this.production = production;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "非终结符=" + nonTerminalSymbol.getName() +
                ", 终结符=" + terminalSymbol.getName() +
                "产生式" + production +
                '}' + '\n';
    }
}
