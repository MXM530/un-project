package symbol;

import java.util.Objects;

public class LR0Item {
         //LR(0)项目
    private NonTerminalSymbol  nonTerminalSymbol;     //非终结符
    private Production production;                  //产生式
    private int dotPosition;          //圆点的位置
    private ItemCategoy type;  //类型。两种：CORE(核心项）；NONCORE(非核心项）
    public LR0Item(NonTerminalSymbol non,Production production,int dot,ItemCategoy item) {
        this.nonTerminalSymbol = non;
        this.production = production;
        this.dotPosition = dot;
        this.type = item;
    }
    public LR0Item(LR0Item item) {
        this.nonTerminalSymbol = item.getNonTerminalSymbol();
        this.production = item.getProduction();
        this.dotPosition = item.getDotPosition()+1;
        this.type = ItemCategoy.CORE;

    }
    public NonTerminalSymbol getNonTerminalSymbol() {
        return nonTerminalSymbol;
    }

    public Production getProduction() {
        return production;
    }

    public int getDotPosition() {
        return dotPosition;
    }

    public ItemCategoy getType() {
        return type;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LR0Item item = (LR0Item) o;
        return dotPosition == item.dotPosition && Objects.equals(nonTerminalSymbol,
                item.nonTerminalSymbol) && Objects.equals(production, item.production)
                && type == item.type;
    }

    @Override
    public String toString() {
        String s = new String();
        for (GrammarSymbol symbol: production.getpBodySymbolTable()) {
            s += symbol.getName();
        }
        return "\nitem {" +
                "非终结符: " + nonTerminalSymbol.getName() +
                ", 产生式: " + s +
                ", 圆点位置: " + dotPosition +
                ", 类别: " + type +
                '}';
    }
}