package symbol;

public class TransitionEdge {
    //变迁边
    /**
     * 驱动文法符有终结符和非终结符两种。
     * 在给driverSymbol赋值时，要先对驱动符进行强制类型转换，变成GrammarSymbol *类型。
     */
    private GrammarSymbol driverSymbol;

    // 出发项集
    private ItemSet fromItemSet; //起点
    // 到达项集
    private ItemSet toItemSet; //目标

    public TransitionEdge(GrammarSymbol driverSymbol, ItemSet fromItemSet, ItemSet toItemSet) {
        this.driverSymbol = driverSymbol;
        this.fromItemSet = fromItemSet;
        this.toItemSet = toItemSet;
    }

    public GrammarSymbol getDriverSymbol() {
        return driverSymbol;
    }

    public void setDriverSymbol(GrammarSymbol driverSymbol) {
        this.driverSymbol = driverSymbol;
    }

    public ItemSet getFromItemSet() {
        return fromItemSet;
    }

    public void setFromItemSet(ItemSet fromItemSet) {
        this.fromItemSet = fromItemSet;
    }

    public ItemSet getToItemSet() {
        return toItemSet;
    }

    public void setToItemSet(ItemSet toItemSet) {
        this.toItemSet = toItemSet;
    }

    @Override
    public String toString() {
        return "\nedge{ " +
                "驱动字符: " + driverSymbol.getName() +
                ", 起始状态: " + fromItemSet.getStateId() +
                ", 到达状态: " + toItemSet.getStateId() +
                '}';
    }
}
